package com.xing.gfox.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import com.xing.gfox.util.model.TPhoneContact;
import com.xing.gfox.util.model.TPhoneRecord;

public class U_contacts {
    public static final Uri CONTENT_URI_HISTORY = Uri.parse("content://com.njty.phoneContacts/history");
    public static final Uri CONTENT_URI_CONTACTS = Uri.parse("content://com.njty.phoneContacts/contacts");
    public static final Uri CONTENT_URI_CONFIG = Uri.parse("content://com.njty.phoneContacts/configuration");

    //获取所有电话本信息
    public static List<TPhoneContact> getPhoneBooks(Context context) {
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_CONTACTS, new String[]{"*"}, null, null, null);
        List<TPhoneContact> list = new ArrayList<TPhoneContact>();
        list.clear();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TPhoneContact phone = new TPhoneContact();
                    phone.setPhoneNum(cursor.getString(cursor.getColumnIndex("phoneNum")));
                    phone.setFlag((byte) cursor.getInt(cursor.getColumnIndex("flag")));
                    phone.setName(cursor.getString(cursor.getColumnIndex("name")));
                    phone.set_id((byte) cursor.getInt(cursor.getColumnIndex("_id")));
                    list.add(phone);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return list;
    }

    //插入联系人
    public static boolean insertContact(Context context, TPhoneContact contact) {
        ContentValues values = new ContentValues();
        values.put("phoneNum", contact.getPhoneNum());
        values.put("flag", contact.getFlag());
        values.put("name", contact.getName());
        return context.getContentResolver().insert(CONTENT_URI_CONTACTS, values) != null;
    }

    //删除联系人
    public static boolean deleteContact(Context context, String phoneNum) {
        int num = context.getContentResolver().delete(CONTENT_URI_CONTACTS, "phoneNum=?", new String[]{phoneNum});
        return num > 0;
    }

    public static boolean deleteContact(Context context, int id) {
        int num = context.getContentResolver().delete(CONTENT_URI_CONTACTS, "_id=?", new String[]{String.valueOf(id)});
        return num > 0;
    }

    //更新联系人
    public static boolean updateContact(Context context, TPhoneContact contact) {
        ContentValues values = new ContentValues();
        values.put("_id", contact.get_id());
        values.put("phoneNum", contact.getPhoneNum());
        values.put("flag", contact.getFlag());
        values.put("name", contact.getName());
        int num = context.getContentResolver().update(CONTENT_URI_CONTACTS, values, "_id=?", new String[]{String.valueOf(contact.get_id())});
        return num > 0;
    }

    //获取电话通话记录
    public static List<TPhoneRecord> getPhoneRecord(Context context) {
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_HISTORY, new String[]{"*"}, null, null, null);
        List<TPhoneRecord> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TPhoneRecord record = new TPhoneRecord();
                    record.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                    record.setCallType(cursor.getInt(cursor.getColumnIndex("callType")));
                    record.setLength(cursor.getInt(cursor.getColumnIndex("length")));
                    record.setPhoneNum(cursor.getString(cursor.getColumnIndex("phoneNum")));
                    record.setTime(cursor.getString(cursor.getColumnIndex("time")));
                    list.add(record);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    //设置来电马赛克
    public static void setIncomingMosaic(Context context, boolean status) {
        ContentValues values = new ContentValues();
        values.put("key", "Incoming_mosaic");
        if (status) {
            values.put("value", "1");
        } else {
            values.put("value", "0");
        }
        context.getContentResolver().update(CONTENT_URI_CONFIG, values, "key=?", new String[]{"Incoming_mosaic"});
    }

    //获取来电马赛克
    public static boolean getIncomingMosaic(Context context) {
        boolean flag = false;
        String value;
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_CONFIG, new String[]{"value"}, "key=?", new String[]{"Incoming_mosaic"}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex("value"));
                flag = value.equals("1");
            }
        }
        cursor.close();
        return flag;
    }

    //设置是否去电马赛克
    public static void setOutgoingMosaic(Context context, boolean status) {
        ContentValues values = new ContentValues();
        values.put("key", "Outgoing_mosaic");
        if (status) {
            values.put("value", "1");
        } else {
            values.put("value", "0");
        }
        context.getContentResolver().update(CONTENT_URI_CONFIG, values, "key=?", new String[]{"Outgoing_mosaic"});
    }


    //获取是否去电马赛克
    public static boolean getOutgoingMosaic(Context context) {
        boolean flag = false;
        String value;
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_CONFIG, new String[]{"value"}, "key=?", new String[]{"Outgoing_mosaic"}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex("value"));
                flag = value.equals("1");
            }
            cursor.close();
        }
        return flag;
    }

    //加入白名单,flag: 1：可以呼入；2：可以呼出；3：可呼入/呼出 ，同一号码只能增加一次，多次添加会失败
    public static boolean addWhiteList(Context context, String phoneNum, int flag) {
        ContentValues values = new ContentValues();
        values.put("phoneNum", phoneNum);
        values.put("name", "打的吧乘客");
        values.put("flag", flag);

        Uri result = context.getContentResolver().insert(CONTENT_URI_CONTACTS, values);
        return result != null;
    }

    //删除呼入
    public static boolean deleteWhiteList(Context context, String phoneNum) {
        int num = context.getContentResolver().delete(CONTENT_URI_CONTACTS, "phoneNum=?", new String[]{phoneNum});
        return num > 0;
    }

    //获取呼入电话白名单
    public static List<String> getIncomingWhiteList(Context context) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_CONTACTS, new String[]{"*"}, "flag=1 or flag=3", null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex("phoneNum")));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    //获取呼出电话白名单
    public static List<String> getOutgoingWhiteList(Context context) {
        List<String> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_CONTACTS, new String[]{"*"}, "flag=2 or flag=3", null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex("phoneNum")));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    //根据号码获取联系人的姓名
    public static String testContactNameByNumber(String number, Context context) {
        String name = "未知";
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{android.provider.ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }
}
