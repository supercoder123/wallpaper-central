package com.apps.fernandes.ashley.wallpapercentral.fragments;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.fernandes.ashley.wallpapercentral.R;

/**
 * Created by gf on 26-06-2017.
 */

public class SearchFragment extends Fragment {
    private EditText searchBox;
    private ImageButton searchButton;
    private View mView;
    private String searchText;


    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        searchBox = (EditText) mView.findViewById(R.id.search_box);
        searchButton = (ImageButton) mView.findViewById(R.id.imageButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = searchBox.getText().toString().trim();

            }
        });

        customToast("Search");

        return mView;


    }
    public void customToast(String text){

        LayoutInflater li = getActivity().getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        View layout = li.inflate(R.layout.custom_toast,
                (ViewGroup) mView.findViewById(R.id.toastRoot));
        TextView textView = (TextView) layout.findViewById(R.id.toastText);
        textView.setText(text);
        final Toast toast = new Toast(getContext().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);//setting the view of custom toast layout
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

}
