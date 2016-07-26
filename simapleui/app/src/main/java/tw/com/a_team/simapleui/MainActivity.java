package tw.com.a_team.simapleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;

    String sex="male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView1);
        editText=(EditText)findViewById(R.id.editText1);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.maleradioButton)
                    sex="male";
                else if (checkedId==R.id.femaleradioButton)
                    sex="female";

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    submit(v);
                    return true; //這樣可以清除 Enter 字元
                }
                return false;
            }
        });


    }

    public void submit(View view)
    {
        String text=editText.getText().toString();
        text=text+" SEX: "+sex;
        textView.setText(text);
        editText.setText("");
    }
}
