package com.hlxyedu.mhk.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.textmining.text.extraction.WordExtractor;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by weidingqiang on 16/3/29.
 */
public class CommonUtils {
    /**
     * 将视图从父视图中移除
     *
     * @param view
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    public static String readDOC(InputStream is) {
        String text = null;
        try {
            int a= is.available();
            WordExtractor extractor = null;
            // 创建WordExtractor
            extractor = new WordExtractor();
            // 对doc文件进行提取
            text = extractor.extractText(is);
            System.out.println("解析得到的东西"+text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (text == null) {
            text = "解析文件出现问题";
        }
        return text;
    }

    /**
     * 返回倒计时
     * @return
     */
    public static int delayTime(String delay){
        int time = 0;

        String[] delays = delay.split(":");
                                
        time = Integer.valueOf(delays[0])*60*60 + Integer.valueOf(delays[1])*60+ Integer.valueOf(delays[2]);

        return time*1000;
    }
}
