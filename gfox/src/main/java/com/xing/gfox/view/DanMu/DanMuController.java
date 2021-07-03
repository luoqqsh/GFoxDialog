package com.xing.gfox.view.DanMu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.xing.gfox.base.interfaces.OnSimpleListener;
import com.xing.gfox.rxHttp.task.TaskDelayBManager;

public class DanMuController {

    private float viewWidth;
    private float viewHeight;
    private float resultX, resultY;

    //==============================动态更换参数==========================
    private int backColor = Color.WHITE;
    private float textSizeScaleOfWidth = 0.5f;
    private int textColor = Color.BLACK;
    private String text = "手持弹幕，来一场没有距离的对话";
    private int speed = 1;
    private int direction;//0:从右往左，1：从左往右
    //==============================动态更换参数==========================

    private String drawText = text;
    private TaskDelayBManager taskDelayBManager;
    private Paint mPaint;
    private float textWidth;
    private int textHeightHalf;
    private OnSimpleListener onLoopListener;
    private float canvasStartX;
    private float canvasEndX;
    private long lastTime;
    private OnSimpleListener onSimpleListener;

    public DanMuController() {
        onInfoChange();
    }

    public void setOnLoopListener(OnSimpleListener onLoopListener) {
        this.onLoopListener = onLoopListener;
    }

    public void setText(String text) {
        if (text == null) return;
        if (this.text.equals(text)) return;
        this.text = text;
        onInfoChange();
    }

    public void setTextSizeScaleOfWidth(float textSizeScaleOfWidth) {
        if (this.textSizeScaleOfWidth == textSizeScaleOfWidth) return;
        this.textSizeScaleOfWidth = textSizeScaleOfWidth;
        onInfoChange();
    }

    public void setTextColor(int textColor) {
        if (this.textColor == textColor) return;
        this.textColor = textColor;
        onInfoChange();
    }

    public void setOnCompleteListener(OnSimpleListener onSimpleListener) {
        this.onSimpleListener = onSimpleListener;
    }

    public void setDirection(int direction) {
        if (this.direction == direction) return;
        if (direction > 1) {
            direction = 1;
        } else if (direction < 0) {
            direction = 0;
        }
        this.direction = direction;
        onInfoChange();
    }

    public void setBackColor(int backColor) {
        if (this.backColor == backColor) return;
        this.backColor = backColor;
    }

    public void setSpeed(int speed) {
        if (this.speed == speed) return;
        if (speed <= 0) {
            speed = 1;
        }
        this.speed = speed;
    }

    public float getViewHeight() {
        return viewHeight;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void onSizeChange(int viewWidth, int viewHeight) {
        if (this.viewWidth == viewWidth && this.viewHeight == viewHeight) return;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        onInfoChange();
    }

    private void onInfoChange() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
        mPaint.setTextSize(textSizeScaleOfWidth * viewWidth);
        mPaint.setColor(textColor);

        if (direction == 0) {
            drawText = text;
        } else if (direction == 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = text.length() - 1; i >= 0; i--) {
                sb.append(text.charAt(i));
            }
            drawText = sb.toString();
        }

        textWidth = mPaint.measureText(drawText);
        Paint.FontMetricsInt mFontMetricsInt = mPaint.getFontMetricsInt();
        textHeightHalf = (mFontMetricsInt.ascent + mFontMetricsInt.descent) / 2;


        //因为要旋转90度，所以画布坐标改变
        //旋转后的手机顶部坐标
        canvasStartX = viewWidth / 2 - viewHeight / 2;
        //旋转后的手机底部坐标
        canvasEndX = viewWidth + viewHeight / 2 - viewWidth / 2;

        if (direction == 0) {
            //向左
            resultX = canvasEndX;
        } else if (direction == 1) {
            //向右
            resultX = canvasStartX - textWidth;
        }
        resultY = viewHeight / 2;
    }

    public void start() {
        stop();
        if (taskDelayBManager == null) taskDelayBManager = new TaskDelayBManager() {
            @Override
            public void onListen(Long aLong) {
                if (onLoopListener != null) {
                    onLoopListener.onListen();
                }
            }
        };
        taskDelayBManager.loop(10);
    }

    public void stop() {
        if (taskDelayBManager != null) {
            taskDelayBManager.cancel();
            taskDelayBManager = null;
        }
    }

    public void draw(Canvas canvas) {
        if (canvas == null) return;
        if (drawText == null) return;


        canvas.drawColor(backColor);


        canvas.save();
        canvas.rotate(90, viewWidth / 2, viewHeight / 2);


        //测试了解旋转后的画布坐标
//        mPaint.setColor1(Color.RED);
//        mPaint.setStrokeWidth(5);
//        canvas.drawLine(0, 0, viewWidth, viewHeight, mPaint);
//        mPaint.setColor1(Color.BLUE);
//        canvas.drawLine(0, viewHeight, viewWidth, 0, mPaint);


        float x = resultX, y = resultY - textHeightHalf;
        float endX = 0;
        for (int i = 0; i < drawText.length(); i++) {
            char c = drawText.charAt(i);
            String singText = String.valueOf(c);
            float singleTextWidth = mPaint.measureText(singText);
            endX = x + singleTextWidth;
            if ((x >= canvasStartX - singleTextWidth && x <= canvasEndX) || (endX >= canvasStartX && endX <= canvasEndX + singleTextWidth)) {
                canvas.drawText(singText, x, y, mPaint);
            }
            x = endX;
        }
        //全绘制的话，如果文本太大太长，会绘制卡顿
//        canvas.drawText(drawText, resultX, resultY - textHeightHalf, mPaint);
        canvas.restore();
        long currentTime = System.currentTimeMillis();
        float passLength = 0;
        if (lastTime != 0) {
            float count = (currentTime - lastTime) * 1.0f / 10;//过了多少个10ms
            passLength = speed * count;
        }
        lastTime = currentTime;
        if (passLength <= 0) return;
        if (direction == 0) {
            //向左
//            resultX -= speed;
            resultX -= passLength;
            if (endX < canvasStartX) {
                resultX = canvasEndX;
                if (onSimpleListener != null) {
                    onSimpleListener.onListen();
                }
            }
        } else if (direction == 1) {
            //向右
//            resultX += speed;
            resultX += passLength;
            if (resultX > canvasEndX) {
                resultX = canvasStartX - textWidth;
                if (onSimpleListener != null) {
                    onSimpleListener.onListen();
                }
            }
        }
    }
}
