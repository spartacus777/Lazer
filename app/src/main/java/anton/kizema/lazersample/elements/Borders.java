package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import anton.kizema.lazersample.helper.PaintHelper;
import anton.kizema.lazersample.helper.UIHelper;
import anton.kizema.lazersample.matrix.GameMatrix;

public class Borders extends BaseElement{

    private Paint paint, demoPaint;
    private GameMatrix gameMatrix;

    public Borders(GameMatrix gameMatrix){
        this.gameMatrix = gameMatrix;

        paint = new Paint();
        PaintHelper.adjustPaint(paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(UIHelper.getPixel(2));
        paint.setColor(Color.RED);


        demoPaint = new Paint();
        PaintHelper.adjustPaint(demoPaint);
        demoPaint.setStyle(Paint.Style.FILL);
        demoPaint.setStrokeWidth(UIHelper.getPixel(8));
        demoPaint.setColor(Color.WHITE);
    }

    @Override
    public void onDraw(Canvas canvas){
        for (int i=0; i < gameMatrix.size; ++i){
            for (int j=0; j < gameMatrix.size; ++j){
                if ( gameMatrix.elements[i][j] == 1){
                    canvas.drawCircle( UIHelper.getW()/(gameMatrix.size) * i, UIHelper.getH()/(gameMatrix.size) * j, UIHelper.getPixel(2), paint);
                }
            }
        }

        demoDraw(canvas);
    }

    private void demoDraw(Canvas canvas){
        canvas.drawCircle(UIHelper.getW() / (gameMatrix.size) * lastSelectedPoint.x, UIHelper.getH() / (gameMatrix.size) * lastSelectedPoint.y, UIHelper.getPixel(6), demoPaint);
    }

    private Point lastSelectedPoint = new Point();

    public void showTouch(Point point){
        lastSelectedPoint.x = point.x;
        lastSelectedPoint.y = point.y;
    }

}
