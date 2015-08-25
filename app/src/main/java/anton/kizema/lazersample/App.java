package anton.kizema.lazersample;

import android.app.Application;
import android.content.Context;

import anton.kizema.lazersample.helper.UIHelper;

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        UIHelper.init(context);
    }

    /**
     * Get application context
     *
     * @return {@link android.content.Context} of application
     */
    public static Context getContext() {
        return context;
    }

}
