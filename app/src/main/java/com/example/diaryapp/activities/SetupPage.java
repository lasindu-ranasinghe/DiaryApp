package com.example.diaryapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import android.util.Base64;
import android.widget.Toast;

import com.example.diaryapp.R;
import com.example.diaryapp.Utils.User;

public class SetupPage extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 1001;
    private Context context =this;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_page);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button saveButton = findViewById(R.id.saveButton);
        mainLayout = findViewById(R.id.mainLayout);

        if (checkPermission()) {
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            requestPermission();
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get user details
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Encrypt password
//                User obj = new User();
//                String encryptedPassword = obj.encryptPassword(password);

                // Store user details in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.putBoolean("isFirstLaunch", false); // Marking first launch as false
                editor.apply();

                // Navigate back to the main activity
                startActivity(new Intent(SetupPage.this, MainActivity.class));
                finish();
            }
        });
    }
    private boolean checkPermission() {
        // Check if the READ_EXTERNAL_STORAGE permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading images
                mainLayout.setVisibility(View.VISIBLE);
            } else {
                requestPermission();
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void requestPermission() {
        // Display a dialog explaining why the permission is needed
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Needed");
        builder.setMessage("This app needs to access your photos to display them in your diary entries.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Continue with permission request
                ActivityCompat.requestPermissions(SetupPage.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.setNeutralButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Intent to open app settings
                openAppSettings();
            }
        });

        builder.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
