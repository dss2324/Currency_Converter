package com.example.currency_converter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.widget.;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1, spinner2;
    private EditText ed1, ed2;

    private final String[] currencies = {"INR", "USD", "JPY", "RUB","GBP","AUD","CNY"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = findViewById(R.id.spinnerFrom);
        spinner2 = findViewById(R.id.spinnerTO);
        ed1 = findViewById(R.id.editTextFrom);
        ed2 = findViewById(R.id.editTextTO);

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
                if (ed1.isFocused()) {
                    double amt = ed1.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(ed1.getText().toString());
                    double convertedCurrency = convertCurrency(amt, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                    ed2.setText(String.valueOf(convertedCurrency));
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
                if (ed2.isFocused()) {
                    double amt = ed2.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(ed2.getText().toString());
                    double convertedCurrency = convertCurrency(amt, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString());
                    ed1.setText(String.valueOf(convertedCurrency));
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sp1 = R.id.spinnerFrom;
        int sp2=R.id.spinnerTO;
        if(parent.getId()==R.id.spinnerFrom){
            double amt1 = ed1.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(ed1.getText().toString());
                double convertedCurrency1 = convertCurrency(amt1, spinner1.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                ed2.setText(String.valueOf(convertedCurrency1));
        }
        else if(parent.getId() == R.id.spinnerTO){
            double amt2 = ed2.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(ed2.getText().toString());
               double convertedCurrency2 = convertCurrency(amt2, spinner2.getSelectedItem().toString(), spinner1.getSelectedItem().toString());
                ed1.setText(String.valueOf(convertedCurrency2));
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
                return indianRupee;
            case "USD":
                return indianRupee * 0.012;
            case "JPY":
                return indianRupee * 1.60;
            case "RUB":
                return indianRupee * 0.93;
            case "GBP":
                return indianRupee * 0.0095;
            case "AUD":
                return indianRupee * 0.018;
            case "CNY":
                return indianRupee * 0.0872;
            default:
                return 0.0;
        }
    }

    private double convertOtherToIndianCurrency(double amt, String firstCurrency) {
        switch (firstCurrency) {
            case "INR":
                return amt;
            case "USD":
                return amt * 82.63;
            case "JPY":
                return amt * 0.62;
            case "RUB":
                return amt * 1.07;
            case "GBP":
                return amt * 105.4;
            case "AUD":
                return amt * 55.58;
            case "CNY":
                return amt * 11.4707;
            default:
                return 0.0;
        }
    }
}


