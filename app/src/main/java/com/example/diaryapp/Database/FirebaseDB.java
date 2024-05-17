package com.example.diaryapp.Database;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseDB {

    private Context mContext;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    public FirebaseDB(Context context) {
        mContext = context;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorage = storage.getReference();
    }

    public void createBackupAndUploadToFirebase(File databaseFile) {
        try {
            // Upload file to Firebase Storage
            StorageReference backupRef = mStorage.child("backups").child("sqlite_backup.db");
            FileInputStream fileInputStream = new FileInputStream(databaseFile);
            backupRef.putStream(fileInputStream)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(mContext, "Backup uploaded to Firebase Storage", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(mContext, "Error uploading backup to Firebase Storage: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Toast.makeText(mContext, "Error creating/uploading backup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void retrieveBackupFromFirebase() {
        // Retrieve file from Firebase Storage
        StorageReference backupRef = mStorage.child("backups").child("sqlite_backup.db");
        try {
            File localFile = File.createTempFile("sqlite_backup", "db");
            backupRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(mContext, "Backup retrieved from Firebase Storage", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(mContext, "Error retrieving backup from Firebase Storage: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Toast.makeText(mContext, "Error retrieving backup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

