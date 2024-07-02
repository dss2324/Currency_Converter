package com.example.currency_converter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ExchangeRateService exchangeRateService;
    private Spinner spinner1, spinner2;
    private EditText ed1, ed2;
    private TextView t1, t2;
    private final String[] currencies = {"INR", "USD", "JPY", "RUB", "GBP", "AUD", "CNY"};
    private boolean updating = false;
    private static final String API_KEY = "c06368765af9d0d6753733b5"; // Replace with your actual API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exchangeRateService = RetrofitClient.getClient().create(ExchangeRateService.class);
        spinner1 = findViewById(R.id.spinnerFrom);
        spinner2 = findViewById(R.id.spinnerTO);
        ed1 = findViewById(R.id.editTextFrom);
        ed2 = findViewById(R.id.editTextTO);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (ed1.isFocused() && !updating) {
                    updating = true;
                    double amt = ed1.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed1.getText().toString());
                    convertCurrency(amt, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), true);
                }
            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (ed2.isFocused() && !updating) {
                    updating = true;
                    double amt = ed2.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed2.getText().toString());
                    convertCurrency(amt, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString(), false);
                }
            }
        });

        if (savedInstanceState != null) {
            Log.d("MainActivity", "onCreate: Restoring state from savedInstanceState");
            ed1.setText(savedInstanceState.getString("editTextFrom"));
            ed2.setText(savedInstanceState.getString("editTextTO"));
            spinner1.setSelection(savedInstanceState.getInt("spinnerFrom"));
            spinner2.setSelection(savedInstanceState.getInt("spinnerTO"));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ed1.setText("0");
        ed2.setText("0");
        if (!updating) {
            updating = true;
            if (parent.getId() == R.id.spinnerFrom) {
                double amt1 = ed1.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed1.getText().toString());
                convertCurrency(amt1, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), true);
            } else if (parent.getId() == R.id.spinnerTO) {
                double amt2 = ed2.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed2.getText().toString());
                convertCurrency(amt2, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString(), false);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void convertCurrency(double amt, String firstCurrency, String secondCurrency, boolean updateEd2) {
        fetchExchangeRate(firstCurrency, secondCurrency, new ExchangeRateCallback() {
            @Override
            public void onExchangeRateFetched(double rate) {
                double convertedCurrency = amt * rate;
                if (updateEd2) {
                    ed2.setText(String.valueOf(convertedCurrency));
                } else {
                    ed1.setText(String.valueOf(convertedCurrency));
                }
                String message1 = String.format("%.3f %s equals ", amt, firstCurrency);
                String message2 = String.format("%.3f %s", convertedCurrency, secondCurrency);
                t1.setText(message1);
                t2.setText(message2);
                updating = false;
            }

            @Override
            public void onExchangeRateFailed(String errorMessage) {
                Log.e("ExchangeRate", "Failed to fetch exchange rate: " + errorMessage);
                updating = false;
            }
        });
    }

    private void fetchExchangeRate(String baseCurrency, String targetCurrency, ExchangeRateCallback callback) {
        Call<ExchangeRateResponse> call = exchangeRateService.getLatestRates(API_KEY, baseCurrency);
        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExchangeRateResponse rateResponse = response.body();
                    Map<String, Double> rates = rateResponse.getRates();
                    if (rates.containsKey(targetCurrency)) {
                        callback.onExchangeRateFetched(rates.get(targetCurrency));
                    } else {
                        callback.onExchangeRateFailed("Exchange rate not found");
                    }
                } else {
                    callback.onExchangeRateFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                callback.onExchangeRateFailed(t.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editTextFrom", ed1.getText().toString());
        outState.putString("editTextTO", ed2.getText().toString());
        outState.putInt("spinnerFrom", spinner1.getSelectedItemPosition());
        outState.putInt("spinnerTO", spinner2.getSelectedItemPosition());
        Log.d("MainActivity", "onSaveInstanceState: Saving instance state");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ed1.setText(savedInstanceState.getString("editTextFrom"));
        ed2.setText(savedInstanceState.getString("editTextTO"));
        spinner1.setSelection(savedInstanceState.getInt("spinnerFrom"));
        spinner2.setSelection(savedInstanceState.getInt("spinnerTO"));
        Log.d("MainActivity", "onRestoreInstanceState: Restoring instance state");
    }
}
