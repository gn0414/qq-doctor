package com.qiqiao.model;

/**
 * @author Simon
 */
public class Result<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }



    public static <T> Result<T> success(T data) {
        return new Result<>(true, 200, "success", data);
    }

    public static <T> Result<T> successNoData() {
        return new Result<>(true, 200, "success");
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(false, code, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}