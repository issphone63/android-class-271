package tw.com.a_team.simapleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016/7/27.
 */
public class OrderAdapter extends BaseAdapter {

    List<Order> orders;
    LayoutInflater inflater;


    public OrderAdapter(Context context,List<Order> orderList)
    {
        this.orders=orderList;
        this.inflater=LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.listview_order_item,null);

        }

        TextView noteTextView=(TextView)convertView.findViewById(R.id.note_textView);
        TextView storeInfoTextView=(TextView)convertView.findViewById(R.id.storeInfo_textView);
        TextView drinkTextView=(TextView)convertView.findViewById(R.id.drink_textView);

        Order order= orders.get(position);
        noteTextView.setText(order.note);
        storeInfoTextView.setText(order.storeInfo);
        drinkTextView.setText(order.drink);
        //orders.get(position).storeInfo



        return convertView;
    }
}
