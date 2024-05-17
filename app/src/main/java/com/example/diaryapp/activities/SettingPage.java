package com.example.diaryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.diaryapp.Database.FirebaseDB;
import com.example.diaryapp.R;

import java.io.File;

public class SettingPage extends AppCompatActivity {
    EditText usernameTxt, emailTxt, passwordTxt, comfirmPasswordTxt;
    Button backupBtn,saveBtn, restoreBtn;
    ImageButton username_editBtn, password_editbtn, backBtn;
    View passwordlayout, comfirmLayoutPassword;
    String password;
    boolean isuserauthorized = false;
    private Context context =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","UserName");
        String email = sharedPreferences.getString("email","User@email.com");
        password = sharedPreferences.getString("password", "");


        usernameTxt = findViewById(R.id.usernameEditTxt);
        emailTxt = findViewById(R.id.emailEditTxt);
        passwordTxt = findViewById(R.id.passwordtxt);
        comfirmPasswordTxt = findViewById(R.id.comfirmpasswordTxt);
        username_editBtn = findViewById(R.id.usernameEditBtn);
        password_editbtn = findViewById(R.id.passwordEditBtn);
        backupBtn = findViewById(R.id.backupBtn);
        restoreBtn = findViewById(R.id.restoreBtn);
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);

        passwordlayout = findViewById(R.id.passwordLayout);
        comfirmLayoutPassword = findViewById(R.id.comfirmpasswordLayout);


        usernameTxt.setText(username);
        emailTxt.setText(email);
        passwordTxt.setText(password);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });
        username_editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameTxt.setText("");
                emailTxt.setText("");
                usernameTxt.setHint("Enter your new username here...");
                emailTxt.setHint("Enter your new email here...");
                saveBtn.setVisibility(View.VISIBLE);

            }
        });
        password_editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorizeuser();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = usernameTxt.getText().toString();
                String newEmail = emailTxt.getText().toString();
                String newpassword = passwordTxt.getText().toString();

                // Update SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.putString("email", newEmail);
                editor.putString("password", newpassword);
                editor.apply();
                startActivity(new Intent(context,MainActivity.class));
                Toast.makeText(SettingPage.this, "Information Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        restoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDB obj = new FirebaseDB(context);
                obj.retrieveBackupFromFirebase();
            }
        });
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File databaseFile = context.getDatabasePath("DiaryApp");
                //Toast.makeText(context, "Database file path: " + databaseFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                FirebaseDB obj = new FirebaseDB(context);
                obj.createBackupAndUploadToFirebase(databaseFile);
            }
        });
    }

    private void authorizeuser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Authorization Required");
        builder.setMessage("Enter your password");
        final EditText passwordInput = new EditText(this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String enteredPassword = passwordInput.getText().toString();
                validatePassword(enteredPassword);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void validatePassword(String enteredPassword) {
        if (password.equals(enteredPassword)) {
            Toast.makeText(SettingPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
            passwordTxt.setText("");
            comfirmPasswordTxt.setText("");
            passwordTxt.setHint("Enter your new password here...");
            comfirmPasswordTxt.setHint("Re enter your password...");
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(SettingPage.this, "You are not authorized for this action", Toast.LENGTH_SHORT).show();
            isuserauthorized = false;
        }
    }
}