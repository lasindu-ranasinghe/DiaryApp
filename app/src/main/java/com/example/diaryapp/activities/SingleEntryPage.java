package com.example.diaryapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diaryapp.Adapters.ImageAdapter;
import com.example.diaryapp.Database.DiaryDBHandler;
import com.example.diaryapp.Models.DiaryEntry;
import com.example.diaryapp.R;

import java.util.Date;
import java.util.List;

public class SingleEntryPage extends AppCompatActivity {
    private ImageButton backBtn, deleteBtn, editBtn ;
    private TextView titleTxt, datetxt, contentTxt ;
    private ListView imageList;
    private int entryId;
    private List<Uri> imageUris;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_entry_page);

        backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);

        titleTxt = findViewById(R.id.titleTxt);
        datetxt = findViewById(R.id.dateTxt);
        contentTxt = findViewById(R.id.contentTxt);

        imageList = findViewById(R.id.imageList);
        context = this;

        backBtn.setOnClickListener(view -> goToHomeFragment());

        DiaryDBHandler dbHandler = new DiaryDBHandler(context);
        Bundle b = getIntent().getExtras();
        if(b != null)
            entryId = b.getInt("entryId");

        DiaryEntry entry = dbHandler.getDiaryEntry(entryId);
        titleTxt.setText(entry.getTitle());
        datetxt.setText(entry.getDate());
        contentTxt.setText(entry.getContent());
        imageUris = entry.getImageUris();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.deleteDiaryEntry(entryId);
                Toast.makeText(context, "Deleted successfull!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context,MainActivity.class));
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeFragment();
            }
        });
        loadImages();

//        if(imageUris == null)
//        {
//
//        }else
//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                // Android is 11 (R) or above
//                loadImages();
//            } else {
//                // Below Android 11
//                int writePermission = ContextCompat.checkSelfPermission(SingleEntryPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                int readPermission = ContextCompat.checkSelfPermission(SingleEntryPage.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//                if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
//                    // Request permissions
//                    ActivityCompat.requestPermissions(SingleEntryPage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
//                } else {
//                    // Permissions already granted, set image
//                    loadImages();
//                }
//            }
//        }

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

    private void loadImages() {
        if (checkPermission()) {
            ImageAdapter adapter = new ImageAdapter(context, R.layout.single_image, imageUris);
            imageList.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Your do not have permission for this... Go to app settings and give nessesory permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToHomeFragment() {
        startActivity(new Intent(context,MainActivity.class));
    }
}