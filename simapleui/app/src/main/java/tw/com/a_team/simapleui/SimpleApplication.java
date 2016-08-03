package tw.com.a_team.simapleui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by user on 2016/8/2.
 */
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Order.class);
        ParseObject.registerSubclass(DrinkOrder.class);
        ParseObject.registerSubclass(Drink.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vhL6LgcU773lpct0KrEdhjExLuutydo29iRgb71U")
                        //.applicationId("76ee57f8e5f8bd628cc9586e93d428d5")
                .server("https://parseapi.back4app.com/") //"http://parseserver-ps662-env.us-east-1.elasticbeanstalk.com/parse/"
                .clientKey("KtYag491w1xTssk7p8GIXSFrjK0kRD3wuzzL9JTe")
                .enableLocalDataStore()
                .build()
        );

    }
}
