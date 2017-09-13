package example.umbrella.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import example.umbrella.DataManager.LocalDataManager;
import example.umbrella.DataManager.UmbrellsConstants;
import example.umbrella.R;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class SettingsActivity extends Activity {
    private SharedPreferences prefs;
    private TextView mZipCode;
    private TextView mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        prefs = getSharedPreferences(UmbrellsConstants.umbrella, Context.MODE_PRIVATE);
        mZipCode = (TextView) findViewById(R.id.zipCode);
        mType = (TextView) findViewById(R.id.unitsType);
    }

    @Override
    public void onResume() {
        super.onResume();
        String zip = LocalDataManager.getInstance().getZipCode();
        mZipCode.setText(zip);
        String type = prefs.getString(UmbrellsConstants.tempType,UmbrellsConstants.english);
        if (type.equals(UmbrellsConstants.english)){
            mType.setText(UmbrellsConstants.fDegree);
        }else {
            mType.setText(UmbrellsConstants.cDegree);
        }
    }

    private void showZipcodeDialog(){
        final EditText zip = new EditText(this);
        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle("Enter ZipCode.");
        alertDialog.setView(zip);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, UmbrellsConstants.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = zip.getText().toString();
                        if (!(value.equals(""))) {
                            prefs.edit().putString(UmbrellsConstants.zipCode, value);
                            mZipCode.setText(value);
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showtypeDialog(){
        Dialog dialog;
        final String[] items = UmbrellsConstants.degrees;
        String itemsSelected = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals(UmbrellsConstants.fDegree)){
                    mType.setText(UmbrellsConstants.fDegree);
                    LocalDataManager.getInstance().setTempType(UmbrellsConstants.english);
                    SharedPreferences preferences = getBaseContext().getSharedPreferences(UmbrellsConstants.umbrella,Context.MODE_PRIVATE);
                    preferences.edit().putString(UmbrellsConstants.tempType,UmbrellsConstants.english).commit();
                }else if (items[which].equals(UmbrellsConstants.cDegree)){
                    mType.setText(UmbrellsConstants.cDegree);
                    LocalDataManager.getInstance().setTempType(UmbrellsConstants.metric);
                    SharedPreferences preferences = getBaseContext().getSharedPreferences(UmbrellsConstants.umbrella,Context.MODE_PRIVATE);
                    preferences.edit().putString(UmbrellsConstants.tempType,UmbrellsConstants.metric).commit();
                }
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void onZipClicked(View v) {
        this.showZipcodeDialog();
    }

    public void onTypeClicked(View v) {
        this.showtypeDialog();
    }
    public void onBackClicked(View v){
        Intent intent = new Intent(SettingsActivity.this, WeatherHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
