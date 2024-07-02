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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Map;


//import androidx.core.widget.;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ExchangeRateService exchangeRateService;
    private Spinner spinner1, spinner2;
    private EditText ed1, ed2;
    private TextView t1,t2;
    private final String[] currencies = {"INR", "USD", "JPY", "RUB","GBP","AUD","CNY"
    };
    private boolean updating = false;


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
                    double convertedCurrency = convertCurrency(amt, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString());

                    ed2.setText(String.valueOf(convertedCurrency));
                    String message1 = String.format("%.3f %s equals ",amt,spinner1.getSelectedItem().toString());
                    String message2 = String.format(" %.3f %s",convertedCurrency,spinner2.getSelectedItem().toString() );
                    t1.setText(message1);
                    t2.setText(message2);
                    updating = false;
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
                    double convertedCurrency = convertCurrency(amt, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString());
                    ed1.setText(String.valueOf(convertedCurrency));
                    String message1 = String.format("%.3f %s equals ", amt,spinner2.getSelectedItem().toString() );
                    String message2 = String.format("%.3f %s",convertedCurrency,spinner1.getSelectedItem().toString());
                    t1.setText(message1);
                    t2.setText(message2);
                    updating = false;

                }
            }

        });

        if (savedInstanceState != null) {
            Log.d("1", "onCreate: Restoring state from savedInstanceState");
            ed1.setText(savedInstanceState.getString("editTextFrom"));
            ed2.setText(savedInstanceState.getString("editTextTO"));
            spinner1.setSelection(savedInstanceState.getInt("spinnerFrom"));
            spinner2.setSelection(savedInstanceState.getInt("spinnerTO"));
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        int sp1 = R.id.spinnerFrom;
//        int sp2=R.id.spinnerTO;
        ed1.setText("0");
        ed2.setText("0");
        if(!updating) {
            updating = true;
            if (parent.getId() == R.id.spinnerFrom) {

                double amt1 = ed1.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed1.getText().toString());
                double convertedCurrency1 = convertCurrency(amt1, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                ed2.setText(String.valueOf(convertedCurrency1));
            } else if (parent.getId() == R.id.spinnerTO) {
                double amt2 = ed2.getText().toString().isEmpty() ? 0 : Double.parseDouble(ed2.getText().toString());
                double convertedCurrency2 = convertCurrency(amt2, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString());
                ed1.setText(String.valueOf(convertedCurrency2));
            }
            updating=false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private double convertCurrency(double amt, String firstCurrency, String secondCurrency) {
        double indianRupee = convertOtherToIndianCurrency(amt, firstCurrency);
        return convertIndianToOtherCurrency(indianRupee, secondCurrency);
    }

    private double convertIndianToOtherCurrency(double indianRupee, String secondCurrency) {
        switch (secondCurrency) {
            case "INR":
//                double e1 = fetchExchangeRate("INR","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee;
            case "USD":
//                double e2 = fetchExchangeRate("INR","USD","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 0.012;
            case "JPY":
                //double e3 = fetchExchangeRate("INR","JPY","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 1.9;
            case "RUB":
                //double e4 = fetchExchangeRate("INR","RUB","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 1.03;
            case "GBP":
                //double e5 = fetchExchangeRate("INR","GBP","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 0.0095;
            case "AUD":
               // double e6 = fetchExchangeRate("INR","AUD","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 0.018;
            case "CNY":
                //double e7 = fetchExchangeRate("INR","CNY","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return indianRupee * 0.087;
            default:
                return 0;
        }
    }

    private double convertOtherToIndianCurrency(double amt, String firstCurrency) {
        switch (firstCurrency) {
            case "INR":
                return amt;
            case "USD":
                //double e1 = fetchExchangeRate("USD","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 83.53;
            case "JPY":
                 //double e2 = fetchExchangeRate("JPY","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 0.51;
            case "RUB":
                //double e3 = fetchExchangeRate("RUB","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 0.96;
            case "GBP":
                //double e4 = fetchExchangeRate("GBP","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 105.52;
            case "AUD":
                //double e5 = fetchExchangeRate("AUD","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 55.47;
            case "CNY":
                //double e6 = fetchExchangeRate("CNY","INR","ISIOprPxFN3sBEH90rEqmuuE6n8QDsNq");
                return amt * 11.4;
            default:
                return 0;
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editTextFrom", ed1.getText().toString());
        outState.putString("editTextTO", ed2.getText().toString());
        outState.putInt("spinnerFrom", spinner1.getSelectedItemPosition());
        outState.putInt("spinnerTO", spinner2.getSelectedItemPosition());
        Log.d("2", "onSaveInstanceState: Saving instance state");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ed1.setText(savedInstanceState.getString("editTextFrom"));
        ed2.setText(savedInstanceState.getString("editTextTO"));
        spinner1.setSelection(savedInstanceState.getInt("spinnerFrom"));
        spinner2.setSelection(savedInstanceState.getInt("spinnerTO"));
        Log.d("3", "onRestoreInstanceState: Restoring instance state");
    }

//    private double fetchExchangeRate(String baseCurrency, String targetCurrency, String apiKey) {
//        Call<ExchangeRateResponse> call = exchangeRateService.getLatestRates(baseCurrency, targetCurrency, apiKey);
//        try {
//            Response<ExchangeRateResponse> response = call.execute();
//            if (response.isSuccessful() && response.body() != null) {
//                ExchangeRateResponse rateResponse = response.body();
//                Map<String, Double> rates = rateResponse.getRates();
//                if (rates.containsKey(targetCurrency)) {
//                    return rates.get(targetCurrency);
//                }
//            } else {
//                Log.e("ExchangeRate", "Failed to fetch exchange rates: " + response.message());
//            }
//        } catch (IOException e) {
//            Log.e("ExchangeRate", "Error fetching exchange rates: " + e.getMessage());
//        }
//        return 0; // Return default value or handle error as needed
//    }












}


