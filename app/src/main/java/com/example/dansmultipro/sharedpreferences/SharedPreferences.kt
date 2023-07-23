package com.example.dansmultipro.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.dansmultipro.model.User

class SharedPreferences(var context: Context) {

    val PRIVATE_MODE = 0
    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLogin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun setPref(response: User) {
        editor?.putString("fullname", response.fullname)
        editor?.putString("email", response.email)
        editor?.commit()
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }

}