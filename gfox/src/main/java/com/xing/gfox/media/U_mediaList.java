package com.xing.gfox.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.FolderBean;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_file;

import com.xing.gfox.util.model.AppInfo;

public class U_mediaList {
    public static final String[] imgType = new String[]{"image/jpeg", "image/png", "image/bmp", "image/gif", "image/heic"};
    public static final String[] audioType = new String[]{"audio/aac",
            "audio/mpeg",
            "audio/ogg",
            "audio/x-wav",
            "audio/x-ms-wma",
            "audio/x-ms-wmv",
            "audio/x-pn-realaudio",
            "audio/x-mpeg",
            "audio/x-mpegurl",
            "audio/mp4a-latm"};
    public static final String[] videoType = new String[]{"video/mp4",
            "video/x-msvideo",
            "video/quicktime",
            "video/3gpp",
            "video/x-ms-asf",
            "video/HEVC",
            "video/x-matroska",
            "video/vnd.mpegurl",
            "video/x-m4v"};
    public static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,//????????????
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,//??????????????????
            MediaStore.Images.Media.DATE_ADDED,//????????????
            MediaStore.Images.Media.DATE_MODIFIED,//????????????
            MediaStore.Images.Media.MIME_TYPE,//?????????????????? image/png
            MediaStore.Images.Media.SIZE,//????????????
            MediaStore.Images.Media.TITLE,//???????????????????????????????????????????????????????????????

            MediaStore.Images.Media.WIDTH, //???
            MediaStore.Images.Media.HEIGHT//???
    };

    public static final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,//????????????
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,//??????????????????
            MediaStore.Video.Media.DATE_ADDED,//????????????
            MediaStore.Video.Media.DATE_MODIFIED,//????????????
            MediaStore.Video.Media.MIME_TYPE,//?????????????????? video/mp4
            MediaStore.Video.Media.SIZE,//????????????
            MediaStore.Video.Media.TITLE,//???????????????????????????????????????????????????????????????

            MediaStore.Video.Media.WIDTH, //???
            MediaStore.Video.Media.HEIGHT,//???
            MediaStore.Video.Media.DURATION,//??????
    };

    public static final String[] AUDIO_PROJECTION = {
            MediaStore.Audio.Media.DATA,//????????????
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,//??????????????????
            MediaStore.Audio.Media.DATE_ADDED,//????????????
            MediaStore.Audio.Media.DATE_MODIFIED,//????????????
            MediaStore.Audio.Media.MIME_TYPE,//?????????????????? video/mp4
            MediaStore.Audio.Media.SIZE,//????????????
            MediaStore.Audio.Media.TITLE,//???????????????????????????????????????????????????????????????
            //
            MediaStore.Audio.Media.DURATION,//??????
            MediaStore.Audio.Media.ARTIST,//?????????
            MediaStore.Audio.Media.ALBUM_ID,//??????id
            MediaStore.Audio.Media.ALBUM,//??????
            MediaStore.Audio.Media.YEAR,//??????
    };

    public static final String[] FILE_PROJECTION = {
            MediaStore.Audio.Media.DATA,//????????????
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.MIME_TYPE,//?????????????????? video/mp4
            MediaStore.Audio.Media.SIZE,//????????????
    };

    //LoaderManager
    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getVideoListByLoader(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByLoader(activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION, videoType, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getAudioListByLoader(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByLoader(activity, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_PROJECTION, audioType, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getImgListByLoader(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, imgType, callback);
    }

    //ContentResolver
    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getVideoListByContentResolver(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION, videoType, 0, 0, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getAudioListByContentResolver(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_PROJECTION, audioType, 0, 0, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getImgListByContentResolver(final T activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, imgType, 0, 0, callback);
    }

    //ContentResolver????????????,start??????????????????count????????????
    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getVideoListByContentResolver(final T activity, int start, int count, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION, videoType, start, count, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getAudioListByContentResolver(final T activity, int start, int count, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_PROJECTION, audioType, start, count, callback);
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getImgListByContentResolver(final T activity, int start, int count, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        getListByContentResolver(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, imgType, start, count, callback);
    }

    //???????????????????????????????????????
    public static <T extends LifecycleOwner & ViewModelStoreOwner> List<FileBean> queryFile(@NonNull final T activity, String fileUrl) {
        Context context;
        if (activity instanceof Fragment) {
            context = ((Fragment) activity).getContext();
        } else {
            context = (Context) activity;
        }
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Files.getContentUri("external"), FILE_PROJECTION, null, null, null);
        List<FileBean> fileList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(FILE_PROJECTION[0]));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(FILE_PROJECTION[1]));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(FILE_PROJECTION[2]));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(FILE_PROJECTION[3]));
                if (fileUrl.equals(path)) {
                    FileBean bean = new FileBean();
                    bean.setFilePath(path);
                    bean.setFilePathUri(Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), id));
                    bean.setFileMimeType(type);
                    bean.setMediaId(id);
                    bean.setFileSize(size);
                    fileList.add(bean);
                }
            } while (cursor.moveToNext());
        } else {
            ViseLog.d("????????????");
        }
        cursor.close();
        return fileList;
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getListByContentResolver(@NonNull final T activity, Uri uri, String[] projection, String[] types, int start, int count, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        Context context;
        if (activity instanceof Fragment) {
            context = ((Fragment) activity).getContext();
        } else {
            context = (Context) activity;
        }
        StringBuilder selection = new StringBuilder();
        if (types != null) {
            selection.append(projection[6]).append(">0 AND ");
            for (int i = 0; i < types.length; i++) {
                selection.append(projection[5]).append("=?");
                if (i != types.length - 1) {
                    selection.append(" OR ");
                }
            }
        }
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Bundle queryArgs = new Bundle();
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection.toString());
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, types);
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, projection[3] + " DESC");
            if (count != 0) {
                queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, count + " offset " + start);//??????????????????????????????
            }
            cursor = mContentResolver.query(uri, projection, queryArgs, null);
        } else {
            if (count != 0) {
                cursor = mContentResolver.query(uri, projection, selection.toString(), types, projection[3] + " DESC limit " + count + " offset " + start);//??????????????????????????????
            } else {
                cursor = mContentResolver.query(uri, projection, selection.toString(), types, projection[3] + " DESC");
            }
        }
        cursor.moveToFirst();
        callback.onListen(dealList(context, cursor, uri, projection));
    }

    public static <T extends LifecycleOwner & ViewModelStoreOwner> void getListByLoader(@NonNull final T activity, Uri uri, String[] projection, String[] types, @NonNull HOnListener<Map<String, FolderBean>> callback) {
        final HOnListener<Map<String, FolderBean>>[] callback1 = new HOnListener[]{callback};
        LoaderManager.getInstance(activity).restartLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Context context;
                if (activity instanceof Fragment) {
                    context = ((Fragment) activity).getContext();
                } else {
                    context = (Context) activity;
                }
                StringBuilder selection = new StringBuilder();
                if (types != null) {
                    selection.append(projection[6]).append(">0 AND ");
                    for (int i = 0; i < types.length; i++) {
                        selection.append(projection[5]).append("=?");
                        if (i != types.length - 1) {
                            selection.append(" OR ");
                        }
                    }
                }
                return new CursorLoader(context, uri, projection, selection.toString(), types, projection[3] + " DESC");
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                Context context;
                if (activity instanceof Fragment) {
                    context = ((Fragment) activity).getContext();
                } else {
                    context = (Context) activity;
                }
                if (callback1[0] != null) {
                    callback1[0].onListen(dealList(context, cursor, uri, projection));
                    callback1[0] = null;
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            }
        });
    }

    //??????uri??????????????????
    public static FileBean getDataByCursor(Context context, Uri uri, String[] projection) {
        FileBean fileBean = new FileBean(uri);
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return fileBean;
        }
        if (cursor.moveToFirst()) {
            return getDataByCursor(cursor, uri, projection, null, null);
        }
        cursor.close();
        return fileBean;
    }

    private static FileBean getDataByCursor(Cursor cursor, Uri uri, String[] projection, Context context, FolderBean folderBean) {
        String path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
        String id = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
        long time_create = cursor.getLong(cursor.getColumnIndexOrThrow(projection[3]));
        long time_modified = cursor.getLong(cursor.getColumnIndexOrThrow(projection[4]));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(projection[5]));
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(projection[6]));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(projection[7]));

        FileBean file = new FileBean();
        file.setFilePath(path);
        file.setFileName(name);
        file.setFileMimeType(type);
        file.setMediaId(id);
        file.setFileCreateDate(time_create);
        file.setFileModifiedDate(time_modified);
        file.setFileSize(size);
        file.setTitle(title);
        file.setFilePathUri(Uri.withAppendedPath(uri, id));

        if (type.contains("image")) {
            int width = cursor.getInt(cursor.getColumnIndexOrThrow(projection[8]));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(projection[9]));
            file.setImgInfo(width, height);
            if (folderBean != null)
                folderBean.setFolderName(context.getString(R.string.pick_all_pic));
        } else if (type.contains("video")) {
            int width = cursor.getInt(cursor.getColumnIndexOrThrow(projection[8]));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(projection[9]));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(projection[10]));
            file.setVideoInfo(width, height, duration);
            if (folderBean != null)
                folderBean.setFolderName(context.getString(R.string.pick_all_video));
        } else if (type.contains("audio")) {
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(projection[8]));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(projection[9]));
            long album_id = cursor.getLong(cursor.getColumnIndexOrThrow(projection[10]));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(projection[11]));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(projection[12]));
            file.setMusicInfo(artist, duration, album, album_id, year, title);
            if (folderBean != null)
                folderBean.setFolderName(context.getString(R.string.pick_all_audio));
        }
        return file;
    }

    private static Map<String, FolderBean> dealList(Context context, Cursor cursor, Uri uri, String[] projection) {
        Map<String, FolderBean> loaderList = new LinkedHashMap<>();
        List<FileBean> allList = new ArrayList<>();
        FolderBean folderBean = new FolderBean();
        folderBean.setFolderPath(U_file.SDROOT);
        folderBean.setMediaFileBeanList(allList);
        loaderList.put(U_file.SDROOT, folderBean);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    FileBean file = getDataByCursor(cursor, uri, projection, context, folderBean);
                    //??????????????????
                    File dirFile = new File(file.getFilePath()).getParentFile();
                    if (dirFile == null) continue;
                    String dirPath = dirFile.getAbsolutePath();
                    if (!loaderList.containsKey(dirPath)) {
                        FolderBean image = new FolderBean();
                        image.setFolderPath(dirPath);
                        image.setFolderName(dirFile.getName());
                        loaderList.put(dirPath, image);
                    }
                    FolderBean imageDir = loaderList.get(dirPath);
                    //??????????????????
                    if (imageDir == null) continue;
                    List<FileBean> fileBeanList = imageDir.getMediaFileBeanList();
                    if (fileBeanList == null) {
                        fileBeanList = new ArrayList<>();
                    }
                    fileBeanList.add(file);
                    allList.add(file);
                    imageDir.setMediaFileBeanList(fileBeanList);
                } while (cursor.moveToNext());
            } else {
                ViseLog.d("????????????");
            }
            cursor.close();
        }
        return loaderList;
    }

    /**
     * ???????????????apk?????????
     */
    public static List<AppInfo> getAppInfoList(Context context) {
        ArrayList<AppInfo> appInfoList = new ArrayList<>();
        //????????????????????????
        PackageManager packageManager = context.getPackageManager();
        //????????????????????????
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        //?????????????????????????????????????????????
        for (PackageInfo packageInfo : installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.setSourceDir(packageInfo.applicationInfo.sourceDir);
            appInfo.setVersionCode(packageInfo.versionCode);
            appInfo.setVersionName(packageInfo.versionName);
            appInfo.setFlags(packageInfo.applicationInfo.flags);
            appInfo.setName(packageInfo.applicationInfo.name);
            appInfo.setFirstInstallTime(packageInfo.firstInstallTime);
            appInfo.setLastUpdateTime(packageInfo.lastUpdateTime);
            appInfo.setTargetSdkVersion(packageInfo.applicationInfo.targetSdkVersion);
            appInfo.setDataDir(packageInfo.applicationInfo.dataDir);
            appInfo.setEnabled(packageInfo.applicationInfo.enabled);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appInfo.setMinSdkVersion(packageInfo.applicationInfo.minSdkVersion);
            }
            appInfo.setProcessName(packageInfo.applicationInfo.processName);
            appInfo.setUid(packageInfo.applicationInfo.uid);
            appInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            appInfo.setApkName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            appInfo.setApkPackageName(packageInfo.packageName);
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }
}
