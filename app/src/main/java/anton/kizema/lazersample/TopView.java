package anton.kizema.lazersample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import anton.kizema.lazersample.elements.Borders;
import anton.kizema.lazersample.elements.DrawingField;
import anton.kizema.lazersample.helper.UIHelper;
import anton.kizema.lazersample.matrix.GameMatrix;

public class TopView extends View implements GameImageView.TouchImageViewCallback {

    public static int MAX_DIST_TOUCH = UIHelper.getW()/40;
    private static int MAX_TIME_TOUCH = 300;//0.3 seconds

    private GameMatrix gameMatrix;
    private DrawingField drawingField;
    private Borders borders;

    private Matrix matrix;

    public TopView(Context context) {
        super(context);

        init();
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        gameMatrix = GameMatrix.createDumpMatrix();
        drawingField = new DrawingField(gameMatrix);
        borders = new Borders(gameMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(matrix);

        super.onDraw(canvas);

        //here we should draw all game stuff
        drawingField.onDraw(canvas);
        borders.onDraw(canvas);

        canvas.restore();
    }

    @Override
    public void applyMatrix(Matrix matrix) {
        this.matrix = matrix;
        invalidate();
    }

    @Override
    public void onBitmapSizesCounted(Matrix matrix, int w, int h) {
        this.matrix = matrix;
    }


    private PointF downPoint = new PointF();
    private PointF origCoordinates = new PointF();
    private long timeOnDown;
    private boolean singleFinger = true;

    @Override
    public void onSendTouch(MotionEvent event) {
        //apply backwards transformation
        MotionEvent ev = MotionEvent.obtain(event);
        Matrix m = new Matrix();
        matrix.invert(m);
        ev.transform(m);

        float x = ev.getX();
        float y = ev.getY();

        ev.recycle();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("TOPVIEW", "ACTION_DOWN x:"+x+", y:"+y);

                singleFinger = true;
                downPoint.x = x;
                downPoint.y = y;

                origCoordinates.x = event.getX();
                origCoordinates.y = event.getY();

                timeOnDown = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v("TOPVIEW", "ACTION_POINTER_DOWN");
                singleFinger = false;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.v("TOPVIEW", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.v("TOPVIEW", "ACTION_UP");

                if ( canBeConsideredAsClick(event) && System.currentTimeMillis() - timeOnDown < MAX_TIME_TOUCH && singleFinger){

                    //We recognize this as touch
                    Log.d("TOPVIEW", "showTouch");

                    /**
                     * This point in terms of GameMatrix coordinates represents
                     * our onClick event's (x,y)
                     */
                    Point selectedCoordinate = getCoordinate(downPoint);

                    borders.showTouch(selectedCoordinate);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.v("TOPVIEW", "ACTION_CANCEL");
                break;
        }
    }

    private boolean canBeConsideredAsClick(MotionEvent event){
        return Math.sqrt(Math.pow(event.getX() - origCoordinates.x, 2) + Math.pow(event.getY() - origCoordinates.y, 2)) < MAX_DIST_TOUCH;
    }

    private Point getCoordinate(PointF clickedPoint){
        float fx = gameMatrix.size * clickedPoint.x / UIHelper.getW();
        int x = (int) (fx);
        if (fx - x > 0.5f){
            ++x;
        }

        float fy = gameMatrix.size * clickedPoint.y / UIHelper.getH();
        int y = (int) (fy);
        if (fy - y > 0.5f){
            ++y;
        }

        return new Point(x, y);
    }

}
