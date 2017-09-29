package com.jinglz.app.ui.winnings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.jinglz.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * a footer class with WatchVideo Again button
 */
public class FooterHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.button_watch_again) Button mButtonWatchAgain;

    /**
     * Constructor of FooterHolder
     */
    public FooterHolder(View itemView, View.OnClickListener onClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mButtonWatchAgain.setOnClickListener(onClickListener);
    }
}
