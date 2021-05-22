package com.xing.gfox.opengl.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES30;

import com.xing.gfox.opengl.shader.base.BitmapEffect;
import com.xing.gfox.opengl.util.U_OpenGL;
import com.xing.gfox.opengl.util.U_Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class BitmapDrawer {
    private Bitmap bitmap;
    private float[] mVertexCoors = {//顶点坐标
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };
    private float[] mTextureCoors = {//纹理坐标
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };
    private FloatBuffer vertexBuffer;
    private FloatBuffer mTextureBuffer;
    private int[] mTextureId = new int[1];

    public BitmapDrawer(Bitmap bitmap) {
        this.bitmap = bitmap;
        init();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private int programId = -1;
    // 顶点坐标接收者
    private int mVertexPosHandler = -1;
    // 纹理坐标接收者
    private int mTexturePosHandler = -1;
    // 纹理接收者
    private int mTextureHandler = -1;

    private void init() {
        if (bitmap == null) return;
        ByteBuffer bbvert = ByteBuffer.allocateDirect(mVertexCoors.length * 4);//每个浮点数:坐标个数* 4字节
        bbvert.order(ByteOrder.nativeOrder());//使用本机硬件设备的字节顺序
        vertexBuffer = bbvert.asFloatBuffer();// 从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(mVertexCoors);// 将坐标添加到FloatBuffer
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标
        ByteBuffer bbfrag = ByteBuffer.allocateDirect(mTextureCoors.length * 4);//每个浮点数:坐标个数* 4字节
        bbfrag.order(ByteOrder.nativeOrder());//使用本机硬件设备的字节顺序
        mTextureBuffer = bbfrag.asFloatBuffer();// 从字节缓冲区创建浮点缓冲区
        mTextureBuffer.put(mTextureCoors);// 将坐标添加到FloatBuffer
        mTextureBuffer.position(0);//设置缓冲区以读取第一个坐标

        //着色器
        programId = U_Shader.createGlProgramByString(BitmapEffect.getVertexShader(), BitmapEffect.getFragmentShader());

        mTextureId[0] = U_OpenGL.createTextureObject(mTextureId[0]);
    }

    public void onDraw() {
        U_OpenGL.clearColor();
        mVertexPosHandler = GLES30.glGetAttribLocation(programId, "aPosition");
        mTexturePosHandler = GLES30.glGetAttribLocation(programId, "aCoordinate");
        mTextureHandler = GLES30.glGetUniformLocation(programId, "uTexture");
        activateTexture();
        if (!bitmap.isRecycled()) {
            //绑定图片到被激活的纹理单元
            U_OpenGL.glGenTextures(mTextureId, bitmap);
//            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        }
        //启用顶点的句柄
        GLES30.glEnableVertexAttribArray(mVertexPosHandler);
        GLES30.glEnableVertexAttribArray(mTexturePosHandler);
        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES30.glVertexAttribPointer(mVertexPosHandler, 2, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(mTexturePosHandler, 2, GLES30.GL_FLOAT, false, 0, mTextureBuffer);
        //开始绘制
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        GLES30.glDisableVertexAttribArray(mVertexPosHandler);
        GLES30.glDisableVertexAttribArray(mTexturePosHandler);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    private void activateTexture() {
        //激活指定纹理单元
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //绑定纹理ID到纹理单元
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId[0]);
        //将激活的纹理单元传递到着色器里面
        GLES30.glUniform1i(mTextureHandler, 0);
        //配置边缘过渡参数
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
    }
}
