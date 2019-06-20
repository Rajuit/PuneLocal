package com.queerdevs.raj.punelocal;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by RAJ on 1/21/2018.
 */

public class CustomAutoCompleteView extends AppCompatAutoCompleteTextView {
    public CustomAutoCompleteView(Context context) {
        super(context);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering("", keyCode);
    }

    protected void replaceText(CharSequence text) {
        super.replaceText(text);
    }
}
