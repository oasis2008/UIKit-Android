package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UIPanGestureRecognizer extends UIGestureRecognizer {

    @Nullable UITouch[] startTouches;
    @NonNull CGPoint translatePoint = new CGPoint(0, 0);
    @NonNull CGPoint velocityPoint = new CGPoint(0, 0);

    public UIPanGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UIPanGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (touches.length > 1) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        startTouches = touches;
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesMoved(touches, event);
        if (startTouches == null) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
//            setTranslation(new CGPoint(0, 0));
            mState = UIGestureRecognizerState.Began;
            markOtherGestureRecognizersFailed(this);
            sendActions();
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Changed;
            sendActions();
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesEnded(touches, event);
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
    }

    @NonNull
    public CGPoint translation() {
        if (lastPoints.length > 0 && translatePoint != null) {
            return new CGPoint(
                    lastPoints[0].getRelativePoint().getX() - translatePoint.getX(),
                    lastPoints[0].getRelativePoint().getY() - translatePoint.getY()
            );
        }
        return new CGPoint(0, 0);
    }

    public void setTranslation(@NonNull CGPoint point) {
        if (lastPoints.length > 0) {
            translatePoint = new CGPoint(
                    lastPoints[0].getRelativePoint().getX() + point.getX(),
                    lastPoints[0].getRelativePoint().getY() + point.getY()
            );
        }
    }

    @NonNull
    public CGPoint velocity() {
        return velocityPoint;
    }

    private void resetVelocity(@NonNull UITouch[] nextTouches) {
        if (lastPoints.length > 0 && nextTouches.length > 0) {
            double vx = (nextTouches[0].getRelativePoint().getX() - lastPoints[0].getRelativePoint().getX()) / ((nextTouches[0].getTimestamp() - lastPoints[0].getTimestamp()) / 1000);
            double vy = (nextTouches[0].getRelativePoint().getY() - lastPoints[0].getRelativePoint().getY()) / ((nextTouches[0].getTimestamp() - lastPoints[0].getTimestamp()) / 1000);
            velocityPoint = new CGPoint(vx, vy);
        }
    }

    private boolean moveOutOfBounds(@NonNull UITouch[] touches) {
        if (startTouches == null) {
            return true;
        }
        UIView view = getView();
        if (view == null) {
            return true;
        }
        int accepted = 0;
        double allowableMovement = 8.0;
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(view);
            for (int j = 0; j < startTouches.length; j++) {
                CGPoint p1 = startTouches[j].locationInView(view);
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted > 0;
    }

}
