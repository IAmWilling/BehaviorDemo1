package com.zhyx.behaviordemo1.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.zhyx.behaviordemo1.R

/**
 * 歌手背景封面
 * Linkage Behavior
 * Created by zhy on 2021/4/21.
 */
class BackgroundFadeBehavior : CoordinatorLayout.Behavior<FrameLayout>, NestedScrollingChild3 {
    private var transY = 0
    private var maxTransY = 0
    private var topTitleHeight = 0
    private var backgroundCoverHeight = 0
    private var tablayoutHeight = 0
    private var contentPaddingTop = 0
    private lateinit var blurImage: View
    private lateinit var coverImage: View
    private lateinit var mChildHelper: NestedScrollingChildHelper
    private lateinit var dependencyV: View
    private var mLastY = -1f
    private val animate = ValueAnimator()

    constructor() {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        context.resources.apply {
            transY = getDimension(R.dimen.content_view_vertical_translationY).toInt()
            topTitleHeight = getDimension(R.dimen.top_title_view_height).toInt()
            tablayoutHeight = getDimension(R.dimen.content_tablayout_view_height).toInt()
            contentPaddingTop = getDimension(R.dimen.content_view_paddingtop).toInt()
            backgroundCoverHeight = getDimension(R.dimen.background_view_height).toInt()
            maxTransY = transY + 300
            animate.setFloatValues(transY.toFloat(), topTitleHeight.toFloat())
        }
        animate.duration = 300
        animate.addUpdateListener {
            dependencyV.translationY = it.animatedValue as Float
        }

    }


    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {
        var flag = false
        if (MotionEvent.ACTION_DOWN == ev.action) {
            mLastY = ev.y
            flag = (ev.y >= topTitleHeight) && (ev.y < dependencyV.translationY)
        }
        if (dependencyV.translationY <= topTitleHeight) {
            return super.onInterceptTouchEvent(parent, child, ev)
        } else {
            //拦截
            if (flag) {
                return true
            } else {
                return super.onInterceptTouchEvent(parent, child, ev)
            }

        }
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {

                animate.cancel()
                val p = dependencyV.translationY - ((mLastY - ev.y))
                if (p <= maxTransY && p >= topTitleHeight) {
                    dependencyV.translationY -= (mLastY - ev.y)
                }

//                if (mLastY - ev.y> 10f) {
//                    if(!animate.isRunning || !animate.isStarted) {
//                        animate.setFloatValues(dependencyV.translationY,topTitleHeight.toFloat())
//                        animate.start()
//                    }
//                }

            }
            MotionEvent.ACTION_UP -> {
//                if (mLastY - ev.y> 10f) {
//                    if(!animate.isRunning || !animate.isStarted) {
                if (dependencyV.translationY >= transY && dependencyV.translationY <= maxTransY) {
                    animate.setFloatValues(dependencyV.translationY, transY.toFloat())

                } else {
                    animate.setFloatValues(dependencyV.translationY, topTitleHeight.toFloat())
                }
                animate.start()
//                    }
//                }
            }
        }
        mLastY = ev.y
        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: FrameLayout,
        layoutDirection: Int
    ): Boolean {
        mChildHelper = NestedScrollingChildHelper(child)
        blurImage = child.findViewById(R.id.people_cover_blur)
        coverImage = child.findViewById(R.id.people_cover)
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FrameLayout,
        dependency: View
    ): Boolean {
        dependencyV = dependency
        return dependency.id == R.id.content_view_layout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FrameLayout,
        dependency: View
    ): Boolean {
        blurImage.alpha = ((transY - dependency.translationY) / (transY - topTitleHeight))

        if (dependency.translationY > transY && dependency.translationY <= maxTransY) {
            //做 放大/缩小 动画
            val fix = ((maxTransY - dependency.translationY) / (maxTransY - transY))
            coverImage.scaleX = 1.0f + (1.0f - fix) / 5
            coverImage.scaleY = 1.0f + (1.0f - fix) / 5
            blurImage.scaleX = coverImage.scaleX
            blurImage.scaleY = coverImage.scaleY
        } else {
            child.translationY = -backgroundCoverHeight * blurImage.alpha * 0.2f
        }
        return true
    }

    private fun logD(str: String) {
        println("BackgroundFadeBehavior ======> $str")
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {

    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        )
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper.hasNestedScrollingParent(type)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper.startNestedScroll(axes, type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return startNestedScroll(axes, ViewCompat.TYPE_TOUCH)
    }

}