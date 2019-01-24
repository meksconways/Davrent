package com.toberli.davrent.networking;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public abstract class NetworkModule {


    @Provides
    @Singleton
    static Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://davrent.eventlinn.com/public/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    static ApiService provideNewsService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

}
