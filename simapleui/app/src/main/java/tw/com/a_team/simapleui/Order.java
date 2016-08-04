package tw.com.a_team.simapleui;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2016/7/27.
 */
@ParseClassName("Order")
public class Order extends ParseObject implements Parcelable {

    static final String NOTE_COL = "note";
    static final String STOREINFO_COL= "storeinfo";
    static final String DRINKORDERS_COL= "drinkOrders";

    public Order()
    {
        super();
    }

    //String drink;
    //List<DrinkOrder> drinkOrders;

    public int total()
    {
        int total=0;
        for (DrinkOrder drinkOrder : getDrinkOrders())
        {
            //total+=drink.mPrice;
            total+=drinkOrder.total();
        }
        return total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (getObjectId() == null)
        {
            dest.writeInt(0);

            dest.writeString(this.getNote());
            dest.writeString(this.getStoreInfo());
            dest.writeParcelableArray((Parcelable[]) getDrinkOrders().toArray(), flags);

        }
        else
        {
            dest.writeInt(1);
            dest.writeString(getObjectId());
        }

    }

    protected Order(Parcel in) {
        super();
        //this.setDrink((Drink)in.readParcelable(Drink.class.getClassLoader()));

        this.setStoreInfo(in.readString()); //= in.readString();
        this.setNote(in.readString()); //= in.readString();
        this.setDrinkOrders(Arrays.asList((DrinkOrder[])in.readArray(DrinkOrder.class.getClassLoader())));

    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            int isDraft= source.readInt();
            if (isDraft == 0)
            {
                return new Order(source);
            }
            else
            {
                return getOrderFromCache(source.readString());
            }

        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getNote() {
        return getString(NOTE_COL);
    }

    public void setNote(String note) {
        put(NOTE_COL, note);
    }

    public String getStoreInfo() {
        return getString(STOREINFO_COL);
    }

    public void setStoreInfo(String storeInfo) {
        put(STOREINFO_COL, storeInfo);
    }

    public List<DrinkOrder> getDrinkOrders() {
        return getList( DRINKORDERS_COL);
    }

    public void setDrinkOrders(List<DrinkOrder> drinkOrders) {
        put(DRINKORDERS_COL, drinkOrders);
    }

    public static ParseQuery<Order> getQuery()
    {
        ParseQuery<Order> query= ParseQuery.getQuery(Order.class);
        query.include(DRINKORDERS_COL);
        query.include(DRINKORDERS_COL + "." + DrinkOrder.DRINK_COL);
        return query;
    }

    public static void getOrdersFromLocalThenRemote(final FindCallback<Order> callback)
    {
        getQuery().fromLocalDatastore().findInBackground(callback);
        getQuery().findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {
                    pinAllInBackground("Order", objects);
                }
                callback.done(objects, e);
            }
        });
    }

    public static Order getOrderFromCache(String objectId)
    {
        try {
            //DrinkOrder drinkOrder = getQuery().setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK).get(objectId);
            Order order = getQuery().fromLocalDatastore().get(objectId);
            return order;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Order.createWithoutData(Order.class, objectId);
    }

}
