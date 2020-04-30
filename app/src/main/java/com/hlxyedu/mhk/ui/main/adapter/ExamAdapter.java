package com.hlxyedu.mhk.ui.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hlxyedu.mhk.R;
import com.hlxyedu.mhk.app.AppContext;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.model.bean.ExamProgressVO;
import com.hlxyedu.mhk.model.bean.ExamVO;
import com.hlxyedu.mhk.model.event.DownLoadEvent;
import com.hlxyedu.mhk.weight.listener.NoDoubleClickListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.skyworth.rxqwelibrary.widget.NetErrorDialog;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends BaseQuickAdapter<ExamVO, BaseViewHolder> {

    private ArrayList<ExamVO> datas;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    public ExamAdapter(int layoutResId,
                       @Nullable List<ExamVO> data) {
        super(layoutResId, data);
        this.datas = (ArrayList<ExamVO>) data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExamVO item) {
        helper.setText(R.id.title_tv, item.getTestName())
//                .setText(R.id.author_tv, item.getTeacherName()+"")
                .setImageResource(R.id.question_type_iv, R.drawable.icon_zh);
        // item.getState() :  0 去考试； 1 已结束； 2 未开始

        switch (item.getState()) {
            case "0":
                helper.setText(R.id.positive_btn, "去考试");
                break;
            case "1":
                helper.setText(R.id.positive_btn, "已结束").addOnClickListener(R.id.positive_btn);
                break;
            case "2":
                helper.setText(R.id.positive_btn, "未开始").addOnClickListener(R.id.positive_btn);
                break;
        }

        Button button = helper.getView(R.id.positive_btn);
        if (StringUtils.equals(item.getState(), "0")) {
            button.setEnabled(true);
            button.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
//                    showDialog(item);
                    showAutoDialog(item);
                }
            });
        } else {
            button.setEnabled(false);
        }

    }

    private void showAutoDialog(ExamVO item) {
        editStr = "";
        QMAutoTestDialogBuilder autoTestDialogBuilder = (QMAutoTestDialogBuilder) new QMAutoTestDialogBuilder(mContext)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (!editStr.equals("我已按照要求开启飞行模式连接无线网络并退出通讯软件登录状态保证考试顺利进行一旦违反影响考试后果自负")){
                            ToastUtils.showShort("内容输入有误");
                            return;
                        }
                        if (!NetworkUtils.isConnected()) {
                            NetErrorDialog.getInstance().showNetErrorDialog(mContext);
                            return;
                        }
                        if (item.getExamZips() == null) {
                            return;
                        }
                        List<ExamProgressVO> examProgressVOS = new ArrayList<>();
                        for (int i = 0; i < item.getExamZips().size(); i++) {
                            ExamProgressVO examProgressVO = new ExamProgressVO();
                            examProgressVO.setId(item.getId());
                            examProgressVO.setExamId(item.getExamZips().get(i).getExamId());
                            examProgressVO.setZipPath(item.getExamZips().get(i).getZipPath());
                            examProgressVO.setType("exam");
                            examProgressVOS.add(examProgressVO);
                        }
                        AppContext.getInstance().setExamProgressVOS(examProgressVOS);
                        RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXAM));
                        dialog.dismiss();
                    }
                });
//        String str = autoTestDialogBuilder.getEditText().getText().toString();
        autoTestDialogBuilder.setCancelable(false);
        autoTestDialogBuilder.setCanceledOnTouchOutside(false);
        autoTestDialogBuilder.create(mCurrentDialogStyle).show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(), true);
    }

    private String editStr = "";
    class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private EditText mEditText;

        public QMAutoTestDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            // TextView
            TextView textView1 = new TextView(mContext);
            textView1.setText("考试须知");
            textView1.setGravity(Gravity.CENTER_HORIZONTAL);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            textView1.setTextColor(ContextCompat.getColor(mContext, R.color.black131));
            textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView1);

            TextView textView2 = new TextView(mContext);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            textView2.setLineSpacing(QMUIDisplayHelper.dp2px(mContext, 2), 1.0f);
            textView2.setText("我已按照要求开启飞行模式连接无线网络并退出通讯软件登录状态保证考试顺利进行一旦违反影响考试后果自负");
            textView2.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView2);

            //EditText
            mEditText = new AppCompatEditText(mContext);
            mEditText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_edit));
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            mEditText.setGravity(Gravity.TOP);
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            mEditText.setPadding(10, 10, 10, 10);
            mEditText.setHeight(240);
//            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint("请严格按照以上内容输入");
            mEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.gray9A9));
            mEditText.setTextColor(ContextCompat.getColor(mContext, R.color.black131));
//            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
//            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(mContext, 14);
//            mEditText.setLayoutParams(editTextLP);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    editStr = s.toString();
                }
            });
            layout.addView(mEditText);

            return layout;
        }
    }

   /* private void showDialog(ExamVO item) {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DialogPlus mMaterialDialog = DialogPlus.newDialog(mContext)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.dialog_declaration))
                .setContentBackgroundResource(R.drawable.shape_radius_4dp)
                .setContentWidth((int) (display
                        .getWidth() * 0.8))
//                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setContentHeight((int) (display
                        .getHeight() * 0.8))
                .setCancelable(false)//设置不可取消   可以取消
                .setOnClickListener((dialog, view1) -> {
                    switch (view1.getId()) {
                        case R.id.btn_neg:
                            dialog.dismiss();
                            break;
                        case R.id.btn_pos:
                            dialog.dismiss();
                            if (!NetworkUtils.isConnected()) {
                                NetErrorDialog.getInstance().showNetErrorDialog(mContext);
                                return;
                            }

                            if (item.getExamZips() == null) {
                                return;
                            }
                            List<ExamProgressVO> examProgressVOS = new ArrayList<>();
                            for (int i = 0; i < item.getExamZips().size(); i++) {
                                ExamProgressVO examProgressVO = new ExamProgressVO();
                                examProgressVO.setId(item.getId());
                                examProgressVO.setExamId(item.getExamZips().get(i).getExamId());
                                examProgressVO.setZipPath(item.getExamZips().get(i).getZipPath());
                                examProgressVO.setType("exam");
                                examProgressVOS.add(examProgressVO);
                            }
                            AppContext.getInstance().setExamProgressVOS(examProgressVOS);
                            RxBus.getDefault().post(new DownLoadEvent(DownLoadEvent.DOWNLOAD_PAPER_EXAM));
                            break;
                    }
                }).create();

        String[] details = mContext.getResources().getStringArray(R.array.declaration);
        String strings = "";
        for (int i = 0; i < details.length; i++) {
            strings += details[i] + "\n\n";
        }
        TextView textView = (TextView) mMaterialDialog.findViewById(R.id.txt_msg);
        textView.setText(strings);
        mMaterialDialog.show();
    }*/

}
