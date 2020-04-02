package com.hlxyedu.mhk.di.component;

import android.app.Activity;

import com.hlxyedu.mhk.di.module.ActivityModule;
import com.hlxyedu.mhk.di.scope.ActivityScope;
import com.hlxyedu.mhk.ui.ebook.activity.TestBookActivity;
import com.hlxyedu.mhk.ui.ecomposition.activity.TestTxtActivity;
import com.hlxyedu.mhk.ui.eread.activity.TestReadActivity;
import com.hlxyedu.mhk.ui.espeak.activity.TestSpeakActivity;
import com.hlxyedu.mhk.ui.exam.activity.ExamFinishActivity;
import com.hlxyedu.mhk.ui.exam.activity.TestScoreActivity;
import com.hlxyedu.mhk.ui.mine.activity.AboutUsActivity;
import com.hlxyedu.mhk.ui.mine.activity.ServiceTermsActivity;
import com.hlxyedu.mhk.ui.select.activity.ExerciseSelectActivity;
import com.hlxyedu.mhk.ui.elistening.activity.TestListeningActivity;
import com.hlxyedu.mhk.ui.login.activity.FoundPsdActivity;
import com.hlxyedu.mhk.ui.login.activity.LoginActivity;
import com.hlxyedu.mhk.ui.main.activity.MainActivity;
import com.hlxyedu.mhk.ui.mine.activity.FeedBackActivity;
import com.hlxyedu.mhk.ui.mine.activity.GradeActivity;
import com.hlxyedu.mhk.ui.select.activity.OperationSelectActivity;
import com.hlxyedu.mhk.ui.splash.activity.GuideActivity;
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

    void inject(GuideActivity guideActivity);

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(FoundPsdActivity foundPsdActivity);

    void inject(ExerciseSelectActivity exerciseSelectActivity);

    void inject(TestScoreActivity testScoreActivity);

    void inject(OperationSelectActivity operationSelectActivity);

    void inject(GradeActivity gradeActivity);

    void inject(FeedBackActivity feedBackActivity);

    void inject(TestListeningActivity testListeningActivity);

    void inject(TestSpeakActivity testSpeakActivity);

    void inject(TestTxtActivity testTxtActivity);

    void inject(TestReadActivity testReadActivity);

    void inject(TestBookActivity testBookActivity);

    void inject(ExamFinishActivity examFinishActivity);

    void inject(ServiceTermsActivity serviceTermsActivity);

    void inject(AboutUsActivity aboutUsActivity);

}
