package com.jinglz.app.ui.confirmcode.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jinglz.app.R;
import com.jinglz.app.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This  activity will open when user changed password successfully
 */
public class PasswordChangedActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, PasswordChangedActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);
        ButterKnife.bind(this);
    }

    /**
     * to get back to previous activity
     */
    @OnClick(R.id.button_lets_jinglz)
    public void onClick() {
        finish();
    }
}
