package tw.com.a_team.simapleui;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
@ParseClassName("storeinfos")
public class StoreInfos extends ParseObject {

    String NAME_COL= "name";
    String ADDR_COL= "address";

    String name;
    String addr;


    public String getName() {
        name = getString(NAME_COL);
        return name;
    }

    public void setName(String name) {
        this.name = name;
        put(NAME_COL, name);
    }

    public String getAddr() {
        addr = getString(ADDR_COL);
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
        put(ADDR_COL, addr);
    }

    public static ParseQuery<StoreInfos> getQuery() { return  ParseQuery.getQuery(StoreInfos.class);}

    public static StoreInfos getStoreInfosFromCache(String objectId)
    {

        try {
            //Drink drink = getQuery().setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK).get(objectId);
            StoreInfos storeInfos = getQuery().fromLocalDatastore().get(objectId);
            return storeInfos;

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return StoreInfos.createWithoutData(StoreInfos.class, objectId);

//        try {
//            drink = getQuery().setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK).get(objectId);
//        } catch (com.parse.ParseException e) {
//            e.printStackTrace();
//        }
//        return drink;
//
//        return Drink.createWithoutData(DrinkOrder.class, objectId);
    }

    public static void getStoreInfoFromLocalThenRemote(final FindCallback callback)
    {
        getQuery().fromLocalDatastore().findInBackground(callback);
        getQuery().findInBackground(new FindCallback<StoreInfos>() {
            @Override
            public void done(final List<StoreInfos> objects, com.parse.ParseException e) {
                if (e == null)
                {
                    unpinAllInBackground("storeinfos", new DeleteCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            pinAllInBackground("storeinfos", objects);
                        }
                    });
                }

                callback.done(objects , e);
            }
        });
    }

}
