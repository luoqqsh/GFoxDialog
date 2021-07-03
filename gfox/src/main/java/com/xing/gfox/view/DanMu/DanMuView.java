package com.xing.gfox.view.DanMu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xing.gfox.base.interfaces.OnSimpleListener;

public class DanMuView extends SurfaceView implements SurfaceHolder.Callback {

    private DanMuController danMuController;
    private int speed;
    private int direction;
    private String text;
    private float textSizeScaleOfWidth;
    private int backColor;
    private int textColor;
    private boolean isShouldStartWhenReady;
    private OnSimpleListener onSimpleListener;

    public DanMuView(Context context) {
        super(context);
        init();
    }

    public DanMuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanMuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        danMuController.onSizeChange(w, h);
//    }

    public void start() {
        isShouldStartWhenReady = true;
        if (danMuController != null) {
            danMuController.start();
        }
    }

    public void stop() {
        isShouldStartWhenReady = false;
        if (danMuController != null) {
            danMuController.stop();
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        if (danMuController != null) {
            danMuController.setSpeed(speed);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
        if (danMuController != null) {
            danMuController.setDirection(direction);
        }
    }

    public void setText(String text) {
        this.text = text;
        if (danMuController != null) {
            danMuController.setText(text);
        }
    }

    public void setTextSizeScaleOfWidth(float textSizeScaleOfWidth) {
        this.textSizeScaleOfWidth = textSizeScaleOfWidth;
        if (danMuController != null) {
            danMuController.setTextSizeScaleOfWidth(textSizeScaleOfWidth);
        }
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
        if (danMuController != null) {
            danMuController.setBackColor(backColor);
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (danMuController != null) {
            danMuController.setTextColor(textColor);
        }
    }

    public void setOnCompleteListener(OnSimpleListener onSimpleListener) {
        this.onSimpleListener=onSimpleListener;
        if (danMuController != null) {
            danMuController.setOnCompleteListener(onSimpleListener);
        }
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        danMuController = new DanMuController();
        danMuController.setSpeed(speed);
        danMuController.setText(text);
        danMuController.setTextColor(textColor);
        danMuController.setTextSizeScaleOfWidth(textSizeScaleOfWidth);
        danMuController.setBackColor(backColor);
        danMuController.setDirection(direction);
        if (onSimpleListener != null) {
            danMuController.setOnCompleteListener(onSimpleListener);
        }
        danMuController.setOnLoopListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                if (danMuController == null) return;
                if (holder == null) return;
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) return;
                danMuController.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        });
        if (isShouldStartWhenReady) {
            danMuController.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (danMuController != null) {
            danMuController.onSizeChange(width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (danMuController != null) {
            danMuController.stop();
            danMuController = null;
        }
    }
}
