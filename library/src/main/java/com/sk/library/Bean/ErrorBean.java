package com.sk.library.Bean;

import org.json.JSONObject;

/**
 * Created by sk on 16/3/15.
 * errorBean
 */
public class ErrorBean {

    private int code;
    private String msg;

    public ErrorBean(JSONObject error) {
        setCode(error.optInt("code", 0));
        setMsg(error.optString("msg"));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
