package com.xing.gfox.opengl.renderer;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.xing.gfox.opengl.util.U_OpenGL;
import com.xing.gfox.opengl.util.U_Shader;

/**
 * 三角形
 */
public class TriangleDrawer {
    private FloatBuffer vertexBuffer;//顶点缓冲
    private final String vertexShaderCode =//顶点着色代码
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    private final String fragmentShaderCode =//片元着色代码
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final int mProgram;
    private int mPositionHandle;//位置句柄
    private int mColorHandle;//颜色句柄
    private final int vertexCount = sCoo.length / COORDS_PER_VERTEX;//顶点个数
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 3*4=12
    // 数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    //三角形每三个数是一个顶点，分别是x,y,z。屏幕-1到1。
    static float sCoo[] = {   //以逆时针顺序
            0.0f, 0.0f, 0.0f, // 顶部
            -1.0f, -1.0f, 0.0f, // 左下
            1.0f, -1.0f, 0.0f  // 右下
    };
    // 颜色，rgba
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public TriangleDrawer() {
        //初始化顶点字节缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(sCoo.length * 4);//每个浮点数:坐标个数* 4字节
        bb.order(ByteOrder.nativeOrder());//使用本机硬件设备的字节顺序
        vertexBuffer = bb.asFloatBuffer();// 从字节缓冲区创建浮点缓冲区
        vertexBuffer.put(sCoo);// 将坐标添加到FloatBuffer
        vertexBuffer.position(0);//设置缓冲区以读取第一个坐标

        //编译、链接着色器
        mProgram = U_Shader.createGlProgramByString(vertexShaderCode, fragmentShaderCode);
        //在OpenGL ES环境中使用程序片段（jvm->c）
        GLES30.glUseProgram(mProgram);

        //获取顶点着色器的vPosition成员的句柄
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, U_Shader.VPOSITION);

        // 获取片元着色器的vColor成员的句柄
        mColorHandle = GLES30.glGetUniformLocation(mProgram, U_Shader.VCOLOR);
    }

    public void onSurfaceChanged(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    public void draw() {
        U_OpenGL.clearColor();
        //启用三角形顶点的句柄
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        //准备三角坐标数据
        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);
        //为三角形设置颜色
        GLES30.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        //GL_TRIANGLES 独立三角形
        //GL_TRIANGLE_STRIP：复用顶点构成三角形
        //GL_TRIANGLE_FAN：复用第一个顶点构成三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);
        //禁用顶点数组
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }
}
