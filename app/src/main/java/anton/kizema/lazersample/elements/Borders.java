package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(UIHelper.getPixel(2));
        paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas canvas){
        for (int i=0; i < gameMatrix.size; ++i){
            for (int j=0; j < gameMatrix.size; ++j){
                if ( gameMatrix.elements[i][j] == 1){
                    Log.d("ANT", "drawPoint i:"+i+";j:"+j);
                    canvas.drawCircle( UIHelper.getW()/(gameMatrix.size) * i, UIHelper.getH()/(gameMatrix.size) * j, UIHelper.getPixel(2), paint);
                }
            }
        }
    }

}
