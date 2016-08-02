package tw.com.a_team.simapleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2016/8/2.
 */
public class Utils {

    public static void writeFile(Context context, String fileName, String content)
    {
        try {
            FileOutputStream fos= context.openFileOutput(fileName, Context.MODE_APPEND);
            try {
                fos.write(content.getBytes());
                fos.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName)
    {
        try {
            FileInputStream fis= context.openFileInput(fileName);
            byte[] buffer= new byte[2048];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            String content= new String(buffer);
            return content;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
