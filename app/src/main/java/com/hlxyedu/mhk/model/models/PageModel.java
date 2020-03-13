package com.hlxyedu.mhk.model.models;

import com.hlxyedu.mhk.model.models.BasePageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weidingqiang on 16/4/9.
 */
public class PageModel {

    //-------------------------------------------------------------------------------//
    //公共部分
    public static final String huanying = "欢迎";
    public static final String jieshu = "结束";

    //听力理解
    public static final String LISTEN_tinglilijie= "听力理解";

    public static final String LISTEN_section_1 = "第一部分";

    public static final String LISTEN_section_2 = "第二部分";

    //阅读理解
    public static final String READ_yuedulijie= "阅读理解";
    //书面表达
    public static final String BOOK_shumianbiaoda = "书面表达";

    //口语
    public static final String SPEAK_langduduanwen = "第一部分：朗读短文";

    public static final String SPEAK_huidawenti = "第二部分：回答问题";

    //作文
    public static final String WRITE_zuowen = "作文";

    //类型
    protected String type;
    //section
    protected String section;

    //是否有选题
    protected Boolean hasquestion =false;
    //基础字段 每个单独类型
    protected List<BasePageModel> basePageModels;
    //重复字段 以后优化需要删除
    protected List<BasePageModel> listenBasePageModels;

    public PageModel(){
        basePageModels = new ArrayList<BasePageModel>();
        listenBasePageModels = new ArrayList<BasePageModel>();
    }

    //------------------------------------------------------------//
    public void addListenBasePageModel(BasePageModel basePageModel){
        hasquestion = true;
        listenBasePageModels.add(basePageModel);
    }

    public List<BasePageModel> getListenBasePageModels() {
        return listenBasePageModels;
    }

    public void addVO(BasePageModel basePageModel){
        basePageModels.add(basePageModel);
    }

    public List<BasePageModel> getBasePageModels() {
        return basePageModels;
    }

    public void setHasquestion(Boolean hasquestion) {
        this.hasquestion = hasquestion;
    }

    public Boolean getHasquestion() {
        return hasquestion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
