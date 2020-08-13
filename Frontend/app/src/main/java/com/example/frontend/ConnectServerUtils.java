package com.example.frontend;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectServerUtils {

    private final static String TAG = "ConnectServerUtils";

    static void getRequest(final Activity activity, final String URL) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .get()
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "postRequest: onFailure");
                        Toast.makeText(activity, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            try {
                                Log.d(TAG, "postRequest: onResponse");
                                // TODO não consegue receber imagem em base 64 com essa função
                                String responseString = response.body().string();
                                Search.updateCustomer(convertJsonToCustomer(responseString));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "postRequest: onResponse: response is failed");
                        }
                    }
                });
            }
        });
    }

    private static Customer convertJsonToCustomer(final String jsonString) {
        final List<Customer> customers = new ArrayList<Customer>();
        JSONObject customerJson;

        try {
            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++) {
                customerJson = new JSONObject(json.getString(i));

                Customer customer = new Customer();
                customer.setName(customerJson.getString("name"));
                customer.setBirthday(customerJson.getString("age"));

                // TODO terminar de criar para os dados sugeridos
//                byte[] decodedString = Base64.decode(customerJson.getString("image"), Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                customer.setImage(decodedByte);

                customers.add(customer);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error in convertJsonToCustomer: ", e);
        }

        return customers.get(0);
    }
}
