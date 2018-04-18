package com.focusmedica.aqrshell;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by trisys-tejesh on 11/4/17.
 */

public class ApiCaller {
    private static volatile ApiCaller instance;
    private final ServiceApi servicesApi;

    public static ApiCaller getInstance() {
        ApiCaller localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiCaller.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiCaller();
                }
            }
        }
        return localInstance;
    }

    private ApiCaller() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
               .baseUrl("http://pantherpublishers.com/distribution/udshell/v3/")
             //   .baseUrl("http://pantherpublishers.com/distribution/udtesting/v1/")
               // .baseUrl("http://pantherpublishers.com/distribution/kh/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        servicesApi = retrofit.create(ServiceApi.class);
    }

    public ServiceApi getServicesApi(){
        return servicesApi;
    }
}
