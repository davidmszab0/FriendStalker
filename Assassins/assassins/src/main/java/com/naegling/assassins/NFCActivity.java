package com.naegling.assassins;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.HttpBitMap;
import com.naegling.assassins.lib.JSONParser;
import com.naegling.assassins.lib.NFCFunction;
import com.naegling.assassins.lib.ProfileFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author Johan Nilsson
 */
public class NFCActivity extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_UID = "uid";

    String mode;
    String uid;
    String oldItem = "";
    String newItem = "";
    String type = "";
    String itemId = "6";
    Bitmap[] pics;
    boolean pic = false;
    boolean item = false;
    Intent intent;
    TextView textViewNFCLabel;
    Context context;
    JSONObject json;
    ProfileFunction profileFunction;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        textViewNFCLabel = (TextView) findViewById(R.id.nfcLabelTextView);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        uid = user.get(KEY_UID);
        intent = getIntent();
        mode = intent.getStringExtra("MODE");

        if(mode.equals("pic")) {
            pic = true;
            item = false;
            setTitle(R.string.title_take_pic);
            textViewNFCLabel.setText(R.string.description_picture);
        } else if (mode.equals("item")) {
            profileFunction = new ProfileFunction();
            item = true;
            pic = false;
            setTitle(R.string.collect_item);
            textViewNFCLabel.setText(R.string.description_item);
            json = profileFunction.collectItem(uid);
            Log.e("User: ", uid);
            try {
                type = json.getString("type");
                JSONObject jsonOldItem = json.getJSONObject("item_old");
                JSONObject jsonNewItem = json.getJSONObject("item_new");
                oldItem = jsonOldItem.getString("name") +
                        "\nKill bonus: " + jsonOldItem.getString("bonus_kill") +
                        "\nDefence bonus: " + jsonOldItem.getString("bonus_surv");
                newItem = jsonNewItem.getString("name") +
                        "\nKill bonus: " + jsonNewItem.getString("bonus_kill") +
                        "\nDefence bonus: " + jsonNewItem.getString("bonus_surv");
                itemId = jsonNewItem.getString("id");
                AsyncTask bitmapTask = new HttpBitMap().execute(new URL(jsonOldItem.getString("picture")), new URL(jsonNewItem.getString("picture")));
                pics = (Bitmap[]) bitmapTask.get();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        NFCFunction.getInstance().setContext(this);
    }

    private void pickUpItem(Intent intent){
        Parcelable[] msgs = intent.getParcelableArrayExtra(NFCFunction.getInstance().getNFCAdapter().EXTRA_NDEF_MESSAGES);

        if (NFCFunction.getInstance().getNFCAdapter().ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            String message = "" + NFCFunction.getInstance().getMessageFromPi(intent);
            Toast.makeText(getApplicationContext(), "Item collected", Toast.LENGTH_LONG).show();

            View popupView = getLayoutInflater().inflate(R.layout.item_collect_popup, null);
            popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            TextView tvOldItem = (TextView) popupView.findViewById(R.id.textViewOldItem);
            TextView tvNewItem = (TextView) popupView.findViewById(R.id.textViewNewItem);
            tvOldItem.setText(oldItem);
            tvNewItem.setText(newItem);
            ImageView ivOldItem = (ImageView) popupView.findViewById(R.id.imageViewOldItem);
            ImageView ivNewItem = (ImageView) popupView.findViewById(R.id.imageViewNewItem);
            ivOldItem.setImageBitmap(pics[0]);
            ivNewItem.setImageBitmap(pics[1]);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

            ivNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Strings:", uid + ", " + type + ", " + itemId);
                    profileFunction.setItem(uid, type, itemId);
                    profileFunction.setItemCollectable(uid, false);

                    popupWindow.dismiss();
                    Toast.makeText(getApplicationContext(), newItem + " selected.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            ivOldItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    profileFunction.setItemCollectable(uid, false);
                    Toast.makeText(getApplicationContext(), oldItem + " selected.", Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                    finish();
                }
            });




        }

    }

    private void takePicture(Intent intent){

        ProfileFunction profileFunction = new ProfileFunction();
        JSONObject json = profileFunction.setUserPicture(uid);

        Parcelable[] msgs = intent.getParcelableArrayExtra(NFCFunction.getInstance().getNFCAdapter().EXTRA_NDEF_MESSAGES);

        if (NFCFunction.getInstance().getNFCAdapter().ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NFCFunction.getInstance().sendMessageToPi("pic " + uid, intent);
            //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();

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