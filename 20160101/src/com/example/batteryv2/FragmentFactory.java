package com.example.batteryv2;

import com.example.batteryv2.R;

import android.app.Fragment;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case R.id.bt1:
                fragment = new BatteryInfoFragment();
                break;
            case R.id.bt2:
                fragment = new ForecastFragment();
                break;
            case R.id.bt3:
                fragment = new StatisticFragment();
                break;
            case R.id.bt4:
                fragment = new SettingFragment();
                break;
            case R.id.bt5:
                fragment = new SqlModeFragment();
                break;
        }
        return fragment;
    }
}
