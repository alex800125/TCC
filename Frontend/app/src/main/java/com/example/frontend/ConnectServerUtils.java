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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectServerUtils {

    private final static String TAG = "ConnectServerUtils";

    static void getRequestSearch(final Activity activity, final String URL) {
        Utils.startLoadingScreen(activity);
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
                        Log.d(TAG, "getRequest: onFailure");
                        Toast.makeText(activity, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                        Utils.removeLoadingScreen();
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
                                Log.d(TAG, "getRequest: isSuccessful");
                                // TODO não consegue receber imagem em base 64 com essa função
                                String responseString = response.body().string();
                                Search.updateCustomer(convertJsonToCustomer(responseString, true));
                                Utils.removeLoadingScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "getRequest: onResponse: response is failed");
                        }
                    }
                });
            }
        });
    }

    static void postRequestCreate(final Activity activity, final String URL, final Customer customer, final boolean isCreate) {
        Utils.startLoadingScreen(activity);
        JSONObject customerJson = convertCustomerToJson(customer);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(customerJson));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(body)
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
                        Utils.removeLoadingScreen();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "postRequest: isSuccessful");
                            try {
                                String responseString = response.body().string();
                                Toast.makeText(activity, "Status = " + convertJsonToString(responseString), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (isCreate) {
                                Create.UpdateLabels();
                            }
                            Utils.removeLoadingScreen();
                        } else {
                            Log.d(TAG, "postRequest: onResponse: response is failed");
                        }
                    }
                });
            }
        });
    }

    static void postRequestEditCheckCpf(final Activity activity, final String URL, final String cpf) {
        Utils.startLoadingScreen(activity);
        JSONObject json = convertCpfToJson(cpf);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(body)
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
                        Utils.removeLoadingScreen();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "postRequest: isSuccessful");
                            try {
                                String responseString = response.body().string();
                                Edit.updateCustomer(convertJsonToCustomer(responseString, false));
                                Utils.removeLoadingScreen();
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

    private static JSONObject convertCustomerToJson(Customer customer) {
        JSONObject customerJson = new JSONObject();
        try {
            customerJson.put("image", Utils.convert(customer.getImage()));
            customerJson.put("name", customer.getName());
            customerJson.put("cpf", customer.getCpf());
            customerJson.put("birthday", customer.getBirthday());

        } catch (JSONException e) {
            Log.e(TAG, "Error in createJson: ", e);
        }
        return customerJson;
    }

    private static Customer convertJsonToCustomer(final String jsonString, final boolean isSearch) {
        Customer customer = new Customer();
        JSONObject customerJson;
        JSONArray customerJsonLastPurchaseItems;
        ArrayList<String> lastPurchaseList = new ArrayList<>();

        try {
            JSONArray json = new JSONArray(jsonString);
            customerJson = new JSONObject(json.getString(0));

            if (isSearch) {
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
            } else {
                customer.setName(customerJson.getString("name"));
                customer.setCpf(customerJson.getString("cpf"));
                customer.setBirthday(customerJson.getString("birthday"));
            }

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

    private static JSONObject convertCpfToJson(String cpf) {
        JSONObject customerJson = new JSONObject();
        try {
            customerJson.put("cpf", cpf);
        } catch (JSONException e) {
            Log.e(TAG, "Error in createJson: ", e);
        }
        return customerJson;
    }

    private static String convertJsonToString(final String jsonString) {
        String string = "";
        JSONObject customerJson;

        try {
            JSONArray json = new JSONArray(jsonString);
            customerJson = new JSONObject(json.getString(0));

            string = customerJson.getString("status");
        } catch (JSONException e) {
            Log.e(TAG, "Error in convertJsonToCustomer: ", e);
        }

        return string;
    }
}
