package com.example.diaryapp.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diaryapp.Adapters.ImageAdapter;
import com.example.diaryapp.Database.DiaryDBHandler;
import com.example.diaryapp.Models.DiaryEntry;
import com.example.diaryapp.R;
import com.example.diaryapp.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddEntryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText title,content;
    private Button saveBtn;
    private ListView imagelist;
    private TextView dateTxt;
    private ImageButton galleryBtn, closeBtn, cameraBtn;
    private DiaryDBHandler diaryDBHandler;
    private FrameLayout mainfragment;
    ActivityResultLauncher<Intent> resultLauncher;
    List<Uri> imageUris;
    Context context;
    private String selectedDateTime;
    CameraDevice cameraDevice;
    CameraCaptureSession captureSession;
    CaptureRequest.Builder captureRequestBuilder;

    private TextureView textureView;
    private Size imageDimension;



    public AddEntryFragment() {

    }
    public static AddEntryFragment newInstance(String param1, String param2) {
        AddEntryFragment fragment = new AddEntryFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_diary, container, false);

        // Initialize the EditText and Button
        title  = view.findViewById(R.id.titleEditTxt);
        content = view.findViewById(R.id.contentEditTxt);
        saveBtn = view.findViewById(R.id.saveBtn);
        closeBtn = view.findViewById(R.id.closeBtn);
        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);
        imagelist = view.findViewById(R.id.imageList);
        dateTxt = view.findViewById(R.id.dateTxt);

        context = this.getContext();
        imageUris = new ArrayList<>();
        diaryDBHandler = new DiaryDBHandler(context);

        //function for image selection
        registerResult();
        //Button click operations
        galleryBtn.setOnClickListener(v -> pickImage());
        closeBtn.setOnClickListener(v -> moveToHomeFragment());

        long currentTimeInMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        selectedDateTime = dateFormat.format(date);
        dateTxt.setText(selectedDateTime);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from EditTexts
                String titleValue = title.getText().toString();
                String contentValue = content.getText().toString();
                selectedDateTime = dateTxt.getText().toString();
                DiaryEntry diaryentry = new DiaryEntry(titleValue,contentValue,selectedDateTime,imageUris);
                try{
                    diaryDBHandler.addDiaryEntry(diaryentry);
                    Toast.makeText(getActivity(), "Note saved successfully!!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Error"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, 0);
                }
                openCamera();

            }
        });

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialog();
            }
        });

        return view;
    }

    private void showDateTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        showTimePickerDialog(selectedDate);
                    }
                },
                year, month, day);

        // Show the Date Picker Dialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final String selectedDate) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        selectedDateTime = selectedDate + " " + selectedTime;

                        Toast.makeText(getActivity(), "Selected Date and Time: " + selectedDateTime, Toast.LENGTH_LONG).show();
                        dateTxt.setText(selectedDateTime);
                    }
                },
                hour, minute, true);

        // Show the Time Picker Dialog
        timePickerDialog.show();
    }


    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            imageUris.add(imageUri);
                            ImageAdapter adapter = new ImageAdapter(context,R.layout.single_image,imageUris);
                            imagelist.setAdapter((adapter));
                        }catch (Exception e){
                            Toast.makeText(context,"No image selected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void moveToHomeFragment() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainfragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Permissions should be checked before calling this method
                return;
            }
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    camera.close();
                    cameraDevice = null;
                    Toast.makeText(getActivity(), "Camera error: " + error, Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to access camera.", Toast.LENGTH_LONG).show();
        }
    }


    private void createCameraPreviewSession() {
        if (cameraDevice == null || !textureView.isAvailable() || imageDimension == null) {
            return;
        }
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return; // Camera was closed
                    }
                    captureSession = session;
                    try {
                        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        session.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(getActivity(), "Failed to configure camera.", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



}
