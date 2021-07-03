package com.xing.gfox.util.model;
/**
 * 通话记录信息
 */
public class TPhoneRecord {
    private int _id;            //序列号
    private String phoneNum;    // 电话号码 STRING 最长为20字节
    private int callType;       //通话的数类型 0:未接，1:已结， 2:已拨
    private String time;        //FORMAT = "yyyy-MM-dd HH:mm:ss"
    private int length;         //通话时长，单位为s

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
