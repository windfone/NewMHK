package com.hlxyedu.mhk.di.component;

import android.app.Activity;

import com.hlxyedu.mhk.di.module.ActivityModule;
import com.hlxyedu.mhk.di.scope.ActivityScope;
import com.hlxyedu.mhk.ui.exam.activity.TestScoreActivity;
import com.hlxyedu.mhk.ui.exercise.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.ui.login.activity.FoundPsdActivity;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.ui.mine.activity.FeedBackActivity;
import com.hlxyedu.mhk.ui.mine.activity.GradeActivity;
import com.hlxyedu.mhk.ui.operation.activity.OperationSelectActivity;
import com.hlxyedu.mhk.ui.splash.activity.SplashActivity;

import dagger.Component;

/**
 * 作者：skyworth on 2017/7/10 16:04
 * 邮箱：dqwei@iflytek.com
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    Activity getActivity();

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(FoundPsdActivity foundPsdActivity);

    void inject(ExerciseSelectActivity exerciseSelectActivity);

    void inject(TestScoreActivity testScoreActivity);

    void inject(OperationSelectActivity operationSelectActivity);

    void inject(GradeActivity gradeActivity);

    void inject(FeedBackActivity feedBackActivity);

}
