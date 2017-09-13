package example.umbrella.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import example.umbrella.DataManager.ImageItem;
import example.umbrella.DataManager.LocalDataManager;
import example.umbrella.DataManager.UmbrellsConstants;
import example.umbrella.InterfacesListeners.NetworkCallBacks;
import example.umbrella.Network.NetworkManager;
import example.umbrella.R;
import example.umbrella.Adapter.TemperatureViewAdapter;

public class WeatherHome extends Activity implements NetworkCallBacks {

    private String[] temeratureArray;
    private String[] conditionArray;
    private String[] timeArray;
    private String[] todayTemeratureArray;
    private String[] todayConditionArray;
    private String[] todayTimeArray;
    private String[] tomTemeratureArray;
    private String[] tomConditionArray;
    private String[] tomTimeArray;
    private TextView mLocation;
    private TextView mClimateValue;
    private TextView mClimateType;
    private GridView mtodayView;
    private GridView mTomorrowView;
    private TemperatureViewAdapter todaysGridAdapter;
    private TemperatureViewAdapter tomGridAdapter;
    private LinearLayout mainView;
    private String zipCode;
    private String tempType;
    private int currentTime;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
        this.createViews();
        prefs = getSharedPreferences(UmbrellsConstants.umbrella,Context.MODE_PRIVATE);
    }

    private void createViews(){
        mLocation = (TextView) findViewById(R.id.locationString);
        mClimateValue = (TextView) findViewById(R.id.climateTemp);
        mClimateType = (TextView) findViewById(R.id.climateType);
        mtodayView = (GridView) findViewById(R.id.todayView);
        mTomorrowView = (GridView) findViewById(R.id.tommorowView);
        mainView = (LinearLayout) findViewById(R.id.climateMainView);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = prefs.edit();
        LocalDataManager.getInstance().setTempType(prefs.getString(UmbrellsConstants.tempType,UmbrellsConstants.english));

        if (prefs.getBoolean(UmbrellsConstants.appStatus,false)){
            editor.putBoolean(UmbrellsConstants.appStatus,true);
            editor.putString(UmbrellsConstants.tempType,UmbrellsConstants.english);
            editor.commit();
            LocalDataManager.getInstance().setTempType(UmbrellsConstants.english);
            this.showZipcodeDialog();
        }else {
            if (prefs.getBoolean(UmbrellsConstants.zipCodeStatus,false)){
                String value = prefs.getString(UmbrellsConstants.zipCode,"");
                if (value.equals("")){
                    editor.putBoolean(UmbrellsConstants.zipCodeStatus,false);
                    editor.commit();
                    this.showZipcodeDialog();
                }else {
                    zipCode = value;
                    editor.putString(UmbrellsConstants.zipCode,zipCode);
                    LocalDataManager.getInstance().setZipCode(zipCode);
                    editor.putBoolean(UmbrellsConstants.zipCodeStatus,true);
                    editor.commit();
                    progressDialog = ProgressDialog.show(this, "", "Loading...");
                    new NetworkManager(this).execute(fullURL(zipCode,UmbrellsConstants.locationNameUrl),UmbrellsConstants.callTypeState);
                }
            }else {
                this.showZipcodeDialog();
            }
        }
    }

    private void showZipcodeDialog(){
        final EditText zip = new EditText(this);
        zip.setGravity(Gravity.CENTER);
        AlertDialog alertDialog = new AlertDialog.Builder(WeatherHome.this).create();
        alertDialog.setTitle("Enter ZipCode.");
        alertDialog.setView(zip);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, UmbrellsConstants.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = zip.getText().toString();
                        SharedPreferences.Editor editor = prefs.edit();
                        if (value.equals("")) {
                            editor.putBoolean(UmbrellsConstants.zipCodeStatus,false);
                            editor.commit();
                            showErrorMessage();
                        }else {
                            editor.putBoolean(UmbrellsConstants.zipCodeStatus,true);
                            editor.putString(UmbrellsConstants.zipCode, value);
                            editor.commit();
                            zipCode= value;
                            LocalDataManager.getInstance().setZipCode(zipCode);
                            progressDialog = ProgressDialog.show(WeatherHome.this, "", "Loading...");
                            new NetworkManager(WeatherHome.this).execute(fullURL(zipCode,UmbrellsConstants.locationNameUrl),UmbrellsConstants.callTypeState);
                            dialog.dismiss();
                        }
                    }
                });
        alertDialog.show();
    }

    private void showErrorMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(WeatherHome.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please Enter Valid ZipCode.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, UmbrellsConstants.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showZipcodeDialog();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //When Settings clicked.
    public void onSettingsClicked(View v) {
        Intent intent = new Intent(WeatherHome.this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onNetworkCallCompleted(Boolean result, Boolean isLocationCall) {
        if (result){
            if (isLocationCall){
                new NetworkManager(this).execute(fullURL(zipCode,UmbrellsConstants.forcastURL),UmbrellsConstants.callTypeForecast);
            }else {
                this.updateUi();
            }

        }else {
            showErrorMessage();
        }
    }

    private void updateUi(){
        mLocation.setText(LocalDataManager.getInstance().getCityFullName());
        mClimateType.setText(LocalDataManager.getInstance().getCondition()[0]);
        char tmpcValue = 0x00B0;
        String cValue = (LocalDataManager.getInstance().getTeperature()[0]);
        String cValueWithDegree = cValue+tmpcValue;
        mClimateValue.setText(cValueWithDegree);
        int tempInInt = Integer.parseInt(cValue);
        if(LocalDataManager.getInstance().getTempType().equals(UmbrellsConstants.english)){
            if (tempInInt > 60){
                this.mainView.setBackgroundColor(getResources().getColor(R.color.hotColor));
            }else {
                this.mainView.setBackgroundColor(getResources().getColor(R.color.coolColor));
            }
        }else if (LocalDataManager.getInstance().getTempType().equals(UmbrellsConstants.metric)){
            if (tempInInt > 16){
                this.mainView.setBackgroundColor(getResources().getColor(R.color.hotColor));
            }else {
                this.mainView.setBackgroundColor(getResources().getColor(R.color.coolColor));
            }
        }

        this.conditionArray = LocalDataManager.getInstance().getCondition();
        this.temeratureArray = LocalDataManager.getInstance().getTeperature();
        this.timeArray = LocalDataManager.getInstance().getHours();
        this.tempType = LocalDataManager.getInstance().getTempType();
        this.currentTime = LocalDataManager.getInstance().getCurrentHour();
        int arraySize = 23 - this.currentTime;
        int tomSize = arraySize + 24;
        this.todayTimeArray = new String[arraySize];
        this.todayTemeratureArray = new String[arraySize];
        this.todayConditionArray = new String[arraySize];
        this.tomConditionArray = new String[24];
        this.tomTemeratureArray = new String[24];
        this.tomTimeArray = UmbrellsConstants.tomTimeArray;
        for (int i = 0; i<arraySize; i++){
            this.todayConditionArray[i] = new String(this.conditionArray[i]);
            this.todayTemeratureArray[i] = new String(this.temeratureArray[i]);
            int time = Integer.parseInt(this.timeArray[i]);
            String timestring = "";
            if (time<12){
                timestring = Integer.toString(time) + ":00 AM";
            }else {
                int newTimeValue = time - 12;
                timestring = Integer.toString(newTimeValue) + ":00 PM";
            }
            this.todayTimeArray[i] = new String(timestring);
        }
        int i =0;
        for (int j = arraySize; j < tomSize; j++){
            this.tomConditionArray[i] = new String(this.conditionArray[j]);
            this.tomTemeratureArray[i] = new String(this.temeratureArray[j]);
            i++;
        }
        this.updateGridValues();
    }

    private void updateGridValues(){
        todaysGridAdapter = new TemperatureViewAdapter(this,R.layout.temperature_items,getTodayData());
        this.tomGridAdapter = new TemperatureViewAdapter(this,R.layout.temperature_items,getTomData());
        this.mtodayView.setAdapter(todaysGridAdapter);
        this.mTomorrowView.setAdapter(tomGridAdapter);
        progressDialog.dismiss();
    }

    private ArrayList<ImageItem> getTodayData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < this.todayTimeArray.length; i++){
            Bitmap bitmap;
            if (this.todayConditionArray[i].contains("Partly")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunnycloud);
            }else if(this.todayConditionArray[i].contains("Sunny")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
            }else if(this.todayConditionArray[i].contains("Clear")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
            }else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
            }
            imageItems.add(new ImageItem(bitmap,this.todayTimeArray[i],this.todayTemeratureArray[i]));
        }
        return imageItems;
    }
    private ArrayList<ImageItem> getTomData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < 24; i++){
            Bitmap bitmap;
            if (this.tomConditionArray[i].contains("Partly")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunnycloud);
            }else if(this.tomConditionArray[i].contains("Sunny")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
            }else if(this.tomConditionArray[i].contains("Clear")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
            }else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
            }
            imageItems.add(new ImageItem(bitmap,this.tomTimeArray[i],this.tomTemeratureArray[i]));
        }
        return imageItems;
    }

    public String fullURL(String zipValue, String url){
        String fullURL = url+zipValue+UmbrellsConstants.json;
        return fullURL;
    }
}
