package anton.kizema.lazersample.helper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 25.08.2015.
 */
public class BitmapHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        return getRoundedCornerBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth()/2);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        return getRoundedCornerBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), pixels);
    }

    public static Bitmap createCopyBtm(Bitmap originalBtm) {
        int width = originalBtm.getWidth();
        int height = originalBtm.getHeight();
        Bitmap newBtm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width * height];
        originalBtm.getPixels(pixels, 0, width, 0, 0, width, height);

        newBtm.setPixels(pixels, 0, width, 0, 0, width, height);

        return newBtm;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Drawable resize(Resources res, Drawable image, int newW, int newH) {
        return new BitmapDrawable(res, resizeBitmap(image, newW, newH));
    }

    public static Bitmap resizeBitmap(Drawable image, int newW, int newH) {
        Bitmap b = BitmapHelper.drawableToBitmap(image);
        return Bitmap.createScaledBitmap(b, newW, newH, false);
    }

    public static Drawable resizeRotate(Resources res, Drawable image, int newW, int newH, int rotation) {
        Bitmap bitmapResized = resizeBitmap(image, newW, newH);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapResized, 0, 0, bitmapResized.getWidth(), bitmapResized.getHeight(), matrix, true);

        return new BitmapDrawable(res, rotatedBitmap);
    }

    public static byte[] bitmapToByteArray(Bitmap result){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static void refreshGallery(Context c, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        c.sendBroadcast(mediaScanIntent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int w, int h, int pixels) {
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap decodeSampledBitmapFromFilename(String fileName, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        options.inSampleSize = BitmapHelper.calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        Bitmap myBitmap = BitmapFactory.decodeFile(fileName, options);

        return myBitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image

        Log.d("ANT", "options.outHeight : " + options.outHeight + "  options.outWidth : " + options.outWidth);
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((height / inSampleSize) > reqHeight
                && (width / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }

        return inSampleSize;
    }

    /**
     * Use this to get requred bitmap. But after you will have to call Bitmap.createScaledBitmap with matrix, to ensure setting correct sizes
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.d("ANT", "options.inSampleSize : " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeAndSetWidthHeight(Bitmap btm, int reqWidth, int reqHeight){
        Matrix m = new Matrix();
        RectF inRect = new RectF(0, 0, btm.getWidth(), btm.getHeight());
        RectF outRect = new RectF(0, 0, reqWidth, reqHeight);
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.FILL);
        float[] values = new float[9];
        m.getValues(values);

        return Bitmap.createScaledBitmap(btm, (int) (btm.getWidth() * values[0]), (int) (btm.getHeight() * values[4]), true);
    }

    /**
     * Use this to get requred with EXACT W and H bitmap
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeAndSetWidthHeight(Resources res, int resId, int reqWidth, int reqHeight){
        Bitmap btm = BitmapHelper.decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight);
        return decodeAndSetWidthHeight(btm, reqWidth, reqHeight);
    }

    public static File bitmapToFile(Bitmap bitmap){
        File outFile = FileHelper.getImageFilePNG();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outFile;
    }

    public static Bitmap cropBitmapToBoundingBox(Bitmap picToCrop, int unusedSpaceColor) {
        int[] pixels = new int[picToCrop.getHeight() * picToCrop.getWidth()];
        int marginTop = 0, marginBottom = 0, marginLeft = 0, marginRight = 0, i;
        picToCrop.getPixels(pixels, 0, picToCrop.getWidth(), 0, 0,
                picToCrop.getWidth(), picToCrop.getHeight());

        for (i = 0; i < pixels.length; i++) {
            if (pixels[i] != unusedSpaceColor) {
                marginTop = i / picToCrop.getWidth();
                break;
            }
        }

        outerLoop1: for (i = 0; i < picToCrop.getWidth(); i++) {
            for (int j = i; j < pixels.length; j += picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginLeft = j % picToCrop.getWidth();
                    break outerLoop1;
                }
            }
        }

        for (i = pixels.length - 1; i >= 0; i--) {
            if (pixels[i] != unusedSpaceColor) {
                marginBottom = (pixels.length - i) / picToCrop.getWidth();
                break;
            }
        }

        outerLoop2: for (i = pixels.length - 1; i >= 0; i--) {
            for (int j = i; j >= 0; j -= picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginRight = picToCrop.getWidth()
                            - (j % picToCrop.getWidth());
                    break outerLoop2;
                }
            }
        }

        return Bitmap.createBitmap(picToCrop, marginLeft, marginTop,
                picToCrop.getWidth() - marginLeft - marginRight,
                picToCrop.getHeight() - marginTop - marginBottom);
    }

    public interface OnFileSavedListener{
        void onComplete(File file);
    }

    /**
     * Exception can be thrown since we call this method from background thread
     * @param iv View to take image from
     * @return iv onDraw() method on this bitmap's canvas
     */
    private static Bitmap getViewBitmap(View iv){
        Bitmap result = null;

        try {
            result = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(result);
            iv.draw(c);
        }catch (Exception ex){
            //do nothing
        }

        return result;
    }

    public static class SaveImageViewBitmapAsyncTask extends AsyncTask<Void, Void, File> {

        private View iv;
        private OnFileSavedListener listener;

        public SaveImageViewBitmapAsyncTask(View iv, OnFileSavedListener listener) {
            this.iv = iv;
            this.listener = listener;
        }

        @Override
        protected File doInBackground(Void ... p) {
            Bitmap result = getViewBitmap(iv);
            if (result == null){
                return null;
            }

            return bitmapToFile(result);
        }

        @Override
        protected void onPostExecute(File file) {
            if (file == null){
                Bitmap result = getViewBitmap(iv);
                file = bitmapToFile(result);
            }

            listener.onComplete(file);
            refreshGallery(iv.getContext(), file);
        }
    }

    /**
     * Crop and save as a file only visible part of ImageView
     */
    public static class SaveCropImageViewBitmapAsyncTask extends AsyncTask<Void, Void, File> {

        private View iv;
        private OnFileSavedListener listener;

        public SaveCropImageViewBitmapAsyncTask(View iv, OnFileSavedListener listener) {
            this.iv = iv;
            this.listener = listener;
        }

        @Override
        protected File doInBackground(Void ... p) {
            Bitmap result = getViewBitmap(iv);
            if (result == null){
                return null;
            }

            return bitmapToFile(cropBitmapToBoundingBox(result, Color.TRANSPARENT));
        }

        @Override
        protected void onPostExecute(File file) {
            if (file == null){
                Bitmap result = getViewBitmap(iv);
                file = bitmapToFile(cropBitmapToBoundingBox(result, Color.TRANSPARENT));
            }

            listener.onComplete(file);
            refreshGallery(iv.getContext(), file);
        }
    }

    public static class DrawableToFileTask extends AsyncTask<Void, Void, File> {

        private Bitmap bitmap;
        private OnFileSavedListener listener;

        public DrawableToFileTask(Bitmap layerDrawable, OnFileSavedListener listener) {
            this.bitmap = layerDrawable;
            this.listener = listener;
        }

        @Override
        protected File doInBackground(Void... p) {
            return bitmapToFile(bitmap);
        }

        @Override
        protected void onPostExecute(File outFile) {
            listener.onComplete(outFile);
        }
    }
}

