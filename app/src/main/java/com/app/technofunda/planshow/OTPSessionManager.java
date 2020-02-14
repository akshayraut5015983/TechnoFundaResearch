package com.app.technofunda.planshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import com.app.technofunda.util.Config;

public class OTPSessionManager {
    SharedPreferences pref1;
    Editor editor1;
    Context _context;
    int PRIVATE_MODE = 0;
    public OTPSessionManager(Context context) {
        this._context = context;
        pref1 = _context.getSharedPreferences(Config.PREF_NAME1, PRIVATE_MODE);
        editor1 = pref1.edit();
    }

    public void createOTPSession(String otp, String planmobile) {
        editor1.putBoolean(Config.IS_OTP, true);
        editor1.putString(Config.KEY_OTP, otp);
        editor1.putString(Config.KEY_OTPMOBILE, planmobile);
        editor1.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Config.KEY_OTP, pref1.getString(Config.KEY_OTP, null));
        user.put(Config.KEY_OTPMOBILE, pref1.getString(Config.KEY_OTPMOBILE, null));
        return user;
    }

    public void ClearOTP() {
        editor1.clear();
        editor1.commit();
        Intent i = new Intent(_context, PlanshowMobileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public boolean isOTPIn()
    {
        return pref1.getBoolean(Config.IS_OTP, false);
    }
}
