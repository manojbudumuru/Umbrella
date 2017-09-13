package example.umbrella.Network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.umbrella.DataManager.LocalDataManager;
import example.umbrella.DataManager.UmbrellsConstants;
import example.umbrella.InterfacesListeners.NetworkCallBacks;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class NetworkManager extends AsyncTask<String,String,String> {
    private NetworkCallBacks mListener;
    private Boolean isLocationCall;


    public NetworkManager(NetworkCallBacks callBacks){
        mListener = callBacks;
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        String callType = params[1];
        URL newUrl = null;
        StringBuffer response = new StringBuffer();
        String inputLine;
        String result = "";
        try {
            newUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) newUrl.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d("response :",response.toString());
                result = response.toString();
                if (callType.equals(UmbrellsConstants.callTypeForecast)){
                    isLocationCall = false;
                    parseTemperatureJsonData(result);
                }else if (callType.equals(UmbrellsConstants.callTypeState)){
                    isLocationCall = true;
                    parseCityJsonData(result);
                }
            } else {
                result = "";
                Log.d("Error :"," "+connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            result = "";
        } catch (ProtocolException e) {
            result = "";
        } catch (IOException e) {
            result = "";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Boolean responceData;
        if (mListener != null){
            if (result.equals("")){
                responceData = false;
            }else {
                responceData = true;
            }
            mListener.onNetworkCallCompleted(responceData,isLocationCall);
        }
    }

    private void parseTemperatureJsonData(String data){

        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        String currentDate = dateFormat.format(date).toString();
        Log.d("Hour Value:",currentDate);
        int currentTime = Integer.parseInt(currentDate);
        int newArrayLength = (24 - currentTime) + 24;
        LocalDataManager.getInstance().setCurrentHour(currentTime+1);
        System.out.print(currentDate);
        String tempType = LocalDataManager.getInstance().getTempType();
        if (tempType.equals("")){
            tempType = UmbrellsConstants.fDegree;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(UmbrellsConstants.hourly_forecast);
            if (jsonArray.length()>0){
                int value = jsonArray.length();
                String hours[] = new String[jsonArray.length()];
                String tempValues[] = new String[jsonArray.length()];
                String conditions[] = new String[jsonArray.length()];
                JSONObject jsonObject1;
                for (int i = 0; i< newArrayLength; i ++){
                    jsonObject1= jsonArray.getJSONObject(i);
                    JSONObject fcTime = jsonObject1.getJSONObject(UmbrellsConstants.fctime);
                    hours[i] = new String(fcTime.getString(UmbrellsConstants.hour));
                    JSONObject temperatures = jsonObject1.getJSONObject(UmbrellsConstants.temp);
                    tempValues[i] = new String(temperatures.getString(tempType));
                    conditions[i] = new String(jsonObject1.getString(UmbrellsConstants.condition));
                }
                LocalDataManager.getInstance().setCondition(conditions);
                LocalDataManager.getInstance().setHours(hours);
                LocalDataManager.getInstance().setTeperature(tempValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("response :","-------------------Done------------------");
    }

    private void parseCityJsonData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject(UmbrellsConstants.location);
            String city = jsonObject1.getString(UmbrellsConstants.city);
            String state = jsonObject1.getString(UmbrellsConstants.state);
            String fullName = city+", "+state;
            LocalDataManager.getInstance().setCityFullName(fullName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("response :","-------------------Done------------------");
    }
}
