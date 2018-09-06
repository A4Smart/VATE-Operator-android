package it.a4smart.vateoperator;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.service.BeaconManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    private NfcAdapter nfcAdapter;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::newBeacon);

        beaconManager = new BeaconManager(this);
        region = new BeaconRegion("Ranging region", UUID.fromString(Constants.VATE_UUID),null, null);

        scanBeacons();
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

    private void setupNFC(final Activity activity) {
        Log.d(TAG, "setupNFC: init");
        final Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcAdapter.enableReaderMode(activity, this::handleNFC, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);

        Log.d(TAG, "setupNFC: done");
    }

    private void disableNFC(final Activity activity) {
        nfcAdapter.disableReaderMode(activity);
        Log.d(TAG, "disableNFC: disabled");
    }

    private void handleNFC(Tag tag) {
        Ndef res = Ndef.get(tag);

        if(res != null) {
            try {
                res.connect();
                Log.d(TAG, "onTagDiscovered: " + new BigInteger(1, res.getNdefMessage().getRecords()[0].getPayload()).toString(16));
            } catch (FormatException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        setupNFC(this);

        beaconManager.connect(() -> beaconManager.startRanging(region));

        super.onResume();
    }

    @Override
    protected void onPause() {
        disableNFC(this);

        beaconManager.stopRanging(region);
        super.onPause();
    }
}
