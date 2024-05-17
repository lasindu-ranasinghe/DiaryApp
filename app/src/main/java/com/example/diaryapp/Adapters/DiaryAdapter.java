package com.example.diaryapp.Adapters;

import android.content.Context;
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

public class DiaryAdapter extends ArrayAdapter<DiaryEntry> {

    private Context context;
    private int resource;
    List<DiaryEntry> diaryEntries;

    public DiaryAdapter(Context context, int resource, List<DiaryEntry> diaryEntries){
        super(context,resource,diaryEntries);
        this.context = context;
        this.resource = resource;
        this.diaryEntries = diaryEntries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource,parent,false);

        TextView titleTxt = row.findViewById(R.id.titleTxt);
        TextView dateTxt = row.findViewById(R.id.dateTxt);

        // entries [obj1,obj2,obj3]
        DiaryEntry entries = diaryEntries.get(position);
        titleTxt.setText(entries.getTitle());

        dateTxt.setText(entries.getDate());

        return row;
    }
}
