package tw.com.a_team.simapleui;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String drink="Black Tea";
    List<Order> orders=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView1);
        editText=(EditText)findViewById(R.id.editText1);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        listView=(ListView)findViewById(R.id.listView);
        spinner=(Spinner)findViewById(R.id.spinner);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                drink = radioButton.getText().toString();

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true; //這樣可以清除 Enter 字元
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order=(Order)parent.getAdapter().getItem(position);
                //Toast.makeText(MainActivity.this,"You click on "+order.note,Toast.LENGTH_SHORT).show();
                Snackbar.make(parent,"You click on "+order.note,Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

            }
        });

        setupListView();
        setupSpinner();
    }

    public void submit(View view)
    {
        String text=editText.getText().toString();
        String result =text+" order: "+drink;
        textView.setText(text);
        editText.setText("");

        //orders.add(text);
        Order order=new Order();
        order.note=text;
        order.drink=drink;
        order.storeInfo=(String)spinner.getSelectedItem();

        orders.add(order);
        setupListView();
    }

    private void setupListView()
    {
//        String[] data=new String[]{"1","2","3","4","5","6","7","8"};
//        ArrayAdapter adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,orders);

        OrderAdapter adapter=new OrderAdapter(this,orders);
        listView.setAdapter(adapter);

    }

    private void setupSpinner()
    {
        String[] data=getResources().getStringArray(R.array.storeinfos);

        //ArrayAdapter adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,data);
        ArrayAdapter adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);
    }

}
