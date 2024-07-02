package com.example.currency_converter;

public interface ExchangeRateCallback {
    void onExchangeRateFetched(double rate);
    void onExchangeRateFailed(String errorMessage);
}
