package anton.kizema.lazersample.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import anton.kizema.lazersample.helper.UIHelper;

public class DrawingField extends BaseElement {

    Paint paint;

    public DrawingField(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER));
        paint.setAntiAlias(true);

        paint.setDither(true);                    // set the dither to true
        paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paint.setPathEffect(new PathEffect());   // set the path effect when they join.

        paint.setStrokeWidth(UIHelper.getPixel(10));
        paint.setColor(Color.RED);
    }

    public void onDraw(Canvas canvas){
        canvas.drawLine(0,0, UIHelper.getPixel(200), UIHelper.getPixel(300), paint);
    }

}
