package com.xing.gfox.util.model;

/**
 * 通讯录信息
 */
public class TPhoneContact {
    private int _id;         //序列号
    private String phoneNum; // 电话号码 STRING 最长为20字节。“*”为通配符，表示[0-9],如果电话号码仅有一个“*”字符，则表示匹配所有号码，否则“*”仅为单字符匹配，同一个号码只能加一条
    private String name;     // 联系人 STRING 最长为10字节
    private byte flag;       // 标志 UINT8 1：可以呼入；2：可以呼出；3：可呼入/呼出

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }
}
