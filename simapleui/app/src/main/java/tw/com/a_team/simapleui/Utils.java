package tw.com.a_team.simapleui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

    public static byte[] urlToBytes(String urlString)
    {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatLngFromAddress(String address)
    {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = "http://maps.google.com/maps/api/geocode/json?address=" + address;

        byte[] data =Utils.urlToBytes(apiURL);
        if (data == null)
            return null;

        String result = new String(data);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("status").equals("OK"))
            {
                JSONObject location = jsonObject.getJSONArray("results")
                                                    .getJSONObject(0)
                                                    .getJSONObject("geometry")
                                                    .getJSONObject("location");

                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                return new double[]{lat, lng};
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getStaticMapFromLatLng(double[] latlng)
    {
        String center = String.valueOf(latlng[0]) + "," + String.valueOf(latlng[1]);
        String apiUrl = "http://map.google.com/maps/api/staticmap?center=" + center + "&size=640x480&zoom=17";
        byte[] data = Utils.urlToBytes(apiUrl);

        if (data == null) {
            return null;
        }

        Log.e("getStaticMapFromLatLng", String.valueOf(data.length));
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

}
