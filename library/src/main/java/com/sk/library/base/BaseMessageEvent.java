package com.sk.library.base;

/**
 * Created by sk on 16/2/16.
 * EventBus 发送消息事件
 */
public class BaseMessageEvent<T> {

    private T bean;
    private int eventCode;

    public BaseMessageEvent(T bean) {
        this.bean = bean;
    }

    public BaseMessageEvent(T bean, int eventCode) {
        this.bean = bean;
        this.eventCode = eventCode;
    }

    public T getBean() {
        return bean;
    }

    public int getEventCode() {
        return eventCode;
    }
}
