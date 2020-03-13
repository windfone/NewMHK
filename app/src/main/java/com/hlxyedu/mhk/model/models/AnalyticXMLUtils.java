package com.hlxyedu.mhk.model.models;


import com.skyworth.rxqwelibrary.app.AppConstants;

import org.dom4j.Element;

import java.io.File;
import java.util.List;

/**
 * Created by weidingqiang on 16/8/2.
 * 解析xml
 * 暂时放在这里
 */
public class AnalyticXMLUtils {

    /**
     * 解析欢迎或者结束部分
     * @param page
     * @param type
     * @param section_name
     */
    public static void encodeWelcomeOrEndPageModel(List<PageModel> pageModels, String zipfilename, Element page, String type, String section_name){
        int current = 0;
        //初始化
        PageModel pageModel = new PageModel();
        pageModel.setType(type);
        pageModel.setSection(section_name);
        //循环遍历
        List<Element> elements = page.elements();
        //这是需要解析的文件 text   richtext  wait
        for (int i=0;i<elements.size();i++){
            Element element = elements.get(i);
            BasePageModel basePageModel = new BasePageModel();
            basePageModel.setCurrent(current++);
            basePageModel.setType(element.getName());
            switch (element.getName()){
                case BasePageModel.TEXT:
                    basePageModel.setContent(element.getStringValue());
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.RICHTEXT:
                    basePageModel.setSrc(getFileUrl(zipfilename,element.attributeValue("src"),true));
                    basePageModel.setSuffix("png");
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.WAIT:
                    basePageModel.setTimeout(element.attributeValue("timeout"));
                    pageModel.addVO(basePageModel);
                    break;
                case BasePageModel.AUDIO:
                    basePageModel.setSrc(getFileUrl(zipfilename,element.attributeValue("src"),false));
                    pageModel.addVO(basePageModel);
                    break;
            }

        }
        pageModels.add(pageModel);
    }

    /**
     *
     * @param zipfilename
     * @param filename
     * @param iswelcome
     * @return
     */
    public static String getFileUrl(String zipfilename, String filename, boolean iswelcome){
        //判断后缀名是否为 rtf 文件，如果是 则换成 png
        try {
            if(filename.substring(filename.length()-3,filename.length()).equals("rtf"))
            {
                if(iswelcome)
                {
                    filename = filename.replace("rtf","png");
                }
                else{
                    filename = filename.replace("rtf","txt");
                }
            }
        }catch (Exception ex)
        {

        }

        return AppConstants.UNFILE_DOWNLOAD_PATH+zipfilename+ File.separator+filename;
    }
}
