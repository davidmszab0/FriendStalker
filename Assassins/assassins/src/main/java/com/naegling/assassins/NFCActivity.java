package com.naegling.assassins;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.naegling.assassins.lib.NFCFunction;
import com.naegling.assassins.lib.ProfileFunction;

import org.json.JSONObject;


public class NFCActivity extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    String mode;
    String uid;
    boolean pic = false;
    boolean item = false;
    Intent intent;
    TextView textViewNFCLabel;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);


        textViewNFCLabel = (TextView) findViewById(R.id.nfcLabelTextView);


        intent = getIntent();
        mode = intent.getStringExtra("MODE");
        uid = intent.getStringExtra("UID");

        if(mode.equals("pic")) {
            pic = true;
            item = false;
            setTitle(R.string.title_take_pic);
            textViewNFCLabel.setText(R.string.description_picture);
        } else if (mode.equals("item")) {
            item = true;
            pic = false;
            setTitle(R.string.collect_item);
            textViewNFCLabel.setText(R.string.description_item);
        }

        NFCFunction.getInstance().setContext(this);
    }

    private void pickUpItem(Intent intent){
        Parcelable[] msgs = intent.getParcelableArrayExtra(NFCFunction.getInstance().getNFCAdapter().EXTRA_NDEF_MESSAGES);

        if (NFCFunction.getInstance().getNFCAdapter().ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            String message = "" + NFCFunction.getInstance().getMessageFromPi(intent);
            if (message.equals("Item collected")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                View popupView = getLayoutInflater().inflate(R.layout.item_collect_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);



            }
            else
                Toast.makeText(getApplicationContext(), "Tag not recognized " + message, Toast.LENGTH_LONG).show();

        }

    }

    private void takePicture(Intent intent){

        ProfileFunction profileFunction = new ProfileFunction();
        JSONObject json = profileFunction.setUserPicture(uid);

        Parcelable[] msgs = intent.getParcelableArrayExtra(NFCFunction.getInstance().getNFCAdapter().EXTRA_NDEF_MESSAGES);

        if (NFCFunction.getInstance().getNFCAdapter().ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NFCFunction.getInstance().sendMessageToPi("pic " + uid, intent);


        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);

        if(pic){
            takePicture(intent);
        }

        if(item){
            pickUpItem(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NFCFunction.getInstance().checkDeviceHasNFC()) {
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass())
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
            if (adapter != null)
                adapter.enableForegroundDispatch(this, intent, null, null);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null)
            adapter.disableForegroundDispatch(this);
    }

}
