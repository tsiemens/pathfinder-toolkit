/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTICE:
 * This file has been modified for the purposes of the Pathfinder Toolkit Project.
 *
 * According to the Apache Licence, Version 2.0, we do not need to consider
 * Pathfinder Toolkit in its entirety to be a derivative work, nor do we require to display
 * this notice in additional places, as this file was not accompanied with a NOTICE file,
 * and we are merely using a modified interface of this work.
 */

package com.lateensoft.pathfinder.toolkit.views.widget;

import android.animation.*;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;

/**
 * The dynamic listview is an extension of listview that supports cell dragging
 * and swapping.
 *
 * This layout is in charge of positioning the hover cell in the correct location
 * on the screen in response to user touch events. It uses the position of the
 * hover cell to determine when two cells should be swapped. If two cells should
 * be swapped, all the corresponding data set and layout changes are handled here.
 *
 * If no cell is selected, all the touch events are passed down to the listview
 * and behave normally. If one of the items in the listview experiences a
 * long press event, the contents of its current visible state are captured as
 * a bitmap and its visibility is set to INVISIBLE. A hover cell is then created and
 * added to this layout as an overlaying BitmapDrawable above the listview. Once the
 * hover cell is translated some distance to signify an item swap, a data set change
 * accompanied by animation takes place. When the user releases the hover cell,
 * it animates into its corresponding position in the listview.
 *
 * When the hover cell is either above or below the bounds of the listview, this
 * listview also scrolls on its own so as to reveal additional content.
 */
public class DynamicListView extends ListView {

    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 150;

    private int lastEventY = -1;
    private Point downEventCoord = new Point(-1, -1);

    private int totalOffset = 0;

    private boolean cellIsMobile = false;
    private boolean isMobileScrolling = false;
    private int smoothScrollAmountAtEdge = 0;

    private final int INVALID_INDEX = -1;
    private int mobileItemIndex = INVALID_INDEX;

    private BitmapDrawable hoverCell;
    private Rect hoverCellCurrentBounds;
    private Rect hoverCellOriginalBounds;

    private final int INVALID_POINTER_ID = -1;
    private int activePointerId = INVALID_POINTER_ID;

    private boolean isWaitingForScrollFinish = false;
    private int scrollState = OnScrollListener.SCROLL_STATE_IDLE;

    public DynamicListView(Context context) {
        super(context);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        setOnScrollListener(scrollListener);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        smoothScrollAmountAtEdge = (int)(SMOOTH_SCROLL_AMOUNT_AT_EDGE / metrics.density);
    }

    @Override
    @Deprecated // Use setDynamicAdapter()
    public void setAdapter(ListAdapter adapter) {
        throw new UnsupportedOperationException(this.getClass().getName() + " must use " +
                DynamicArrayAdapter.class.getName());
    }

    public void setDynamicAdapter(DynamicArrayAdapter adapter) {
        super.setAdapter(adapter);
    }

    public boolean canHoverRows() {
        return hoverCell == null;
    }

    /** Hovers the cell as position. The cell must be visible. **/
    public void hoverRow(int position) throws IllegalArgumentException {
        if(!canHoverRows()) throw new IllegalStateException("Cannot hover multiple rows at once");
        totalOffset = 0;

        int itemNum = position - getFirstVisiblePosition();
        int lastVisiblePos = getLastVisiblePosition();
        if (itemNum < 0 || itemNum > lastVisiblePos) {
            throw new IllegalArgumentException("Tried to hover row at " + position +
                    ". Must be between 0 and " + lastVisiblePos);
        }

        View selectedView = getChildAt(itemNum);
        mobileItemIndex = position;
        hoverCell = getAndAddHoverView(selectedView);
        selectedView.setVisibility(INVISIBLE);

        cellIsMobile = true;
    }


    /**
     * Creates the hover cell with the appropriate bitmap and of appropriate
     * size. The hover cell's BitmapDrawable is drawn on top of the bitmap every
     * single time an invalidate call is made.
     */
    private BitmapDrawable getAndAddHoverView(View v) {

        int w = v.getWidth();
        int h = v.getHeight();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapWithBorder(v);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        hoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        hoverCellCurrentBounds = new Rect(hoverCellOriginalBounds);

        drawable.setBounds(hoverCellCurrentBounds);

        return drawable;
    }

    /** Draws a border over the screenshot of the view passed in. */
    private Bitmap getBitmapWithBorder(View v) {
        return ((DynamicArrayAdapter)getAdapter()).getBitmapForMobileRow(v);
    }

    /** Retrieves the view in the list corresponding to itemID */
    public View getViewForPosition (int position) {
        int firstVisiblePosition = getFirstVisiblePosition();
        int lastVisiblePosition = getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            return getChildAt(position - firstVisiblePosition);
        }
        return null;
    }

    /**
     *  dispatchDraw gets invoked when all the child views are about to be drawn.
     *  By overriding this method, the hover cell (BitmapDrawable) can be drawn
     *  over the listview's items whenever the listview is redrawn.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (hoverCell != null) {
            hoverCell.draw(canvas);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        updateStateForTouch(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        boolean shouldContinue = updateStateForTouch(event);
        return shouldContinue ? super.onTouchEvent(event) : false;
    }

    private boolean updateStateForTouch(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downEventCoord.x = (int)event.getX();
                downEventCoord.y = (int)event.getY();
                activePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (activePointerId == INVALID_POINTER_ID) {
                    break;
                }

                int pointerIndex = event.findPointerIndex(activePointerId);

                lastEventY = (int) event.getY(pointerIndex);
                int deltaY = lastEventY - downEventCoord.y;

                if (cellIsMobile) {
                    hoverCellCurrentBounds.offsetTo(hoverCellOriginalBounds.left,
                            hoverCellOriginalBounds.top + deltaY + totalOffset);
                    hoverCell.setBounds(hoverCellCurrentBounds);
                    invalidate();

                    handleCellSwitch();

                    isMobileScrolling = false;
                    handleMobileCellScroll();

                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchEventsEnded();
                break;
            case MotionEvent.ACTION_CANCEL:
                touchEventsCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                /* If a multitouch event took place and the original touch dictating
                 * the movement of the hover cell has ended, then the dragging event
                 * ends and the hover cell is animated to its corresponding position
                 * in the listview. */
                pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                        MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    touchEventsEnded();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * This method determines whether the hover cell has been shifted far enough
     * to invoke a cell swap. If so, then the respective cell swap candidate is
     * determined and the data set is changed. Upon posting a notification of the
     * data set change, a layout is invoked to place the cells in the right place.
     * Using a ViewTreeObserver and a corresponding OnPreDrawListener, we can
     * offset the cell being swapped to where it previously was and then animate it to
     * its new position.
     */
    private void handleCellSwitch() {
        final int deltaY = lastEventY - downEventCoord.y;
        int deltaYTotal = hoverCellOriginalBounds.top + totalOffset + deltaY;

        View belowView = getViewForPosition(mobileItemIndex + 1);
        View mobileView = getViewForPosition(mobileItemIndex);
        View aboveView = getViewForPosition(mobileItemIndex - 1);

        boolean isBelow = (belowView != null) && (deltaYTotal > belowView.getTop());
        boolean isAbove = (aboveView != null) && (deltaYTotal < aboveView.getTop());

        if (isBelow || isAbove) {

            View switchView = isBelow ? belowView : aboveView;
            final int switchItemPosition = getPositionForView(switchView);
            final int originalItemPosition = getPositionForView(mobileView);

            if (switchView == null) {
                return;
            }

            DynamicArrayAdapter adapter = (DynamicArrayAdapter) getAdapter();
            adapter.swapItems(originalItemPosition, switchItemPosition);
            mobileItemIndex = switchItemPosition;

            adapter.notifyDataSetChanged();

            downEventCoord.y = lastEventY;

            final int switchViewStartTop = switchView.getTop();

            mobileView.setVisibility(View.VISIBLE);
            switchView.setVisibility(View.INVISIBLE);

            final ViewTreeObserver observer = getViewTreeObserver();
            if (observer != null) {
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        observer.removeOnPreDrawListener(this);

                        View switchView = getViewForPosition(switchItemPosition);


                            totalOffset += deltaY;

                            int switchViewNewTop = switchView.getTop();
                            int delta = switchViewStartTop - switchViewNewTop;

                            switchView.setTranslationY(delta);

                            ObjectAnimator animator = ObjectAnimator.ofFloat(switchView,
                                    View.TRANSLATION_Y, 0);
                            animator.setDuration(MOVE_DURATION);
                            animator.start();


                        return true;
                    }
                });
            }
        }
    }

    /**
     * Resets all the appropriate fields to a default state while also animating
     * the hover cell back to its correct location.
     */
    private void touchEventsEnded () {
        final View mobileView = getViewForPosition(mobileItemIndex);
        if (cellIsMobile || isWaitingForScrollFinish) {
            cellIsMobile = false;
            isWaitingForScrollFinish = false;
            isMobileScrolling = false;
            activePointerId = INVALID_POINTER_ID;

            // If the autoscroller has not completed scrolling, we need to wait for it to
            // finish in order to determine the final location of where the hover cell
            // should be animated to.
            if (scrollState != OnScrollListener.SCROLL_STATE_IDLE) {
                isWaitingForScrollFinish = true;
                return;
            }



            hoverCellCurrentBounds.offsetTo(hoverCellOriginalBounds.left, mobileView.getTop());

            ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(hoverCell, "bounds",
                    sBoundEvaluator, hoverCellCurrentBounds);
            hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    invalidate();
                }
            });
            hoverViewAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mobileItemIndex = INVALID_INDEX;
                    mobileView.setVisibility(VISIBLE);
                    hoverCell = null;
                    setEnabled(true);
                    invalidate();
                }
            });
            hoverViewAnimator.start();
        } else {
            touchEventsCancelled();
        }
    }

    /**
     * Resets all the appropriate fields to a default state.
     */
    private void touchEventsCancelled () {
        View mobileView = getViewForPosition(mobileItemIndex);
        if (cellIsMobile) {
            mobileItemIndex = INVALID_INDEX;
            mobileView.setVisibility(VISIBLE);
            hoverCell = null;
            invalidate();
        }
        cellIsMobile = false;
        isMobileScrolling = false;
        activePointerId = INVALID_POINTER_ID;
    }

    /**
     * This TypeEvaluator is used to animate the BitmapDrawable back to its
     * final location when the user lifts his finger by modifying the
     * BitmapDrawable's bounds.
     */
    private final static TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() {
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left, fraction),
                    interpolate(startValue.top, endValue.top, fraction),
                    interpolate(startValue.right, endValue.right, fraction),
                    interpolate(startValue.bottom, endValue.bottom, fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int)(start + fraction * (end - start));
        }
    };

    /**
     *  Determines whether this listview is in a scrolling state invoked
     *  by the fact that the hover cell is out of the bounds of the listview;
     */
    private void handleMobileCellScroll() {
        isMobileScrolling = handleMobileCellScroll(hoverCellCurrentBounds);
    }

    /**
     * This method is in charge of determining if the hover cell is above
     * or below the bounds of the listview. If so, the listview does an appropriate
     * upward or downward smooth scroll so as to reveal new items.
     */
    public boolean handleMobileCellScroll(Rect r) {
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int hoverViewTop = r.top;
        int hoverHeight = r.height();

        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-smoothScrollAmountAtEdge, 0);
            return true;
        }

        if (hoverViewTop + hoverHeight >= height && (offset + extent) < range) {
            smoothScrollBy(smoothScrollAmountAtEdge, 0);
            return true;
        }

        return false;
    }

    /**
     * This scroll listener is added to the listview in order to handle cell swapping
     * when the cell is either at the top or bottom edge of the listview. If the hover
     * cell is at either edge of the listview, the listview will begin scrolling. As
     * scrolling takes place, the listview continuously checks if new cells became visible
     * and determines whether they are potential candidates for a cell swap.
     */
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener () {

        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;
        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentScrollState;

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            mCurrentFirstVisibleItem = firstVisibleItem;
            mCurrentVisibleItemCount = visibleItemCount;

            mPreviousFirstVisibleItem = (mPreviousFirstVisibleItem == -1) ? mCurrentFirstVisibleItem
                    : mPreviousFirstVisibleItem;
            mPreviousVisibleItemCount = (mPreviousVisibleItemCount == -1) ? mCurrentVisibleItemCount
                    : mPreviousVisibleItemCount;

            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();

            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            scrollState = scrollState;
            isScrollCompleted();
        }

        /**
         * This method is in charge of invoking 1 of 2 actions. Firstly, if the listview
         * is in a state of scrolling invoked by the hover cell being outside the bounds
         * of the listview, then this scrolling event is continued. Secondly, if the hover
         * cell has already been released, this invokes the animation for the hover cell
         * to return to its correct position after the listview has entered an idle scroll
         * state.
         */
        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE) {
                if (cellIsMobile && isMobileScrolling) {
                    handleMobileCellScroll();
                } else if (isWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        /**
         * Determines if the listview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (cellIsMobile && mobileItemIndex != INVALID_INDEX) {
                    handleCellSwitch();
                }
            }
        }

        /**
         * Determines if the listview scrolled down enough to reveal a new cell at the
         * bottom of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleLastVisibleCellChange() {
            int currentLastVisibleItem = mCurrentFirstVisibleItem + mCurrentVisibleItemCount;
            int previousLastVisibleItem = mPreviousFirstVisibleItem + mPreviousVisibleItemCount;
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (cellIsMobile && mobileItemIndex != INVALID_INDEX) {
                    handleCellSwitch();
                }
            }
        }
    };
}
