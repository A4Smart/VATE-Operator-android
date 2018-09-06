package it.a4smart.vateoperator;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        //TODO eventually change with the definitive one, with all the beacons.
        EstimoteSDK.initialize(getApplicationContext(),"vate-operator-4jg", "07281b6927ce3e6377ed2eb04d35949f");

        super.onCreate();
    }
}
