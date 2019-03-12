package com.king.faceunlockdemo;


import com.google.gson.Gson;

public class DetectJsonParse {


    public static String getFaceToken(String json){
        FaceppDetectBean.FacesBean facesBean = new Gson().fromJson(json,FaceppDetectBean.FacesBean.class);
        String faceToken = facesBean.getFace_token();
        return faceToken;
    }
}
