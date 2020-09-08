package com.example.frontend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Edit extends Fragment {

    final private static String URL_EDIT = Constants.URL_NGROK + "/edit";
    final private static String URL_EDIT_CHECK = Constants.URL_NGROK + "/edit_check";
    static final int REQUEST_IMAGE_CAPTURE = 0;

    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    private static AlertDialog.Builder mAlertDialog;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mEditBirthday;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mEditName;
    @SuppressLint("StaticFieldLeak")
    private static TextInputLayout mEditCpf;
    private static AppCompatImageView mEditPreviewImage;
    private static Bitmap mPreviewImageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit, container, false);
        final View buttonEdit = view.findViewById(R.id.edit_button_customer);
        mEditName = view.findViewById(R.id.edit_name);
        mEditBirthday = view.findViewById(R.id.edit_birthday);
        mEditCpf = view.findViewById(R.id.edit_cpf);
        mEditPreviewImage = view.findViewById(R.id.edit_preview_image);
        final View buttonTakePicture = view.findViewById(R.id.edit_button_take_picture);
        mActivity = getActivity();

        mEditBirthday.setEnabled(false);
        mEditCpf.setEnabled(false);

        buttonEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editCustomer();
                    }
                }
        );

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        showCheckCpf();

        return view;
    }

    public void editCustomer() {
        Customer customer = new Customer();
        boolean error = false;

        if (mEditPreviewImage != null) {
            customer.setImage(mPreviewImageBitmap);
            mEditPreviewImage.setBackground(null);
        } else {
            error = true;
            mEditPreviewImage.setBackground(getResources().getDrawable(R.drawable.border_error_bg));
        }

        String name = Objects.requireNonNull(mEditName.getEditText()).getText().toString().trim();
        if (!name.isEmpty()) {
            mEditName.setError(null);
            customer.setName(name);
        } else {
            error = true;
            mEditName.setError("You need to enter a name");
        }

        String cpf = Objects.requireNonNull(mEditCpf.getEditText()).getText().toString().trim();
        customer.setCpf(cpf);

        // Show a toast with a error
        if (!error) {
            ConnectServerUtils.postRequestCreate(mActivity, URL_EDIT, customer, false);
        } else {
            Toast.makeText(mActivity, "Check all fields and make sure they are not null", Toast.LENGTH_LONG).show();
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                mPreviewImageBitmap = (Bitmap) extras.get("data");
                mEditPreviewImage.setImageBitmap(mPreviewImageBitmap);
            }
        }
    }

    private void showCheckCpf() {
        mAlertDialog = new AlertDialog.Builder(mActivity);
        mAlertDialog.setTitle("Edit");
        mAlertDialog.setMessage("Enter with the CPF to check if exist");

        final EditText input = new EditText(mActivity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(11);
        input.setFilters(filterArray);

        mAlertDialog.setView(input);

        mAlertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String cpf = input.getText().toString();
                        ConnectServerUtils.postRequestEditCheckCpf(mActivity, URL_EDIT_CHECK, cpf);
                        Toast.makeText(mActivity, "Wait for the information to load", Toast.LENGTH_SHORT).show();
                    }
                });

        mAlertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        mAlertDialog.show();
    }

    public static void updateCustomer(Customer customer) {
        TextInputEditText cpf = mActivity.findViewById(R.id.edit_cpf_text);
        String fieldValue = customer.getCpf();
        mEditCpf.setHintAnimationEnabled(fieldValue == null);
        cpf.setText(fieldValue);

        TextInputEditText name = mActivity.findViewById(R.id.edit_name_text);
        fieldValue = customer.getName();
        mEditName.setHintAnimationEnabled(fieldValue == null);
        name.setText(fieldValue);

        TextInputEditText birthday = mActivity.findViewById(R.id.edit_birthday_text);
        fieldValue = customer.getBirthday();
        mEditBirthday.setHintAnimationEnabled(fieldValue == null);
        birthday.setText(fieldValue);

        mAlertDialog.setCancelable(true);
    }
}