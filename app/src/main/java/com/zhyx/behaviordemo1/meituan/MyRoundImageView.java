package com.zhyx.behaviordemo1.meituan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.zhyx.behaviordemo1.R;

public class MyRoundImageView extends AppCompatImageView {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    public MyRoundImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(getBitmapShader(BitmapFactory.decodeResource(getResources(), R.mipmap.mdlicon)));
        canvas.drawRoundRect(0,0,mWidth,mHeight,13f,13f,mPaint);
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }
    private BitmapShader getBitmapShader(Bitmap bitmap){
        BitmapShader bitmapShader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        float scale = Math.max(mWidth * 1.0f / bitmap.getWidth(),mHeight * 1.0f / bitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        return bitmapShader;
    }
}
