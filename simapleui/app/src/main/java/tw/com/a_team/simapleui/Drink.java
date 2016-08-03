package tw.com.a_team.simapleui;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.List;

/**
 * Created by user on 2016/7/28.
 */
@ParseClassName("Drink")
public class Drink extends ParseObject implements Parcelable {

    String NAME_COL= "name";
    String LPRICE_COL= "lPrice";
    String MPRICE_COL= "mPrice";



    //String name;
    //int lPrice;
    //int mPrice;
    int imageID;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (getObjectId() == null) {
            dest.writeInt(0);

            dest.writeString(this.getName());
            dest.writeInt(this.getlPrice());
            dest.writeInt(this.getmPrice());

        } else
        {
            dest.writeInt(1);
            dest.writeString(getObjectId());
        }



        //dest.writeInt(this.imageID);
    }

    public Drink() {
    }

    protected Drink(Parcel in) {
        //this.name = in.readString();
        //this.lPrice = in.readInt();
        //this.mPrice = in.readInt();
        setName(in.readString());
        setlPrice(in.readInt());
        setmPrice(in.readInt());

        //this.imageID = in.readInt();
    }

    public static final Parcelable.Creator<Drink> CREATOR = new Parcelable.Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel source) {
            int isDraft= source.readInt();
            if (isDraft == 0)
            {
                return new Drink(source);
            }
            else
            {
                return getDrinkFromCache(source.readString());
            }
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    public String getName() {
        return getString(NAME_COL);
    }

    public void setName(String name) {
        //this.name = name;
        put(NAME_COL, name);
    }

    public int getlPrice() {
        return getInt(LPRICE_COL);
    }

    public void setlPrice(int lPrice) {
        //this.lPrice = lPrice;
        put(LPRICE_COL, lPrice);
    }

    public int getmPrice() {
        return getInt(MPRICE_COL);
    }

    public void setmPrice(int mPrice) {
        //this.mPrice = mPrice;
        put(MPRICE_COL, mPrice);
    }

    public ParseFile getParseFile()
    {
        return getParseFile("Image");
    }

    public static ParseQuery<Drink> getQuery() { return  ParseQuery.getQuery(Drink.class);}

    public static Drink getDrinkFromCache(String objectId)
    {

        try {
            //Drink drink = getQuery().setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK).get(objectId);
            Drink drink = getQuery().fromLocalDatastore().get(objectId);
            return drink;

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return Drink.createWithoutData(Drink.class, objectId);

//        try {
//            drink = getQuery().setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK).get(objectId);
//        } catch (com.parse.ParseException e) {
//            e.printStackTrace();
//        }
//        return drink;
//
//        return Drink.createWithoutData(DrinkOrder.class, objectId);
    }

    public static void getDrinkFromLocalThenRemote(final FindCallback callback)
    {
        getQuery().fromLocalDatastore().findInBackground(callback);
        getQuery().findInBackground(new FindCallback<Drink>() {
            @Override
            public void done(final List<Drink> objects, com.parse.ParseException e) {
                if (e == null)
                {
                    unpinAllInBackground("Drink", new DeleteCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            pinAllInBackground("Drink", objects);
                        }
                    });
                }

                callback.done(objects , e);
            }
        });
    }

}
