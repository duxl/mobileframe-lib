/*
 * Copyright (C) 2014 
 * 版权所有
 *
 * 功能描述：编辑头像的Activity
 * 		     主要提供拍照和从相册获取图片，可设置是否需要剪切，
 * 		     图片获取成功后，回调onPictureBack方法
 *
 *
 * 创建标识：duxl 20141216
 */
package com.duxl.mobileframe.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 编辑头像的Activity<br />
 * 主要提供拍照和从相册获取图片，可设置是否需要剪切<br />
 * 图片获取成功后，回调{@link #onPictureBack(String)}方法
 * 
 * @author duxl
 * 
 */
public abstract class EditPictureActivity extends Activity {

	private final int mRequestCodeFromCamera = 3001; // 拍照
	private final int mRequestCodeFromPhoto = 3002; // 相册
	private final int mRequestCodeCutImage = 3003; // 裁剪
	private final String mSharedFileName = "cachefile";
	private final String GetFileKey = "getFile";
	private final String CutFileKey = "cutFile";
//	private File mGetFile; // 拍照、相册到的图片文件
//	private File mCutFile; // 裁剪后的图片文件
//	private File saveDir;
	private int mAspectX, mAspectY, mOutputX, mOutputY; // 图片需要裁剪是的参数
	private boolean mIsNeedCut; // 是否需要裁剪图片
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSaveDir();
	}
	
	protected File getSaveDir() {
		File saveDir = new File(Environment.getExternalStorageDirectory(), getPackageName() + "/imgs");
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		return saveDir;
	}

	/**
	 * 拍照、相册到的图片文件
	 * @return
	 */
	protected File fileGet() {
		String filePath = getSharedPreferences(mSharedFileName, Context.MODE_PRIVATE).getString(GetFileKey, "");
		return new File(filePath);
	}
	
	/**
	 * 裁剪后的图片文件
	 * @return
	 */
	protected File fileCut() {
		String filePath = getSharedPreferences(mSharedFileName, Context.MODE_PRIVATE).getString(CutFileKey, "");
		return new File(filePath);
	}
	
	private void createFile() {
		SharedPreferences sp = getSharedPreferences(mSharedFileName, Context.MODE_PRIVATE);
		sp.edit()
		.putString(GetFileKey, new File(getSaveDir(), "get-" + System.currentTimeMillis() + ".jpg").getAbsolutePath())
		.putString(CutFileKey, new File(getSaveDir(), "cut-" + (System.currentTimeMillis() + 1) + ".jpg").getAbsolutePath())
		.commit();
	}

	/**
	 * 显示默认拍照、相册选择对话框，如需裁剪，先调用{@link #setHeadZoomParam setHeadZoomParam(int,
	 * int, int, int)}方法
	 */
	public void showDefaultHeadFromDialog(CharSequence title) {
		AlertDialog.Builder builder = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		} else {
			builder = new AlertDialog.Builder(this);
		}

		builder.setTitle(title);
		String[] items = { "拍照", "从相册" };
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) { // 拍照
					fromCamera();
				} else if (which == 1) { // 从相册
					fromPhoto();
				}
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onPictureBack(null);
			}
		});
		builder.create().show();
	}

	/**
	 * 通过拍照获取图片，如需裁剪，先调用{@link #setHeadZoomParam setHeadZoomParam(int, int, int,
	 * int)}方法
	 */
	public void fromCamera() {
		createFile();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileGet()));
		startActivityForResult(intent, mRequestCodeFromCamera);
	}

	/**
	 * 从相册选择图片，如需裁剪，先调用{@link #setHeadZoomParam setHeadZoomParam(int, int, int,
	 * int)}方法
	 */
	public void fromPhoto() {
		createFile();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, mRequestCodeFromPhoto);
	}

	/**
	 * 设置剪切图片的参数，例如setZoomParam(1, 1, 180, 180)
	 * 
	 * @param aspectX
	 *            是宽高的比例
	 * @param aspectY
	 *            是宽高的比例
	 * @param outputX
	 *            是裁剪图片宽
	 * @param outputY
	 *            是裁剪图片高
	 */
	public void setHeadZoomParam(int aspectX, int aspectY, int outputX, int outputY) {
		mIsNeedCut = true;
		mAspectX = aspectX;
		mAspectY = aspectY;
		mOutputX = outputX;
		mOutputY = outputY;
	}

	/**
	 * 显示截取框
	 * 
	 * @param file
	 */
	private void startPhotoZoom(File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", mAspectX);
		intent.putExtra("aspectY", mAspectY);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", mOutputX);
		intent.putExtra("outputY", mOutputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileCut()));
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, mRequestCodeCutImage);
	}

	/**
	 * 从相册或拍照获取到图片后的下一步处理
	 * 
	 * @param file
	 */
	private void doImage(File file) {
		if (mIsNeedCut) {
			startPhotoZoom(file);
		} else {
			onPictureBack(file.getAbsolutePath());
		}
	}

	private void show(File f) {
		System.out.println("############show################f.begin = [" + f.exists() + "]");
		System.out.println("############show################f.begin = [" + f.length() / 1.0f / 1024 + "KB]");
		Luban.get(this)
				.load(f)                     //传人要压缩的图片
				.putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
				.setCompressListener(new OnCompressListener() { //设置回调

					@Override
					public void onStart() {
						// TODO 压缩开始前调用，可以在方法内启动 loading UI
						System.out.println("############show################f.end = [onStart]");
					}
					@Override
					public void onSuccess(File file) {
						// TODO 压缩成功后调用，返回压缩后的图片文件
						System.out.println("############show################f.end = [onSuccess]");
						System.out.println("############show################f.end = [" + file.exists() + "]");
						System.out.println("############show################f.end = [" + file.length() / 1.0f / 1024  + "KB]");
						System.out.println("############show################f.end = [" + file.getPath() + "]");
						doImage(file);
					}

					@Override
					public void onError(Throwable e) {
						// TODO 当压缩过去出现问题时调用
						System.out.println("############show################f.end = [onError]");
						e.printStackTrace();
					}
				}).launch();    //启动压缩
	}

	private void show3(File f) {
		System.out.println("############show################f.begin = [" + f.exists() + "]");
		System.out.println("############show################f.begin = [" + f.length() / 1.0f / 1024 / 1024 + "M]");
		if(f.exists()) {
			long targetKB = 200; // 目标KB大小200
			long sourceKB = f.length() / 1024;
			int inSampleSize = (int)(sourceKB / targetKB); // 需要将图片缩小的倍数
			if(inSampleSize > 1) {
				Options options = new Options();
				options.inSampleSize = inSampleSize;
				try {
					Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), options);
					FileOutputStream out = new FileOutputStream(fileGet());
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					doImage(fileGet());

					System.out.println("############show################f.end = [" + fileGet().exists() + "]");
					System.out.println("############show################f.end = [" + fileGet().length() / 1.0f / 1024 / 1024 + "M]");
					System.out.println("############show################f.end = [" + fileGet().getPath() + "]");

				} catch (Exception e) {
					e.printStackTrace();
					doImage(f);
				}
			}


		}



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			if (requestCode == mRequestCodeFromCamera) { // 拍照
				show(fileGet());
//				ContentResolver cr = getContentResolver();
//				try {
//					DisplayMetrics dm = new DisplayMetrics();
//					getWindowManager().getDefaultDisplay().getMetrics(dm);
//					int screenWidth = dm.widthPixels;
//					Uri imgUri = Uri.fromFile(fileGet());
//					Options option = ImageUtils.getBitmapOption(cr.openInputStream(imgUri), screenWidth);
//					Bitmap b = ImageUtils.getBitmapFromStream(cr.openInputStream(imgUri), option, screenWidth);
//					createFile();
//					FileOutputStream out = new FileOutputStream(fileGet());
//					b.compress(Bitmap.CompressFormat.JPEG, 100, out);
//					out.flush();
//					out.close();
//					doImage(fileGet());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
			} else if (requestCode == mRequestCodeFromPhoto) { // 相册
				Uri uri = data.getData();

				String[] proj = { MediaStore.Images.Media.DATA };

				Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);

				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				actualimagecursor.moveToFirst();

				String img_path = actualimagecursor.getString(actual_image_column_index);

				File file = new File(img_path);
				show(file);

				// *********************************
//				ContentResolver cr = getContentResolver();
//				try {
//					DisplayMetrics dm = new DisplayMetrics();
//					getWindowManager().getDefaultDisplay().getMetrics(dm);
//					int screenWidth = dm.widthPixels;
//					Options option = ImageUtils.getBitmapOption(cr.openInputStream(data.getData()), screenWidth);
//					Bitmap b = ImageUtils.getBitmapFromStream(cr.openInputStream(data.getData()), option, screenWidth);
//					fileGet().delete();
//					fileGet().createNewFile();
//					FileOutputStream out = new FileOutputStream(fileGet());
//					b.compress(Bitmap.CompressFormat.JPEG, 100, out);
//					out.flush();
//					out.close();
//					doImage(fileGet());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}

			} else if (requestCode == mRequestCodeCutImage) { // 裁剪
				onPictureBack(fileGet().getAbsolutePath());
			}

		} else {
			// 返回，没有获取到任何图片
			if (requestCode == mRequestCodeFromCamera || requestCode == mRequestCodeFromPhoto || requestCode == mRequestCodeCutImage) {
				onPictureBack(null);
			}

		}
	}

	public abstract void onPictureBack(String path);

	private static class ImageUtils {
		public static Options getBitmapOption(InputStream in, int screenWidth) {
			int sampleSize = 1;
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			if (options.outWidth > screenWidth || options.outHeight > screenWidth) {
				if (options.outWidth > options.outHeight) {
					sampleSize = options.outWidth / screenWidth;
				} else {
					sampleSize = options.outHeight / screenWidth;
				}
			}
			options.inDither = false;
			options.inSampleSize = sampleSize;
			options.inTempStorage = new byte[16 * 1024];
			options.inJustDecodeBounds = false;
			return options;
		}

		public static Bitmap getBitmapFromStream(InputStream in, Options options, int screenWidth) {
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, screenWidth, screenWidth);
			return bitmap;
		}
	}
}
