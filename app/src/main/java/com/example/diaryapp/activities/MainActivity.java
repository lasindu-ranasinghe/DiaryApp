package com.example.diaryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.diaryapp.Fragments.AddEntryFragment;
import com.example.diaryapp.Fragments.DiariesFragment;
import com.example.diaryapp.Fragments.HomeFragment;
import com.example.diaryapp.R;
import com.example.diaryapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment(username));
        binding.bottomNavigationView.setBackground(null);

        binding.floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Put your click handling code here
//                Toast.makeText(view.getContext(), "Floating button clicked!", Toast.LENGTH_SHORT).show();
                replaceFragment(new AddEntryFragment());
            }
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment(username));
            } else if (itemId == R.id.diaries) {
                replaceFragment(new DiariesFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}