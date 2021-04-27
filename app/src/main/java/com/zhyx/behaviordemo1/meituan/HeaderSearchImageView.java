package com.zhyx.behaviordemo1.meituan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.math.MathUtils;

import com.zhyx.behaviordemo1.R;

public class HeaderSearchImageView extends AppCompatImageView {
    private Paint mPaint; //圆角背景画笔
    private Paint mTextPaint; //文本画笔
    private int colorAlpha = 255;  //圆角背景
    private int textColorAlpha = 255;
    private float fix;
    private int left;
    private int right;
    private int mHeight;
    private int mWidth;
    public HeaderSearchImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStrokeWidth(1f);
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);//抗锯齿
        mTextPaint.setTextSize(38);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
        mWidth = getWidth();
        left = mWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float a = mHeight / 2 - 1.0f / 2;
        mPaint.setColor(Color.argb(colorAlpha,232,232,232));
        mTextPaint.setColor(Color.argb(textColorAlpha,175,175,175));
        RectF rectF = new RectF(left,0,mWidth,mHeight);
//        RectF rectF = new RectF(left,0,mWidth - mHeight / 2,mHeight);
//        canvas.drawCircle(mWidth - mHeight / 2,mHeight / 2,a,mPaint);
//        canvas.drawRect(rectF,mPaint);
        canvas.drawRoundRect(rectF,mHeight / 2,mHeight / 2,mPaint);
        Matrix matrix = new Matrix();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.search_black);
        canvas.drawBitmap(bitmap,left + mHeight / 2,bitmap.getHeight() / 2,mPaint);
        if(fix >= 0.8) {
            canvas.drawText("请输入商品名",left + mHeight + 15,mHeight / 1.5f,mTextPaint);
        }

    }
    public void setDrawLeft(float fix){
        float f = fix;
        if(f >= 1.0f) {
            f = 1.0f;
        }
        this.left = (int) (mWidth * (1.0f - (f)));

        colorAlpha = (int) MathUtils.clamp((fix) * 255,0,255);
        this.fix = f;
        if(fix >= 0.8) {
            textColorAlpha = (int) MathUtils.clamp((fix) * 255,0,255);
        }

        invalidate();
    }

}
