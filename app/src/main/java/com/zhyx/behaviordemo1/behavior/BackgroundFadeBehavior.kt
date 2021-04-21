package com.zhyx.behaviordemo1.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import com.zhyx.behaviordemo1.R

/**
 * 歌手背景封面
 * Linkage Behavior
 * Created by zhy on 2021/4/21.
 */
class BackgroundFadeBehavior : CoordinatorLayout.Behavior<FrameLayout> {
    private var transY = 0
    private var maxTransY = 0
    private var topTitleHeight = 0
    private var backgroundCoverHeight = 0
    private var tablayoutHeight = 0
    private var contentPaddingTop = 0
    private lateinit var blurImage: View
    private lateinit var coverImage: View

    constructor() {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        context.resources.apply {
            transY = getDimension(R.dimen.content_view_vertical_translationY).toInt()
            topTitleHeight = getDimension(R.dimen.top_title_view_height).toInt()
            tablayoutHeight = getDimension(R.dimen.content_tablayout_view_height).toInt()
            contentPaddingTop = getDimension(R.dimen.content_view_paddingtop).toInt()
            backgroundCoverHeight = getDimension(R.dimen.background_view_height).toInt()
            maxTransY = transY + 200
        }
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: FrameLayout,
        layoutDirection: Int
    ): Boolean {
        blurImage = child.findViewById(R.id.people_cover_blur)
        coverImage = child.findViewById(R.id.people_cover)
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FrameLayout,
        dependency: View
    ): Boolean {
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
        }else {
            child.translationY = -backgroundCoverHeight * blurImage.alpha * 0.2f
        }
        return true
    }

    private fun logD(str: String) {
        println("BackgroundFadeBehavior ======> $str")
    }

}