package com.hlxyedu.mhk.di.module;


import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.http.HttpHelper;
import com.hlxyedu.mhk.model.http.RetrofitHelper;
import com.hlxyedu.mhk.model.prefs.ImplPreferencesHelper;
import com.hlxyedu.mhk.model.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 作者：skyworth on 2017/7/10 14:43
 * 邮箱：dqwei@iflytek.com
 */
@Module
public class AppModule {

    private final AppContext application;

    public AppModule(AppContext application) {
        this.application = application;
    }

    @Provides
    @Singleton
    AppContext provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
        return retrofitHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper) {
        return implPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, PreferencesHelper preferencesHelper) {
        return new DataManager(httpHelper,preferencesHelper);
    }
}
