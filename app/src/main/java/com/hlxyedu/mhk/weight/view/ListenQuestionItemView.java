package com.hlxyedu.mhk.weight.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.model.models.BasePageModel;
import com.hlxyedu.mhk.model.models.ListenQOptionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weidingqiang on 16/4/12.
 */
public class ListenQuestionItemView extends LinearLayout implements View.OnClickListener {

    private TextView question_title;

    private LinearLayout layout_a, layout_b, layout_c, layout_d;

    private TextView image_a, image_b, image_c, image_d;

    private TextView text_a, text_b, text_c, text_d;

    private BasePageModel basePageModel;

    private List<TextView> imageViews;

    private List<TextView> textViews;

    private List<LinearLayout> layOuts;

    private onAnswerClickListener clickListener;

    public ListenQuestionItemView(Context context) {
        this(context, null);
    }

    public ListenQuestionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.question_item_view, this, true);

        question_title = getView(this, R.id.question_title);

        image_a = getView(this, R.id.image_a);
        image_b = getView(this, R.id.image_b);
        image_c = getView(this, R.id.image_c);
        image_d = getView(this, R.id.image_d);

        imageViews = new ArrayList<TextView>();
        imageViews.add(image_a);
        imageViews.add(image_b);
        imageViews.add(image_c);
        imageViews.add(image_d);


        text_a = getView(this, R.id.text_a);
        text_b = getView(this, R.id.text_b);
        text_c = getView(this, R.id.text_c);
        text_d = getView(this, R.id.text_d);

        textViews = new ArrayList<TextView>();
        textViews.add(text_a);
        textViews.add(text_b);
        textViews.add(text_c);
        textViews.add(text_d);

        layout_a = getView(this, R.id.layout_a);
        layout_b = getView(this, R.id.layout_b);
        layout_c = getView(this, R.id.layout_c);
        layout_d = getView(this, R.id.layout_d);
        layOuts = new ArrayList<LinearLayout>();

        layOuts.add(layout_a);
        layOuts.add(layout_b);
        layOuts.add(layout_c);
        layOuts.add(layout_d);

    }


    public void setData(BasePageModel basePageModel) {
        this.basePageModel = basePageModel;

        String str = basePageModel.getQuestionid();
        String[] strs = str.split("_");
        question_title.setText(strs[1] + ".");//+ "  "+ listenQuestionModel.getText());

        for (int i = 0; i < basePageModel.getListenQOptionModels().size(); i++) {
            ListenQOptionModel listenQOptionModel = basePageModel.getListenQOptionModels().get(i);
            TextView textView = textViews.get(i);
            textView.setText(listenQOptionModel.getOptiontext());
            LinearLayout layout = layOuts.get(i);
            layout.setOnClickListener(this);
        }
    }

    public <T extends View> T getView(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_a:
                clickListener.setAnswer(this, "A");
                setUIA();
//                setImageBackGround(R.drawable.button_select_a, R.drawable.button_b, R.drawable.button_c, R.drawable.button_d);
                break;
            case R.id.layout_b:
                clickListener.setAnswer(this, "B");
                setUIB();
//                setImageBackGround(R.drawable.button_a, R.drawable.button_select_b, R.drawable.button_c, R.drawable.button_d);
                break;
            case R.id.layout_c:
                clickListener.setAnswer(this, "C");
                setUIC();
//                setImageBackGround(R.drawable.button_a, R.drawable.button_b, R.drawable.button_select_c, R.drawable.button_d);
                break;
            case R.id.layout_d:
                clickListener.setAnswer(this, "D");
                setUID();
//                setImageBackGround(R.drawable.button_a, R.drawable.button_b, R.drawable.button_c, R.drawable.button_select_d);
                break;
        }
    }


    private void setUIA() {
        image_a.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_circle));
        image_a.setTextColor(getResources().getColor(R.color.whitefff));
        image_b.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_b.setTextColor(getResources().getColor(R.color.gray7C7));
        image_c.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_c.setTextColor(getResources().getColor(R.color.gray7C7));
        image_d.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_d.setTextColor(getResources().getColor(R.color.gray7C7));
    }

    private void setUIB() {
        image_a.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_a.setTextColor(getResources().getColor(R.color.gray7C7));
        image_b.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_circle));
        image_b.setTextColor(getResources().getColor(R.color.whitefff));
        image_c.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_c.setTextColor(getResources().getColor(R.color.gray7C7));
        image_d.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_d.setTextColor(getResources().getColor(R.color.gray7C7));
    }

    private void setUIC() {
        image_a.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_a.setTextColor(getResources().getColor(R.color.gray7C7));
        image_b.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_b.setTextColor(getResources().getColor(R.color.gray7C7));
        image_c.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_circle));
        image_c.setTextColor(getResources().getColor(R.color.whitefff));
        image_d.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_d.setTextColor(getResources().getColor(R.color.gray7C7));
    }

    private void setUID() {
        image_a.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_a.setTextColor(getResources().getColor(R.color.gray7C7));
        image_b.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_b.setTextColor(getResources().getColor(R.color.gray7C7));
        image_c.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_gray_circle));
        image_c.setTextColor(getResources().getColor(R.color.gray7C7));
        image_d.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_blue_circle));
        image_d.setTextColor(getResources().getColor(R.color.whitefff));
    }

    public void setAnswerClickListener(onAnswerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface onAnswerClickListener {
        public void setAnswer(View view, String answer);
    }
}
