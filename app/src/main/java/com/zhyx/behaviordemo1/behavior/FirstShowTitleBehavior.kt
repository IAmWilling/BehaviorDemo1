package com.zhyx.behaviordemo1.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zhyx.behaviordemo1.R

/**
 * 内容上层第一交互标题栏
 * Linkage Behavior
 * Created by zhy on 2021/4/21.
 */

class FirstShowTitleBehavior :CoordinatorLayout.Behavior<ConstraintLayout> {
    private var transY = 0
    private var maxTransY = 0
    private var topTitleHeight = 0
    private var backgroundCoverHeight = 0
    private var tablayoutHeight = 0
    private var contentPaddingTop = 0

    constructor() {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        context.resources.apply {
            transY = getDimension(R.dimen.content_view_vertical_translationY).toInt()
            topTitleHeight = getDimension(R.dimen.top_title_view_height).toInt()
            tablayoutHeight = getDimension(R.dimen.content_tablayout_view_height).toInt()
            contentPaddingTop = getDimension(R.dimen.content_view_paddingtop).toInt()
            backgroundCoverHeight = getDimension(R.dimen.background_view_height).toInt()
            maxTransY = transY
        }
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        layoutDirection: Int
    ): Boolean {
        child.translationY = (transY - child.height).toFloat()
        return super.onLayoutChild(parent, child, layoutDirection)
    }
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        val isContentView = dependency.id == R.id.content_view_layout
        return isContentView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        child.alpha =  1.0f - (((transY - dependency.translationY) / (transY - topTitleHeight)) * 1.5f)
        child.translationY = dependency.translationY - child.height
        return true
    }
}