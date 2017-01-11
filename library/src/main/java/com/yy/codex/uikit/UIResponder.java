package com.yy.codex.uikit;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by it on 17/1/6.
 */

public class UIResponder extends FrameLayout {

    /* UIResponder initialize methods */
    private UIResponder nextResponder;
    public void setNextResponder(UIResponder responder) {
        this.nextResponder = responder;
    }
    public UIResponder getNextResponder() {
        return this.nextResponder;
    }

    public UIResponder(Context context) {
        super(context);
    }

    public UIResponder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIResponder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UIResponder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void touchesBegan() {
        nextResponder.touchesBegan();
    }

    public void touchesMoved() {
        nextResponder.touchesMoved();
    }

    public void touchesEnded() {
        nextResponder.touchesEnded();
    }
}