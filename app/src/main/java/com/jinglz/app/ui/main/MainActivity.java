package com.jinglz.app.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jinglz.app.App;
import com.jinglz.app.R;
import com.jinglz.app.business.analytics.AnalyticsFacade;
import com.jinglz.app.business.analytics.Event;
import com.jinglz.app.business.auth.AuthException;
import com.jinglz.app.business.auth.AuthInteractor;
import com.jinglz.app.data.network.images.ImageLoader;
import com.jinglz.app.data.network.models.ContestResult;
import com.jinglz.app.ui.base.BaseActivity;
import com.jinglz.app.ui.base.NavigationFragment;
import com.jinglz.app.ui.contactus.ContactUsFragment;
import com.jinglz.app.ui.entrieswinnings.EntriesAndWinningsDialog;
import com.jinglz.app.ui.feed.FeedFragment;
import com.jinglz.app.ui.feed.models.ShortUserData;
import com.jinglz.app.ui.howitworks.HowItWorksFragment;
import com.jinglz.app.ui.redeem.RedeemFragment;
import com.jinglz.app.ui.settings.SettingsFragment;
import com.jinglz.app.ui.sharewin.ShareAndWinFragment;
import com.jinglz.app.ui.start.StartActivity;
import com.jinglz.app.ui.start.signin.models.SignInManualModel;
import com.jinglz.app.ui.tutorials.TutorialsFragment;
import com.jinglz.app.ui.videohistory.GameHistoryFragment;
import com.jinglz.app.updateChecker.UpdateRunnable;
import com.jinglz.app.utils.CommonUtils;
import com.jinglz.app.utils.GPSTracker;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

import static com.jinglz.app.ui.feed.FeedFragment.player;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    @Inject ImageLoader mImageLoader;

    @InjectPresenter MainPresenter mPresenter;

    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;

    private TextView mTextEmail;
    private TextView mTextName;
    private ImageView mImageAvatar;
    private ActionBarDrawerToggle mDrawerToggle;

    public static MainActivity mainActivity;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.get().getSessionComponent().inject(this);

        mainActivity= MainActivity.this;
        bind();

        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawer, R.string.text_drawer_open, R.string.text_drawer_close);
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            final NavigationFragment fragment = ((NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            if (fragment != null) {
                final int id = fragment.getDrawerItemId();
                mNavigationView.setCheckedItem(id);
            }
        });
        mNavigationDrawer.addDrawerListener(mDrawerToggle);
        initNavigationHeader();
        openHomeFragment();

         new UpdateRunnable(this, new Handler()).start();
        // new UpdateRunnable(this, new Handler()).force(true).start();

        hasLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasLocation()){

            if (LatitudeStr.equals("")){
                hasLocation();
            }
            else if (LatitudeStr.equals("0.0")){
                hasLocation();
            }else{
                String s = CommonUtils.getCountryName(getApplicationContext(), Double.parseDouble(LatitudeStr),Double.parseDouble(LongitudeStr));
                Log.e("ADRES","country name"+ s);
            }
        }else{
            if (player != null)
                player.stop();

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_gps);
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            dialog.findViewById(R.id.cancel_button).setOnClickListener(v -> {
                dialog.dismiss();
                onUnauthorized();
                finish();
            });

            dialog.findViewById(R.id.confirm_button).setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                closeDrawer();
            } else {
                mNavigationDrawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        final boolean checked;
        switch (item.getItemId()) {
            case R.id.menu_home:
                showFragment(FeedFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_redeem:
                showFragment(RedeemFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_game_history:
                showFragment(GameHistoryFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_how_it_works:
                showFragment(HowItWorksFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_contact_us:
                showFragment(ContactUsFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_share:
                showFragment(ShareAndWinFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_tutorials:
                showFragment(TutorialsFragment.newInstance());
                checked = true;
                break;
            case R.id.menu_settings:
                showFragment(SettingsFragment.newInstance());
                checked = true;
                break;
            default:
                checked = false;
                break;
        }
        if (checked) {
            mNavigationView.setCheckedItem(item.getItemId());
        }
        return checked;
    }
    /**
     * Overridden method to set user data such as email and name of the user . it is
     * used to set image in circular form by calling
     * {@link ImageLoader#displayCircularImage(String, ImageView, int, int)}.
     *
     * @param data ShortUserData contains email, firstName, lastName, avatarUrl
     */
    @Override
    public void setUserData(ShortUserData data) {
        Log.d(TAG, "setUserData: image avatar " + data.avatarUrl());
        mTextEmail.setText(data.email());
        mTextName.setText(data.name());
        mImageLoader.displayCircularImage(data.avatarUrl(), mImageAvatar,
                                          R.dimen.avatarSize,
                                          R.drawable.avatar_placeholder);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    //passo
    String LatitudeStr,LongitudeStr;
    private boolean hasLocation() {
        boolean loc = false;
        if (askForLocPermission()) {
            GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
            if (gpsTracker.getIsGPSTrackingEnabled()) {
                LatitudeStr = String.valueOf(gpsTracker.latitude);
                LongitudeStr = String.valueOf(gpsTracker.longitude);
                loc = true;
            }
        }
        return loc;
    }

    /**
     * method is used to ask location permission from user to if it is not already provided.
     * @return Returns {@code allowed} as true if permission is already provided, ask otherwise.
     */
    public boolean askForLocPermission() {
        boolean allowed = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
            } else {
                allowed = true;
            }
        } else {
            allowed = true;
        }
        return allowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.permission_location), Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }

    @Override
    public void showEntriesAndWinningsDialog(List<ContestResult> results) {
        EntriesAndWinningsDialog.newInstance(results)
                .show(getSupportFragmentManager(), EntriesAndWinningsDialog.class.getName());
    }

    @Override
    public void onUnauthorized() {
        Toast.makeText(this, R.string.error_your_session_has_expired, Toast.LENGTH_LONG).show();
        finishAffinity();
        startActivity(StartActivity.getIntent(this, true));
    }

    /**
     * method with specified fragment to set fragment by calling
     * {@link #showFragment(Fragment, boolean)}. boolean value is passed to
     * true for adding fragment to stack.
     *
     * @param fragment Fragment to be passed.
     */
    public void showFragment(Fragment fragment) {
        showFragment(fragment, true);
    }



    /**
     * method with specified Fragment and addToBackStack for initializing the
     * fragment. {@code fragment} will be added to stack if {@code addToBackStack}
     * is true. FragmentTransaction will be performed using setCustomAnimations.
     *
     * @param fragment Fragment to be replaced.
     * @param addToBackStack boolean value, if true fragment will be added to stack.
     */
    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment != null && ((NavigationFragment) currentFragment).getDrawerItemId() == ((NavigationFragment) fragment).getDrawerItemId()) {
            return;
        }
        final boolean popped = fragmentManager.popBackStackImmediate(fragment.getClass().getName(), 0);
        if (!popped) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(fragment.getClass().getName());
            }
            transaction.commit();
        }
    }

    public void popTo(Class<? extends NavigationFragment> fragmentClass) {
        getSupportFragmentManager().popBackStack(fragmentClass.getName(), 0);
    }
    /**
     * This method is used to set toolbar. {@code title} will be set as
     * a title of the navigational toolbar.
     *
     * @param toolbar Toolbar to be set.
     * @param title int contains number of title to be set.
     */
    public void setNavigationToolbar(Toolbar toolbar, @StringRes int title) {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(title);
        toolbar.setSubtitle(null);
    }
    /**
     * This method is used to set toolbar. {@code title} will be set as
     * a title of the navigational toolbar.
     *
     * @param toolbar Toolbar to be set.
     * @param title CharSequence contains title to be set.
     */
    public void setNavigationToolbar(Toolbar toolbar, CharSequence title) {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(title);
        toolbar.setSubtitle(null);
    }

    /**
     * Call this method to open home fragment. it will create new instance
     * of FeedFragment and pass {@code fragment} to {@link #showFragment(Fragment, boolean)}
     */
    public void openHomeFragment() {
        final Fragment fragment = FeedFragment.newInstance();
        mNavigationView.setCheckedItem(((NavigationFragment) fragment).getDrawerItemId());
        showFragment(fragment, true);
    }
    /**
     * This method is used to find views and initialize {@see mTextEmail}, {@see mTextName}
     * and {@see mImageLoader} respectively.
     */
    private void initNavigationHeader() {
        final View navHeader = mNavigationView.getHeaderView(0);
        mTextEmail = ButterKnife.findById(navHeader, R.id.text_email);
        mTextName = ButterKnife.findById(navHeader, R.id.text_name);
        mImageAvatar = ButterKnife.findById(navHeader, R.id.image_avatar);
    }
    /**
     * Call this method to close drawer.
     */
    private void closeDrawer() {
        mNavigationDrawer.closeDrawer(GravityCompat.START);
    }

}
