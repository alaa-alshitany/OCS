package com.example.ocs.Intro.login

import android.content.Context
import android.content.SharedPreferences

class Prefrences(context: Context) {
    private val TAG_STATUS="status"
    private val TAG_LEVEL="level"
    private val TAG_APP="app"

    private val pref:SharedPreferences= context.getSharedPreferences(TAG_APP,Context.MODE_PRIVATE)

    var prefStatus : Boolean
        get() = pref.getBoolean(TAG_STATUS,false)
        set(value)=pref.edit().putBoolean(TAG_STATUS,value).apply()

    var prefLevel:String?
        get() = pref.getString(TAG_LEVEL,"")
        set(value) = pref.edit().putString(TAG_LEVEL,value).apply()

    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()
    }
}