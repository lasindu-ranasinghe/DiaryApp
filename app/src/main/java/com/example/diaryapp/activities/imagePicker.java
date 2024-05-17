package com.example.diaryapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diaryapp.R;

public class imagePicker extends AppCompatActivity {

    Button imagePickBtn;
    ImageView imageview;

    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        imagePickBtn = findViewById(R.id.imagePickerBtn);
        imageview = findViewById(R.id.imageView);
        registerResult();

        imagePickBtn.setOnClickListener(view -> pickImage());
    }
    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            imageview.setImageURI(imageUri);
                        }catch (Exception e){
                            Toast.makeText(imagePicker.this,"No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}