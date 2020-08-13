package com.example.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Search extends Fragment {

    private final static String TAG = "Search";
    private static AppCompatImageView mSearchPreviewImage;
    private static TextView mSearchName;
    private static TextView mSearchAge;

    private View mView;

    private TextView mSearchSuggestion;
    private View mButtonTakePicture;

    // for this link to work, ngrok must be active and the link must be updated
    final private static String URL_SEARCH = "http://1aa3ddaa2efc.ngrok.io/search";

    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        mButtonTakePicture = view.findViewById(R.id.search_button);
        mSearchPreviewImage = view.findViewById(R.id.search_preview_image);
        this.mView = view;
        mActivity = getActivity();

        mSearchName = mView.findViewById(R.id.search_name);
        mSearchAge = mView.findViewById(R.id.search_age);
        mSearchSuggestion = mView.findViewById(R.id.search_suggestion);

        mButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer(v);
            }
        });

        return view;
    }

    private void searchCustomer(View v) {
        // TODO implementar chamada para o servidor em python

        Log.d(TAG, "searchCustomer: implement call to Server");

        ConnectServerUtils.getRequest(mActivity, URL_SEARCH);

//        mSearchName.setText("Alex");
//        mSearchAge.setText("23" + " anos");
//        mSearchSuggestion.setText("Camisa xadrez");
//        image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.person, null));
    }

    public static void updateCustomer(Customer customer) {
        Log.d(TAG, "updateCustomer customer: " + customer);
//        mSearchPreviewImage.setImageBitmap(customer.getImage());
        mSearchName.setText(customer.getName());
        mSearchAge.setText(customer.getBirthday() + " anos");
    }

    void connectServer(View v) {
//        String postBodyText = "Hello";
//        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
//        RequestBody postBody = RequestBody.create(mediaType, postBodyText);


    }
//
//    private RequestBody buildRequestBody(String msg) {
//        postBodyString = msg;
//        mediaType = MediaType.parse("text/plain");
//        requestBody = RequestBody.create(postBodyString, mediaType);
//        return requestBody;
//    }


}