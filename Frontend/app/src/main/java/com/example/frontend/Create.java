package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Create extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.create, container, false);
        final View button = view.findViewById(R.id.button_create_customer);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createCustomer(v);
                    }
                }
        );
        return view;
    }

    public void createCustomer(View v){
        // TODO implementar chamada para o servidor em Python
        Log.d("TAG", "createCustomer: implement call to Server");
    }
}