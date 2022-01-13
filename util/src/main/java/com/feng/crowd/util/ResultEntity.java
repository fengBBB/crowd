package com.feng.crowd.util;

public class ResultEntity<T> {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    //用来封装处理结果 成功或者失败
    private String result;
    //用来封装失败的错误信息
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    //要返回的数据
    private T data;

    //处理成功且不用返回数据
    public static <type> ResultEntity<type> successWithoutData() {
        return new ResultEntity<type>(SUCCESS, null, null);
    }

    //处理成功并且需要返回数据
    public static <type> ResultEntity<type> successWithoutData(type data) {
        return new ResultEntity<type>(SUCCESS, null, data);
    }

    public static <type> ResultEntity<type> fialed(String message) {
        return new ResultEntity<type>(FAILED, message, null);
    }


    public ResultEntity() {

    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

}
