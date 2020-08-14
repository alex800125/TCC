package com.example.frontend;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

//    TODO descomentar quando estiver finalizado
//    static void postRequest(final Activity activity, final String URL, final Customer customer) {
//        createJson(customer);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request
//                .Builder()
//                .post()
//                .url(URL)
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(final Call call, final IOException e) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "postRequest: onFailure");
//                        Toast.makeText(activity, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        call.cancel();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.isSuccessful()) {
//
//                            try {
//                                Log.d(TAG, "postRequest: onResponse");
//                                String responseString = response.body().string();
//                                Search.updateCustomer(convertJsonToCustomer(responseString));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Log.d(TAG, "postRequest: onResponse: response is failed");
//                        }
//                    }
//                });
//            }
//        });
//    }

    //    TODO json criado porém ainda não foi testado
    private JSONObject convertCustomerToJson(Customer customer) {
        JSONObject customerJson = new JSONObject();
        try {
//            TODO terminar de converter a image para base64
            customerJson.put("image", customer.getImage());
            customerJson.put("name", customer.getName());
            customerJson.put("cpf", customer.getCpf());
            customerJson.put("birthday", customer.getBirthday());

        } catch (JSONException e) {
            Log.e(TAG, "Error in createJson: ", e);
        }
        return customerJson;
    }

    private static Customer convertJsonToCustomer(final String jsonString) {
        Customer customer = new Customer();
        JSONObject customerJson;
        JSONArray customerJsonLastPurchaseItems;
        ArrayList<String> lastPurchaseList = new ArrayList<>();

        try {
            JSONArray json = new JSONArray(jsonString);
            customerJson = new JSONObject(json.getString(0));

            customer.setName(customerJson.getString("name"));
            customer.setBirthday(customerJson.getString("age"));
            customer.setLastPurchaseDate(customerJson.getString("ultima_compra_data"));
            customer.setLastPurchaseValue(customerJson.getString("ultima_compra_valor"));

            customerJsonLastPurchaseItems = new JSONArray(customerJson.getString("itens_comprados"));
            for (int i = 0; i < customerJsonLastPurchaseItems.length(); i++) {
                JSONObject items = new JSONObject(customerJsonLastPurchaseItems.getString(i));
                lastPurchaseList.add(items.getString("item"));
            }

            customer.setLastPurchaseList(lastPurchaseList);

            // TODO terminar de criar para os dados sugeridos
            // testes:
            // byte[] decodedString = Base64.decode(customerJson.getString("image"), Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            // customer.setImage(decodedByte);
        } catch (JSONException e) {
            Log.e(TAG, "Error in convertJsonToCustomer: ", e);
        }

        return customer;
    }
}
