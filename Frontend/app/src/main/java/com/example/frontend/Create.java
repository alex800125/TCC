package com.example.frontend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Create extends Fragment {

    final private static String URL_SEARCH = Constants.URL_NGROK + "/create";
    final static int REQUEST_IMAGE_CAPTURE = 0;

    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mCreateBirthday;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mCreateName;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mCreateCpf;
    private static AppCompatImageView mCreatePreviewImage;
    private static Bitmap mPreviewImageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.create, container, false);
        final View buttonCreate = view.findViewById(R.id.button_create_customer);
        final View buttonTakePicture = view.findViewById(R.id.create_button_take_picture);
        mCreatePreviewImage = view.findViewById(R.id.create_preview_image);
        mCreateName = view.findViewById(R.id.create_name);
        mCreateBirthday = view.findViewById(R.id.create_birthday);
        mCreateCpf = view.findViewById(R.id.create_cpf);
        mActivity = getActivity();

        buttonCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createCustomer();
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

    private void createCustomer() {
        Customer customer = new Customer();
        boolean error = false;

        if (mPreviewImageBitmap != null) {
            customer.setImage(mPreviewImageBitmap);
            mCreatePreviewImage.setBackground(null);
        } else {
            error = true;
            mCreatePreviewImage.setBackground(getResources().getDrawable(R.drawable.border_error_bg));
        }

        String name = Objects.requireNonNull(mCreateName.getEditText()).getText().toString().trim();
        if (!name.isEmpty()) {
            mCreateName.setError(null);
            customer.setName(name);
        } else {
            error = true;
            mCreateName.setError("You need to enter a name");
        }

        String birthday = Objects.requireNonNull(mCreateBirthday.getEditText()).getText().toString().trim();
        if (!birthday.isEmpty()) {
            mCreateBirthday.setError(null);
            customer.setBirthday(birthday);
        } else {
            error = true;
            mCreateBirthday.setError("You need to enter a Birthday");
        }

        String cpf = Objects.requireNonNull(mCreateCpf.getEditText()).getText().toString().trim();
        if (!cpf.isEmpty()) {
            mCreateCpf.setError(null);
            customer.setCpf(cpf);
        } else {
            error = true;
            mCreateCpf.setError("You need to enter a CPF");
        }

        // Show a toast with a error
        if (!error) {
            ConnectServerUtils.postRequestCreate(mActivity, URL_SEARCH, customer, true);
        } else {
            Toast.makeText(mActivity, "Check all fields and make sure they are not null", Toast.LENGTH_LONG).show();
        }

        Log.d("TAG", "createCustomer: implement call to Server");
    }

    public static void UpdateLabels() {
        mCreateName.setError(null);
        mCreateBirthday.setError(null);
        mCreateCpf.setError(null);
        mCreatePreviewImage.setBackground(null);
        mCreatePreviewImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.person));
        mPreviewImageBitmap = null;
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