package tw.com.a_team.simapleui;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by user on 2016/8/1.
 */
@ParseClassName("DrickOrder")
public class DrinkOrder extends ParseObject implements Parcelable {

    static final String DRINK_COL= "drink";
    static final String MNUMBER_COL= "mNumber";
    static final String LNUMBER_COL= "lNumber";
    static final String ICE_COL= "ice";
    static final String SUGAR_COL= "sugar";
    static final String NOTE_COL= "note";
    /*
    Drink drink;
    int mNumber = 0 ;
    int lNumber = 0 ;
    String ice="Regular";
    String sugar="Regular";
    String note="";
    */


    public DrinkOrder(Drink drink)
    {
        //this.drink=drink;
        this.setDrink(drink);

    }

    public int total()
    {
        return getDrink().lPrice * getlNumber() + getDrink().mPrice * getmNumber();

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
            dest.writeParcelable(this.getDrink(), flags);
            dest.writeInt(this.getmNumber());
            dest.writeInt(this.getlNumber());
            dest.writeString(this.getIce());
            dest.writeString(this.getSugar());
            dest.writeString(this.getNote());
        }
        else
        {
            dest.writeInt(1);
            dest.writeString(getObjectId());
        }

    }

    protected DrinkOrder(Parcel in) {
        this.setDrink(in.readParcelable(Drink.class.getClassLoader()));
        this.setmNumber(in.readInt());// = in.readInt();
        this.setlNumber(in.readInt());// = in.readInt();
        this.setIce(in.readString()); //= in.readString();
        this.setSugar(in.readString()); //= in.readString();
        this.setNote(in.readString()); //= in.readString();
    }

    public static final Parcelable.Creator<DrinkOrder> CREATOR = new Parcelable.Creator<DrinkOrder>() {
        @Override
        public DrinkOrder createFromParcel(Parcel source) {
            int isDraft= source.readInt();
            if (isDraft == 0)
            {

            }
            else
            {

            }
            return new DrinkOrder(source);
        }

        @Override
        public DrinkOrder[] newArray(int size) {
            return new DrinkOrder[size];
        }
    };

    public Drink getDrink() {
        //return drink;
        return getParseObject(DRINK_COL);
    }

    public void setDrink(Drink drink) {
        //this.drink = drink;
        put(DRINK_COL, drink);
    }

    public int getmNumber() {
        //return mNumber;
        return getInt(MNUMBER_COL);
    }

    public void setmNumber(int mNumber) {
        //this.mNumber = mNumber;
        put(MNUMBER_COL, mNumber);
    }

    public int getlNumber() {
        //return lNumber;
        return getInt(LNUMBER_COL);
    }

    public void setlNumber(int lNumber) {
        //this.lNumber = lNumber;
        put(LNUMBER_COL, lNumber);
    }

    public String getIce() {
        //return ice;
        return getString(ICE_COL);
    }

    public void setIce(String ice) {
        //this.ice = ice;
        put(ICE_COL, ice);
    }

    public String getSugar() {
        //return sugar;
        return getString(SUGAR_COL);
    }

    public void setSugar(String sugar) {
        //this.sugar = sugar;
        put(SUGAR_COL, sugar);
    }

    public String getNote() {
        //return note;
        return getString(NOTE_COL);
    }

    public void setNote(String note) {
        //this.note = note;
        put(NOTE_COL, note);
    }
}
