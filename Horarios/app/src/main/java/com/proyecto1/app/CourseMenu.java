package com.proyecto1.app;

/**
 * Created by Victor on 4/5/14.
 */
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;


public class CourseMenu extends LinearLayout {


    private View menu_view;
    private View content;
    protected static final int menuMargin = 500;
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;
    protected Scroller menuScroller = new Scroller(this.getContext(),new LinearInterpolator());
    protected Runnable animationRunnable = new AnimationRunnable();
    protected Handler animationHandler = new Handler();
    private static final int animationDuration = 250;
    private static final int pollingInterval = 16;

    public enum MenuState {
        CLOSED, OPEN, CLOSING, OPENING
    };

    public CourseMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public CourseMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseMenu(Context context) {
        super(context);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.menu_view = this.getChildAt(0);
        this.content = this.getChildAt(1);
        this.menu_view.setVisibility(View.GONE);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
        if (changed)
            this.setDimmensions();
            this.menu_view.layout(left, top, right - menuMargin, bottom);
            this.content.layout(left + this.currentContentOffset, top, right + this.currentContentOffset, bottom);
    }

    public void moveMenu() {
        switch (this.menuCurrentState) {
            case CLOSED:
                this.menuCurrentState = MenuState.OPENING;
                this.menu_view.setVisibility(View.VISIBLE);
                this.menuScroller.startScroll(0, 0, this.getMenuWidth(), 0, animationDuration);
                break;
            case OPEN:
                this.menuCurrentState = MenuState.CLOSING;
                this.menuScroller.startScroll(this.currentContentOffset,0, -this.currentContentOffset, 0, animationDuration);
                break;
            default:
                return;
        }
        this.animationHandler.postDelayed(this.animationRunnable,pollingInterval);
        this.invalidate();
    }


    private int getMenuWidth() {

        return this.menu_view.getLayoutParams().width;
    }


    private void setDimmensions() {
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();
        this.menu_view.getLayoutParams().width = this.getWidth() - menuMargin;
        this.menu_view.getLayoutParams().height = this.getHeight();
    }


    private void placePosition(boolean isAnimationOngoing) {
        int scrollerOffset = this.menuScroller.getCurrX();
        this.content.offsetLeftAndRight(scrollerOffset- this.currentContentOffset);
        this.currentContentOffset = scrollerOffset;
        this.invalidate();
        if (isAnimationOngoing)
            this.animationHandler.postDelayed(this.animationRunnable, pollingInterval);
        else
            this.onMenuTransitionComplete();
    }

    private void onMenuTransitionComplete() {
        switch (this.menuCurrentState) {
            case OPENING:
                this.menuCurrentState = MenuState.OPEN;
                break;
            case CLOSING:
                this.menuCurrentState = MenuState.CLOSED;
                this.menu_view.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }


    protected class SmoothInterpolator implements Interpolator{
        @Override
        public float getInterpolation(float t) {
            return (float)Math.pow(t-1, 5) + 1;
        }
    }
    protected class AnimationRunnable implements Runnable {


        @Override
        public void run() {
            CourseMenu.this
                    .placePosition(CourseMenu.this.menuScroller
                            .computeScrollOffset());
        }


    }
}