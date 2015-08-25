package anton.kizema.lazersample.helper;

import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by admin on 25.08.2015.
 */
public class PaintHelper {

    public static void adjustPaint(Paint localPaint){
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER));
        localPaint.setAntiAlias(true);

        localPaint.setDither(true);                    // set the dither to true
        localPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        localPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        localPaint.setPathEffect(new PathEffect());   // set the path effect when they join.
    }
}
