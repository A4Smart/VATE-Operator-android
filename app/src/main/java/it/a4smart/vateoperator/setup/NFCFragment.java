package it.a4smart.vateoperator.setup;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.math.BigInteger;

import it.a4smart.vateoperator.InstallableBeacon;
import it.a4smart.vateoperator.R;

public class NFCFragment extends Fragment {
    private final String TAG = "NFCFragment";
    private NfcAdapter nfcAdapter;
    private InstallableBeacon beacon;

    public static NFCFragment newInstance() {
        return new NFCFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        return inflater.inflate(R.layout.fragment_nfc, container, false);
    }

    @Override
    public void onResume() {
        setupNFC(getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        disableNFC(getActivity());
        super.onPause();
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
        String id = null;

        if(res != null) {
            try {
                res.connect();
                id = byteArrToHex(res.getNdefMessage().getRecords()[0].getPayload());
            } catch (FormatException | IOException e) {
                e.printStackTrace();
                nfcRetrySnackbar();
            }

            if (id!=null && saveBeaconId(id)) {
                enableNextBtn();
            }
        }
    }

    private boolean saveBeaconId(String id) {
        if  (!id.equals("") && (!beacon.identifierSetted() || !beacon.equalsIdentifier(id))) {
            beacon.setIdentifier(id);
            return true;
        }
        return false;
    }

    String byteArrToHex(byte[] arr) {
        return new BigInteger(1, arr).toString(16);
    }

    private void nfcRetrySnackbar() {
        Snackbar.make(getView(), R.string.retry_snackbar, Snackbar.LENGTH_LONG).show();
    }

    private void enableNextBtn() {
        Intent intent = new Intent("frag_finished");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        Log.d(TAG, "enableNextBtn: told activity to show button");
    }


}
