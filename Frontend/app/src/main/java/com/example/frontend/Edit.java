package com.example.frontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class Edit extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 0;

    private AppCompatImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit, container, false);
        final View buttonEdit = view.findViewById(R.id.edit_button_customer);
        final TextInputLayout textViewName = view.findViewById(R.id.edit_name);
        final TextInputLayout textViewBirthday = view.findViewById(R.id.edit_birthday);
        final TextInputLayout textViewCpf = view.findViewById(R.id.edit_cpf);
        final View buttonTakePicture = view.findViewById(R.id.edit_button_take_picture);
        image = view.findViewById(R.id.edit_preview_image);

        textViewBirthday.setEnabled(false);
        textViewCpf.setEnabled(false);

        buttonEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editCustomer(v);
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

    public void editCustomer(View v) {
        // TODO implementar chamada para o servidor em Python
        Log.d("TAG", "editCustomer: implement call to Server");
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
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(imageBitmap);
            }
        }
    }
}