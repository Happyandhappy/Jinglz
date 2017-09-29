package com.jinglz.app.injection.session;

import com.jinglz.app.data.repositories.SessionRepository;
import com.jinglz.app.ui.changepassword.ChangePasswordPresenter;
import com.jinglz.app.ui.contactus.ContactUsPresenter;
import com.jinglz.app.ui.entrieswinnings.EntriesAndWinningsDialog;
import com.jinglz.app.ui.entrieswinnings.EntriesAndWinningsPresenter;
import com.jinglz.app.ui.feed.FeedPresenter;
import com.jinglz.app.ui.main.MainActivity;
import com.jinglz.app.ui.main.MainPresenter;
import com.jinglz.app.ui.profile.edit.EditProfilePresenter;
import com.jinglz.app.ui.redeem.RedeemPresenter;
import com.jinglz.app.ui.services.ServicesPresenter;
import com.jinglz.app.ui.settings.SettingsPresenter;
import com.jinglz.app.ui.sharewin.ShareAndWinPresenter;
import com.jinglz.app.ui.showvideo.ShowVideoPresenter;
import com.jinglz.app.ui.videohistory.tabs.month.MonthGameHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.month.MonthVideoHistoryTabPresenter;
import com.jinglz.app.ui.videohistory.tabs.today.TodayGameHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.today.TodayVideoHistoryTabPresenter;
import com.jinglz.app.ui.videohistory.tabs.week.WeekGameHistoryTabFragment;
import com.jinglz.app.ui.videohistory.tabs.week.WeekVideoHistoryTabPresenter;
import com.jinglz.app.ui.videorules.VideoRulesPresenter;
import com.jinglz.app.ui.winnings.WinningsActivity;
import com.jinglz.app.ui.winnings.WinningsPresenter;

import dagger.Subcomponent;

@PerSession
@Subcomponent(modules = SessionModule.class)
public interface SessionComponent {

    void inject(FeedPresenter presenter);

    void inject(EditProfilePresenter presenter);

    void inject(ShareAndWinPresenter presenter);

    void inject(MainPresenter presenter);

    void inject(SettingsPresenter presenter);

    void inject(WinningsPresenter presenter);

    void inject(WinningsActivity activity);

    void inject(MainActivity activity);

    void inject(VideoRulesPresenter presenter);

    void inject(RedeemPresenter presenter);

    void inject(ShowVideoPresenter presenter);

    void inject(ServicesPresenter presenter);

    void inject(ContactUsPresenter presenter);

    void inject(TodayGameHistoryTabFragment fragment);

    void inject(TodayVideoHistoryTabPresenter presenter);

    void inject(WeekGameHistoryTabFragment fragment);

    void inject(WeekVideoHistoryTabPresenter presenter);

    void inject(MonthGameHistoryTabFragment fragment);

    void inject(MonthVideoHistoryTabPresenter presenter);

    void inject(EntriesAndWinningsDialog presenter);

    void inject(EntriesAndWinningsPresenter presenter);

    void inject(ChangePasswordPresenter presenter);

    @Subcomponent.Builder interface Builder {

        SessionComponent.Builder sessionModule(SessionModule sessionModule);

        SessionComponent build();

    }

    SessionRepository provideSessionRepository();
}
