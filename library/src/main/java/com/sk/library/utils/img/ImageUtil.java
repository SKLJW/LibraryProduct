package com.sk.library.utils.img;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;


import com.sk.library.utils.ScreenSizeUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 图片工具类
 *
 * @author sunny
 */
public class ImageUtil {


    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return null;
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     * <p/>
     * </br>修改�?/br>
     * imageButton_fav.setImageBitmap(BitmapUtils.readBitMap(this,
     * R.drawable.guide_fav_1));
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        try {
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * make sure the color data size not more than 5M
     *
     * @param rect
     * @return
     */
    public static boolean makesureSizeNotTooLarge(Rect rect) {
        final int FIVE_M = 5 * 1024 * 1024;
        if (rect.width() * rect.height() * 2 > FIVE_M) {
            // 不能超过5M
            return false;
        }
        return true;
    }

    /**
     * 通过url获取网络图片
     *
     * @param urlStr
     * @return
     */
    public static Bitmap getHeadImage(String urlStr) {
        InputStream is = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        Bitmap headImage = null;
        try {
            URL url = new URL(urlStr);
            is = url.openConnection().getInputStream();
            // FileUtils.saveImageToSDCard(is, urlStr+".png");
            if (is != null) {
                bis = new BufferedInputStream(is);
                bos = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = bis.read(buff)) != -1) {
                    bos.write(buff, 0, len);
                }
                byte[] byteArray = bos.toByteArray();
                headImage = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headImage;
    }

    /**
     * 获取样品
     *
     * @param rect
     * @return
     */
    public static int getSampleSizeOfNotTooLarge(Rect rect) {
        final int FIVE_M = 5 * 1024 * 1024;
        double ratio = ((double) rect.width()) * rect.height() * 2 / FIVE_M;
        return ratio >= 1 ? (int) ratio : 1;
    }

    /**
     * 自适应屏幕大小 得到最大的smapleSize 同时达到此目标： 自动旋转 以适应view的宽高后, 不影响界面显示效果
     *
     * @param vWidth  view width
     * @param vHeight view height
     * @param bWidth  bitmap width
     * @param bHeight bitmap height
     * @return
     */
    public static int getSampleSizeAutoFitToScreen(int vWidth, int vHeight,
                                                   int bWidth, int bHeight) {
        if (vHeight == 0 || vWidth == 0) {
            return 1;
        }

        int ratio = Math.max(bWidth / vWidth, bHeight / vHeight);

        int ratioAfterRotate = Math.max(bHeight / vWidth, bWidth / vHeight);

        return Math.min(ratio, ratioAfterRotate);
    }

    /**
     * 检测是否可以解析成位图
     *
     * @param datas
     * @return
     */
    public static boolean verifyBitmap(byte[] datas) {
        return verifyBitmap(new ByteArrayInputStream(datas));
    }

    /**
     * 检测是否可以解析成位图
     *
     * @param input
     * @return
     */
    public static boolean verifyBitmap(InputStream input) {
        if (input == null) {
            return false;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        input = input instanceof BufferedInputStream ? input
                : new BufferedInputStream(input);
        BitmapFactory.decodeStream(input, null, options);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (options.outHeight > 0) && (options.outWidth > 0);
    }

    /**
     * 检测是否可以解析成位图
     *
     * @param path
     * @return
     */
    public static boolean verifyBitmap(String path) {
        try {
            return verifyBitmap(new FileInputStream(path));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得drawable图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static BitmapDrawable readDrawable(Context context, int resId) {
        return readDrawable(context.getResources(), resId);
    }

    /**
     * 获得drawable图片
     *
     * @param resId
     * @return
     */
    public static BitmapDrawable readDrawable(Resources res, int resId) {
        BitmapDrawable drawable = null;
        Bitmap bitmap = null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Config.ARGB_4444;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        try {
            InputStream ips = res.openRawResource(resId);
            if (ips != null) {
                bitmap = BitmapFactory.decodeStream(ips, null, opts);
            }
            if (bitmap != null) {
                drawable = new BitmapDrawable(res, bitmap);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 获得drawable图片
     *
     * @param res
     * @param file
     * @return
     */
    public static BitmapDrawable readDrawable(Resources res, File file) {
        BitmapDrawable drawable = null;
        Bitmap bitmap = null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Config.ARGB_4444;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        try {
            InputStream ips = new FileInputStream(file);
            if (ips != null) {
                bitmap = BitmapFactory.decodeStream(ips, null, opts);
            }
            if (bitmap != null) {
                drawable = new BitmapDrawable(res, bitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 通过uri获取图片
     *
     * @param context
     * @param uriStr
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapFromUri(Context context, String uriStr)
            throws FileNotFoundException {
        ContentResolver cr = context.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Uri uri = Uri.parse(uriStr);
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),
                null, options);
        return bitmap;
    }

    /**
     * 通过uri获取图片
     *
     * @param context
     * @param uriStr
     * @return
     * @throws FileNotFoundException
     */
    public static Drawable getRoundCornerDrawableFromUri(Context context,
                                                         String uriStr, int pixels) throws FileNotFoundException {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(uriStr);
        InputStream inputStream = cr.openInputStream(uri);
        BitmapDrawable d = (BitmapDrawable) Drawable.createFromStream(
                inputStream, "src");
        return toRoundCorner(d, pixels);
    }

    /**
     * 通过url获取流
     *
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getRequest(String path) {
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static byte[] readInputStream(InputStream inStream) {
        byte[] arr = null;
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        try {
            if (inStream != null) {
                while ((len = inStream.read(buffer)) != -1) {
                    outSteam.write(buffer, 0, len);
                }
            }
            arr = outSteam.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (outSteam != null) {
                    outSteam.close();
                }
                if (outSteam != null) {
                    inStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * 通过url 获取网络上的图片，
     *
     * @param url
     * @return Drawable格式的图片
     * @throws Exception
     */
    public static Drawable loadImageFromUrl(String url) throws Exception {
        URL m;
        InputStream i = null;
        m = new URL(url);
        i = (InputStream) m.getContent();
        Drawable d = Drawable.createFromStream(i, "src");
        i.close();
        return d;
    }

    /**
     * 图片缩放
     *
     * @param bitmap
     * @param dst_w
     * @param dst_h
     * @return
     */
    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {

        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);

        return dstbmp;
    }

    /**
     * 通过url获取图片
     *
     * @return
     * @throws FileNotFoundException
     */
    public static Drawable getDrawableFromUrl(String url) throws Exception {
        return Drawable.createFromStream(getRequest(url), null);
    }

    /**
     * 从网上获取圆角图片
     *
     * @param url
     * @param pixels
     * @return
     * @throws Exception
     */
    public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
            throws Exception {
        InputStream inputStream = getRequest(url);
        Bitmap result = null;
        if (inputStream != null) {
            byte[] bytes = getBytesFromSream(inputStream);
            Bitmap bitmap = byteToBitmap(bytes);
            result = toRoundCorner(bitmap, pixels);
        }
        return result;
    }

    /**
     * 从网上获取圆角图片
     *
     * @param pixels
     * @return
     * @throws Exception
     */
    public static Bitmap getRoundBitmapFromStream(InputStream inputStream,
                                                  int pixels) throws Exception {
        Bitmap result = null;
        if (inputStream != null) {
            byte[] bytes = getBytesFromSream(inputStream);
            Bitmap bitmap = byteToBitmap(bytes);
            result = toRoundCorner(bitmap, pixels);
        }
        return result;
    }

    /**
     * 从网上获取圆角图片
     *
     * @param url
     * @param pixels
     * @return
     * @throws Exception
     */
    public static Drawable getRoundDrawableFromUrl(String url, int pixels)
            throws Exception {
        InputStream inputStream = getRequest(url);
        BitmapDrawable drawable = null;
        if (inputStream != null) {
            byte[] bytes = getBytesFromSream(inputStream);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
            drawable = toRoundCorner(bitmapDrawable, pixels);
        }
        return drawable;
    }

    public static Drawable getRoundDrawableFromStream(InputStream inputStream,
                                                      int pixels) throws Exception {
        BitmapDrawable drawable = null;
        if (inputStream != null) {
            byte[] bytes = getBytesFromSream(inputStream);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
            drawable = toRoundCorner(bitmapDrawable, pixels);
        }
        return drawable;
    }

    /**
     * 从网上获取图片
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        byte[] bytes;
        try {
            bytes = getBytesFromUrl(url);
            if (bytes != null) {
                bitmap = byteToBitmap(bytes);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] getBytesFromUrl(String url) throws Exception {
        return readInputStream(getRequest(url));
    }

    public static byte[] getBytesFromSream(InputStream inputStream)
            throws Exception {
        return readInputStream(inputStream);
    }

    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray != null && byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
        return Drawable.createFromStream(ins, null);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * drawable图片变灰
     *
     * @param drawable
     * @return
     */
    public static Drawable drawableToGrayscale(Drawable drawable) {
        drawable.mutate();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(cf);
        return drawable;
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        if (bitmap != null) {

            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } else {
            return null;
        }
    }

    /**
     * sk
     *
     * @param srcPath 判断图片大小，进行压缩处理 有待优化，根据具体手机分辨率压缩图片，目前给了一个定值1280*720
     * @return
     */
    private static Bitmap zoomImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1280f;
        float ww = 720f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * sk
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static void compressImage(String path) {
        System.out.println(path);
        Bitmap bitmap = getBitmapFromURIs(path);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
            if (bitmap.getByteCount() / 100000 > 5) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * sk
     */
    public static void compressLocalImage(String bigpath, String smallpath) {
        Bitmap bitmap = getBitmapFromURI(bigpath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(smallpath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * sk
     * 图片旋转
     *
     * @param uri picture path
     * @return
     */
    public static Bitmap getBitmapFromURIs(String uri) {
        int degree = readPictureDegree(uri);
        Bitmap bitmap = zoomImage(uri);
        Bitmap roateBitmap = rotaingImageView(degree, bitmap);
        return roateBitmap;
    }


    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
                                               int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    public static Bitmap getBitmapFromURI(String uri, int width, int height) {
        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
         */
        int degree = readPictureDegree(uri);

        Bitmap bitmap = null;
        Bitmap tBitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 1;
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(uri, opt);
        int bitmapSize = opt.outHeight * opt.outWidth * 4;
        opt.inSampleSize = bitmapSize / (1000 * 2000);
        opt.inJustDecodeBounds = false;
        tBitmap = BitmapFactory.decodeFile(uri, opt);

        Bitmap roateBitmap = rotaingImageView(degree, tBitmap);

        bitmap = ThumbnailUtils.extractThumbnail(roateBitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 图片旋转
     *
     * @param uri picture path
     * @return
     */
    public static Bitmap getBitmapFromURI(String uri) {
        int degree = readPictureDegree(uri);
        Bitmap bitmap = getSmallBitmap(uri);
        Bitmap roateBitmap = rotaingImageView(degree, bitmap);
        return roateBitmap;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        // System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param url
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(Context context, int angle, String url) {
        Bitmap resizedBitmap;
        Bitmap bitmap = getSmallBitmap(context, url);
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        Matrix matrixScale = new Matrix();
        matrix.postRotate(angle);
        if (angle != 0) {
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else
            resizedBitmap = bitmap;
        float width = ScreenSizeUtils.getScreenWidth(context);
        float height = ScreenSizeUtils.getScreenHeight(context);
        float bWidth = resizedBitmap.getWidth();
        float bHeight = resizedBitmap.getHeight();
        float wscale = width / bWidth;
        float hscale = height / bHeight;
        float scale = wscale > hscale ? hscale : wscale;
        matrixScale.postScale(scale, scale);
        Bitmap bitmaps = Bitmap.createBitmap(resizedBitmap, 0, 0,
                resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrixScale, true);
        return bitmaps;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            float reqWidth, float reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        } else if (height < reqHeight || width < reqWidth) {
            final int hRatio = Math.round((float) height / (float) reqHeight);
            final int wRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = hRatio > wRatio ? wRatio : hRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 10品质低无法接受

        return byteToBitmap(baos.toByteArray());
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(Context context, String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float width = ScreenSizeUtils.getScreenWidth(context);
        float heith = ScreenSizeUtils.getScreenHeight(context);
        options.inSampleSize = calculateInSampleSize(options, width, heith);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath, float width, float height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }


    /**
     * 存储bitmap到本地文件
     *
     * @param file
     * @param bitmap
     */
    public static void saveBitmap(File file, Bitmap bitmap, CallBack callBack) {
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            if (callBack != null)
                callBack.callBack(file.getAbsolutePath());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface CallBack {
        void callBack(String url);
    }

}
