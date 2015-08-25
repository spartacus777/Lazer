package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import anton.kizema.lazersample.helper.PaintHelper;
import anton.kizema.lazersample.helper.UIHelper;
import anton.kizema.lazersample.matrix.GameMatrix;

public class DrawingField extends BaseElement {

    private Paint paint, borderPaint;
    private GameMatrix gameMatrix;

    public DrawingField(GameMatrix gameMatrix){
        this.gameMatrix = gameMatrix;

        paint = new Paint();
        PaintHelper.adjustPaint(paint);
        paint.setStrokeWidth(UIHelper.getPixel(10));
        paint.setColor(Color.RED);

        borderPaint = new Paint();
        PaintHelper.adjustPaint(borderPaint);
        borderPaint.setStrokeWidth(UIHelper.getPixel(1));
        borderPaint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas canvas){
        for (int i=0; i <= gameMatrix.size; ++i){
            canvas.drawLine(0, i * UIHelper.getH()/( gameMatrix.size +1 ) ,
                    UIHelper.getW(), i * UIHelper.getH()/ ( gameMatrix.size +1), borderPaint);
        }

        for (int i=0; i <= gameMatrix.size; ++i){
            canvas.drawLine(i * UIHelper.getW()/ ( gameMatrix.size +1), 0,
                    i * UIHelper.getW()/ ( gameMatrix.size +1), UIHelper.getH(), borderPaint);
        }

//        canvas.drawLine(0,0, UIHelper.getPixel(200), UIHelper.getPixel(300), paint);
    }

}
