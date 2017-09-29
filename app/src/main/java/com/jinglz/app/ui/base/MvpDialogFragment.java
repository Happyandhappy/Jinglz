package com.jinglz.app.ui.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpDelegate;

/**
 * Dialog view for {@link com.jinglz.app.ui.entrieswinnings.EntriesAndWinningsDialog}
 */
public class MvpDialogFragment extends DialogFragment implements BaseView {

    private boolean mIsStateSaved;
    private MvpDelegate<? extends MvpAppCompatFragment> mMvpDelegate;

    /**
     * Empty constructor
     */
    public MvpDialogFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getMvpDelegate().onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        this.mIsStateSaved = false;
        this.getMvpDelegate().onAttach();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mIsStateSaved = true;
        this.getMvpDelegate().onSaveInstanceState(outState);
        this.getMvpDelegate().onDetach();
    }

    public void onStop() {
        super.onStop();
        this.getMvpDelegate().onDetach();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.getMvpDelegate().onDetach();
        this.getMvpDelegate().onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mIsStateSaved) {
            this.mIsStateSaved = false;
        } else {
            boolean anyParentIsRemoving = false;

            for (Fragment
                    parent = this.getParentFragment();
                    !anyParentIsRemoving && parent != null;
                    parent = parent.getParentFragment()) {
                anyParentIsRemoving = parent.isRemoving();
            }

            if (this.isRemoving() || anyParentIsRemoving || this.getActivity().isFinishing()) {
                this.getMvpDelegate().onDestroy();
            }

        }
    }


    public MvpDelegate getMvpDelegate() {
        if (this.mMvpDelegate == null) {
            this.mMvpDelegate = new MvpDelegate(this);
        }
        return this.mMvpDelegate;
    }

    /**
     * overridden method to show toast.
     * @param error String variable containing error to be displayed
     */
    @Override
    public void showError(String error) {

    }

    /**
     * overridden method to show progress
     */
    @Override
    public void showProgress() {

    }

    /**
     * overridden method to hide progress
     */
    @Override
    public void hideProgress() {

    }

    /**
     * overridden method to hide keyboard
     */
    @Override
    public void hideKeyboard() {

    }
}
