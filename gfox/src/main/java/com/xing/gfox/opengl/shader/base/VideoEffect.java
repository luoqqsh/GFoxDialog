package com.xing.gfox.opengl.shader.base;


import com.xing.gfox.opengl.shader.ShaderInterface;

/**
 * 无效果
 * Displays the normal video without any effect.
 */
public class VideoEffect implements ShaderInterface {
    @Override
    public String getShader(int width, int height) {

        String shader = "#extension GL_OES_EGL_image_external : require\n"
                + "precision mediump float;\n"
                + "varying vec2 vTextureCoord;\n"
                + "uniform samplerExternalOES sTexture;\n" + "void main() {\n"
                + "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                + "}\n";

        return shader;

    }
}