package com.app.technofunda.booking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.technofunda.R;

/**
 * Created by Administrator on 08/03/2017.
 */

public class bus extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bus, container, false);
        return v;


    }

    public static bus newInstance(String text) {

        bus f = new bus();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}
