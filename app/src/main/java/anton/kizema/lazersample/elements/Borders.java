package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import anton.kizema.lazersample.helper.PaintHelper;
import anton.kizema.lazersample.helper.UIHelper;
import anton.kizema.lazersample.matrix.GameMatrix;

public class Borders extends BaseElement{

    private Paint paint;
    private GameMatrix gameMatrix;

    public Borders(GameMatrix gameMatrix){
        this.gameMatrix = gameMatrix;

        paint = new Paint();
        PaintHelper.adjustPaint(paint);
        paint.setStrokeWidth(UIHelper.getPixel(10));
        paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas canvas){
        for (int i=0; i < gameMatrix.size; ++i){
            for (int j=0; j < gameMatrix.size; ++j){

            }
        }
//
//        for (int i=0; i <= gameMatrix.size; ++i){
//            canvas.drawLine(i * UIHelper.getW()/ ( gameMatrix.size +1), 0,
//                    i * UIHelper.getW()/ ( gameMatrix.size +1), UIHelper.getH(), borderPaint);
//        }

//        canvas.drawLine(0,0, UIHelper.getPixel(200), UIHelper.getPixel(300), paint);
    }

}
