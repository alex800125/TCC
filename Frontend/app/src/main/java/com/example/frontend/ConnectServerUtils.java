package com.example.frontend;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

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

                            Log.d(TAG, "getRequest: isSuccessful");
                            // TODO não consegue receber imagem em base 64 com essa função
                            Gson gson = new Gson();
                            // erro android.os.NetworkOnMainThreadException ----> sugestao é tentar rodar fora da thread UI, testar amanha

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        StringReader reader = new StringReader(response.body().string());
                                        Search.updateCustomer(convertJsonToCustomer(reader));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
                                if (convertJsonToString(responseString).equals("true")) {
                                    if (isCreate) {
                                        Create.UpdateLabels();
                                        Toast.makeText(activity, "Customer created with successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, "Customer updated with successful", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (isCreate) {
                                        Toast.makeText(activity, "There was an error creating, please check the information again", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(activity, "There was an error updating, please check the information again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
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

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        StringReader reader = new StringReader(response.body().string());
                                        Edit.updateCustomer(convertJsonToCustomer(reader));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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
            customerJson.put("image", Utils.convert(customer.getImageBitmap()));
            customerJson.put("name", customer.getName());
            customerJson.put("cpf", customer.getCpf());
            customerJson.put("birthday", customer.getBirthday());

        } catch (JSONException e) {
            Log.e(TAG, "Error in createJson: ", e);
        }
        return customerJson;
    }

    private static Customer convertJsonToCustomer(final StringReader jsonStringReader) {
        Gson gson = new Gson();
        // gson convert the jsonStringReader in a customer item (the vector is necessary because this return a item's list)
        Customer[] customer = gson.fromJson(jsonStringReader, Customer[].class);

        try {
            // this is to remove the part of base64 string = b'...'
            customer[0].getImageBase64().setCharAt(0, ' ');
            customer[0].getImageBase64().setCharAt(1, ' ');
            customer[0].getImageBase64().setCharAt(customer[0].getImageBase64().length() - 1, ' ');

            byte[] stringBase64 = customer[0].getImageBase64().toString().getBytes("UTF-8");
            byte[] decodedString = Base64.decode(stringBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            customer[0].setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO terminar de criar para os dados sugeridos

        return customer[0];
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
        // TODO uma possivel melhora será carregar o motivo do status ter vindo como falso
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
