package com.example.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Search extends Fragment {
    private View mView;
    private static Activity mActivity;

    private static AppCompatImageView mSearchPreviewImage;
    private static TextView mSearchName;
    private static TextView mSearchAge;
    private static TextView mLastPurchaseValue;
    private static TextView mLastPurchaseDate;
    private static ListView mListViewLastPurchase;
    private static ListView mListViewSuggestion;

    private View mButtonTakePicture;

    // for this link to work, ngrok must be active and the link must be updated
    final private static String URL_SEARCH = Constants.URL_NGROK + "search";

    private final static String TAG = "Search";

    String[] emptyArray = {""};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        this.mView = view;
        mActivity = getActivity();

        mButtonTakePicture = mView.findViewById(R.id.search_button);
        mSearchPreviewImage = mView.findViewById(R.id.search_preview_image);
        mSearchName = mView.findViewById(R.id.search_name);
        mSearchAge = mView.findViewById(R.id.search_age);
        mLastPurchaseDate = mView.findViewById(R.id.search_last_buy_date);
        mLastPurchaseValue = mView.findViewById(R.id.search_last_buy_value);
        mListViewLastPurchase = mView.findViewById(R.id.search_last_buy_list);
        mListViewSuggestion = mView.findViewById(R.id.search_suggestion_list);

        ArrayAdapter adapterLastPurchase = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, emptyArray);
        mListViewLastPurchase.setAdapter(adapterLastPurchase);

        ArrayAdapter adapterSuggestion = new ArrayAdapter<String>(mActivity, R.layout.search_list_last_purchase, emptyArray);
        mListViewSuggestion.setAdapter(adapterSuggestion);

        mButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer(v);
            }
        });

        return view;
    }

    private void searchCustomer(View v) {
        Log.d(TAG, "searchCustomer: call to Server");
        ConnectServerUtils.getRequest(mActivity, URL_SEARCH);
    }

    public static void updateCustomer(Customer customer) {
        // mSearchPreviewImage.setImageBitmap(customer.getImage());
        mSearchName.setText(customer.getName());
        mSearchAge.setText(customer.getBirthday() + " anos");
        mLastPurchaseDate.setText(customer.getLastPurchaseDate());
        mLastPurchaseValue.setText("R$ " + customer.getLastPurchaseValue().replace(".",","));
        ArrayAdapter adapterLastPurchase = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, customer.getLastPurchaseList());
        mListViewLastPurchase.setAdapter(adapterLastPurchase);

        // TODO falta os itens sugeridos
    }

//        String postBodyText = "Hello";
//        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
//        RequestBody postBody = RequestBody.create(mediaType, postBodyText);
//
//    private RequestBody buildRequestBody(String msg) {
//        postBodyString = msg;
//        mediaType = MediaType.parse("text/plain");
//        requestBody = RequestBody.create(postBodyString, mediaType);
//        return requestBody;
//    }
}