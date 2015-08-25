package anton.kizema.lazersample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import anton.kizema.lazersample.helper.BitmapHelper;
import anton.kizema.lazersample.helper.UIHelper;

public class GameImageView extends ImageView {

    private static final int ERROR_IMAGE_W = UIHelper.getW();
    private static final int ERROR_IMAGE_H = UIHelper.getH();
    private static final int ERROR_IMAGE = R.drawable.auth_background;

    private static final int CLICK = 3;

    // Remember some things for zooming
    private PointF last = new PointF();
    private PointF start = new PointF();
    private float minScale = 1f;
    private float maxScale = 3f;
    private float[] m;
    private int viewWidth, viewHeight;
    private float saveScale = 1f;

    private MODE mode = MODE.NONE;
    private Matrix matrix;
    private int drawableRotation = 0;

    private float origWidth, origHeight;
    private int oldMeasuredWidth, oldMeasuredHeight;
    private ScaleGestureDetector mScaleDetector;

    private String fileName;

    private boolean centerCrop = false;

    private Bitmap readyBitmap;

    private List<TouchImageViewCallback> touchImageViewCallback;
    private OnNoScaleTouchListener onNoScaleTouchListener;
    private OnReadyListener onReadyListener;

    private ImageTouchListener imageTouchListener;

    public enum MODE {
        NONE, DRAG, ZOOM
    }

    public interface TouchImageViewCallback {
        void applyMatrix(Matrix matrix);

        void onBitmapSizesCounted(Matrix m, int w, int h);
    }

    public interface OnReadyListener {
        void onReady();
    }

    public interface OnNoScaleTouchListener {
        void onTouch(MotionEvent event);
    }

    public void setOnNoScaleTouchListener(OnNoScaleTouchListener onNoScaleTouchListener) {
        this.onNoScaleTouchListener = onNoScaleTouchListener;
    }

    public GameImageView(Context context, Bitmap btm) {
        super(context);

        readyBitmap = btm;
        setImageBitmap(readyBitmap);

        touchImageViewCallback = new LinkedList<>();
        sharedConstructing(context);
    }

    public void addTouchImageViewCallback(TouchImageViewCallback touchImageViewCallback) {
        this.touchImageViewCallback.add(touchImageViewCallback);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);

        scaleListener = new ScaleListener();
        mScaleDetector = new ScaleGestureDetector(context, scaleListener);
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        imageTouchListener = new ImageTouchListener();
        setOnTouchListener(imageTouchListener);
    }

    ScaleListener scaleListener;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (TouchImageViewCallback callback : touchImageViewCallback) {
            callback.applyMatrix(getImageMatrix());
        }
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = MODE.ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();

            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            } else {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            }
            fixTrans();
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }


    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;
        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private class BitmapWorkerTask extends AsyncTask<Void, Void, Drawable> {
        BitmapFactory.Options options;

        private volatile Context context;
        private volatile Resources res;

        public BitmapWorkerTask(BitmapFactory.Options options) {
            this.options = options;

            context = getContext();
            res = getResources();
        }

        @Override
        protected Drawable doInBackground(Void... p) {
            options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(fileName, options);

            if (myBitmap == null) {
                return context.getResources().getDrawable(ERROR_IMAGE);
            }

            Drawable dr = new BitmapDrawable(context.getResources(), myBitmap);
            dr = BitmapHelper.resizeRotate(res, dr, myBitmap.getWidth(), myBitmap.getHeight(), drawableRotation);
            return dr;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            Log.d("ANT", " = FINISHED = ");
            setImageDrawable(drawable);
            forceLayout();
        }
    }

    private Point savedDrawableSize = new Point();
    private BitmapWorkerTask task;

    private int c = 0;

    public Point adjustDrawable() {
        ++c;

        if (c > 1) {
            return savedDrawableSize;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        options.inSampleSize = BitmapHelper.calculateInSampleSize(options, viewWidth / 2, (viewHeight) / 2);
//        options.inSampleSize = BitmapHelper.calculateInSampleSize(options, viewWidth, viewHeight);
        Log.d("ANT", "viewWidth " + viewWidth + "    viewHeight " + viewHeight + "   options.inSampleSize :" + options.inSampleSize);

        Point ret = new Point();
        ret.x = options.outWidth / options.inSampleSize;
        ret.y = options.outHeight / options.inSampleSize;

        //if we have an error drawable, thene init with default value
        if (ret.x == 0 || ret.y == 0) {
            Log.d("ANT", " ret.x == 0 && ret.y == 0 ");
            ret.x = ERROR_IMAGE_W;
            ret.y = ERROR_IMAGE_H;
        }

        savedDrawableSize.x = ret.x;
        savedDrawableSize.y = ret.y;

        if (drawableRotation == 90 || drawableRotation == 270) {
            savedDrawableSize.x = ret.y;
            savedDrawableSize.y = ret.x;
        }

        Log.d("ANT", "outWidth : " + options.outWidth + "    outHeight: " + options.outHeight + "   inSampleSize:" + options.inSampleSize);

        if (task != null) {
            if (!task.isCancelled()) {
                task.cancel(true);
            }
            task = new BitmapWorkerTask(options);
            task.execute();
            return ret;
        }

        task = new BitmapWorkerTask(options);
        task.execute();

        return ret;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (oldMeasuredWidth == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;

        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;
            Point p;

            if (readyBitmap != null) {
                p = new Point();
                p.x = readyBitmap.getWidth();
                p.y = readyBitmap.getHeight();
            } else {
                p = adjustDrawable();
            }

            if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 || getDrawable().getIntrinsicHeight() == 0) {
                return;
            }

            int bmWidth = p.x;
            int bmHeight = p.y;

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;

            scale = Math.min(scaleX, scaleY);
            if (centerCrop) {
                scale = Math.max(scaleX, scaleY);
            }

            matrix.setScale(scale, scale);
            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;
            matrix.postTranslate(redundantXSpace, redundantYSpace);
            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;

            setImageMatrix(matrix);

            for (TouchImageViewCallback callback : touchImageViewCallback) {
                callback.onBitmapSizesCounted(matrix, bmWidth, bmHeight);
            }

            if (onReadyListener != null) {
                onReadyListener.onReady();
            }
        }
        fixTrans();
    }

    public void setOnReadyListener(OnReadyListener onReadyListener) {
        this.onReadyListener = onReadyListener;
    }

    private class ImageTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());

            if (saveScale == 1 && onNoScaleTouchListener != null) {
                onNoScaleTouchListener.onTouch(event);
                return true;
            }

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Log.v("TOUCH", "ACTION_DOWN");
                    last.set(curr);
                    start.set(last);
                    mode = MODE.DRAG;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.v("TOUCH", "ACTION_MOVE");

                    if (event.getPointerCount() >= 2 || mode == MODE.DRAG) {
                        float deltaX = curr.x - last.x;
                        float deltaY = curr.y - last.y;
                        float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                        float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);

                        matrix.postTranslate(fixTransX, fixTransY);

                        fixTrans();
                        last.set(curr.x, curr.y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.v("TOUCH", "ACTION_UP");
                    mode = MODE.NONE;
                    int xDiff = (int) Math.abs(curr.x - start.x);
                    int yDiff = (int) Math.abs(curr.y - start.y);
                    if (xDiff < CLICK && yDiff < CLICK)
                        performClick();
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.v("TOUCH", "ACTION_POINTER_UP");
                    mode = MODE.NONE;
                    break;
            }

            setImageMatrix(matrix);
            return true; // indicate event was handled
        }
    }
}
