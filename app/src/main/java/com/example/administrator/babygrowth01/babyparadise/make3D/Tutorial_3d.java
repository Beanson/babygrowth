package com.example.administrator.babygrowth01.babyparadise.make3D;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.babygrowth01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tutorial_3d extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.model3d_tutorial, container, false);
        return view;
    }

    public void initWidget(){

    }

    private View view;
}
