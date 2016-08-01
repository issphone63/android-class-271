package tw.com.a_team.simapleui;

import java.util.List;

/**
 * Created by user on 2016/7/27.
 */
public class Order {
    String note;
    String storeInfo;
    //String drink;
    List<DrinkOrder> drinkOrders;

    public int total()
    {
        int total=0;
        for (DrinkOrder drinkOrder :drinkOrders)
        {
            //total+=drink.mPrice;
            total+=drinkOrder.total();
        }
        return total;
    }

}
