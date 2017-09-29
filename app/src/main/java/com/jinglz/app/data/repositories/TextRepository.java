package com.jinglz.app.data.repositories;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.jinglz.app.config.FontConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.network.api.Api;
import com.jinglz.app.data.network.models.ContactUsRequest;
import com.jinglz.app.data.network.models.ContactUsResponse;
import com.jinglz.app.data.network.models.DataResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

@Singleton
public class TextRepository {

    private static final String TAG = "TextRepository";
    private final Context mContext;
    private final Api mApi;

    /**
     * Constructs new TextRepository with specified context and api.
     * it is basically used to keep track of terms, conditions and application policies.
     *
     * @param context for application specific data
     * @param api Api to retrieve privacy policy, terms and conditions
     */
    @Inject
    public TextRepository(Context context, Api api) {
        mContext = context;
        mApi = api;
    }

    public CharSequence getHowItWorks() {

        String full = mContext.getString(R.string.text_how_it_works);
        final SpannableStringBuilder result = new SpannableStringBuilder(full);
        final Pattern titlePattern = Pattern.compile("%(.+)%");
        final Pattern boldPattern = Pattern.compile("#(.+)#");
        while (true) {
            final Matcher matcher = titlePattern.matcher(full);
            if (matcher.find()) {
                final String titleReplace = matcher.group(0);
                String titleReplaceTo = matcher.group(1);
                final Matcher boldMather = boldPattern.matcher(titleReplaceTo);
                final int titleIdx = full.indexOf(titleReplace);
                int boldIdx = -1;
                int boldLength = -1;
                if (boldMather.find()) {
                    final String boldReplace = boldMather.group(0);
                    final String boldReplaceTo = boldMather.group(1);
                    boldIdx = titleReplaceTo.indexOf(boldReplace);
                    titleReplaceTo = titleReplaceTo.replace(boldReplace, boldReplaceTo);
                    boldLength = boldReplaceTo.length();
                }
                result.replace(titleIdx, titleIdx + titleReplace.length(), titleReplaceTo);
                full = full.replace(titleReplace, titleReplaceTo);
                result.setSpan(new RelativeSizeSpan(1.25f), titleIdx, titleIdx + titleReplaceTo.length(),
                               Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (boldIdx >= 0) {
                    result.setSpan(
                            new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), FontConfig.black)),
                            titleIdx + boldIdx, titleIdx + boldIdx + boldLength,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * This method is used to retrieve terms and conditions of the application.
     * it calls {@link Api#getTermAndConditions()}.
     *
     * @return Single object of String type.
     */
    public Single<String> getTermsAndConditions() {
        return mApi.getTermAndConditions();
    }

    /**
     * This method is used to retrieve privacy policy of the application.
     * it calls {@link Api#getTermAndConditions()}.
     *
     * @return Single object of String type.
     */
    public Single<String> getPrivacyPolicy() {
        return mApi.getPrivacyPolicy();
    }

    /**
     * This method is used to send contact request for sending feedback.
     * it uses {@link ContactUsRequest} to hit api {@link Api#contactUs(ContactUsRequest)}.
     *
     * @param request request to send
     * @return ContactUsResponse object.
     */
    public Single<ContactUsResponse> sendContactUsRequest(ContactUsRequest request) {
        return mApi.contactUs(request)
                .map(DataResponse::data);
    }

}
