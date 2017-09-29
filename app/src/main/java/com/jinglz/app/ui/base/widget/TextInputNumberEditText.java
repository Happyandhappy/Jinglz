package com.jinglz.app.ui.base.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class TextInputNumberEditText extends TextInputEditText {

    private static final DecimalFormat FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    /**
     * Text watcher for TextView
     */
    private final TextWatcher mTextWatcher = new TextWatcher() {

        private final DecimalFormat mDecimalFormat = new DecimalFormat();{
            final DecimalFormatSymbols decimalFormatSymbols = mDecimalFormat.getDecimalFormatSymbols();
            decimalFormatSymbols.setGroupingSeparator(',');
            mDecimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //ignored
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //ignored
        }

        @Override
        public void afterTextChanged(Editable s) {
            removeTextChangedListener(this);

            try {
                final int startLength = getText().length();

                final String value = s.toString()
                        .replace(String.valueOf(mDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator()), "");
                final Number number = mDecimalFormat.parse(value);
                final int selectionStart = getSelectionStart();

                setText(mDecimalFormat.format(number));

                final int endLength = getText().length();
                final int selection = (selectionStart + (endLength - startLength));
                if (selection > 0 && selection <= getText().length()) {
                    setSelection(selection);
                } else {
                    setSelection(getText().length() - 1);
                }
            } catch (NumberFormatException | ParseException nfe) {
                // ignored
            }

            addTextChangedListener(this);

        }
    };

    /**
     * Constructs new TextInputNumberEditText with specified context.
     *
     * @param context for accessing application-specific data
     */
    public TextInputNumberEditText(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs new TextInputNumberEditText with specified context and attrs.
     *
     * @param context for accessing application-specific data
     * @param attrs collection of attributes to be used
     */
    public TextInputNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructs new TextInputNumberEditText with specified context,attrs and defStyleAttr.
     *
     * @param context for accessing application-specific data
     * @param attrs collection of attributes to be used
     * @param defStyleAttr style parameter
     */
    public TextInputNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * used to Adds a TextWatcher to the list of those whose methods are called
     * whenever this TextView's text changes.
     */
    private void init() {
        addTextChangedListener(mTextWatcher);
    }

    /**
     * Returns a text after formatting in Editable format
     * @return  Editable instance
     */
    public Editable getFormattedText() {
        return new SpannableStringBuilder(removeSeparator(getText().toString()));
    }

    /**
     * used to remove any kind of separator from string.
     * @param value String va
     * @return
     */
    private String removeSeparator(String value) {
        return value.replace(String.valueOf(FORMAT.getDecimalFormatSymbols().getGroupingSeparator()), "");
    }
}
