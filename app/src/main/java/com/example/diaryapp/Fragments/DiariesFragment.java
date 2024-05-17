package com.example.diaryapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diaryapp.Adapters.DiaryAdapter;
import com.example.diaryapp.Database.DiaryDBHandler;
import com.example.diaryapp.Models.DiaryEntry;
import com.example.diaryapp.R;
import com.example.diaryapp.activities.MainActivity;
import com.example.diaryapp.activities.SingleEntryPage;

import java.util.ArrayList;
import java.util.List;

public class DiariesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DiaryEntry> diaryEntries;
    private TextView messageTxt;
    private ListView listView;
    private DiaryDBHandler diaryDBHandler;
    Context context;

    public DiariesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Diaries.
     */
    // TODO: Rename and change types and number of parameters
    public static DiariesFragment newInstance(String param1, String param2) {
        DiariesFragment fragment = new DiariesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diaries, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = this.getContext();

        diaryDBHandler= new DiaryDBHandler(context);
        listView = view.findViewById(R.id.diarieslistview);
        messageTxt = view.findViewById(R.id.messageTxt);

        diaryEntries = new ArrayList<>();

        diaryEntries = diaryDBHandler.getAllDiaryEntries();
        if(diaryEntries.size() != 0){
            DiaryAdapter adapter = new DiaryAdapter(context,R.layout.single_entry,diaryEntries);
            listView.setAdapter((adapter));
        }else{
            messageTxt.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DiaryEntry diaryentry = diaryEntries.get(position);
                int entryId = diaryentry.getEntry_id();
                Intent intent = new Intent(getActivity(), SingleEntryPage.class);
                Bundle b = new Bundle();
                b.putInt("entryId", entryId);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }

}