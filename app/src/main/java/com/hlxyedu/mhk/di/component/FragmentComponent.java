package com.hlxyedu.mhk.di.component;

import android.app.Activity;

import com.hlxyedu.mhk.di.module.FragmentModule;
import com.hlxyedu.mhk.di.scope.FragmentScope;
import com.hlxyedu.mhk.ui.ebook.fragment.BookFragment;
import com.hlxyedu.mhk.ui.ecomposition.fragment.TxtFragment;
import com.hlxyedu.mhk.ui.elistening.fragment.ListeningFragment;
import com.hlxyedu.mhk.ui.eread.fragment.ReadFragment;
import com.hlxyedu.mhk.ui.espeak.fragment.SpeakFragment;
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

    void inject(ListeningFragment listeningFragment);

    void inject(SpeakFragment speakFragment);

    void inject(TxtFragment txtFragment);

    void inject(ReadFragment readFragment);

    void inject(BookFragment bookFragment);

}
