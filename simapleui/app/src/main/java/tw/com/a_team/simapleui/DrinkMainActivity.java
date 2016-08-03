package tw.com.a_team.simapleui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DrinkMainActivity extends AppCompatActivity implements DrinkOrderDialog.OnDrinkOrderListener {

    ListView drinkMenuListView;
    TextView totalTextView;

    String[] drinkNames = new String[]{"White gourd Tea","Black Tea","Pearl black Tea","Milk Tea"};
    int[] lPrices = new int[]{25,35,35,25};
    int[] mPrices = new int[]{15,25,25,15};
    int[] images = new int[]{R.drawable.drink1,R.drawable.drink4,R.drawable.drink3,R.drawable.drink2};

    List<Drink> drinkList = new ArrayList<>();
    ArrayList<DrinkOrder> drinkOderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_main);

        drinkMenuListView=(ListView)findViewById(R.id.drinkmenu_listView);
        totalTextView=(TextView)findViewById(R.id.total_textView);

        setData();

        drinkOderList= getIntent().getParcelableArrayListExtra("drinkOrderList");
        setupTotalTextView();
        //setupDrinkMenuListView();

        Log.d("Debug", "DrinkMainActivity onCreate");

    }

    public void setData()
    {
//        for(int ii=0 ; ii<4 ; ii++)
//        {
//            Drink drink=new Drink();
//            drink.name=drinkNames[ii];
//            drink.lPrice=lPrices[ii];
//            drink.mPrice=mPrices[ii];
//            drink.imageID=images[ii];
//            drinkList.add(drink);
//        }

        Drink.getDrinkFromLocalThenRemote(new FindCallback<Drink>() {
              @Override
              public void done(List<Drink> objects, ParseException e) {
                  if ( e == null)
                  {
                      drinkList = objects;
                      setupDrinkMenuListView();
                  }

              }

        });


//                Drink.getQuery().findInBackground(new FindCallback<Drink>() {
//                    @Override
//                    public void done(List<Drink> objects, ParseException e) {
//                        if (e == null) {
//                            drinkList = objects;
//                            setupDrinkMenuListView();
//                        }
//                    }
//                });

    }

    public  void setupDrinkMenuListView()
    {
        DrinkAdapter adapter=new DrinkAdapter(this,drinkList);
        drinkMenuListView.setAdapter(adapter);

        drinkMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drink drink = (Drink) parent.getAdapter().getItem(position);
                //drinkOderList.add(drink);
                showDrinkOrderDialog(drink);
                setupTotalTextView();

            }
        });
    }

    private void showDrinkOrderDialog(Drink drink)
    {

        DrinkOrder order = new DrinkOrder(drink);


        for (DrinkOrder drinkOrder: drinkOderList)
        {
            if (drinkOrder.getDrink().getObjectId().equals((drink.getObjectId())))
            {
                order=drinkOrder;
                break;
            }
        }


        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        DrinkOrderDialog dialog = DrinkOrderDialog.newInstance(order);

        //ft.replace(R.id.root, dialog);
        //ft.commit();

        dialog.show(ft,"DrinkOrderDialog");


    }

    public void setupTotalTextView()
    {
        int total=0;
        for (DrinkOrder drinkOrder :drinkOderList)
        {
            //total+=drink.mPrice;
            total+=drinkOrder.total();
        }

        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("results", drinkOderList);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void cancel(View view)
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "DrinkMainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "DrinkMainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "DrinkMainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "DrinkMainActivity onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "DrinkMainActivity onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "DrinkMainActivity onDestroy");
    }


    @Override
    public void OnDrinkOrderFinished(DrinkOrder drinkOrder) {
        for (int ii = 0; ii<drinkOderList.size(); ii++)
        {
            if (drinkOderList.get(ii).getDrink().getObjectId().equals(drinkOrder.getDrink().getObjectId()))
            {
                drinkOderList.set(ii, drinkOrder);
                setupTotalTextView();
                return;
            }
        }

        drinkOderList.add(drinkOrder);
        setupTotalTextView();

    }

}
