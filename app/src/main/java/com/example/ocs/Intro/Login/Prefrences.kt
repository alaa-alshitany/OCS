package com.example.ocs.Intro.Login

import android.content.Context
import android.content.SharedPreferences

class Prefrences(context: Context) {
    private val TAG_STATUS="status"
    private val TAG_LEVEL="level"
    private val TAG_APP="app"
    private val TAG_ID="userID"
    private val TAG_USERNAME=""
    private val pref:SharedPreferences= context.getSharedPreferences(TAG_APP,Context.MODE_PRIVATE)

    var prefStatus : Boolean
        get() = pref.getBoolean(TAG_STATUS,false)
        set(value)=pref.edit().putBoolean(TAG_STATUS,value).apply()

    var prefLevel:String?
        get() = pref.getString(TAG_LEVEL,"")
        set(value) = pref.edit().putString(TAG_LEVEL,value).apply()

    var prefID:String?
    get() = pref.getString(TAG_ID,"")
    set(value) =pref.edit().putString(TAG_ID,value).apply()

    var userName:String?
    get() = pref.getString(TAG_USERNAME,"")
    set(value) = pref.edit().putString(TAG_USERNAME,value).apply()

    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()
        pref.edit().remove(TAG_ID).apply()
    }
}