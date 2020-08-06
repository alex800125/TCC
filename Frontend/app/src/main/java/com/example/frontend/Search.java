package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Search extends Fragment {

    private View mView;

    private TextView mName;
    private TextView mAge;
    private TextView mSuggestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        this.mView = view;

        mName = mView.findViewById(R.id.name);
        mName.setText("Alex");
        mAge = mView.findViewById(R.id.age);
        mAge.setText("23" + " anos");
        mSuggestion = mView.findViewById(R.id.suggestion);
        mSuggestion.setText("Camisa xadrez");

        return view;
    }
}