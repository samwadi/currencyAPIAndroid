package com.example.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExchangeRateActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCurrencies;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_rate_activity);

        recyclerViewCurrencies = findViewById(R.id.recyclerViewCurrencies);
        initRecyclerView();

        new FetchExchangeRateTask().execute();
    }

    private void initRecyclerView() {
        currencyAdapter = new CurrencyAdapter();
        recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCurrencies.setAdapter(currencyAdapter);
    }

    private class FetchExchangeRateTask extends AsyncTask<Void, Void, List<Currency>> {

        @Override
        protected List<Currency> doInBackground(Void... voids) {
            String apiUrl = "https://v6.exchangerate-api.com/v6/cded56fb5aef1507961f27e6/latest/USD";

            try {
                URL url = new URL(apiUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        InputStream inputStream = urlConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        return parseExchangeRateResponse(stringBuilder.toString());
                    } else {
                        //handle exceptions? or errors
                    }

                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            super.onPostExecute(currencies);

            if (currencies != null) {
                Log.d("ExchangeRateActivity", "Number of currencies: " + currencies.size());
                currencyAdapter.setCurrencies(currencies);
            } else {
                Log.e("ExchangeRateActivity", "Failed to fetch currencies");
            }
        }

        private void processExchangeRateResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);

                JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
                List<Currency> currencies = new ArrayList<>();

                Iterator<String> keys = conversionRates.keys();
                while (keys.hasNext()) {
                    String currencyCode = keys.next();
                    double exchangeRate = conversionRates.getDouble(currencyCode);
                    currencies.add(new Currency(currencyCode, exchangeRate));
                }

                currencyAdapter.setCurrencies(currencies);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        private List<Currency> parseExchangeRateResponse(String jsonResponse) {
            List<Currency> currencies = new ArrayList<>();

            try {
                JSONObject jsonResponseObj = new JSONObject(jsonResponse);

                JSONObject conversionRates = jsonResponseObj.getJSONObject("conversion_rates");
                Iterator<String> keys = conversionRates.keys();

                while (keys.hasNext()) {
                    String currencyCode = keys.next();
                    double exchangeRate = conversionRates.getDouble(currencyCode);
                    currencies.add(new Currency(currencyCode, exchangeRate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return currencies;
        }

    }
}
