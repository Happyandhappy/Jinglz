package com.jinglz.app.data.network.api;

import com.jinglz.app.Constants;
import com.jinglz.app.data.network.models.ChangePasswordRequest;
import com.jinglz.app.data.network.models.ContactUsRequest;
import com.jinglz.app.data.network.models.ContactUsResponse;
import com.jinglz.app.data.network.models.ContestLeadershipRecord;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.data.network.models.DataResponse;
import com.jinglz.app.data.network.models.ForgotPasswordRequest;
import com.jinglz.app.data.network.models.ForgotPasswordResponse;
import com.jinglz.app.data.network.models.PayPalCode;
import com.jinglz.app.data.network.models.PhoneVerificationCodeRequest;
import com.jinglz.app.data.network.models.PhoneVerificationRequest;
import com.jinglz.app.data.network.models.RedeemRequest;
import com.jinglz.app.data.network.models.RedeemResponse;
import com.jinglz.app.data.network.models.ResetPasswordRequest;
import com.jinglz.app.data.network.models.coin.CoinsHistoryResponse;
import com.jinglz.app.data.network.models.coin.CurrentCoinsResponse;
import com.jinglz.app.data.network.models.contest.ContestResponse;
import com.jinglz.app.data.network.models.contest.TakePartInContestRequest;
import com.jinglz.app.data.network.models.history.GameHistoryResponse;
import com.jinglz.app.data.network.models.user.ActivateUserRequest;
import com.jinglz.app.data.network.models.user.AlreadyRegisterResponse;
import com.jinglz.app.data.network.models.user.SignInRequest;
import com.jinglz.app.data.network.models.user.SignInSocialRequest;
import com.jinglz.app.data.network.models.user.SignUpRequest;
import com.jinglz.app.data.network.models.user.SignUpSocialRequest;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.network.models.video.ShowVideoResponse;
import com.jinglz.app.data.network.models.video.VideoResponse;
import com.jinglz.app.ui.profile.edit.model.EditProfileModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Single;

public interface Api {

    @POST(Constants.API_VERSION + "sign-in")
    Single<DataResponse<UserResponse>> signIn(@Body SignInRequest model);

    @POST(Constants.API_VERSION + "sign-up")
    Single<DataResponse<UserResponse>> signUp(@Body SignUpRequest request);

    @POST(Constants.API_VERSION + "sign-in/{provider}")
    Single<DataResponse<UserResponse>> signInSocial(@Body SignInSocialRequest model,
                                                    @Path("provider") String provider);

    @POST(Constants.API_VERSION + "sign-up/{provider}")
    Single<DataResponse<UserResponse>> signUpSocial(@Body SignUpSocialRequest model,
                                                    @Path("provider") String provider);

    @GET(Constants.API_VERSION + "users/facebook/{facebookId}")
    Single<AlreadyRegisterResponse> checkFacebookAlreadyRegister(@Path("facebookId") String facebookId);

    @PUT(Constants.API_VERSION + "users")
    Single<DataResponse<UserResponse>> updateUser(@Body EditProfileModel request);

    @POST(Constants.API_VERSION + "users/activate")
    Single<UserResponse> activateUser(@Body ActivateUserRequest request);

    @GET(Constants.API_VERSION + "users/validate-code")
    Single<DataResponse<Boolean>> validateCode(@Query("code") String code,
                                               @Query("email") String email);

    @POST(Constants.API_VERSION + "sms_check")
    Single<UserResponse> verifyPhone(@Body PhoneVerificationRequest request);

    @POST(Constants.API_VERSION + "sms")
    Single<DataResponse<String>> requestPhoneVerificationCode(@Body PhoneVerificationCodeRequest request);

    @GET(Constants.API_VERSION + "users/me")
    Single<DataResponse<UserResponse>> currentUser();

    @POST(Constants.API_VERSION + "users/forgot-password")
    Single<DataResponse<List<ForgotPasswordResponse>>> forgotPassword(@Body ForgotPasswordRequest email);

    @GET
    @Streaming
    Single<ResponseBody> downloadFile(@Url String url);

    @POST(Constants.API_VERSION + "users/reset-password")
    Single<DataResponse<Boolean>> resetPassword(@Body ResetPasswordRequest request);

    @GET(Constants.API_VERSION + "coins/history")
    Single<DataResponse<CoinsHistoryResponse>> coinsHistory(@Query("gmt") int gmt);

    @GET(Constants.API_VERSION + "videos")
    Single<DataResponse<List<VideoResponse>>> availablesVideos();

    @GET(Constants.API_VERSION + "contests/user/closed")
    Single<DataResponse<List<VideoResponse>>> closedContests();

    @GET(Constants.API_VERSION + "videos/history")
    Single<DataResponse<List<VideoResponse>>> history(@Query("filter") String startTime,
                                                      @Query("page") int page,
                                                      @Query("limit") int limit);

    @GET(Constants.API_VERSION + "videos/history/{type}")
    Single<GameHistoryResponse> history(@Path("type") String type,
                                        @Query("offset") long offset,
                                        @Query("page") int page,
                                        @Query("limit") int limit);

    @POST(Constants.API_VERSION + "tutorial/rules")
    Single<Void> completeTutorialVideo();

    @POST(Constants.API_VERSION + "tutorial/details")
    Single<Void> completeTutorialDetails();

    @POST(Constants.API_VERSION + "tutorial/afterVideo")
    Single<Void> completeTutorialAfterVideo();

    @POST(Constants.API_VERSION + "tutorial/history")
    Single<Void> completeTutorialHistory();

    @GET("terms-and-conditions")
    Single<String> getTermAndConditions();

    @GET("privacy-policy")
    Single<String> getPrivacyPolicy();

    @GET(Constants.API_VERSION + "contests/{contestId}/leaderboard")
    Single<DataResponse<List<ContestLeadershipRecord>>> getContestUserRanking(@Path("contestId") String contestId);

    @GET(Constants.API_VERSION + "contests/user/unfetched-results")
    Single<DataResponse<List<ContestResult>>> getContestResults();

    @GET(Constants.API_VERSION + "videos/{id}/")
    Single<DataResponse<ShowVideoResponse>> video(@Path("id") String id);

    @GET(Constants.API_VERSION + "contests/{id}/")
    Single<DataResponse<ContestResponse>> contest(@Path("id") String id);

    @POST(Constants.API_VERSION + "contest/video")
    Single<Void> takePartInContest(@Body TakePartInContestRequest request);

    @POST(Constants.API_VERSION + "paypal")
    Single<UserResponse> verifyUser(@Body PayPalCode request);

    @POST(Constants.API_VERSION + "users/paypal")
    Single<UserResponse> unbindPayPal();

    @POST(Constants.API_VERSION + "redeem")
    Single<DataResponse<RedeemResponse>> redeemCoins(@Body RedeemRequest request);

    @POST(Constants.API_VERSION + "contact")
    Single<DataResponse<ContactUsResponse>> contactUs(@Body ContactUsRequest request);

    @POST(Constants.API_VERSION + "users/change-password")
    Single<DataResponse<UserResponse>> changePassword(@Body ChangePasswordRequest request);

    @GET(Constants.API_VERSION + "coins/current")
    Single<CurrentCoinsResponse> getCurrentCoins();
}
