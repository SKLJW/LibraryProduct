package com.sk.library.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;


/**
 * @author sk
 */
public class SDCardUtil {
	// SD卡的最小剩余容量大小1MB
	private final static long DEFAULT_LIMIT_SIZE = 1;
	private static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + "/btmedia/fileCache/images";

	private static SDCardUtil instance;
	private final Context context;

	public static SDCardUtil getInstance(Context context) {
		if (instance == null) {
			instance = new SDCardUtil(context);
		}
		return instance;
	}

	public SDCardUtil(Context context) {
		this.context = context;
	}

	public String parseUrl(String url) {
		url = url.substring(url.lastIndexOf("/") + 1, url.length());
		return url;
	}

	// 往sd卡保存一张图片
	public void SaveToSdCard(String filename, Bitmap bitmap) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File folder = new File(SDCARD_PATH);
			if (!folder.exists()) {
				folder.mkdir();
			}
			File file = new File(folder, filename);
			if (file.exists()) {
				// Toast.makeText(context, "图片已经下载过了，在" + file.getPath(), Toast.LENGTH_SHORT).show();
			} else {
				try {
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
					// Toast.makeText(context, "下载成功" + file.getPath(), Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					// Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
					// Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
				}

			}
		} else {
			// Toast.makeText(context, "内存卡不存在", Toast.LENGTH_SHORT).show();
		}
	}

	// 往sd卡保存一张图片
	public void SaveToSdCards(String filename, Bitmap bitmap) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File folder = new File(SDCARD_PATH);
			if (!folder.exists()) {
				folder.mkdir();
			}
			File file = new File(folder, filename);
			if (file.exists()) {
			} else {
				try {
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
		}
	}

	/**
	 * 判断SD卡是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSDCardAvaiable(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (getSDFreeSize() > DEFAULT_LIMIT_SIZE) {
				return true;
			} else {
				// ToastUtil.showToast(context, "SD卡容量不足，请检查");
				return false;
			}
		} else {
			// ToastUtil.showToast(context, "SD卡不存在或者不可用");
			return false;
		}
	}

	/**
	 * 获取SDCard的剩余大小
	 * 
	 * @return 多少MB
	 */
	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 获取SD卡的总容量
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 获取SDCard路径
	 * 
	 * @return
	 */
	public static String getSDCardPath(Context mContext) {
		if (isSDCardAvaiable(mContext)) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return "";
	}
}
