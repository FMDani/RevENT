package com.example.revent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class LogFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "method_name";
    private static final String ARG_PARAM2 = "method_prms";

    // TODO: Rename and change types of parameters
    /*
    private String mParam1;
    private String mParam2;
    */
    public LogFragment() {
        // Required empty public constructor
    }


    protected String callbackName;
    protected Class[] callbackPrms;

    public static LogFragment newInstance(Class<? extends LogFragment> clazz, String param1, Class<?>... prms) {
        //LoginFragment fragment = new LoginFragment();
        LogFragment instance = null;

        try {
            Constructor<?> constructor = clazz.getConstructor();
            instance = (LogFragment) constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 java.lang.InstantiationException e) {

            // TODO: Handle exception
            throw new RuntimeException(e);
        }
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, prms);
        instance.setArguments(args);
        return  instance;
    }
}
