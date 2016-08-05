package tw.com.a_team.simapleui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

/**
 * Created by user on 2016/8/5.
 */
public class GeoCodingTask extends AsyncTask<String, Void, double[]>
{
    WeakReference<GeoCodingResponse> geoCodingResponseWeakReference;

    @Override
    protected double[] doInBackground(String... params) {   //背景執行序
        //Log.e("doInBackground Thread ID ", Long.toString(Thread.currentThread().getId()));
        double[] latlng = Utils.getLatLngFromAddress(params[0]);
        if (latlng != null)
        {
            Log.e("getLatLngFromAddress", "OK");
            return latlng;
        }
        return null;
    }

    @Override
    protected void onPostExecute(double[] latlng) {   //主執行序
        super.onPostExecute(latlng);
        //Log.e("onPostExecute Thread ID ", Long.toString(Thread.currentThread().getId()));
        if(latlng != null)
        {
            LatLng result = new LatLng(latlng[0], latlng[1]);
            if (geoCodingResponseWeakReference.get() != null )
            {
                GeoCodingResponse geoCodingResponse = geoCodingResponseWeakReference.get();
                geoCodingResponse.callbackWithGeoCodingResult(result);
            }

        }
    }

    public GeoCodingTask(GeoCodingResponse geoCodingResponse)
    {
        geoCodingResponseWeakReference = new WeakReference<GeoCodingResponse>(geoCodingResponse);
    }

    interface GeoCodingResponse{
        void callbackWithGeoCodingResult(LatLng latLng);

    }

}
