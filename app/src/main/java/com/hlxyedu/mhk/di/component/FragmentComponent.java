package com.hlxyedu.mhk.di.component;

import android.app.Activity;

import com.hlxyedu.mhk.di.module.FragmentModule;
import com.hlxyedu.mhk.di.scope.FragmentScope;
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

}
