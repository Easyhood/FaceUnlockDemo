package com.king.faceunlockdemo;

import com.google.gson.Gson;

public class CompareJsonParse {

    public static float getConfindence(String json){

        Gson gson = new Gson();
        FaceppCompareBean faceppCompareBean = gson.fromJson(json,FaceppCompareBean.class);
        float confindence = faceppCompareBean.getConfidence();
        return confindence;
    }

}
