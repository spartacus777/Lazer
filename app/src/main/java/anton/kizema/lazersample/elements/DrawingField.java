package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import anton.kizema.lazersample.helper.PaintHelper;
import anton.kizema.lazersample.helper.UIHelper;
import anton.kizema.lazersample.matrix.GameMatrix;

public class DrawingField extends BaseElement {

    private Paint borderPaint;
    private GameMatrix gameMatrix;

    public DrawingField(GameMatrix gameMatrix){
        this.gameMatrix = gameMatrix;

        borderPaint = new Paint();
        PaintHelper.adjustPaint(borderPaint);
        borderPaint.setStrokeWidth(UIHelper.getPixel(1));
        borderPaint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas canvas){
        for (int i=0; i <= gameMatrix.size; ++i){
            canvas.drawLine(0, i * UIHelper.getH()/( gameMatrix.size ) ,
                    UIHelper.getW(), i * UIHelper.getH()/ ( gameMatrix.size), borderPaint);
        }

        for (int i=0; i <= gameMatrix.size; ++i){
            canvas.drawLine(i * UIHelper.getW()/ ( gameMatrix.size), 0,
                    i * UIHelper.getW()/ ( gameMatrix.size), UIHelper.getH(), borderPaint);
        }

    }

}
