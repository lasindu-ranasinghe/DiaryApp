package com.example.diaryapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diaryapp.Models.DiaryEntry;
import com.example.diaryapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends ArrayAdapter<Uri> {

    private Context context;
    private int resource;
    List<Uri> imagesUris;

    public ImageAdapter(Context context, int resource, List<Uri> imagesUris){
        super(context,resource,imagesUris);
        this.context = context;
        this.resource = resource;
        this.imagesUris = imagesUris;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(imagesUris != null){

        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource,parent,false);

        ImageView imageView = row.findViewById(R.id.singleImageView);

        // todos [obj1,obj2,obj3]
        Uri imageuri = imagesUris.get(position);
        if(imageuri != null){
            imageView.setImageURI(imageuri);
        }
        return row;
    }
}
