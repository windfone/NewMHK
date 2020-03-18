package com.hlxyedu.mhk.di.component;

import android.app.Activity;

import com.hlxyedu.mhk.di.module.FragmentModule;
import com.hlxyedu.mhk.di.scope.FragmentScope;
import com.hlxyedu.mhk.ui.ecomposition.fragment.TxtFragment;
import com.hlxyedu.mhk.ui.espeak.fragment.SpeakFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.ESelectTxtFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.EEndFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.ERepeatFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.ETxtFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.EWelcomeFragment;
import com.hlxyedu.mhk.ui.elistening.fragment.ListeningFragment;
import com.hlxyedu.mhk.ui.exercise.fragment.TestFinishFragment;
import com.hlxyedu.mhk.ui.main.fragment.ExamFragment;
import com.hlxyedu.mhk.ui.main.fragment.ExerciseFragment;
import com.hlxyedu.mhk.ui.main.fragment.MineFragment;
import com.hlxyedu.mhk.ui.main.fragment.OperationFragment;

import dagger.Component;

/**
 * Created by skyworth on 16/8/7.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(ExerciseFragment exerciseFragment);

    void inject(OperationFragment operationFragment);

    void inject(ExamFragment examFragment);

    void inject(MineFragment mineFragment);

    void inject(EWelcomeFragment eWelcomeFragment);

    void inject(ESelectTxtFragment eSelectTxtFragment);

    void inject(ERepeatFragment eRepeatFragment);

    void inject(ETxtFragment eTxtFragment);

    void inject(EEndFragment eEndFragment);

    void inject(ListeningFragment listeningFragment);

    void inject(TestFinishFragment testFinishFragment);

    void inject(SpeakFragment speakFragment);

    void inject(TxtFragment txtFragment);

}
