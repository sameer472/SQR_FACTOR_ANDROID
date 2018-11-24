package com.hackerkernel.user.sqrfactor.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    //instance field
    private static SharedPreferences mSharedPreference;
    private static MySharedPreferences mInstance = null;
    private static Context mContext;

    //Shared Preference key
    private String KEY_PREFERENCE_NAME = "sqr_factor";


    //private keyS
    public String KEY_DEFAULT = null;


    public MySharedPreferences() {
        if (mContext != null){
            mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }

    //Method to set boolean for (AppIntro)
    public void setBooleanKey(String keyname) {
        mSharedPreference.edit().putBoolean(keyname, true).apply();
    }

    public void setBooleanKey(String keyname, boolean state) {
        mSharedPreference.edit().putBoolean(keyname, state).apply();
    }

    /*
     * Method to get boolan key
     * true = means set
     * false = not set (show app intro)
     * */
    public boolean getBooleanKey(String keyname) {
        return mSharedPreference.getBoolean(keyname, false);
    }

    //Method to store user Mobile number
    public boolean setKey(String keyname, String mobile) {
        mSharedPreference.edit().putString(keyname, mobile).apply();
        return false;
    }

    //Method to get ChatUser mobile number
    public String getKey(String keyname) {
        return mSharedPreference.getString(keyname, KEY_DEFAULT);
    }

    public boolean setID(String keyname, String id) {
        mSharedPreference.edit().putString(keyname, id).apply();
        return false;
    }

    //Method to get ChatUser mobile number
    public String getID(String keyname) {
        return mSharedPreference.getString(keyname, KEY_DEFAULT);
    }
    public void setInt (String key, int value){
        mSharedPreference.edit().putInt(key,value).apply();;
    }
    public int getInt (String key){
        return mSharedPreference.getInt(key,0);
    }
    public Boolean chk (String key){
        return mSharedPreference.contains(key);
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.remove(key);
        editor.apply();
    }
}
