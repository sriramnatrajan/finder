package com.focusmedica.aqrshell;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by windev on 8/2/2017.
 */

public class LogApi {
    private static volatile LogApi instance;
    private final ServiceApi servicesApi;

    public static LogApi getInstance() {
        LogApi localInstance = instance;
        if (localInstance == null) {
            synchronized (LogApi.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LogApi();
                }
            }
        }
        return localInstance;
    }

    private LogApi() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://pantherpublishers.com/kriticalhealth/aqr/")
                .baseUrl("http://192.168.1.39/eaa/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        servicesApi = retrofit.create(ServiceApi.class);
    }

    public ServiceApi getServicesApi(){
        return servicesApi;
    }
}
