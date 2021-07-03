package com.xing.gfox.view.webView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.xing.gfox.util.U_screen;

public class HtmlImageGetter implements Html.ImageGetter {
    private final Context context;
    private final TextView text;

    public HtmlImageGetter(Context context, TextView text) {
        this.text = text;
        this.context = context;
    }

    /**
     * 获取图片
     */
    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        d.setBounds(0, 0, U_screen.getScreenWidth(context),
                d.getIntrinsicHeight());
        new LoadImage().execute(source, d);
        return d;
    }

    /**
     * 异步下载图片类
     */
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 图片下载完成后执行
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                /**
                 * 适配图片大小 <br/>
                 * 默认大小：bitmap.getWidth(), bitmap.getHeight()<br/>
                 * 适配屏幕：getDrawableAdapter
                 */
                mDrawable = getDrawableAdapter(context, mDrawable,
                        bitmap.getWidth(), bitmap.getHeight());
                // mDrawable.setBounds(0, 0, bitmap.getWidth(),
                // bitmap.getHeight());
                mDrawable.setLevel(1);

                /**
                 * 图片下载完成之后重新赋值textView<br/>
                 * mtvActNewsContent:我项目中使用的textView
                 *
                 */
                text.invalidate();
                CharSequence t = text.getText();
                text.setText(t);
            }
        }

        /**
         * 加载网络图片,适配大小
         */
        public LevelListDrawable getDrawableAdapter(Context context,
                                                    LevelListDrawable drawable, int oldWidth, int oldHeight) {
            LevelListDrawable newDrawable = drawable;
            long newHeight = 0;// 未知数
            int newWidth = U_screen.getScreenWidth(context);// 默认屏幕宽
            newHeight = (newWidth * oldHeight) / oldWidth;
            newDrawable.setBounds(0, 0, newWidth, (int) newHeight);
            return newDrawable;
        }
    }
}
