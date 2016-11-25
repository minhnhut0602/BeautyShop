package mimishop.yanji.com.mimishop.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.dialog.CameraAndPhotoDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectPhotoManager {
    private final String TAG = "SelectPhotoManager";

	/*
     * private final int PHOTO_UPLOAD_START = 0; private final int
	 * PHOTO_UPLOAD_PROGRESS = 1; private final int PHOTO_UPLOAD_FINISH = 2;
	 */

    public final static int PHOTO_SELECT_FAILED_BY_CRASH = 3000;
    public final static int PHOTO_SELECT_FAILED_BY_SIZE_LIMIT = 3001;

    final String SDCARD_FOLDER = "mimishop";
    public static final String CAPTURE_BASE_DIR = "/sdcard/mimishop/";

    private final int MAX_RESOLUTION = 1020; // 1020 px
    private final int MAX_IMAGE_SIZE = 5120; // 5M

    private Activity mActivity = null;
    private Fragment mFragment = null;

    private static Uri mImageCaptureUri = null;
    private static Uri mCropImageUri = null;
    // private String filename = "";
    private Bitmap mUploadImage = null;

    boolean GallaryPhotoSelected = false;

    public final static int PICK_FROM_CAMERA = 1;
    public final static int PICK_FROM_FILE = 2;
    public final static int CROP_FROM_CAMERA = 3;
    private PhotoSelectCallback mCallback = null;



    // private PhotoCallback mCallback = null;

	/*
	 * public interface PhotoCallback { public void onStartUploadImage();
	 * //public void onUploadingImage(int progress); public void
	 * onFinishUploadImage(Bitmap image); public void onFailedUploadImage(int
	 * errorCode, String err); //0 is Crashed when select image, 1 is File Size
	 * Overflow }
	 * 
	 * public void setPhotoCallback(PhotoCallback cb) { mCallback = cb; }
	 */

    public interface PhotoSelectCallback {
        public void onSelectImageDone(Bitmap image, File file);

        public void onFailedSelectImage(int errorCode, String err);

        public void onDeleteImage();
    }

    public void setPhotoSelectCallback(PhotoSelectCallback cb) {
        mCallback = cb;
    }

    protected SelectPhotoManager() {
        // Exists only to defeat instantiation.
    }

    public SelectPhotoManager(Activity activity) {
        mActivity = activity;
    }

    public SelectPhotoManager(Fragment fragment) {
        mFragment = fragment;
        mActivity = mFragment.getActivity();
    }

    /*public void ShowPhotoActionSheet(final String imageName,
                                     final Bitmap bitmap,
                                     final boolean mBitmapDeleteFlag,
                                     final int xRatio,
                                     final int yRatio) {
        try {

            new CameraAndPhotoDialog(mActivity, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.ib_dialog_close:
                            break;

                        case R.id.ib_photo_album:
                            doPickFromGallery();
                            break;

                        case R.id.ib_photo_camera:
                            doTakePicture();
                            break;
                    }
                }
            }).show();

        } catch (final Exception ex) {
            Log.d(TAG, "error occured in runOnUiThread" + ex.toString());
        }
    }*/

    public void ShowPhotoActionSheet(final String imageName,
                                     final Bitmap bitmap, final boolean mBitmapDeleteFlag) {
        try {
            String[] items;
            ArrayAdapter<String> adapter;
            AlertDialog.Builder builder;
            AlertDialog dialog;

            String option1 = mActivity.getResources().getString(
                    R.string.photo_option_1);
            String option2 = mActivity.getResources().getString(
                    R.string.photo_option_2);
            String option3 = mActivity.getResources().getString(
                    R.string.photo_option_3);
            String title = mActivity.getResources().getString(
                    R.string.photo_select_title);

            if ((imageName.length() > 0 || bitmap != null)
                    && mBitmapDeleteFlag == false) {
                items = new String[]{option1, option2, option3};
            } else {
                items = new String[]{option1, option2};
            }
            adapter = new ArrayAdapter<String>(mActivity,
                    android.R.layout.select_dialog_item, items);
            builder = new AlertDialog.Builder(mActivity);

            builder.setTitle(title);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Take a picture
                    if (item == 0) {
                        doTakePicture();
                    }
                    // Select photo from gallery
                    else if (item == 1) {
                        doPickFromGallery();
                    }
                    // Delete image
                    else {
                        mCallback.onDeleteImage();
                    }
                    dialog.cancel();
                }
            });

            dialog = builder.create();
            dialog.show();
        } catch (final Exception ex) {
            Log.d(TAG, "error occured in runOnUiThread" + ex.toString());
        }
    }

    public void ShowPhotoAlbumActionSheet(final String imageName,
                                          final Bitmap bitmap, final boolean mBitmapDeleteFlag) {
        if ((imageName.length() > 0 || bitmap != null)
                && mBitmapDeleteFlag == false) {
        } else {
            doPickFromGallery();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    // code runs in a thread
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] items;
                            ArrayAdapter<String> adapter;
                            AlertDialog.Builder builder;
                            AlertDialog dialog;

                            String option1 = mActivity.getResources()
                                    .getString(R.string.photo_option_2);
                            String option2 = mActivity.getResources()
                                    .getString(R.string.photo_option_3);
                            String title = mActivity.getResources().getString(
                                    R.string.photo_select_title);

                            items = new String[]{option1, option2};
                            adapter = new ArrayAdapter<String>(mActivity,
                                    android.R.layout.select_dialog_item, items);
                            builder = new AlertDialog.Builder(mActivity);

                            builder.setTitle(title);
                            builder.setAdapter(adapter,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int item) {
                                            // Select photo from gallery
                                            if (item == 0) {
                                                doPickFromGallery();
                                            }
                                            // Delete image
                                            else {
                                                mCallback.onDeleteImage();
                                            }
                                            dialog.cancel();
                                        }
                                    });

                            dialog = builder.create();
                            dialog.show();
                        }
                    });
                } catch (final Exception ex) {
                    Log.d(TAG, "error occured in runOnUiThread" + ex.toString());
                }
            }
        }.start();
    }

    public void doTakePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String filename = ts + ".png";

        File folder = new File(Environment.getExternalStorageDirectory()
                + "/" + SDCARD_FOLDER);
        folder.mkdirs();
        File file = new File(folder.toString(), filename);
        mImageCaptureUri = Uri.fromFile(file);

        try {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            // intent.putExtra("return-data", true);

            // Utility.setSelectingPhotoState(true);
            mActivity.startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        // Utility.setSelectingPhotoState(true);
        mActivity.startActivityForResult(intent, PICK_FROM_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            // Utility.setSelectingPhotoState(false);
            return;
        }

        if (requestCode == CROP_FROM_CAMERA) {
            Bundle extras = data.getExtras();
			/*
			 * Long tsLong = System.currentTimeMillis()/1000; String ts =
			 * tsLong.toString(); String cropFilename = ts+".png";
			 */

            if (extras != null) {
				/*
				 * Bitmap photo = extras.getParcelable("data"); // crop된 bitmap
				 *
				 * mUploadImage = resizeBitmapImage(photo, MAX_RESOLUTION);
				 * storeCropImage(mUploadImage, cropFilename);
				 */

                BitmapFactory.Options onlyBoundsOptions = getBitmapFactoryFromUri(mCropImageUri);
                if ((onlyBoundsOptions.outWidth == -1)
                        || (onlyBoundsOptions.outHeight == -1))
                    return;
                double ratio = getRatio(onlyBoundsOptions);

                Bitmap bitmap = getResizeBitmapFromUriWithRatio(mCropImageUri,
                        ratio);
                storeCropImage(bitmap, mCropImageUri.getPath());

                // ---------------Used Media Scanner-----------
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        mActivity.sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://"
                                        + Environment
                                        .getExternalStorageDirectory()
                                        + "/" + SDCARD_FOLDER)));
                    } else {
                        Intent mediaScanIntent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(
                                Environment.getExternalStorageDirectory()
                                        + "/" + SDCARD_FOLDER);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        mActivity.sendBroadcast(mediaScanIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) {
                    file.delete();
                }
            }

            // Utility.setSelectingPhotoState(false);
            return;

        } else if (requestCode == PICK_FROM_FILE) {
            GallaryPhotoSelected = true;
            mImageCaptureUri = data.getData();
            doCrop();

        } else {
            // PICK_FROM_CAMERA
            GallaryPhotoSelected = false;

            BitmapFactory.Options onlyBoundsOptions = getBitmapFactoryFromUri(mImageCaptureUri);
            if ((onlyBoundsOptions.outWidth == -1)
                    || (onlyBoundsOptions.outHeight == -1))
                return;
            double ratio = getRatio(onlyBoundsOptions);

            Bitmap bitmap = getResizeBitmapFromUriWithRatio(mImageCaptureUri,
                    ratio);
            bitmap = checkOrientationAndRotate(bitmap,
                    mImageCaptureUri.getPath());

            saveBitmapToFile(bitmap, mImageCaptureUri.getPath());

            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    mActivity.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                            + Environment.getExternalStorageDirectory()
                            + "/" + SDCARD_FOLDER)));
                } else {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(Environment.getExternalStorageDirectory()
                            + "/" + SDCARD_FOLDER);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    mActivity.sendBroadcast(mediaScanIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            doCrop();
        }
    }

    public String getCacheFileName() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String filename = ts + ".png";

			/*
			 * File file = new
			 * File(Environment.getExternalStorageDirectory()+"/vinapang",
			 * filename);
			 */
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/" + SDCARD_FOLDER);
        folder.mkdirs();
        File file = new File(folder.toString(), filename);
        return file.getAbsolutePath();
    }

    public Bitmap getCachePhotoFromGallary(Uri uri, String cachePath){
        try {
            BitmapFactory.Options onlyBoundsOptions = getBitmapFactoryFromUri(uri);
            if ((onlyBoundsOptions.outWidth == -1)
                    || (onlyBoundsOptions.outHeight == -1))
                return null;
            double ratio = getRatio(onlyBoundsOptions);

            Bitmap bitmap = getResizeBitmapFromUriWithRatio(uri, ratio);
            bitmap = checkOrientationAndRotate(bitmap, uri.getPath());
            storeCropImage(bitmap, cachePath);

            return bitmap;
        }
        catch (Exception e) {
            Toast.makeText(mActivity, "이미지를 창조할수 없습니다.", Toast.LENGTH_SHORT);
        }
        return  null;
    }

    private Bitmap getPhotoFromGallary(Uri uri) {
        //return  getResizeBitmapFromUriWithRatio(uri, 1.0);
        try {
            Bitmap bitmap = getResizeBitmapFromUriWithRatio(uri, MAX_RESOLUTION);
            return bitmap;
        }
        catch (Exception e) {
            Toast.makeText(mActivity, "이미지를 창조할수 없습니다.", Toast.LENGTH_SHORT);
        }
        return  null;
    }

    public Bitmap getPhotoFromCamera(String capture_path){

        File file = new File(capture_path);

        if(file.isFile() == false) {
            return null;
        }

        Bitmap bitmap = getCameraBitmapFromPath(capture_path);

        int rotate = getRightAngleOfImage(capture_path);
        bitmap = getResizeAndRotateBitmap(bitmap, 1.0f, rotate);

        saveBitmapToFile(bitmap, capture_path);

        return bitmap;
    }

    public void saveBitmapToFile(Bitmap bitmap, String filepath) {
        OutputStream fOut = null;
        File file = new File(filepath);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        bitmap.compress(CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Bitmap getResizeBitmapFromUriWithRatio(Uri uri, final int requiredSize)
            throws FileNotFoundException {

        Context c = null;
        if(mActivity != null) {
            c = mActivity;
        }
        else {
            c = mFragment.getActivity();
        }

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int originalSize = (o.outHeight > o.outWidth) ? o.outHeight : o.outWidth;

        double ratio = (originalSize > requiredSize) ? (originalSize / requiredSize) : 1.0;

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        o2.inDither=true;//optional
        o2.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        Bitmap bitmap =  BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);

        return bitmap;
    }


    public Bitmap getResizeBitmapFromUriWithRatio(Uri uri, double ratio) {
        InputStream input = null;
        try {
            input = mActivity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public double getRatio(BitmapFactory.Options onlyBoundsOptions) {
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
                : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > MAX_RESOLUTION) ? (originalSize/MAX_RESOLUTION)
                : 1.0;
        return ratio;
    }

    public Bitmap getCameraBitmapFromPath(String path) {
        if(false) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            return BitmapFactory.decodeFile(path, options);
        }

        // camera store the capture image into file. so must resize when decoding.
        try
        {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(path);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(path);
            options = new BitmapFactory.Options();

            boolean landscape = false;

            if(inWidth > inHeight) {
                landscape = true;
            }

            float scale_factor = 1.0f;
            if (landscape == true) {
                scale_factor = (float) MAX_RESOLUTION / inWidth;
            }
            else  {
                scale_factor = (float) MAX_RESOLUTION / inHeight;
            }

            int dstWidth = (int)(scale_factor * inWidth);
            int dsHeight = (int)(scale_factor * inHeight);

            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dsHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dsHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            if(false) {
                try
                {
                    FileOutputStream out = new FileOutputStream(path);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                }
                catch(Exception e)
                {
                    Log.e("Image", e.getMessage(), e);
                }
            }

            return resizedBitmap;
        }
        catch (IOException e)
        {
            Log.e("Image", e.getMessage(), e);
        }
        return null;
    }

    private int getRightAngleOfImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return degree;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }



    public Bitmap cropCenterBitmap(Bitmap bitmap, String filepath) {
        final int IMAGE_SIZE = 255;
        boolean landscape = false;

        if(bitmap.getWidth() > bitmap.getHeight()) {
            landscape = true;
        }

        float scale_factor;
        if (landscape == true) {
            scale_factor = (float) IMAGE_SIZE / bitmap.getHeight();
        }
        else  {
            scale_factor = (float) IMAGE_SIZE / bitmap.getWidth();
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale_factor, scale_factor);

        int degree = getRightAngleOfImage(filepath);
        if(degree != 0) {
            matrix.postRotate(degree);
        }

        Bitmap croppedBitmap;
        if (landscape == true) {
            int start = (bitmap.getWidth() - bitmap.getHeight()) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, start, 0, bitmap.getHeight(), bitmap.getHeight(), matrix, true);
        } else {
            int start = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, 0, start, bitmap.getWidth(), bitmap.getWidth(), matrix, true);
        }

        return croppedBitmap;
    }

    public Bitmap cropCenterBitmap(Bitmap bitmap) {
        final int IMAGE_SIZE = 255;
        boolean landscape = false;

        if(bitmap.getWidth() > bitmap.getHeight()) {
            landscape = true;
        }

        float scale_factor;
        if (landscape == true) {
            scale_factor = (float) IMAGE_SIZE / bitmap.getHeight();
        }
        else  {
            scale_factor = (float) IMAGE_SIZE / bitmap.getWidth();
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale_factor, scale_factor);

        Bitmap croppedBitmap;
        if (landscape == true) {
            int start = (bitmap.getWidth() - bitmap.getHeight()) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, start, 0, bitmap.getHeight(), bitmap.getHeight(), matrix, true);
        } else {
            int start = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, 0, start, bitmap.getWidth(), bitmap.getWidth(), matrix, true);
        }

        return croppedBitmap;
    }

    public Bitmap getResizeAndRotateBitmap(Bitmap bitmap, float ratio, int rotate) {

        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);

        int degree = rotate;
        if(degree != 0) {
            matrix.postRotate(degree);
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public BitmapFactory.Options getBitmapFactoryFromUri(Uri uri) {
        InputStream input = null;
        try {
            input = mActivity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return onlyBoundsOptions;
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public Bitmap checkOrientationAndRotate(Bitmap bitmap, String filename) {
        ExifInterface ei = null;
        int orientation = -1;
        try {
            ei = new ExifInterface(filename);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                break;
        }
        return bitmap;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj,
                null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, "image/*");

        List<ResolveInfo> list = mActivity.getPackageManager()
                .queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Log.d(TAG, "Can not find image crop app!");
            return;
        } else {
            // intent.putExtra("outputX", 0);
            // intent.putExtra("outputY", 0);
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            // intent.putExtra("return-data", true);

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            String filename = ts + ".png";

			/*
			 * File file = new
			 * File(Environment.getExternalStorageDirectory()+"/vinapang",
			 * filename);
			 */
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/" + SDCARD_FOLDER);
            folder.mkdirs();
            File file = new File(folder.toString(), filename);
            mCropImageUri = Uri.fromFile(file);

            intent.putExtra("return-data", false);
            // intent.putExtra("output", mCropImageUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri);
            intent.putExtra("outputFormat",
                    CompressFormat.PNG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);

            i.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));

            // Utility.setSelectingPhotoState(true);
            mActivity.startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    public void RemoveImage(String name) {
		/*
		 * File file = new
		 * File(Environment.getExternalStorageDirectory()+"/vinapang", name);
		 */
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/" + SDCARD_FOLDER);
        folder.mkdirs();
        File file = new File(folder.toString(), name);

        if (file != null && file.exists()) {
            Log.d(TAG, "Remove previous temp profile file.");
            file.delete();
        }
    }

    /**
     * Bitmap이미지의 가로, 세로 사이즈를 리사이징 한다.
     *
     * @param source        원본 Bitmap 객체
     * @param maxResolution 제한 해상도
     * @return 리사이즈된 이미지 Bitmap 객체
     */
    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(CompressFormat.PNG, 100, out);

            int file_size = Integer
                    .parseInt(String.valueOf(copyFile.length() / 1024));
            if (file_size > MAX_IMAGE_SIZE) {
                mCallback.onFailedSelectImage(
                        PHOTO_SELECT_FAILED_BY_SIZE_LIMIT,
                        mActivity.getResources().getString(
                                R.string.image_max_size_limit_msg));
                if (copyFile != null && copyFile.exists()) {
                    copyFile.delete();
                }
            }

            out.flush();
            out.close();

            if(mCallback != null) {
                mCallback.onSelectImageDone(bitmap, copyFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Sorry, Camera Crashed in createNewFile");
            mCallback.onFailedSelectImage(PHOTO_SELECT_FAILED_BY_CRASH,
                    e.toString());
            mUploadImage = null;
            if (copyFile != null && copyFile.exists()) {
                copyFile.delete();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }

    public void startCameraCropActivity(Activity activity, Uri uri, int req) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        //indicate image type and Uri
        cropIntent.setDataAndType(uri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);

        //start the activity - we handle returning in onActivityResult
        activity.startActivityForResult(cropIntent, req);
    }

    public void startGallaryActivity(Activity context, int reqCode) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, reqCode);
    }

    public void startGallaryActivity(Fragment context, int reqCode) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, reqCode);
    }

    public boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        if (false) {
            return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
                    + android.os.Build.DEVICE);
        }

        return true;

    }

    public String startCaptureActivity(Activity context, int reqCode, String filename) {

        File file = new File(CAPTURE_BASE_DIR);
        if(file.exists() != false) {
            file.mkdir();
        }

        String path = CAPTURE_BASE_DIR + filename;

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasImageCaptureBug()) {
            i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        } else {
            i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        context.startActivityForResult(i, reqCode);

        return path;
    }

    public String startCaptureActivity(Fragment context, int reqCode, String filename) {
        File file = new File(CAPTURE_BASE_DIR);
        if(file.exists() != false) {
            file.mkdir();
        }

        String path = CAPTURE_BASE_DIR + filename;

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasImageCaptureBug()) {
            i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        } else {
            i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        context.startActivityForResult(i, reqCode);

        return path;
    }
}
