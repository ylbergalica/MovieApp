package com.york.moviesapp.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class APIRequest {

    public interface ApiCallback {
        void onSuccess(JsonElement data);
        void onFailure(String errorMessage);
    }

    public static void sendRequest(final String api, final ApiCallback callback) {
        new AsyncTask<Void, Void, JsonElement>() {
            @Override
            protected JsonElement doInBackground(Void... voids) {
                return performRequest(api);
            }

            @Override
            protected void onPostExecute(JsonElement result) {
                if (result != null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure("Failed to fetch data from " + api);
                }
            }
        }.execute();
    }

    private static JsonElement performRequest(String api) {
        Log.d("APIRequest", "Sending request to " + api);
        JsonElement data = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(api)
                .addHeader("Content-Type", "application/json")
                .addHeader("Host", "app-vpigadas.herokuapp.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();

            if (response.isSuccessful() && responseBody != null) {
                String dataStr = responseBody.string();
                Log.d("APIRequest", "Response from " + api + ": " + dataStr);
                Gson gson = new Gson();
                data = gson.fromJson(dataStr, JsonElement.class);
            } else {
                Log.e("APIRequest", "Error: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            Log.e("APIRequest", "Exception occurred while sending request: " + e.getMessage());
        }

        return data;
    }

    public static void getDemoMovies(ApiCallback callback) {
        String stocksAPI = "https://app-vpigadas.herokuapp.com/api/movies/demo/";
        sendRequest(stocksAPI, callback);
    }

    public static void getMovies(ApiCallback callback) {
        String demoStocksAPI = "https://app-vpigadas.herokuapp.com/api/movies/";
        sendRequest(demoStocksAPI, callback);
    }

    public static void getMovie(String id, ApiCallback callback) {
        String stockDetailsAPI = "https://app-vpigadas.herokuapp.com/api/movies/" + id;
        sendRequest(stockDetailsAPI, callback);
    }

    public static void getDemoMovie(String id, ApiCallback callback) {
        String stockDetailsAPI = "https://app-vpigadas.herokuapp.com/api/movies/demo/" + id;
        sendRequest(stockDetailsAPI, callback);
    }
}