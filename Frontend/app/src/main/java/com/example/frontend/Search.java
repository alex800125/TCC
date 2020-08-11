package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

public class Search extends Fragment {

    private View mView;

    private TextView mSearchName;
    private TextView mSearchAge;
    private TextView mSearchSuggestion;
    private AppCompatImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        final View buttonTakePicture = view.findViewById(R.id.search_button);
        image = view.findViewById(R.id.search_preview_image);
        this.mView = view;

        mSearchName = mView.findViewById(R.id.search_name);
        mSearchAge = mView.findViewById(R.id.search_age);
        mSearchSuggestion = mView.findViewById(R.id.search_suggestion);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer(v);
            }
        });

        return view;
    }

    private void searchCustomer(View v) {
        // TODO implementar chamada para o servidor em python

        Log.d("TAG", "searchCustomer: implement call to Server");

        mSearchName.setText("Alex");
        mSearchAge.setText("23" + " anos");
        mSearchSuggestion.setText("Camisa xadrez");
        image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.person, null));
    }
}