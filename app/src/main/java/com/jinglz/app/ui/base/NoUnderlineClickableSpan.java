package com.jinglz.app.ui.base;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * If an object of this type is attached to the text of a TextView. it removes
 * underline from that.
 */
public abstract class NoUnderlineClickableSpan extends ClickableSpan {

    /**
     * method with specified ds that is an instance of {@link TextPaint} to handle underlines.
     * it sets {@code ds.setUnderlineText(false)}
     *
     * @param ds instance of TextPaint extension of Paint
     */
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
