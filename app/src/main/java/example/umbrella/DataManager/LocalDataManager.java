package example.umbrella.DataManager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class LocalDataManager {

    private int currentHour;
    private String zipCode;
    private String tempType;
    private String cityFullName;
    private String hours[];
    private String condition[];
    private String teperature[];

    private static LocalDataManager sharedManager = null;

    public static LocalDataManager getInstance(){
        if (sharedManager == null){
            sharedManager = new LocalDataManager();
        }
        return sharedManager;
    }
    private LocalDataManager(){

    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getCityFullName() {
        return cityFullName;
    }

    public void setCityFullName(String cityFullName) {
        this.cityFullName = cityFullName;
    }

    public String[] getHours() {
        return hours;
    }

    public void setHours(String[] hours) {
        this.hours = hours;
    }

    public String[] getCondition() {
        return condition;
    }

    public void setCondition(String[] condition) {
        this.condition = condition;
    }

    public String[] getTeperature() {
        return teperature;
    }

    public void setTeperature(String[] teperature) {
        this.teperature = teperature;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }
}
