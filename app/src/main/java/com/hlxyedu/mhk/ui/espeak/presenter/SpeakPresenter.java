package com.hlxyedu.mhk.ui.espeak.presenter;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hlxyedu.mhk.base.RxBus;
import com.hlxyedu.mhk.base.RxPresenter;
import com.hlxyedu.mhk.model.DataManager;
import com.hlxyedu.mhk.model.bean.UserVO;
import com.hlxyedu.mhk.model.event.CommitEvent;
import com.hlxyedu.mhk.model.http.response.HttpResponseCode;
import com.hlxyedu.mhk.ui.espeak.contract.SpeakContract;
import com.hlxyedu.mhk.utils.RegUtils;
import com.hlxyedu.mhk.utils.RxUtil;
import com.hlxyedu.mhk.weight.CommonSubscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zhangguihua
 */
public class SpeakPresenter extends RxPresenter<SpeakContract.View> implements SpeakContract.Presenter {
    private DataManager mDataManager;

    private List<File> files = new ArrayList<>();
    private int current = 0;

    private String fileName, examId, homeworkId, testId, testType;

    @Inject
    public SpeakPresenter(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(SpeakContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CommitEvent.class)
                .compose(RxUtil.<CommitEvent>rxSchedulerHelper())
                .filter(new Predicate<CommitEvent>() {
                    @Override
                    public boolean test(@NonNull CommitEvent commitEvent) throws Exception {
                        return commitEvent.getType().equals(CommitEvent.COMMIT);
                    }
                })
                .subscribeWith(new CommonSubscriber<CommitEvent>(mView) {
                    @Override
                    public void onNext(CommitEvent s) {

                        if (!mView.isShow()) {
                            return;
                        }

                        files = FileUtils.listFilesInDir((String) s.getAnswer());

                        examId = s.getExamId();
                        homeworkId = s.getHomeworkId();
                        testId = s.getTestId();
                        testType = s.getTestType();
                        cimmitAnswer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );
    }

    //String fileName,String examId,String homeworkId,String testId,String testType
    public void cimmitAnswer() {
        fileName = files.get(current).getName().substring(0, files.get(current).getName().length() - 4);
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), getUserId());
        RequestBody examIdBody = RequestBody.create(MediaType.parse("text/plain"), examId);
        if (homeworkId == null) {
            homeworkId = "";
        }
        if (testId == null) {
            testId = "";
        }
        if (testType == null) {
            testType = "";
        }
        RequestBody homeworkIdBody = RequestBody.create(MediaType.parse("text/plain"), homeworkId);
        RequestBody testIdBody = RequestBody.create(MediaType.parse("text/plain"), testId);
        RequestBody testTypeBody = RequestBody.create(MediaType.parse("text/plain"), testType);
        RequestBody fileNameBody = RequestBody.create(MediaType.parse("text/plain"), fileName);
        File file = files.get(current);

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("audioData", file.getName(), fileRequestBody);

//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("audioData", file.getName(),
//                RequestBody.create(MediaType.parse("image/*"), file));
        addSubscribe(mDataManager.uploadRecord(userIdBody, examIdBody, homeworkIdBody, testIdBody, testTypeBody, fileNameBody, fileData)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleTestResult())
                .subscribeWith(
                        new CommonSubscriber<String>(mView) {
                            @Override
                            public void onNext(String s) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                //当数据返回为null时 做特殊处理
                                if (e instanceof HttpException) {
                                    HttpResponseCode httpResponseCode = RegUtils
                                            .onError((HttpException) e);
                                    ToastUtils.showShort(httpResponseCode.getMsg());
                                }
                                mView.responeError("数据请求失败，请检查网络！");
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                current++;
                                if (current < files.size()) {
                                    cimmitAnswer();
                                } else {
                                    mView.commitSuccess();
                                }
                            }
                        }
                )
        );


//        File zipFile = new File(filename);
//
//        //设置Content-Type:application/octet-stream
//        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), zipFile);
//        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
//        MultipartBody.Part fileData = MultipartBody.Part.createFormData("fileData", zipFile.getName(), fileRequestBody);
//
//        String time_mills = String.valueOf(TimeUtils.getNowTimeMills());
//        String key = Sha1Util.getSha1(time_mills+ Constants.KEY);
//
//        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), zipFile.getName());
//
//        RequestBody time_mills_body = RequestBody.create(MediaType.parse("text/plain"), time_mills);
//        RequestBody key_body = RequestBody.create(MediaType.parse("text/plain"), key);
//        RequestBody exam_no_body = RequestBody.create(MediaType.parse("text/plain"), AppContext.getInstance().getUserVO().getExam_no());
//
//        RequestBody md5Str = RequestBody.create(MediaType.parse("text/plain"), md5);
//
//        // 添加一个参数，上传正确答案和考生答案  （无socket 监狱版 需要上传 不加密答案参数）
//        Gson gson = new Gson();
//
//        StringAnswer stringAnswer = AppContext.getInstance().getStringAnswer();
//        String subStuAnswer = stringAnswer.getStuAnswer().substring(0,stringAnswer.getStuAnswer().length()-1); // 截掉最后一个 "#" 号；
//        stringAnswer.setStuAnswer(subStuAnswer);
//        String dataJson = gson.toJson(stringAnswer);
//        RequestBody dataStr = RequestBody.create(MediaType.parse("text/plain"), dataJson);

    }

    @Override
    public String getUserId() {
        UserVO userVO = GsonUtils.fromJson(mDataManager.getSpUserInfo(), UserVO.class);
        return userVO.getId();
    }

}