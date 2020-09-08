package com.example.frontend;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;

public class Search extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;

//    private static AppCompatImageView mSearchPreviewImage;
    @SuppressLint("StaticFieldLeak")
    private static TextView mSearchName;
    @SuppressLint("StaticFieldLeak")
    private static TextView mSearchAge;
    @SuppressLint("StaticFieldLeak")
    private static TextView mLastPurchaseValue;
    @SuppressLint("StaticFieldLeak")
    private static TextView mLastPurchaseDate;
    @SuppressLint("StaticFieldLeak")
    private static ListView mListViewLastPurchase;
    @SuppressLint("StaticFieldLeak")
    private static ListView mListViewSuggestion;

    // for this link to work, ngrok must be active and the link must be updated
    final private static String URL_SEARCH = Constants.URL_NGROK + "/search";

    private final static String TAG = "Search";

    String[] emptyArray = {""};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        mActivity = getActivity();

        View mButtonTakePicture = view.findViewById(R.id.search_button);
        // TODO carregar imagem vinda do Servidor
        // mSearchPreviewImage = mView.findViewById(R.id.search_preview_image);
        mSearchName = view.findViewById(R.id.search_name);
        mSearchAge = view.findViewById(R.id.search_age);
        mLastPurchaseDate = view.findViewById(R.id.search_last_buy_date);
        mLastPurchaseValue = view.findViewById(R.id.search_last_buy_value);
        mListViewLastPurchase = view.findViewById(R.id.search_last_buy_list);
        mListViewSuggestion = view.findViewById(R.id.search_suggestion_list);

        ArrayAdapter adapterLastPurchase = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, emptyArray);
        mListViewLastPurchase.setAdapter(adapterLastPurchase);

        ArrayAdapter adapterSuggestion = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, emptyArray);
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
        ConnectServerUtils.getRequestSearch(mActivity, URL_SEARCH);
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