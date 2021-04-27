package com.zhyx.behaviordemo1.nestedscrollingdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;

import com.zhyx.behaviordemo1.R;

public class MyNestedScrollLayout extends FrameLayout implements NestedScrollingParent3 {
    private View mContentView; //内容view
    private View mHeaderView; //头部view
    private View mBodyView; //图片等bodyview
    private View mBgLayout;//背景黑色
    private View mMyInfoView;//我的信息info
    private TextView mTextViewTitle;
    private View mBgImageView;

    private float mContentViewTransY; //上边界内容 transY view
    private float mHeaderViewHeight;//头部view高度
    private float downContentTransY;//拉动到最底部距离
    private float slace;
    private boolean flag = false;
    public MyNestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(R.id.user_content_view);
        mHeaderView = findViewById(R.id.toolbarNavigationLayout);
        mBodyView = findViewById(R.id.cl_body);
        mBgLayout = findViewById(R.id.bg_framelayout);
        mMyInfoView = findViewById(R.id.my_info_view);
        mTextViewTitle= findViewById(R.id.toolbarnavigationTitle);
        mBgImageView = findViewById(R.id.bg_image);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        ViewGroup.LayoutParams params = mContentView.getLayoutParams();
        params.height = (int) (getMeasuredHeight() - mHeaderViewHeight);
        //重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentViewTransY = getResources().getDimension(R.dimen.user_content_view_transY);
        downContentTransY = mContentViewTransY + 200;

    }

    /*NestedScrollingParent3*/
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if(type == ViewCompat.TYPE_NON_TOUCH) {
            //惯性
            if(!target.canScrollVertically(-1) && mContentView.getTranslationY() >= mHeaderViewHeight && mContentView.getTranslationY()<mContentViewTransY) {
                Log.d("Tag",dyUnconsumed + "");
                flag = false;
//                translationByConsume(mContentView,mContentView.getTranslationY() + dyUnconsumed,consumed,dyUnconsumed);
                onNestedPreScroll(target,dxUnconsumed,dyUnconsumed,consumed,type);
            }
        }
    }
    /*NestedScrollingParent2*/
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        flag = true;
        return child.getId() == mContentView.getId() && axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
       if(mContentView.getTranslationY() > mContentViewTransY) {
           setAnimateScale(mBgImageView,200,1);
           mContentView.animate().setDuration(200).translationY(mContentViewTransY);
           mMyInfoView.animate().setDuration(200).translationY(0);
//           mContentView.setTranslationY(mContentViewTransY);
       }

        Log.d("Tag","onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    //处理快速滑动（猛滑）
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //preNestedScroll
        return false;
    }
//
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        float contentTransY = mContentView.getTranslationY();
        float dy = contentTransY - velocityY; //计算实际快速滑动的距离
        float alpha1 = Math.abs(contentTransY - mHeaderViewHeight )/ Math.abs(mContentViewTransY - mHeaderViewHeight);
        if(velocityY > 0) {
            if(contentTransY <= mHeaderViewHeight) {
                mMyInfoView.setAlpha(0);
                mBgLayout.setAlpha(1.0f - alpha1);
//                mTextViewTitle.setAlpha(0);
            }
        }
        if(velocityY < 0) {


            if(contentTransY >= mContentViewTransY) {
                mMyInfoView.setAlpha(1);
                mBgLayout.setAlpha(1.0f - alpha1);
//                mTextViewTitle.setAlpha(1);
            }
            if (contentTransY >mHeaderViewHeight && contentTransY<= mContentViewTransY) {


            }

        }
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        float contenttransY = mContentView.getTranslationY() - dy;
        float alpha1 = Math.abs(contenttransY - mHeaderViewHeight )/ Math.abs(mContentViewTransY - mHeaderViewHeight);
        float bodyTransY = (mBodyView.getTranslationY() - dy);
        if(dy > 0) {
            //上滑
            if(contenttransY >= mHeaderViewHeight) {
                translationByConsume(mContentView,contenttransY,consumed,dy);
                flag = false;
            }else {
                flag = false;
                translationByConsume(mContentView, mHeaderViewHeight, consumed, (mContentView.getTranslationY() - mHeaderViewHeight));
            }
            if(contenttransY > mContentViewTransY) {
                translationByConsume(mContentView,contenttransY,consumed,dy);

                float scale = ((mContentView.getTranslationY()-mContentViewTransY) /(downContentTransY - mContentViewTransY));
                float ts = (float) (this.slace - (1.0f - (scale)));
                float infoViewTransY = (mContentView.getTranslationY()-mContentViewTransY);

                if(ts >  1.0f){
                    mBgImageView.setScaleY(ts);
                    mBgImageView.setScaleX(ts);
                    mBgLayout.setScaleX(ts);
                    mBgLayout.setScaleY(ts);
                    mMyInfoView.setTranslationY(infoViewTransY);
                }else {
//                    mBgImageView.animate().setDuration(200).scaleY(1);
//                    mBgImageView.animate().setDuration(200).scaleX(1);
//                    mBgLayout.animate().setDuration(200).scaleY(1);
//                    mBgLayout.animate().setDuration(200).scaleX(1);
                    setAnimateScale(mBgImageView,200,1);
                    setAnimateScale(mBgLayout,200,1);
                    mMyInfoView.animate().setDuration(200).translationY(0);
                }

            }
        }
        if(dy < 0 && !target.canScrollVertically(-1)) {
            //下滑
            if(contenttransY >= mHeaderViewHeight && contenttransY <= mContentViewTransY) {
                translationByConsume(mContentView,contenttransY,consumed,dy);

                mBodyView.setTranslationY(bodyTransY);
            }else {
                if(contenttransY >= mContentViewTransY && contenttransY <= downContentTransY) {
                    translationByConsume(mContentView,contenttransY,consumed,dy);
                    float scale = ((mContentView.getTranslationY()-mContentViewTransY) /(downContentTransY - mContentViewTransY)) ;
                    this.slace = (float) (1.0f + (scale));
                    setScale(mBgImageView,this.slace);
                    setScale(mBgLayout,this.slace);
                    float infoViewTransY = (mContentView.getTranslationY()-mContentViewTransY);
                    mMyInfoView.setTranslationY(infoViewTransY);
                }
            }

        }

            float upCollapsedContentTransPro = getUpCollapsedContentTransPro();
            transCollapsedContentByPro(upCollapsedContentTransPro);
            float alpha = getUpAlphaScalePro();
            mMyInfoView.setAlpha((1.0f - alpha));
            mBgLayout.setAlpha(0.3f +  alpha);
            mTextViewTitle.setAlpha(alpha);


    }

    private void translationByConsume(View view, float translationY, int[] consumed, float consumedDy) {
        consumed[1] = (int) consumedDy;
        view.setTranslationY(translationY);
    }

    private float getAlphaToTransY(){
        return (mContentViewTransY - MathUtils.clamp(mContentView.getTranslationY(),mContentViewTransY - dp2px(180),mContentViewTransY)/ (mContentViewTransY-(mContentViewTransY - dp2px(180))));
    }
    private void transCollapsedContentByPro(float upCollapsedContentTransPro) {
        float collapsedContentTranY = - (upCollapsedContentTransPro * (mContentViewTransY - mHeaderViewHeight));
        translation(mBodyView,collapsedContentTranY * 0.5f);
    }
    private void translation(View view, float translationY) {
        view.setTranslationY(translationY);
    }
    private float getUpCollapsedContentTransPro() {
        return (mContentViewTransY - MathUtils.clamp(mContentView.getTranslationY(), mHeaderViewHeight, mContentViewTransY)) / (mContentViewTransY-mHeaderViewHeight);
    }
    private float getUpAlphaScalePro() {
        return (mContentViewTransY - MathUtils.clamp(mContentView.getTranslationY(), mContentViewTransY - dp2px(180), mContentViewTransY)) / (mContentViewTransY-(mContentViewTransY - dp2px(180)));
    }
    private float getScalePro() {
        return (downContentTransY- MathUtils.clamp(mContentView.getTranslationY(), downContentTransY - dp2px(350), downContentTransY)) / (downContentTransY-(downContentTransY - dp2px(350)));
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }
    private void setScale(View view, float s){
        view.setScaleX(s);
        view.setScaleY(s);
    }
    private void setAnimateScale(View view, int duration, float s){
        view.animate().setDuration(duration).scaleY(s);
        view.animate().setDuration(duration).scaleX(s);
    }

}
