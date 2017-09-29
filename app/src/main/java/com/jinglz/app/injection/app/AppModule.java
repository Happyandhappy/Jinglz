package com.jinglz.app.injection.app;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.jinglz.app.App;
import com.jinglz.app.BuildConfig;
import com.jinglz.app.Constants;
import com.jinglz.app.business.network.UnauthorizedException;
import com.jinglz.app.data.local.PreferencesHelper;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.images.PicassoImageLoader;
import com.jinglz.app.data.panel.FirebaseManager;
import com.jinglz.app.data.panel.MixpanelManager;
import com.jinglz.app.utils.AutoValueGsonTypeAdapterFactory;
import com.jinglz.app.utils.PrimitiveConverterFactory;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jinglz.app.data.network.api.HttpCode.UNAUTHORIZED;

@Module
public class AppModule {

    private final static String SSL = "SSL";

    private final Context mApplicationContext;

    public AppModule(App app) {
        mApplicationContext = app;
    }

    /**
     * This method is used to provide OkHttpClient.it is good to create {@code OkHttpClient} instance
     * and reuse it for all of your HTTP calls. This is because each client holds its own connection pool
     * and thread pools. Reusing connections and threads reduces latency. Conversely, creating a client
     * for each request wastes resources on idle pools.
     *
     * @param prefs to retrieve authentication token
     * @param sslContext secure socket protocol implementation which acts as a factory for secure socket
     *                   factories
     * @param hostnameVerifier Verify that the host name is an acceptable match with the
     *                         server's authentication scheme.
     * @return OkHttpClient instance
     */
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(PreferencesHelper prefs,
                                     SSLContext sslContext,
                                     HostnameVerifier hostnameVerifier) {
        final OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.addNetworkInterceptor(new StethoInterceptor());

        okClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            final String token = prefs.getAuthToken();
            if (token != null) {
                final Headers headers = request.headers()
                        .newBuilder()
                        .add(Constants.AUTHORIZATION_HEADER, Constants.TOKEN_PREFIX + token)
                        .build();
                request = request.newBuilder().headers(headers).build();
            }
            return chain.proceed(request);
        });

        okClientBuilder.addInterceptor(chain -> {
            final Request request = chain.request().newBuilder().build();
            final Response response = chain.proceed(request);
            final Headers headers = response.headers();
            final String token = headers.get(Constants.AUTHORIZATION_HEADER);
            if (token != null) {
                prefs.setAuthToken(token);
            }
            return response;
        });
        okClientBuilder.addInterceptor(chain -> {
            final Request request = chain.request().newBuilder().build();
            final Response response = chain.proceed(request);
            if (response.code() == UNAUTHORIZED && prefs.getAuthToken() != null) {
                prefs.setAuthToken(null);
                throw new UnauthorizedException();
            }
            return response;
        });

        final LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Log.INFO)
                .request("Request")
                .response("Response")
                .build();
        okClientBuilder.addInterceptor(httpLoggingInterceptor);

        final File baseDir = mApplicationContext.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            okClientBuilder.cache(new Cache(cacheDir, 1024 * 1024 * 50));
        }

        okClientBuilder.sslSocketFactory(sslContext.getSocketFactory());
        okClientBuilder.hostnameVerifier(hostnameVerifier);

        okClientBuilder.connectTimeout(Constants.TIMEOUT_DURATION_SEC, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(Constants.TIMEOUT_DURATION_SEC, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(Constants.TIMEOUT_DURATION_SEC, TimeUnit.SECONDS);
        return okClientBuilder.build();
    }


    /**
     * This method is used to provide TrustManager. TrustManager are responsible for
     * managing the trust material and making trust decisions
     * 
     * @return TrustManager[] instance
     * @throws CertificateException if certificate problem occurs.
     */
    @Provides
    @Singleton
    public TrustManager[] provideTrustManager() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        }};
    }

    /**
     * method with specified trustManagers, used to provide instance of secure socket layer context.
     * This will search for the installed security providers and return instance of {@code SSLContext}.
     *
     * @param trustManagers sources of peer authentication trust decisions
     * @return {@code sslContext}
     * @throws RuntimeException if found KeyManagementException or NoSuchAlgorithmException
     */
    @Provides
    @Singleton
    public SSLContext provideSslContext(TrustManager[] trustManagers) {
        try {
            final SSLContext sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            return sslContext;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * this method is used to check the host names it verify the URL's hostname and the server's
     * identification hostname mismatch. it match {@link Constants#BASE_URL} or {@link Constants#GOOGLE_API_HOST}
     *
     * @return {@code host} name
     */

    @Provides
    @Singleton
    public HostnameVerifier provideHostnameVerifier() {
        return (host, session) -> Constants.BASE_URL.contains(host) || Constants.GOOGLE_API_HOST.contains(host);
    }

    /**
     * Returns new instance of PicassoImageLoader.
     *
     * @return PicassoImageLoader instance
     */
    @Provides
    @Singleton
    ImageLoader provideImageLoader() {
        return new PicassoImageLoader(mApplicationContext);
    }

    /**
     * This method is used to provide {@code BASE_URL} and construct new Retrofit builder to make Http calls.
     * so requests can be made.
     *
     * @param okHttpClient to send HTTP requests and read their responses.
     * @param gsonConverterFactory uses Gson for JSON parsing
     *
     * @return  Returns a new {@link Retrofit}
     */
    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(PrimitiveConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    /**
     * This method is used to Create an implementation of the API endpoints by the specified
     * {@code Api} service interface.
     *
     * @param retrofit to create an implementation
     * @return new instance of Api
     */
    @Provides
    @Singleton
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    /**
     * Used to provide application context for accessing application-specific resources
     * @return {@code mApplicationContext}
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplicationContext;
    }

    /**
     * method with specified gson used to Create an instance using a default
     * {@link Gson} instance for conversion.
     *
     * @param gson Gson fro Json parsing
     * @return  instance of GsonConverterFactory
     */
    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    /**
     * Used to provide new instance of GsonBuilder that can be used to build Gson with various configuration
     * settings.
     *
     * @return Insatance of GsonBuilder
     */
    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
                .create();
    }

    /**
     * used to provide AWS credentials such as its AWS access key and AWS secret.
     * @return Instance of AWSCredentials
     */
    @Provides
    @Singleton
    AWSCredentials provideAWSCredentials() {
        return new BasicAWSCredentials(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET);
    }

    /**
     * This method is used to return new instance of Amazon S3 client using specified {@code credentials}
     * to connect with Amazon S3.
     *
     * @param credentials AWS credentials for accessing Amazon S3.
     * @return Instance of AmazonS3Client
     */
    @Provides
    @Singleton
    AmazonS3Client provideAmazonS3(AWSCredentials credentials) {
        return new AmazonS3Client(credentials);
    }

    /**
     * used to provide new Instance of MixpanelApi
     *
     * @return MixpanelAPI instance
     */
    @Singleton
    @Provides
    public MixpanelAPI provideMixpanelApi() {
        return MixpanelAPI.getInstance(mApplicationContext, BuildConfig.MIXPANEL_API_KEY);
    }

    /**
     * Used to keep track of app and other loggable information. create new instance of MixpanelManager
     * with specified mixpanelAPI, gson and {@link BuildConfig#PUSH_SENDER_ID}.
     *
     * @param mixpanelAPI MixpanelAPI object
     * @param gson Gson for Json parsing
     * @return MixpanelManager instance
     */
    @Singleton
    @Provides
    public MixpanelManager provideMixpanelManager(MixpanelAPI mixpanelAPI, Gson gson) {
        return new MixpanelManager(mixpanelAPI, gson, BuildConfig.PUSH_SENDER_ID);
    }

    /**
     * Used to provide application update. update can be performed by constructing new AppUpdaterUtils
     * and calling {@link AppUpdaterUtils#setUpdateFrom(UpdateFrom)} with {@link UpdateFrom#GOOGLE_PLAY}.
     *
     * @return AppUpdaterUtils instance
     */
    @Singleton
    @Provides
    public AppUpdaterUtils provideAppUpdater() {
        return new AppUpdaterUtils(mApplicationContext)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY);
    }

    @Singleton
    @Provides
    public FirebaseAnalytics provideFireaseAnalytics() {
        return FirebaseAnalytics.getInstance(mApplicationContext);
    }

    @Singleton
    @Provides
    public FirebaseManager provideFirebaseManager(FirebaseAnalytics firebaseAnalytics, Gson gson) {
        return new FirebaseManager(firebaseAnalytics, gson);
    }
}
