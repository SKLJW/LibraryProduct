package com.sk.library.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.lp.library.utils.SDCardUtil;


/**
 * 文件选择专用工具类
 *
 * @author vendor
 */
public class FileChooseUtil {

    private static final String TAG = "FileChooseUtil";
    public static final String path = "imgCache";
    private Activity mAct = null;
    private Fragment mFragment = null;


    public static final int TYPE_PHOTO = 0x991;//拍照
    public static final int TYPE_ALBUM = 0x992;//相册
    public static final int TYPE_CROP_IMAGE = 0x993;//截屏

    //上一次记录的路径 因为拍照有缺陷 所以只能这样记录
    private static String sFilePath = null;
    //Image文件本地路径
    public String mFileDir = null;

    public FileChooseUtil(Activity act) {
        mAct = act;
        mFileDir = path;
    }

    public FileChooseUtil(Fragment fragment) {
        mAct = fragment.getActivity();
        mFragment = fragment;
        mFileDir = path;
    }

    /**
     * 选择文件
     *
     * @param requestCode
     * @return 不一定有返回值 只有在拍照以及录像的时候可能有
     */
    public final String chooseFile(int requestCode) {
        String fileDir = null;
        if (SDCardUtil.isSDCardAvaiable(mAct)) {
            fileDir = getSavePath();
            Intent intent = null;
            switch (requestCode) {
                case TYPE_PHOTO:
                    sFilePath = fileDir + "/" + createImageFileName();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(sFilePath)));
                    startActivityForResult(intent, TYPE_PHOTO);
                    break;
                case TYPE_ALBUM:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, TYPE_ALBUM);
                    break;
                default:
                    break;
            }
        }
        return sFilePath;  // 不一定有返回值
    }

    private void startActivityForResult(Intent intent, int type) {
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, type);
        } else {
            mAct.startActivityForResult(intent, type);
        }
    }

    /**
     * 是否是文件选择返回
     */
    public static final boolean contain(int requestCode) {
        switch (requestCode) {
            case TYPE_PHOTO:
            case TYPE_ALBUM:
            case TYPE_CROP_IMAGE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 确认文件返回
     *
     * @param requestCode
     * @param data
     */
    public final String resultFile(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }

        String filePath = null;

        switch (requestCode) {
            case TYPE_PHOTO:
                filePath = FileChooseUtil.sFilePath;
                break;
            case TYPE_ALBUM:
                filePath = getMediaPath(mAct, data);
                break;
            case TYPE_CROP_IMAGE:
                filePath = getCropImageViewPath(mAct, data);
                break;
            default:
                break;
        }

        return filePath;
    }

    /**
     * 剪切图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public final void cropImage(int requestCode, int resultCode, Intent data, int width, int height) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");

        switch (requestCode) {
            case TYPE_PHOTO:
                // 需要裁减的图片格式
                try {
                    intent.setDataAndType(Uri.fromFile(new File(FileChooseUtil.sFilePath)), "image/*");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case TYPE_ALBUM:
                if (data == null) {
                    return;
                }

                intent.setData(data.getData());// data是图库选取文件传回的参数
                break;
        }

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, TYPE_CROP_IMAGE);
    }

    /**
     * 获取媒体文件的路径<br />
     *
     * @return
     */
    public static final String getMediaPath(Activity act, Intent data) {
        final ContentResolver resolver = act.getContentResolver();
        final Uri originalUri = data.getData();

        String path = null;
        Cursor cursor = null;

        if ("content".equalsIgnoreCase(originalUri.getScheme())) {
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = resolver.query(originalUri, proj, null, null, null);
                if (cursor == null) {
                    return null;
                }

                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    path = cursor.getString(index);
                }
                ;
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(originalUri.getScheme())) {
            path = originalUri.getPath();
        }

        return path;
    }

    /**
     * 获取截屏之后的图片地址
     *
     * @param context
     * @param data
     * @return
     */
    private String getCropImageViewPath(Context context, Intent data) {
        String path = null;

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            String dir = getSavePath();
            String fileName = createImageFileName();

            path = dir + "/" + fileName;

            boolean isSuccess = savePhotoToSDCard(context, bitmap, dir, fileName);
            if (!isSuccess) {
                return null;
            }

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }

        return path;
    }

    /**
     * 获取文件的路径
     *
     * @return
     */
    private static final String getFilePath(Activity act, Intent data) {
        final ContentResolver resolver = act.getContentResolver();
        final Uri uri = data.getData();

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = resolver.query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        Uri baseUri = Uri.parse("content://media/external/images/media");
        return Uri.withAppendedPath(baseUri, "" + id);
    }

    public static boolean savePhotoToSDCard(Context context, Bitmap photoBitmap, String path, String photoName) {
        boolean flag = false;
        if (SDCardUtil.isSDCardAvaiable(context)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {

                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                }

                flag = true;
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
                flag = false;
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                }
            }
        }

        return flag;
    }

    /**
     * 获取保存的路径
     *
     * @return
     */
    private String getSavePath() {
        String fileDir;
        if (SDCardUtil.isSDCardAvaiable(mAct)) {
            fileDir = FileUtil.getInstance().getDirectory(mAct, mFileDir).getAbsolutePath();
        } else {
            fileDir = "/data/" + mFileDir;
        }

        return fileDir;
    }

    /**
     * 获取文件名称
     *
     * @return
     */
    private static final String createImageFileName() {
        return System.currentTimeMillis() + ".jpg";
    }
}
