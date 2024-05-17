package com.example.diaryapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diaryapp.R;
import com.example.diaryapp.activities.SettingPage;

import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton settingBtn;
    Context context;
    private String username;
    private static final String UNSPLASH_API_KEY = "wmhhlkg5YGHN1iioZxPoRt0_TaGkFVDldYk9jAZek-s";
    private static final String UNSPLASH_API_URL = "https://api.unsplash.com/photos/random";



    public HomeFragment() {
        // Required empty public constructor
    }
    public HomeFragment(String username) {
        this.username = username;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        settingBtn = view.findViewById(R.id.settingBtn);
        TextView usernameTxt = view.findViewById(R.id.usernameTxt);
        usernameTxt.setText(username);
        context = view.getContext();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SettingPage.class));
            }
        });



        return view;
    }
//    private void loadRandomNatureImage() {
//        String url = UNSPLASH_API_URL + "?query=nature&client_id=" + UNSPLASH_API_KEY;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//            try {
//                JSONObject urlsObject = response.getJSONObject("urls");
//                String imageUrl = urlsObject.getString("regular");
//                Glide.with(this)
//                        .load(imageUrl)
//                        .into(ima);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, error -> {
//            // Handle error
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }



}