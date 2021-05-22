package com.xing.gfox.opengl;

import android.content.Context;

import com.xing.gfox.opengl.renderer.BitmapDrawer;
import com.xing.gfox.opengl.renderer.TriangleDrawer;
import com.xing.gfox.opengl.util.U_OpenGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class GLRenderer extends BaseGLRenderer {
    private TriangleDrawer mTriangle;
    private BitmapDrawer mBitmapDrawer;
    private Context context;

    public GLRenderer() {
    }

    public GLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        U_OpenGL.clearColor();
        mTriangle = new TriangleDrawer();
//        mBitmapDrawer = new BitmapDrawer(hl_bitmap.getBitmapFromDrawableId(context, R.mipmap.img_dingbu));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //OpenGL在GLSurfaceView中的绘制区域
//        GLES30.glViewport(0, 0, width, height);
        mTriangle.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mTriangle.draw();
//        mBitmapDrawer.onDraw();
    }

}
