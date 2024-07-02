//package com.example.currency_converter;
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//public interface ExchangeRateService {
//
//    @GET("latest")
//    Call<ExchangeRateResponse> getLatestRates(
//            @Query("base") String baseCurrency,
//            @Query("symbols") String targetCurrency,
//            @Query("access_key") String accessKey
//    );
//}
package com.example.currency_converter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExchangeRateService {
    @GET("{apiKey}/latest/{baseCurrency}")
    Call<ExchangeRateResponse> getLatestRates(
            @Path("apiKey") String apiKey,
            @Path("baseCurrency") String baseCurrency
    );
}
