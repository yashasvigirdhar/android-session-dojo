package com.yashasvi.android.session.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.yashasvi.android.session.network.ServerHelperFunctions;

public class CitiesDownloadService extends IntentService {

    String LOG_TAG = "CitiesDownloadService";

    public static final String RECEIVER_FILTER = "com.yashasvi.android.session.cities.receiver";

    public static String CITIES_KEY = "cities";
    public static String CITIES_URL = "url";


    public CitiesDownloadService() {
        super("CitiesDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(CITIES_URL);
        Log.d(LOG_TAG, url);
        String resultString = ServerHelperFunctions.getJSON(url);
        publishResults(resultString);
    }

    private void publishResults(String result) {
        Intent intent = new Intent(RECEIVER_FILTER);
        intent.putExtra(CITIES_KEY, result);
        sendBroadcast(intent);
    }
}