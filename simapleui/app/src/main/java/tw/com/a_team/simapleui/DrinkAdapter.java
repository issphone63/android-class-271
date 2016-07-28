package tw.com.a_team.simapleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016/7/28.
 */
public class DrinkAdapter extends BaseAdapter {

    List<Drink> drinks;
    LayoutInflater inflater;

    public DrinkAdapter(Context context,List<Drink> drinksList)
    {
        this.drinks=drinksList;
        this.inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.listview_drink_item,null);
            holder=new Holder();
            holder.drinkNameTextView=(TextView)convertView.findViewById(R.id.drinkname_textView);
            holder.lPriceTextView=(TextView)convertView.findViewById(R.id.lPrice_textView);
            holder.mPriceTextView=(TextView)convertView.findViewById(R.id.mPrice_textView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        }
        else
        {
            holder=(Holder)convertView.getTag();

        }

        Drink drink=drinks.get(position);

        holder.drinkNameTextView.setText(drink.name);
        holder.lPriceTextView.setText(String.valueOf(drink.lPrice)); //String.valueOf 轉換成字串
        holder.mPriceTextView.setText(String.valueOf(drink.mPrice));
        holder.imageView.setImageResource(drink.imageID);

        //orders.get(position).storeInfo

        return convertView;
    }

    class Holder
    {
        ImageView imageView;
        TextView drinkNameTextView;
        TextView lPriceTextView;
        TextView mPriceTextView;

    }

}
