package com.xing.gfox.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Size;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_file;

public class U_media {
    /**
     * 刷新媒体库
     *
     * @param context context
     * @param path    文件路径
     */
    public static void updateMedia(Context context, String path) {
        try {
            if (U_file.isFileExist(path)) {
                MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            } else {
                ViseLog.e("UError", "文件路径错误：" + path);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    /**
     * 刷新媒体库
     *
     * @param context context
     * @param path    文件夹路径
     */
    public static void updateMedias(Context context, String path) {
        try {
            if (U_file.isFolderExist(path)) {
                List<String> file = new ArrayList<>();
                File[] fileList = new File(path).listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        if (U_file.isFileExist(f.getAbsolutePath())) {
                            file.add(f.getAbsolutePath());
                        }
                    }
                }
                MediaScannerConnection.scanFile(context.getApplicationContext(), file.toArray(new String[0]), null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            } else {
                ViseLog.e("UError", "文件夹路径错误：" + path);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    /**
     * 获取音乐的封面图
     *
     * @param album_id 封面图id
     * @param context  context
     * @return 返回后用Glide加载
     */
    public static String getAlbumArt(Context context, int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id), projection, null, null, null);
        if (cur == null) return null;
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        return album_art;
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbnail(Context context, Uri uri, int width, int height, CancellationSignal cancel) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return context.getContentResolver().loadThumbnail(uri, new Size(width, height), cancel);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取縮略圖,僅支持本地文件，MICRO_KIND 96*96 MINI_KIND 512*512
    public Bitmap getVideoThumbnail(int id, Context context) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, options);
        return bitmap;
    }

    //获取縮略圖,僅支持本地文件，MICRO_KIND 96*96 MINI_KIND 512*512
    public static Bitmap getVideoThumbnails(String videoPath) {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
    }

    /**
     * 获取网络mp4视频第一帧，不支持本地视频
     *
     * @param videoUrl 视频地址
     * @return bitmap
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            return retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } finally {
            retriever.release();
        }
    }

    /**
     * 获取本地mp4视频第一帧，不支持网络视频
     *
     * @param uri uri
     * @return bitmap
     */
    public static Bitmap getVideoThumb(Context context, Uri uri) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(context, uri);
        return media.getFrameAtTime();
    }


    /**
     * 是否视频文件
     *
     * @param filePath 文件路径
     * @return 是否视频
     */
    public static boolean isVideoFile(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        String type = URLConnection.getFileNameMap().getContentTypeFor(fileName);
        return file.exists() && type.contains("video");
    }

    /**
     * 是否图片文件
     *
     * @param filePath 文件路径
     * @return 是否图片
     */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outWidth != -1;
    }

    /**
     * 获取00：00格式时间
     *
     * @param timeMs 时长，单位毫秒
     * @return 00：00格式文本
     */
    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getUriStringById(int id) {
        return MediaStore.Video.Media
                .EXTERNAL_CONTENT_URI
                .buildUpon()
                .appendPath(String.valueOf(id)).build().toString();
    }

    //判断uri对应的文件是否存在
    //这种方法最大的问题即是，对应于一个同步 I/O 调用，易造成线程等待。因此，目前对于 MediaStore 中扫描出来的文件可能不存在的情况，没有直接的好方法可以解决过滤。
    public static boolean isContentUriExists(Context context, Uri uri) {
        if (null == context || uri == null) {
            return false;
        }
        ContentResolver cr = context.getContentResolver();
        try {
            AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
            if (null == afd) {
                return false;
            } else {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取视频/音频的时长
     *
     * @param filePath 文件路径
     * @return 时长
     */
    public static long getMediaTimeMs(String filePath) {
        int duration = 0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath); //在获取前，设置文件路径（应该只能是本地路径）
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            int originWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));//视频宽
//            int originHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));//视频高
//            int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));//码率
            retriever.release(); //释放
            if (!TextUtils.isEmpty(durationStr)) {
                duration = Integer.parseInt(durationStr);
            }
        } catch (Exception e) {
            return duration;
        }
        return duration;
    }

    private int getMediaTimeMs(Context context, Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            mediaPlayer.release();
            return duration;
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.release();
        }
        return 0;
    }

    public static FileBean getMediaInfoFromDB(Context context, FileBean fileBean) {
        Cursor cursor = context.getContentResolver().query(fileBean.getFilePathUri(), null, null, null, null);
        if (cursor == null) {
            return fileBean;
        }
        if (cursor.moveToFirst()) {
            fileBean.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));
            fileBean.setFileSize(cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)));
        }
        cursor.close();
        return fileBean;
    }
}
