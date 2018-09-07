package it.a4smart.vateoperator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.service.BeaconManager;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";

    private BeaconManager beaconManager;
    private BeaconRegion region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::newBeacon);

        beaconManager = new BeaconManager(this);
        region = new BeaconRegion("Ranging region", UUID.fromString(Constants.VATE_UUID),null, null);

        //scanBeacons();
    }

    private void scanBeacons() {
        beaconManager.setRangingListener((BeaconManager.BeaconRangingListener) (beaconRegion, beacons) -> {
            if (!beacons.isEmpty()) {
                Log.d(TAG, "onBeaconsDiscovered: "+ beacons);
            }
        });
    }

    private void newBeacon (View view){
        startActivity(new Intent(MainActivity.this, SetupActivity.class));
    }

    @Override
    protected void onResume() {
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(() -> beaconManager.startRanging(region));

        super.onResume();
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }
}
