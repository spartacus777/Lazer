package anton.kizema.lazersample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import anton.kizema.lazersample.elements.DrawingField;

public class TopView extends ImageView implements GameImageView.TouchImageViewCallback {

    private DrawingField drawingField;

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
        drawingField = new DrawingField();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(matrix);


        super.onDraw(canvas);

        //TODO here we should draw all game stuff
        drawingField.onDraw(canvas);

        canvas.restore();
    }

    @Override
    public void applyMatrix(Matrix matrix) {
        setImageMatrix(matrix);
        this.matrix = matrix;
        invalidate();
    }

    @Override
    public void onBitmapSizesCounted(Matrix matrix, int w, int h) {
        setImageMatrix(matrix);
        this.matrix = matrix;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO here we can intersept touches from GameImageView
        return super.onTouchEvent(event);
    }
}
