package tw.com.a_team.simapleui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

import javax.security.auth.callback.CallbackHandler;


public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Order order = getIntent().getParcelableExtra("order");

        TextView noteTextView =(TextView) findViewById(R.id.note_textView);
        TextView orderResultTextView =(TextView) findViewById(R.id.oredrResult_textView);
        TextView storeInfoTextView =(TextView) findViewById(R.id.storeInfo_textView);
        //final TextView testTextView =(TextView) findViewById(R.id.test_textView);




        noteTextView.setText(order.getNote());
        storeInfoTextView.setText(order.getStoreInfo());

        String orderResultText= "";
        for (DrinkOrder drinkOrder: order.getDrinkOrders())
        {
            String mNumber = String.valueOf(drinkOrder.getmNumber());
            String lNumber = String.valueOf(drinkOrder.getlNumber());
            String drinkName = drinkOrder.getDrink().getName();
            orderResultText += drinkName + " M: " + mNumber + " L: " + lNumber + "\n";

        }

        orderResultTextView.setText(orderResultText);

//        final Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                testTextView.setText("Hello Handler");
//                Log.e("Handler Thread ID ", Long.toString(Thread.currentThread().getId()));
//                return false;
//            }
//        });
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                testTextView.setText("Hello POST DELAY");
//            }
//        },10000);
//
//        //新增執行序去另外執行多工,不能去修改ui
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    Log.e("Thread ID ", Long.toString(Thread.currentThread().getId()));
//                    handler.sendMessage(new Message());
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();

        ImageView staticMapImagetView =(ImageView) findViewById(R.id.staticMapImageView);

        (new GeoCodingTask(staticMapImagetView)).execute("台北市羅斯福路四段一號");

        Log.e("Main Thread ID ", Long.toString(Thread.currentThread().getId()));
    }

    public static class GeoCodingTask extends AsyncTask<String, Void, Bitmap>
    {
        WeakReference<ImageView> imageViewWeakReference;

        @Override
        protected Bitmap doInBackground(String... params) {   //背景執行序
            //Log.e("doInBackground Thread ID ", Long.toString(Thread.currentThread().getId()));
            double[] latlng = Utils.getLatLngFromAddress(params[0]);
            if (latlng != null)
            {
                Log.e("getLatLngFromAddress","OK" );
                return Utils.getStaticMapFromLatLng(latlng);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {   //主執行序
            super.onPostExecute(bitmap);
            //Log.e("onPostExecute Thread ID ", Long.toString(Thread.currentThread().getId()));
            if(bitmap != null)
            {
                if (imageViewWeakReference.get() !=  null)
                {
                    Log.e("setImageBitmap","OK" );
                    ImageView imageView = imageViewWeakReference.get();
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public GeoCodingTask(ImageView imageView)
        {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }
    }


}
