package com.zhyx.behaviordemo1.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zhyx.behaviordemo1.R

/**
 * 内容上层第二交互标题栏
 * Linkage Behavior
 * Created by zhy on 2021/4/21.
 */

class TopTitleFadeBehavior : CoordinatorLayout.Behavior<ConstraintLayout> {
    private var transY = 0
    private var maxTransY = 0
    private var topTitleHeight = 0
    private var backgroundCoverHeight = 0
    private var tablayoutHeight = 0
    private var contentPaddingTop = 0
    private lateinit var collectText: View
    private lateinit var userNameText: View

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

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        collectText = child.findViewById(R.id.tv_collect)
        userNameText = child.findViewById(R.id.tv_title)
        return dependency.id == R.id.content_view_layout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        collectText.alpha = ((transY - dependency.translationY) / (transY - topTitleHeight))
        collectText.translationX = 30 * (1.0f - collectText.alpha)
        userNameText.alpha = collectText.alpha
        return true
    }
}