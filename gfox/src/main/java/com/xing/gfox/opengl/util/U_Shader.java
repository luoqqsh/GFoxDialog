package com.xing.gfox.opengl.util;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES30;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.GL_VALIDATE_STATUS;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glGetProgramInfoLog;

import com.xing.gfox.log.ViseLog;

public class U_Shader {
    public static final String ACOORD = "aCoord";//共有-采样点的坐标
    public static final String VMATRIX = "vMatrix";//顶点-矩阵变换
    public static final String VCOORD = "vCoord";//顶点-采样图片的坐标
    public static final String VPOSITION = "vPosition";//顶点-位置

    public static final String VTEXTURE = "vTexture";//片元-纹理采样器
    public static final String VCOLOR = "vColor";//片元-纹理颜色

    /**
     * 从assets加载着色器资源
     *
     * @param res  context.getResources()
     * @param vert 顶点着色器
     * @param frag 片元着色器
     * @return programId
     */
    public static int createGlProgramByAssets(Resources res, String vert, String frag) {
        return createGlProgramByString(uRes(res, vert), uRes(res, frag));
    }

    /**
     * 从raw加载着色器资源
     *
     * @param context context
     * @param vert    顶点着色器
     * @param frag    片元着色器
     * @return programId
     */
    public static int createGlProgramByRaw(Context context, int vert, int frag) {
        String vertexSharder = readRawTextFile(context, vert);
        String framentShader = readRawTextFile(context, frag);
        return createGlProgramByString(vertexSharder, framentShader);
    }

    /**
     * 加载着色器并创建program
     *
     * @param vertexShader   顶点着色器string
     * @param fragmentShader 片元着色器string
     * @return programId
     */
    public static int createGlProgramByString(String vertexShader, String fragmentShader) {
        int vertexShaderId = loadShader(GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderId = loadShader(GL_FRAGMENT_SHADER, fragmentShader);
        //校验
        if (vertexShaderId == 0) {
            ViseLog.e("顶点着色器创建失败：\n" + vertexShader);
            return 0;
        }
        if (fragmentShaderId == 0) {
            ViseLog.e("片元着色器创建失败：\n" + fragmentShader);
            return 0;
        }
        //创建程序
        int programId = glCreateProgram();
        //校验
        if (programId == 0) {
            ViseLog.e("programId = 0,创建失败：\n" +
                    "vertexShaderId：" + vertexShaderId + "  " +
                    "fragmentShaderId：" + fragmentShaderId);
            return 0;
        }
        //将着色器加入程序
        GLES30.glAttachShader(programId, vertexShaderId);
        GLES30.glAttachShader(programId, fragmentShaderId);
        GLES30.glLinkProgram(programId);
        //到此可以返回了，下面只要负责查错
        //iv对应status，检查是否正确创建，没有其他作用
        //offset内存地址偏移，防止被逆向，一般为0即可。
        int[] linkStatus = new int[1];
        GLES30.glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES30.GL_TRUE) {
            ViseLog.e("programId", glGetProgramInfoLog(programId));
            GLES30.glDeleteProgram(programId);
            return 0;
        }
        int[] linkVALIDATEStatus = new int[1];
        GLES30.glGetProgramiv(programId, GL_VALIDATE_STATUS, linkVALIDATEStatus, 0);
        if (linkStatus[0] != GLES30.GL_TRUE) {
            ViseLog.e("programId", glGetProgramInfoLog(programId));
            GLES30.glDeleteProgram(programId);
            return 0;
        }
        //合并成功后删除着色器
        GLES30.glDeleteShader(vertexShaderId);
        GLES30.glDeleteShader(fragmentShaderId);
        return programId;
    }

    /**
     * 加载着色器
     *
     * @param shaderType 着色器类型：GLES30.GL_VERTEX_SHADER、GLES30.GL_FRAGMENT_SHADER
     * @param source     着色器string字符串
     * @return 着色器id
     */
    private static int loadShader(int shaderType, String source) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, source);
            GLES30.glCompileShader(shader);
            //查看配置 主动获取是否成功，非必须
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] != GLES30.GL_TRUE) {
                ViseLog.e(shaderType + "着色器读取创建失败。\n" + GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    //通过路径加载Assets中的文本内容
    public static String uRes(Resources mRes, String path) {
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = mRes.getAssets().open(path);
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))) {
                result.append(new String(buffer, 0, ch));
            }
        } catch (Exception e) {
            return null;
        }
        return result.toString().replaceAll("\\r\\n", "\n");
    }

    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
