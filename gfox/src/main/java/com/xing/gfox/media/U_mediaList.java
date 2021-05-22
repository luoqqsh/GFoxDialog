package com.xing.gfox.media;

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
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.FolderBean;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_file;

import com.xing.gfox.util.model.AppInfo;

public class U_mediaList {
    public static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,//文件路径
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,//文件名带后缀
            MediaStore.Images.Media.DATE_ADDED,//创建时间
            MediaStore.Images.Media.MIME_TYPE,//文件格式，如 image/png
            MediaStore.Images.Media.SIZE,//文件大小
            MediaStore.Images.Media.TITLE,//有的是文件名去除后缀，有的就是文件名带后缀
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //所在文件夹
            MediaStore.Images.Media.WIDTH, //
            MediaStore.Images.Media.HEIGHT };//


    public static final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,//文件路径
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,//文件名带后缀
            MediaStore.Video.Media.DATE_ADDED,//创建时间
            MediaStore.Video.Media.MIME_TYPE,//文件格式，如 image/png
            MediaStore.Video.Media.SIZE,//文件大小
            MediaStore.Video.Media.TITLE,//有的是文件名去除后缀，有的就是文件名带后缀
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, //所在文件夹
            MediaStore.Video.Media.WIDTH, //
            MediaStore.Video.Media.HEIGHT,//
            MediaStore.Video.Media.DURATION//
    };

    /**
     * 获取本机音乐列表
     *
     * @param context 上下文
     * @return 列表
     */
//    public static List<MediaFileBean> getMusics(Context context) {
//        ContentResolver mContentResolver = context.getContentResolver();
//        ArrayList<MediaFileBean> musics = new ArrayList<>();
//        Cursor c = null;
//        try {
//            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
//                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//            if (c != null) {
//                while (c.moveToNext()) {
//                    MediaFileBean music = new MediaFileBean();
//                    music.setFileName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
//                    music.setMusicName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
//                    music.setMusicAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
//                    music.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
//                    music.setFileSize(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
//                    music.setMediaDuration(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
//                    music.setMediaId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
//                    music.setMusicAlbumId(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
//                    music.setFilePath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
//                    music.setType(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)));
//                    music.setYear(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)));
//                    musics.add(music);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return musics;
//    }

    /**
     * 通过文件类型得到相应文件的集合
     **/
    public List<FileBean> getFilesByType(int fileType, Context context) {
        List<FileBean> files = new ArrayList<>();
        // 扫描files文件库
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_size"}, null, null, null);
            int dataindex = 0;
            int sizeindex = 0;
            if (c != null) {
                dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            }
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(dataindex);

                    if (U_file.getFileType(path) == fileType) {
                        if (!U_file.isFileExist(path)) {
                            continue;
                        }
                        long size = c.getLong(sizeindex);
                        FileBean fileBean = new FileBean(path);
                        files.add(fileBean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }


    /**
     * 得到图片文件夹集合
     */
    public static List<ImgFolderBean> getImageFolders(Context context) {
        List<ImgFolderBean> folders = new ArrayList<>();
        // 扫描图片
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            List<String> mDirs = new ArrayList<>();//用于保存已经添加过的文件夹目录
            if (c != null) {
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;

                    String dir = parentFile.getAbsolutePath();
                    if (mDirs.contains(dir))//如果已经添加过
                        continue;

                    mDirs.add(dir);//添加到保存目录的集合中
                    ImgFolderBean folderBean = new ImgFolderBean();
                    folderBean.setDir(dir);
                    folderBean.setFistImgPath(path);
                    if (parentFile.list() == null)
                        continue;
                    int count = parentFile.list((dir1, filename) -> U_file.getExtFromFilename(filename).equals(".jpeg") || U_file.getExtFromFilename(filename).equals(".jpg") || U_file.getExtFromFilename(filename).equals(".png")).length;
                    folderBean.setCount(count);
                    folders.add(folderBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return folders;
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (!directory.exists()) {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String path = file.getAbsolutePath();
                if (U_file.isPicFile(path)) {
                    imgPaths.add(path);
                }
            }
        }
        return imgPaths;
    }

    /**
     * 获取已安装apk的列表
     */
    public static List<AppInfo> getAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        //获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获得所有的安装包
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        //遍历每个安装包，获取对应的信息
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
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    public static void getMediaFromLoader(final FragmentActivity activity, String[] videoTypes, String filterDirPath, int mediaType, @NonNull final HOnListener<List<FileBean>> callback) {
        getMediaFromLoaderByPage(activity, videoTypes, filterDirPath, 0, 0, mediaType, callback);
    }

    public static void getMediaFromLoaderByPage(final FragmentActivity activity, String[] imageTypes, String filterDirPath, int page, int size, int mediaType, @NonNull final HOnListener<List<FileBean>> callback) {
        String filterDirPathFinal;
        String filterDirFileName;
        String allPath = Environment.getExternalStorageDirectory() + "";
        if (TextUtils.isEmpty(filterDirPath) || filterDirPath.equals(allPath)) {
            filterDirPathFinal = null;
            filterDirFileName = null;
        } else {
            if (filterDirPath.endsWith("/")) {
                filterDirPath = filterDirPath.substring(0, filterDirPath.length() - 1);
            }
            int dir_endIndex = filterDirPath.lastIndexOf("/");
            if (dir_endIndex < 0) return;
            filterDirFileName = filterDirPath.substring(dir_endIndex + 1);

            filterDirPathFinal = filterDirPath;
            ViseLog.d("filterDirPath = " + filterDirPath);
        }
        LoaderManager.getInstance(activity).restartLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String sortOrder;
                if (page >= 0 && size > 0) {
                    sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC limit " + size + " offset " + page * size;
                } else {
                    sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
                }
                String[] selectionArgs;
                if (!TextUtils.isEmpty(filterDirPathFinal)) {
                    int selectionTypeLength = imageTypes.length;
                    selectionArgs = new String[selectionTypeLength + 2];
                    selectionArgs[0] = filterDirPathFinal + "%";
                    selectionArgs[1] = filterDirFileName + "%";
                    if (imageTypes.length > 0) {
                        int otherSize = selectionArgs.length - selectionTypeLength;
                        System.arraycopy(imageTypes, 0, selectionArgs, otherSize, selectionTypeLength);
                        //System.arraycopy 含义如下
//                    for (int i = 0; i < imageTypesFinal.length; i++) {
//                        selectionArgs[i+1] = imageTypesFinal[i];
//                    }
                    }
                } else {
                    selectionArgs = imageTypes;
                }
                ViseLog.d("onCreateLoader");
                ViseLog.d(IMAGE_PROJECTION);
                ViseLog.d(getSelection(imageTypes, filterDirPathFinal, mediaType));
                ViseLog.d(selectionArgs);
                ViseLog.d(sortOrder);
                return new CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        getSelection(imageTypes, filterDirPathFinal, mediaType),
                        selectionArgs,
                        "date_added DESC");
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                ViseLog.d("onLoadFinished");
//                new TaskManager<List<FileBean>>() {
//                    @Override
//                    public List<FileBean> runOnBackgroundThread() {
//                        List<FileBean> dataList = new ArrayList<>();
//                        if (cursor != null) {
//                            if (cursor.getCount() > 0) {
//                                cursor.moveToFirst();
//
//                                do {
//                                    if (activity.isFinishing()) break;
//                                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
//                                    int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.WIDTH));
//                                    int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.HEIGHT));
//                                    long SIZE = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
////                                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                                    long duration = 0;
//                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                                        duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//                                    }
//                                    if (TextUtils.isEmpty(path)) {
//                                        continue;
//                                    }
//                                    if (filterDirPathFinal != null && filterDirPathFinal.length() > 0) {
//                                        int endIndex = path.lastIndexOf("/");
//                                        if (endIndex < 0) continue;
//                                        String parentFilePath = path.substring(0, endIndex);//不以 / 结尾
//                                        if (filterDirPathFinal.equals(parentFilePath)) {
//                                            FileBean mediaInfo = new FileBean();
//                                            switch (mediaType) {
//                                                case CPickType.music:
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.getMusicFileBean().setMediaDuration(duration);
//                                                    mediaInfo.setFileSize(SIZE);
//                                                    mediaInfo.setFileName(name);
////                                                    mediaInfo.setTitle(title);
//                                                    dataList.add(mediaInfo);
//                                                    break;
//                                                case CPickType.videos:
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.getVideoFileBean().setMediaDuration(duration);
//                                                    mediaInfo.getVideoFileBean().setVWidth(width);
//                                                    mediaInfo.getVideoFileBean().setVHeight(height);
//                                                    mediaInfo.setFileSize(SIZE);
//                                                    mediaInfo.setFileName(name);
////                                                    mediaInfo.setTitle(title);
//                                                    dataList.add(mediaInfo);
//                                                    break;
//                                                case CPickType.images:
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.setFilePath(path);
//                                                    mediaInfo.getImgFileBean().setImgWidth(width);
//                                                    mediaInfo.getImgFileBean().setImgHeight(height);
//                                                    mediaInfo.setFileSize(SIZE);
//                                                    mediaInfo.setFileName(name);
////                                                    mediaInfo.setTitle(title);
//                                                    dataList.add(mediaInfo);
//                                                    break;
//                                            }
//                                        }
//                                    } else {
//                                        FileBean mediaInfo = new FileBean();
//                                        switch (mediaType) {
//                                            case CPickType.music:
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.getMusicFileBean().setMediaDuration(duration);
//                                                mediaInfo.setFileSize(SIZE);
//                                                mediaInfo.setFileName(name);
////                                                mediaInfo.setTitle(title);
//                                                dataList.add(mediaInfo);
//                                                break;
//                                            case CPickType.videos:
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.getVideoFileBean().setMediaDuration(duration);
//                                                mediaInfo.getVideoFileBean().setVWidth(width);
//                                                mediaInfo.getVideoFileBean().setVHeight(height);
//                                                mediaInfo.setFileSize(SIZE);
//                                                mediaInfo.setFileName(name);
////                                                mediaInfo.setTitle(title);
//                                                dataList.add(mediaInfo);
//                                                break;
//                                            case CPickType.images:
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.setFilePath(path);
//                                                mediaInfo.getImgFileBean().setImgWidth(width);
//                                                mediaInfo.getImgFileBean().setImgHeight(height);
//                                                mediaInfo.setFileSize(SIZE);
//                                                mediaInfo.setFileName(name);
////                                                mediaInfo.setTitle(title);
//                                                dataList.add(mediaInfo);
//                                                break;
//                                        }
//                                    }
//
//                                } while (cursor.moveToNext());
//                            }
//                            cursor.close();
//                        }
//                        return dataList;
//                    }
//
//                    @Override
//                    public void runOnUIThread(List<FileBean> dataList) {
//                        if (activity.isFinishing()) return;
//                        callback.onListen(dataList);
//                    }
//                }.start();
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                ViseLog.d("onLoaderReset");
            }
        });
    }

    public static void getLocalImgFromLoader(final FragmentActivity activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {

        LoaderManager.getInstance(activity).restartLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[5] + ">0 AND " + IMAGE_PROJECTION[4] + "=? OR " + IMAGE_PROJECTION[4] + "=? OR " + IMAGE_PROJECTION[4] + "=? ",
                        new String[]{"image/jpeg", "image/png", "image/heic"}, IMAGE_PROJECTION[3] + " DESC");
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                Map<String, FolderBean> imageMap = new LinkedHashMap<>();
                FolderBean folderBean = new FolderBean();
                String folderPath = Environment.getExternalStorageDirectory() + "";
                folderBean.setFolderPath(folderPath);
                folderBean.setFolderName("全部照片");
                List<FileBean> allImageImageList = new ArrayList<>();
                folderBean.setMediaFileBeanList(allImageImageList);
                imageMap.put(folderPath, folderBean);
                if (data != null && data.getCount() > 0) {
                    data.moveToFirst();
                    do {
                        if (activity.isFinishing()) return;//防止在加载的时候直接返回了。
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String id = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String type = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        if (!U_file.isFileExist(path)) {
                            continue;
                        }
                        File dirFile = new File(path).getParentFile();
                        if (dirFile == null || !dirFile.exists()) continue;
                        String dirPath = dirFile.getAbsolutePath();
                        if (!imageMap.containsKey(dirPath)) {
                            FolderBean image = new FolderBean();
                            image.setFolderPath(dirPath);
                            image.setFolderName(dirFile.getName());
                            imageMap.put(dirPath, image);
                        }
                        FolderBean imageDir = imageMap.get(dirPath);
                        if (imageDir == null) continue;
                        List<FileBean> imageList = imageDir.getMediaFileBeanList();
                        if (imageList == null) {
                            imageList = new ArrayList<>();
                        }
                        FileBean image = new FileBean();
                        image.setFilePath(path);
                        image.setFileName(name);
                        image.setFileMimeType(type);
                        image.setMediaId(id);
                        image.setFileDate(time);
                        image.setFileSize(size);
                        image.setFilePathUri(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));
                        imageList.add(image);
                        allImageImageList.add(image);
                        imageDir.setMediaFileBeanList(imageList);
                    } while (data.moveToNext());
                }
                callback.onListen(imageMap);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            }
        });
    }

    /**
     * 获取本地所有视频
     */
    public static void getLocalVideoFromLoader(final FragmentActivity activity, @NonNull final HOnListener<Map<String, FolderBean>> callback) {
        final String[] IMAGE_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID};
        String[] videoType = new String[]{"mp4", "mov"};
        String[] selectionType;
        selectionType = new String[videoType.length];
        for (int i = 0; i < videoType.length; i++) {
            selectionType[i] = "video/" + videoType[i];
        }
        LoaderManager.getInstance(activity).restartLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(activity,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        MediaStore.Video.Media.SIZE + ">0 AND " + MediaStore.Video.Media.MIME_TYPE + "=? OR " + MediaStore.Video.Media.MIME_TYPE + "=? ",
                        selectionType, MediaStore.Video.Media.DATE_ADDED + " DESC");
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                Map<String, FolderBean> imageMap = new LinkedHashMap<>();
                if (data != null && data.getCount() > 0) {
                    data.moveToFirst();
                    FolderBean folderBean = new FolderBean();
                    String folderPath = Environment.getExternalStorageDirectory() + "";
                    folderBean.setFolderPath(folderPath);
                    folderBean.setFolderName("全部视频");
                    List<FileBean> allImageImageList = new ArrayList<>();
                    folderBean.setMediaFileBeanList(allImageImageList);
                    imageMap.put(folderPath, folderBean);
                    do {
                        if (activity.isFinishing()) return;//防止在加载的时候直接返回了。
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long duration = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String type = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        String id = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                        if (!U_file.isFileExist(path)) {
                            continue;
                        }
                        File dirFile = new File(path).getParentFile();
                        if (dirFile == null || !dirFile.exists()) continue;
                        String dirPath = dirFile.getAbsolutePath();
                        if (!imageMap.containsKey(dirPath)) {
                            FolderBean image = new FolderBean();
                            image.setFolderPath(dirPath);
                            image.setFolderName(dirFile.getName());
                            imageMap.put(dirPath, image);
                        }
                        FolderBean imageDir = imageMap.get(dirPath);
                        if (imageDir == null) continue;
                        List<FileBean> imageList = imageDir.getMediaFileBeanList();
                        if (imageList == null) {
                            imageList = new ArrayList<>();
                        }
                        FileBean video = new FileBean();
                        video.setFilePath(path);
                        video.setFileName(name);
                        video.setFileMimeType(type);
                        video.setMediaId(id);
                        video.setFileDate(time);
                        video.setFileSize(size);
                        video.setFilePathUri(Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id));
                        video.setVideoInfo(0, 0, duration);
                        imageList.add(video);
                        allImageImageList.add(video);
                        imageDir.setMediaFileBeanList(imageList);
                    } while (data.moveToNext());
                }
                callback.onListen(imageMap);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            }
        });
    }

    public static String getSelection(String[] selectionType, String filterDirPathFinal, @NonNull int mediaType) {
        StringBuilder selectionSb = new StringBuilder();
        switch (mediaType) {
            default:
            case CPickType.videos:
                selectionSb.append(MediaStore.Video.Media.SIZE + ">0");
                break;
            case CPickType.images:
                selectionSb.append(MediaStore.Images.Media.SIZE + ">0");
                break;
            case CPickType.music:
                selectionSb.append(MediaStore.Audio.Media.SIZE + ">0");
                break;
        }
        if (!TextUtils.isEmpty(filterDirPathFinal)) {
            switch (mediaType) {
                default:
                case CPickType.videos:
                    selectionSb.append(" AND " + MediaStore.Video.Media.DATA + " like ?");
                    selectionSb.append(" AND " + MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " like ?");
                    break;
                case CPickType.images:
                    selectionSb.append(" AND " + MediaStore.Images.Media.DATA + " like ?");
                    selectionSb.append(" AND " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " like ?");
                    break;
                case CPickType.music:
                    selectionSb.append(" AND " + MediaStore.Audio.Media.DATA + " like ?");
                    selectionSb.append(" AND " + MediaStore.Audio.Media.BUCKET_DISPLAY_NAME + " like ?");
                    break;
            }
        }
        if (selectionType != null && selectionType.length > 0) {
            if (selectionType.length == 1) {
                selectionSb.append(" AND ");
                switch (mediaType) {
                    default:
                    case CPickType.videos:
                        selectionSb.append(MediaStore.Video.Media.MIME_TYPE + "=?");
                        break;
                    case CPickType.images:
                        selectionSb.append(MediaStore.Images.Media.MIME_TYPE + "=?");
                        break;
                    case CPickType.music:
                        selectionSb.append(MediaStore.Audio.Media.MIME_TYPE + "=?");
                        break;
                }
            } else {
                for (int i = 0; i < selectionType.length; i++) {
                    if (i == 0) {
                        selectionSb.append(" AND ( ");
                    } else {
                        selectionSb.append(" OR ");
                    }
                    switch (mediaType) {
                        default:
                        case CPickType.videos:
                            selectionSb.append(MediaStore.Video.Media.MIME_TYPE + "=?");
                            break;
                        case CPickType.images:
                            selectionSb.append(MediaStore.Images.Media.MIME_TYPE + "=?");
                            break;
                        case CPickType.music:
                            selectionSb.append(MediaStore.Audio.Media.MIME_TYPE + "=?");
                            break;
                    }
                }
                selectionSb.append(" ) ");
            }
        }
        return selectionSb.toString();
    }
}
