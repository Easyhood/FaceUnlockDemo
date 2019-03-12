package com.king.faceunlockdemo;

import java.lang.reflect.Array;

/**
 * 面部比较结果bean
 */

public class FaceppCompareBean {


    /**
     *  "time_used": 473,
     *   "confidence": 96.46,
     *   "thresholds": {
     *     "1e-3": 65.3,
     *     "1e-5": 76.5,
     *     "1e-4": 71.8
     *   },
     *   "request_id": "1469761507,07174361-027c-46e1-811f-ba0909760b18"
     *
     *   "time_used": 5,
     *   "error_message": "INVALID_FACE_TOKEN:c2fc0ad7c8da3af5a34b9c70ff764da0",
     *   "request_id": "1469761051,ec285c20-8660-47d3-8b91-5dc2bffa0049"
     */


    private int time_used;
    private float confidence;
    private Object thresholds;
    private String request_id;
    private String image_id1;
    private String image_id2;
    private Array faces1;
    private Array faces2;
    private String error_message;



    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public Object getThresholds() {
        return thresholds;
    }

    public void setThresholds(Object thresholds) {
        this.thresholds = thresholds;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getImage_id1() {
        return image_id1;
    }

    public void setImage_id1(String image_id1) {
        this.image_id1 = image_id1;
    }

    public String getImage_id2() {
        return image_id2;
    }

    public void setImage_id2(String image_id2) {
        this.image_id2 = image_id2;
    }

    public Array getFaces1() {
        return faces1;
    }

    public void setFaces1(Array faces1) {
        this.faces1 = faces1;
    }

    public Array getFaces2() {
        return faces2;
    }

    public void setFaces2(Array faces2) {
        this.faces2 = faces2;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }


    @Override
    public String toString() {
        return "FaceppCompareBean{" +
                "time_used=" + time_used +
                ", confidence=" + confidence +
                ", thresholds=" + thresholds +
                ", request_id='" + request_id + '\'' +
                ", image_id1='" + image_id1 + '\'' +
                ", image_id2='" + image_id2 + '\'' +
                ", faces1=" + faces1 +
                ", faces2=" + faces2 +
                ", error_message='" + error_message + '\'' +
                '}';
    }
}
