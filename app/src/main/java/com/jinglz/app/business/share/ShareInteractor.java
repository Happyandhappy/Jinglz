package com.jinglz.app.business.share;

import android.content.Context;

import com.jinglz.app.BuildConfig;
import com.jinglz.app.R;
import com.jinglz.app.data.network.models.user.UserResponse;
import com.jinglz.app.data.repositories.SessionRepository;
import com.jinglz.app.injection.session.PerSession;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

@PerSession
public class ShareInteractor {

    private final SessionRepository mSessionRepository;
    private final Context mContext;

    /**
     * constructs new ShareInteractor with specified context and sessionRepository
     *
     * @param context handle application specific data
     * @param sessionRepository keep track of current session
     */
    @Inject
    public ShareInteractor(Context context, SessionRepository sessionRepository) {
        mSessionRepository = sessionRepository;
        mContext = context;
    }

    public Observable<String> getReferralCode() {
        return mSessionRepository.getCurrentSession()
                .subscribeOn(Schedulers.io())
                .map(UserResponse::inviteCode);
    }

    public Observable<String> getReferralLink() {
        return getReferralCode()
                .map(code -> BuildConfig.SHARE_LINK + "?r=" + code);
    }

    public Observable<String> getShareMessage() {
        return getReferralLink()
                .map(link -> mContext.getString(R.string.text_share_message, link));
    }

    /**
     * method with specified coins to show number won message.
     *
     * @param coins number of coins to be printed
     * @return Observable object of type string
     */
    public Observable<String> getShareWonCoinMessage(int coins) {
        return getReferralLink()
                .map(link -> mContext.getString(R.string.text_entries_and_winnings_share_message, coins, link));
    }
}
