package com.example.diaryapp.Models;

import android.net.Uri;

import java.util.Date;
import java.util.List;

public class DiaryEntry {
    private int entry_id;
    private String title, content;
    private String date;
    private List<Uri> ImageUris;

    public DiaryEntry() {
    }

    public DiaryEntry(String title, String content, String date, List<Uri> imageUris) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.ImageUris = imageUris;
    }

    public DiaryEntry(int entry_id, String title, String content, String date, List<Uri> imageUris) {
        this.entry_id = entry_id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.ImageUris = imageUris;
    }

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Uri> getImageUris() {
        return ImageUris;
    }

    public void setImageUris(List<Uri> imageUris) {
        ImageUris = imageUris;
    }
}
