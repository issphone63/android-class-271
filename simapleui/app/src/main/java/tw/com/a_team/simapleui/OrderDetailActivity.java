package tw.com.a_team.simapleui;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Connection;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Handler;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import javax.security.auth.callback.CallbackHandler;


public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {

    final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 0;

    GoogleMap map;
    LatLng storeLoction;

    GoogleApiClient googleApiClient;

    LocationRequest locationRequest;

    Polyline polyline ;

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

        MapFragment mapFragment =(MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                (new GeoCodingTask(OrderDetailActivity.this)).execute("台北市羅斯福路四段一號");
            }
        });

        ImageView staticMapImagetView =(ImageView) findViewById(R.id.staticMapImageView);


        Log.e("Main Thread ID ", Long.toString(Thread.currentThread().getId()));
    }

    @Override
    public void callbackWithGeoCodingResult(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("NTU").snippet("Hello M");
        map.moveCamera(cameraUpdate);
        map.addMarker(markerOptions);

        storeLoction = latLng;

        createGoogleApiClient();
    }

    private void createGoogleApiClient()
    {
        if (googleApiClient == null)
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) { //連線
        //檢查使用者是否同意,23版本以後會先問,以前是後問
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                //問user 同意
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);

            }
            return;
        }

        if (locationRequest == null) //多久跟user要一次座標
        {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER); // 沒電就減少定位
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        //先定位一次取座標
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        LatLng start =new LatLng(25.0294,121.542);
        if (location != null) {
            start = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("location", "OK");
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 17));

        Routing routing =  new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .alternativeRoutes(false)
                .waypoints(start, storeLoction)
                .withListener(this)
                .build();
        routing.execute();

    }

    //使用者選取是否同意
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                onConnected(null);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) { //暫停

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { //連線失敗

    }

    //每次定位取得user座標，每五秒
    @Override
    public void onLocationChanged(Location location) {
        if (polyline == null)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

            Routing routing =  new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(location.getLatitude(), location.getLongitude()), storeLoction)
                    .withListener(this)
                    .build();
            routing.execute();
            return;

        }
        List<LatLng> points = polyline.getPoints();
        int index = 0;

        for ( int i=0 ; i < points.size() ; i++ )
        {
            if (i < points.size()-1)
            {
                LatLng point1 = points.get(i);
                LatLng point2 = points.get(i+1);
                double offset = 0.0001;
                Double maxLat = Math.max(point1.latitude, point2.latitude) + offset;
                Double maxLng = Math.max(point1.latitude, point2.latitude) + offset;
                Double minLat = Math.min(point1.latitude, point2.latitude) - offset;
                Double minLng = Math.min(point1.latitude, point2.latitude) - offset;
                if (location.getLatitude() >= maxLat && location.getLatitude() <= minLat && location.getLongitude() >= maxLng && location.getLongitude() <= minLng)
                {
                    index =i;
                    break;
                }

            }
        }

        if ( index != -1)
        {
            for ( int 1=index-1 ; i >=0 ; i++)
            {
                points
            }

        }

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        Route route = arrayList.get(i);
        List<LatLng> points = route.getPoints();//讀入全部的點
        PolylineOptions polylineOptions = new PolylineOptions();//多邊線
        polylineOptions.addAll(points);
        polylineOptions.color(Color.GREEN);
        polylineOptions.width(10);

        map.addPolyline(polylineOptions);
    }

    @Override
    public void onRoutingCancelled() {

    }


//    public static class GeoCodingTask extends AsyncTask<String, Void, Bitmap>
//    {
//        WeakReference<ImageView> imageViewWeakReference;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {   //背景執行序
//            //Log.e("doInBackground Thread ID ", Long.toString(Thread.currentThread().getId()));
//            double[] latlng = Utils.getLatLngFromAddress(params[0]);
//            if (latlng != null)
//            {
//                Log.e("getLatLngFromAddress","OK" );
//                return Utils.getStaticMapFromLatLng(latlng);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {   //主執行序
//            super.onPostExecute(bitmap);
//            //Log.e("onPostExecute Thread ID ", Long.toString(Thread.currentThread().getId()));
//            if(bitmap != null)
//            {
//                if (imageViewWeakReference.get() !=  null)
//                {
//                    Log.e("setImageBitmap","OK" );
//                    ImageView imageView = imageViewWeakReference.get();
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        }
//
//        public GeoCodingTask(ImageView imageView)
//        {
//            imageViewWeakReference = new WeakReference<ImageView>(imageView);
//        }
//    }


}
