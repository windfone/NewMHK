package com.hlxyedu.mhk.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by weidingqiang on 16/4/21.
 */
public class WaveView extends View {

    private int _recordlength;

    private float gap;

    private Boolean isfrist = true;

    private double _rate = -1;

    private Paint paint;

    private ArrayList<Double> heightrects;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.WHITE);

        heightrects = new ArrayList<Double>();

    }

    public void set_recordlength(int recordlength) {
        _recordlength = recordlength * 13;

        //每次设置 都需要重新计算
        if(!isfrist)
        {
            gap = (float)getMeasuredWidth() / (float) _recordlength;
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            String p=decimalFormat.format(gap);//format 返回的是字符串
            gap = Float.valueOf(p);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //第一次计算gap值
        if(isfrist)
        {
            gap = (float)getMeasuredWidth() / (float) _recordlength;
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            String p=decimalFormat.format(gap);//format 返回的是字符串
            gap = Float.valueOf(p);
            isfrist = false;

            return;
        }

        if(_rate != -1){

            for (int i =0;i<heightrects.size();i++)
            {
                int _height = (int)(getHeight()*heightrects.get(i));
                int _left = (getHeight()- _height)/2;
                canvas.drawRect(i*gap,_left,(i+1)*gap,_left+_height,paint);
            }
        }
    }

    public void setHeightRect(double rate){
        _rate = rate;
        if(_rate<0.01)
        {
            _rate = 0.01;
        }
        heightrects.add(_rate);
        invalidate();
    }

    public void clear(){
        heightrects.clear();
        invalidate();
    }

}
