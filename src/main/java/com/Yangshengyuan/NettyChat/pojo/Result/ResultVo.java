package com.Yangshengyuan.NettyChat.pojo.Result;

public class ResultVo {

    private Boolean success;
    private String Message;
    private Object result;

    public ResultVo(Boolean success, String message) {
        this.success = success;
        Message = message;
    }

    public ResultVo(Boolean success, String message, Object result) {
        this.success = success;
        Message = message;
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
