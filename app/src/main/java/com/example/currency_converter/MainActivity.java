package com.example.currency_converter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerFrom,spinnerTO;
    private EditText editTextFrom, editTextTO;

    private  final  double[][] conversationRates= {
            {1,0.93,1.49,83.34,0.79,160.94,7.8,86.25},//for usd
            {1.10,1,1.60,90,0.85,140,8.60,95},//for eur
            {0.69,0.62,1,56,0.53,87,5.35,59},//for AUD
            {0.012,0.011,0.018,1,0.0094,1.55,0.096,1.05},//for inr
            {1.30,1.18,1.88,105,1,164,10.15,111},//for gbp
            {0.0078,0.0071,0.011,0.64,0.0061,1,0.062,0.68},//for jpy
            {0.13,0.12,0.19,10.5,0.098,16.13,1,11},//hkd
            {0.010,0.0095,0.017,0.96,0.009,1.46,0.091,1}//rub
    };
    private boolean isConverting=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTO = findViewById(R.id.spinnerTO);
        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTO = findViewById(R.id.editTextTO);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.currencies));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(myAdapter);
        spinnerTO.setAdapter(myAdapter);



        editTextFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isConverting) {
                    convertCurrencyFrom();
                }
            }
        });

        editTextTO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if(!isConverting) {
                        convertCurrencyTO();
                    }
            }
        });
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isConverting){
                  convertCurrencyFrom();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };

        spinnerFrom.setOnItemSelectedListener(itemSelectedListener);
        spinnerTO.setOnItemSelectedListener(itemSelectedListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void convertCurrencyFrom(){
        String fromValue=editTextFrom.getText().toString();
        if(!fromValue.isEmpty()){
            isConverting=true;
            int fromIndex=spinnerFrom.getSelectedItemPosition();
            int toIndex=spinnerTO.getSelectedItemPosition();
            double fromAmount=Double.parseDouble(fromValue);
            double conversationRate=conversationRates[fromIndex][toIndex];
            double toAmount=fromAmount*conversationRate;
            editTextTO.setText(String.format("%.3f",toAmount));
            isConverting=false;
        }
        else{
            editTextTO.setText("");
        }
    }

    public void convertCurrencyTO(){
        String toValue=editTextTO.getText().toString();
        if(!toValue.isEmpty()){
            isConverting=true;
            int fromIndex=spinnerFrom.getSelectedItemPosition();
            int toIndex=spinnerTO.getSelectedItemPosition();
            double toAmount=Double.parseDouble(toValue);
            double conversationRate=conversationRates[toIndex][fromIndex];
            double fromAmount=toAmount*conversationRate;
            editTextFrom.setText(String.format("%.3f",fromAmount));
            isConverting=false;
        }
        else{
            editTextTO.setText("");
        }
    }




    }
