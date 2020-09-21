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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Search extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;

    //    private static AppCompatImageView mSearchPreviewImage;
    @SuppressLint("StaticFieldLeak")
    private static TextView mSearchName;
    @SuppressLint("StaticFieldLeak")
    private static TextView mSearchAge;
    @SuppressLint("StaticFieldLeak")
    private static TextView mSearchSex;
    @SuppressLint("StaticFieldLeak")
    private static TextView mLastPurchaseValue;
    @SuppressLint("StaticFieldLeak")
    private static TextView mLastPurchaseDate;
    @SuppressLint("StaticFieldLeak")
    private static TextView mLastPurchaseNotExist;
    @SuppressLint("StaticFieldLeak")
    private static ListView mListViewLastPurchase;
    @SuppressLint("StaticFieldLeak")
    private static ListView mListViewSuggestion;
    private static AppCompatImageView mSearchPreviewImage;

    // for this link to work, ngrok must be active and the link must be updated
    final private static String URL_SEARCH = Constants.URL_NGROK + "/search";

    private final static String TAG = "Search";

    final String[] emptyArray = {""};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        mActivity = getActivity();

        View mButtonTakePicture = view.findViewById(R.id.search_button);
        mSearchPreviewImage = view.findViewById(R.id.search_preview_image);
        mSearchName = view.findViewById(R.id.search_name);
        mSearchAge = view.findViewById(R.id.search_age);
        mSearchSex = view.findViewById(R.id.search_sexo);
        mLastPurchaseDate = view.findViewById(R.id.search_last_buy_date);
        mLastPurchaseValue = view.findViewById(R.id.search_last_buy_value);
        mListViewLastPurchase = view.findViewById(R.id.search_last_buy_list);
        mListViewSuggestion = view.findViewById(R.id.search_suggestion_list);
        mLastPurchaseNotExist = view.findViewById(R.id.search_last_buy_not_exist);

        ArrayAdapter adapterLastPurchase = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, emptyArray);
        mListViewLastPurchase.setAdapter(adapterLastPurchase);

        ArrayAdapter adapterSuggestion = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, emptyArray);
        mListViewSuggestion.setAdapter(adapterSuggestion);

        mButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer();
            }
        });

        return view;
    }

    private void searchCustomer() {
        Log.d(TAG, "searchCustomer: call to Server");
        ConnectServerUtils.getRequestSearch(mActivity, URL_SEARCH);
    }

    public static void updateCustomer(final Customer customer) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchPreviewImage.setImageBitmap(customer.getImageBitmap());
                mSearchName.setText(customer.getName());
                mSearchAge.setText(customer.getBirthday() + " anos");

                if (customer.getSex().equals("M")) {
                    mSearchSex.setText("Masculino");
                } else {
                    mSearchSex.setText("Feminino");
                }

                // This is necessary because the Gson uses the class Item to create this array
                ArrayList<ItemSuggestion> ListItemsS = customer.getSuggestions();
                ArrayList<String> ListItemsSuggestion = new ArrayList<>();
                for (ItemSuggestion item : ListItemsS) {
                    ListItemsSuggestion.add(item.getItemSuggestion());
                }

                ArrayAdapter adapterSuggestion = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, ListItemsSuggestion);
                mListViewSuggestion.setAdapter(adapterSuggestion);

                // verify if the customer exist a last buy
                if (customer.getLastPurchaseValue() != null && !customer.getLastPurchaseDate().equals("None")) {
                    mLastPurchaseDate.setVisibility(View.VISIBLE);
                    mLastPurchaseValue.setVisibility(View.VISIBLE);
                    mListViewLastPurchase.setVisibility(View.VISIBLE);
                    mLastPurchaseNotExist.setVisibility(View.INVISIBLE);

                    mLastPurchaseDate.setText(customer.getLastPurchaseDate());
                    mLastPurchaseValue.setText("R$ " + customer.getLastPurchaseValue().replace(".", ","));

                    // This is necessary because the Gson uses the class Item to create this array
                    ArrayList<Item> ListItems = customer.getLastPurchaseList();
                    ArrayList<String> ListItemsString = new ArrayList<>();
                    for (Item item : ListItems) {
                        ListItemsString.add(item.getItem());
                    }

                    ArrayAdapter adapterLastPurchase = new ArrayAdapter<>(mActivity, R.layout.search_list_last_purchase, ListItemsString);
                    mListViewLastPurchase.setAdapter(adapterLastPurchase);
                } else {
                    mLastPurchaseDate.setVisibility(View.INVISIBLE);
                    mLastPurchaseValue.setVisibility(View.INVISIBLE);
                    mListViewLastPurchase.setVisibility(View.INVISIBLE);
                    mLastPurchaseNotExist.setVisibility(View.VISIBLE);
                }

                Utils.removeLoadingScreen();
            }
        });
    }
}