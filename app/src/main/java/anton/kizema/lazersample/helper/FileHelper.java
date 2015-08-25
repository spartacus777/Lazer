package anton.kizema.lazersample.helper;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {
    private static final String TAG = FileHelper.class.toString();

    public static File getImageFileJPG() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/someFolder");
        dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        return new File(dir, fileName);
    }

    public static File getImageFilePNG() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/someFolder");
        dir.mkdirs();

        String fileName = String.format("%d.png", System.currentTimeMillis());
        return new File(dir, fileName);
    }

    public static void writeBinaryToFile(File outFile, byte[]... data) {
        try {
            FileOutputStream outStream = new FileOutputStream(outFile);
            outStream.write(data[0]);
            outStream.flush();
            outStream.close();

            Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String file){
        if (file == null || file.length() == 0){
            return false;
        }

        File f = new File(file);
        if (f.exists()){
            return true;
        } else
            return false;
    }
}
