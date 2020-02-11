package com.hlxyedu.mhk.di.component;


import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.di.module.AppModule;
import com.hlxyedu.mhk.di.module.HttpModule;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 作者：skyworth on 2017/7/10 14:40
 * 邮箱：dqwei@iflytek.com
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    AppContext getContext();  // 提供App的Context

    DataManager getDataManager(); //数据中心

    RetrofitHelper retrofitHelper();  //提供http的帮助类
}
