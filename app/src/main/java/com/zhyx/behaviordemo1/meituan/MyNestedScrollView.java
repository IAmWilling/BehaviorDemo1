package com.zhyx.behaviordemo1.meituan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.zhyx.behaviordemo1.R;

public class MyNestedScrollView extends NestedScrollView {
    private View mRecyclerViewContainer;
    private RecyclerView mClassifyRecyclerView;
    private RecyclerView mDetailContentRecyclerView;
    private View lay;
    private float mContentHeader;
    private float mHeaderHeight;
    private boolean ishuadong = false;
    private NestedScrollingChildHelper helper;
    private IsHuaDongImp isHuaDongImp;

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public interface IsHuaDongImp {
        boolean canSlide();
    }


    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        lay = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        helper = new NestedScrollingChildHelper(this);
    }

    public void setIsHuaDongImp(IsHuaDongImp isHuaDongImp) {
        this.isHuaDongImp = isHuaDongImp;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mRecyclerViewContainer = findViewById(R.id.recycler_container);
        mClassifyRecyclerView = findViewById(R.id.classify_recyclerview);
        mDetailContentRecyclerView = findViewById(R.id.detail_content_recyclerview);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContentHeader = getResources().getDimension(R.dimen.translate_fold_Y) - getResources().getDimension(R.dimen.dp_30);
        mHeaderHeight = getResources().getDimension(R.dimen.dp_200);
        ViewGroup.LayoutParams params = mRecyclerViewContainer.getLayoutParams();
        params.height = (int) (getMeasuredHeight() - mContentHeader);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return ((target.getId() == mClassifyRecyclerView.getId()) || (target.getId() == mDetailContentRecyclerView.getId())) && axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int scrollY = getScrollY() + dy;
        if (isHuaDongImp == null) throw new IllegalArgumentException("huadongimp is null");
        if (dy > 0) {
            //上滑
            if (!isHuaDongImp.canSlide()) {
                super.onNestedPreScroll(target, dx, dy, consumed, type);
            }
            if (getScrollY() <= mHeaderHeight && isHuaDongImp.canSlide()) {
                scrollTo(0, scrollY);
                consumed[1] = (int) dy;
                if (type == ViewCompat.TYPE_NON_TOUCH && (mClassifyRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING || mDetailContentRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING)) {
                    mDetailContentRecyclerView.stopScroll();
                    mClassifyRecyclerView.stopScroll();
                }

            }
        }
        if (dy < 0 && !canScrollVertically(-1)) {
            //下滑
            super.onNestedPreScroll(target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            //惯性
            if (!canScrollVertically(-1)) {
                onNestedPreScroll(target, dxUnconsumed, dyUnconsumed, consumed, type);
            }
        }
    }


    private void translationByConsume(View view, float translationY, int[] consumed, float consumedDy) {
        consumed[1] = (int) consumedDy;
        view.setTranslationY(translationY);
    }
}
