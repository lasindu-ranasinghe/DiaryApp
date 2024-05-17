package com.example.diaryapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import android.util.Base64;

import com.example.diaryapp.R;
import com.example.diaryapp.Utils.User;

public class Login extends AppCompatActivity {
    Context context;
    Button loginButton;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            startActivity(new Intent(this, SetupPage.class));
            finish();
        } else {
            setContentView(R.layout.activity_login);
        }

        passwordEditText = findViewById(R .id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        context =this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                String storedEncryptedPassword = sharedPreferences.getString("password", "");
                String username = sharedPreferences.getString("username", "");

                String enteredPassword = passwordEditText.getText().toString();

                // Encrypt the entered password
//                User obj = new User();
//                String enteredEncryptedPassword = obj.encryptPassword(enteredPassword);

                // Compare encrypted passwords
                if (storedEncryptedPassword.equals(enteredPassword)) {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "Login Failed. Incorrect Password", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Login.this, "Login Failed. Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
