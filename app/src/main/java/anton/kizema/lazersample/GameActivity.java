package anton.kizema.lazersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import anton.kizema.lazersample.helper.BitmapHelper;
import anton.kizema.lazersample.helper.UIHelper;

public class GameActivity extends AppCompatActivity {

    private GameImageView gameImageView;
    private TopView topView;

    private ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        parent = (ViewGroup) findViewById(R.id.parent);

        initGameView();
    }

    private void initGameView(){
        gameImageView = new GameImageView(this, BitmapHelper.resizeBitmap(getResources().getDrawable(R.drawable.auth_background),
                UIHelper.getW(), UIHelper.getH()));

        gameImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gameImageView.setMaxZoom(10);
        parent.addView(gameImageView);

        topView = new TopView(this);
        topView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.addView(topView);

        gameImageView.addTouchImageViewCallback(topView);
    }

}
