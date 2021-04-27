package com.zhyx.behaviordemo1.meituan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;

import com.zhyx.behaviordemo1.R;

public class MTScrollLayout extends FrameLayout implements NestedScrollingParent3 {
    private View mHeaderNavigationLayout; //顶部导航view
    private View mFoldContainerLayout; //可折叠内容view
    private View mContainerLayout; //container所有的内容view
    private View mTopContainerLayout;//上层view高度
    private View mSwiperContainerLayout; //下层可滑动联动效果lview
    private ImageView mBackOut;
    private HeaderSearchImageView searchImageView;
    private ImageView mMoreImage;
    private View mBgImage;
    private View mTabLayout;
    private float mStartTransY; //起始transY移动距离
    private float mMinContainerTransY; //到达顶部的transY最小距离
    private float swiperTransY; //swiper第一层置顶的滑动的起始距离
    private float headerHeight;
    private boolean canChildSlide;
    public MTScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();

    }

    public boolean isCanChildSlide() {
        return canChildSlide;
    }

    public void setCanChildSlide(boolean canChildSlide) {
        this.canChildSlide = canChildSlide;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStartTransY = getResources().getDimension(R.dimen.translate_fold_Y);
        headerHeight = getResources().getDimension(R.dimen.top_bar_height);
        Log.d("yumi",headerHeight + "");
        swiperTransY = getResources().getDimension(R.dimen.swiper_container_transY);
        ViewGroup.LayoutParams mFoldContainerLayoutParams = mFoldContainerLayout.getLayoutParams();
        mFoldContainerLayoutParams.height = (int) (getMeasuredHeight() - mStartTransY);
        ViewGroup.LayoutParams mContainerLayoutParams = mContainerLayout.getLayoutParams();
        mContainerLayoutParams.height = mFoldContainerLayoutParams.height;
        ViewGroup.LayoutParams swiperLayoutParams = mSwiperContainerLayout.getLayoutParams();
        swiperLayoutParams.height = (int) (getMeasuredHeight() - headerHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        headerHeight = headerHeight;

    }
    private void initView(){
        mFoldContainerLayout = findViewById(R.id.fold_container_layout);
        mContainerLayout = findViewById(R.id.container_layout);
        mTopContainerLayout = findViewById(R.id.top_container_layout);
        mSwiperContainerLayout = findViewById(R.id.swiper_container_layout);
        mHeaderNavigationLayout = findViewById(R.id.header_navigation);
        mBgImage = findViewById(R.id.bg_image);
        mTabLayout = findViewById(R.id.tablayout);
        mBackOut = findViewById(R.id.back_out);
        searchImageView = findViewById(R.id.search_imagview);
        mMoreImage = findViewById(R.id.more);
    }



    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return child.getId() == mSwiperContainerLayout.getId() && axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        if( mSwiperContainerLayout.getTranslationY() > swiperTransY + (getHeight() -swiperTransY) / 4 ) {
            mSwiperContainerLayout.animate().setDuration(200).setInterpolator(new AccelerateInterpolator()).translationY(getHeight());
        }else if(mSwiperContainerLayout.getTranslationY() > swiperTransY && mSwiperContainerLayout.getTranslationY() < swiperTransY + (getHeight() -swiperTransY) / 4){
            mSwiperContainerLayout.animate().setDuration(200).setInterpolator(new AccelerateInterpolator()).translationY(swiperTransY);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        float translateY = mSwiperContainerLayout.getTranslationY() - dy; //需要滑动的距离
        if(dy >0) {
            //向上滑动
            if(translateY >= headerHeight) {
                translationByConsume(mSwiperContainerLayout, translateY, consumed, dy);
            }else {
                translationByConsume(mSwiperContainerLayout, headerHeight, consumed, (mSwiperContainerLayout.getTranslationY() - headerHeight));
//                EventBus.getDefault().post(new MessageEvent(true));
                canChildSlide = true;
            }
        }
        if(dy < 0 && !target.canScrollVertically(-1)) {
            //向下滑动
            //处理下滑
            if (translateY >= headerHeight && translateY <= swiperTransY) {
                translationByConsume(mSwiperContainerLayout, translateY, consumed, dy);
//                EventBus.getDefault().post(new MessageEvent(false));
                canChildSlide = false;
            }else {
                if(type == ViewCompat.TYPE_TOUCH) {
                    translationByConsume(mSwiperContainerLayout, translateY, consumed, dy);
                }else {
                    translationByConsume(mSwiperContainerLayout, swiperTransY, consumed, (mSwiperContainerLayout.getTranslationY() - swiperTransY));
                }
            }
        }
        float fix = (swiperTransY - MathUtils.clamp(mSwiperContainerLayout.getTranslationY(),headerHeight,swiperTransY)) / (swiperTransY - headerHeight);
        float containerTransY = mStartTransY -(fix * (swiperTransY - headerHeight));
        float bgImageTransY = 0-(fix * (swiperTransY - headerHeight));
        mContainerLayout.setTranslationY(containerTransY);
        mBgImage.setTranslationY(bgImageTransY);
        float navigationAlpha = MathUtils.clamp((fix * 2) * 255,0,255);
        float mtablayoutAlpha = MathUtils.clamp((fix) * 255,0,255);
        if(navigationAlpha > 255.0f / 1.2) {
            ThemeUtil.setStatusBarTransparent((Activity) getContext(),true,false);
            mBackOut.setImageResource(R.mipmap.back_black);
            mMoreImage.setImageResource(R.mipmap.more_black);
        }else {
            ThemeUtil.setStatusBarTransparent((Activity) getContext(),false,false);
            mBackOut.setImageResource(R.mipmap.houtui);
            mMoreImage.setImageResource(R.mipmap.more);
        }
        mHeaderNavigationLayout.setBackgroundColor(Color.argb((int)navigationAlpha,250,250,250));
        mTabLayout.setBackgroundColor(Color.argb((int)mtablayoutAlpha,250,250,250));
        searchImageView.setDrawLeft(fix * 2);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    private void translationByConsume(View view, float translationY, int[] consumed, float consumedDy) {
        consumed[1] = (int) consumedDy;
        view.setTranslationY(translationY);
    }
}
