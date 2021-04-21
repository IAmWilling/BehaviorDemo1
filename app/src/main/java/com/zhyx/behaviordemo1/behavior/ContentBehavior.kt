package com.zhyx.behaviordemo1.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.zhyx.behaviordemo1.R

/**
 * 内容容器
 * Linkage Behavior
 * Created by zhy on 2021/4/21.
 */

class ContentBehavior : CoordinatorLayout.Behavior<ConstraintLayout> {
    private var transY = 0
    private var maxTransY = 0
    private var topTitleHeight = 0
    private var backgroundCoverHeight = 0
    private var tablayoutHeight = 0
    private var contentPaddingTop = 0
    private lateinit var mContentView: ConstraintLayout
    private lateinit var animate: ValueAnimator

    constructor() {}
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        context.resources.apply {
            transY = getDimension(R.dimen.content_view_vertical_translationY).toInt()
            topTitleHeight = getDimension(R.dimen.top_title_view_height).toInt()
            tablayoutHeight = getDimension(R.dimen.content_tablayout_view_height).toInt()
            contentPaddingTop = getDimension(R.dimen.content_view_paddingtop).toInt()
            backgroundCoverHeight = getDimension(R.dimen.background_view_height).toInt()
            maxTransY = transY + 200
        }
        animate = ValueAnimator()
        animate.duration = 500
        animate.addUpdateListener {
            val value = it.animatedValue as Float
            mContentView.translationY = value
        }
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child1: ConstraintLayout,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val childHeight = child1.layoutParams.height
        if (childHeight == ViewGroup.LayoutParams.MATCH_PARENT || childHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //如果高度设置自适应或者全部高度则进行重新计算布局
            var availableHeight: Int = View.MeasureSpec.getSize(parentHeightMeasureSpec)
            if (availableHeight == 0) {
                availableHeight = parent.measuredHeight
            }
            val curHeight = availableHeight - topTitleHeight
            val curHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                curHeight,
                if (childHeight == ViewGroup.LayoutParams.MATCH_PARENT) View.MeasureSpec.EXACTLY else View.MeasureSpec.AT_MOST
            )
            parent.onMeasureChild(
                child1,
                parentWidthMeasureSpec,
                widthUsed,
                curHeightMeasureSpec,
                heightUsed
            )
            return true
        }
        return false
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        layoutDirection: Int
    ): Boolean {
        val handleLayout = super.onLayoutChild(parent, child, layoutDirection)
        mContentView = child
        return handleLayout
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return directTargetChild == mContentView && axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {

        return false
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }


    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val curTransY = child.translationY - dy
        animate.cancel()
        if (dy > 0) {
            //手指上滑动
            if (curTransY >= topTitleHeight && !target.canScrollVertically(-1)) {
                //已经内容滚动在顶部
                //消费dy
                consumeDy(consumed, dy)
                setTranslateY(child, curTransY)
            } else {
                consumeDy(consumed, 0)
                setTranslateY(child, topTitleHeight.toFloat())
            }

        }
        if (dy < 0 && !target.canScrollVertically(-1)) {
            //手指下滑动
            if (curTransY <= transY && curTransY >= topTitleHeight) {
                consumeDy(consumed, dy)
                setTranslateY(child, curTransY)
            } else {
                if (curTransY <= maxTransY && curTransY >= transY && type == ViewCompat.TYPE_TOUCH) {
                    consumeDy(consumed, dy)
                    setTranslateY(child, curTransY)
                } else {
                    //child.translationY < maxTransY 增加条件防止惯性滑动触发临界点设置固定死距离
                    if (curTransY > transY && type == ViewCompat.TYPE_NON_TOUCH && child.translationY < maxTransY) {
                        consumeDy(consumed, 0)
                        if (child.translationY <= transY) {
                            //惯性滑动时 需要特别注意闪现的问题。这里已经做了处理
                            setTranslateY(child, transY.toFloat())
                        }

                    } else {
                        consumeDy(consumed, 0)
                        setTranslateY(child, maxTransY.toFloat())
                    }
                }

            }

        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (child.translationY > transY && child.translationY <= maxTransY) {
            if (animate.isRunning || animate.isStarted) {
                animate.cancel()
            }
            animate.setFloatValues(child.translationY, transY.toFloat())
            animate.start()
        }

    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ConstraintLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (ViewCompat.TYPE_NON_TOUCH == type && !target.canScrollVertically(-1) && transY > child.translationY && child.translationY >= topTitleHeight) {
            onNestedPreScroll(
                coordinatorLayout,
                child,
                target,
                dxUnconsumed,
                dyUnconsumed,
                consumed,
                type
            )
        }
    }


    private fun logD(str: String) {
        println("onNestedPreScroll  ======> $str")
    }

    private fun consumeDy(consumed: IntArray, dy: Int) {
        consumed[1] = dy
    }

    private fun setTranslateY(view: View, y: Float) {
        view.translationY = y
    }


}