package com.example.frontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

public class Create extends Fragment {

    final private static String URL_SEARCH = Constants.URL_NGROK + "/create";
    static final int REQUEST_IMAGE_CAPTURE = 0;

    private AppCompatImageView mCreatePreviewImage;
    private TextView mCreateName;
    private TextView mCreateBirthday;
    private TextView mCreateCpf;

    private Bitmap mPreviewImageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.create, container, false);
        final View buttonCreate = view.findViewById(R.id.button_create_customer);
        final View buttonTakePicture = view.findViewById(R.id.create_button_take_picture);
        mCreatePreviewImage = view.findViewById(R.id.create_preview_image);
        mCreateName = view.findViewById(R.id.search_name);
        mCreateBirthday = view.findViewById(R.id.search_age);
        mCreateCpf = view.findViewById(R.id.search_age);

        buttonCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createCustomer(v);
                    }
                }
        );

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(v);
            }
        });

        return view;
    }

    private void createCustomer(View v) {
        // TODO falta a parte para chamar a função no ConnectServerUtils (está crashando atualmente)
        Customer customer = new Customer();
        customer.setImage(mPreviewImageBitmap);
        customer.setName(mCreateName.getText().toString());
        customer.setBirthday(mCreateBirthday.getText().toString());
        customer.setCpf(mCreateCpf.getText().toString());
        Log.d("TAG", "createCustomer: implement call to Server");
    }

    private void takePicture(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                mPreviewImageBitmap = (Bitmap) extras.get("data");
                mCreatePreviewImage.setImageBitmap(mPreviewImageBitmap);
            }
        }
    }
}