package com.jiang.chatroom.entity.chat;

import java.util.Date;
import java.util.Objects;

/**
 * @description: 消息
 * @author: jiangBUG@outlook.com
 * @create: 2019-02-26 12:55
 */
public class Message {

    /**
     * 消息时间戳
     */
    private Date timeStamp;

    /**
     * 消息类型
     * "0" -> 好友消息
     * "1" -> 系统消息
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息投递对象（好友名称）
     */
    private String contactName;

    /**
     * 消息投递目的IP地址（好友IP地址）
     */
    private String contactIpAddr;


    public Message() {
        super();
    }


    public Message(String msg, String contactName, String msgType) {
        this.msg = msg;
        this.msgType = msgType;
        this.contactName = contactName;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactIpAddr() {
        return contactIpAddr;
    }

    public void setContactIpAddr(String contactIpAddr) {
        this.contactIpAddr = contactIpAddr;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(timeStamp, message.timeStamp) &&
                Objects.equals(msgType, message.msgType) &&
                Objects.equals(msg, message.msg) &&
                Objects.equals(contactName, message.contactName) &&
                Objects.equals(contactIpAddr, message.contactIpAddr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(timeStamp, msgType, msg, contactName, contactIpAddr);
    }

    @Override
    public String toString() {
        return "Message{" +
                "timeStamp=" + timeStamp +
                ", msgType='" + msgType + '\'' +
                ", msg='" + msg + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactIpAddr='" + contactIpAddr + '\'' +
                '}';
    }
}
