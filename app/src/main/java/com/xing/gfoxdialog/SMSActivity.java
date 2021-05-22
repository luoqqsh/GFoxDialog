package com.xing.gfoxdialog;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.util.U_permissions;
import com.xing.gfoxdialog.BaseApp.BaseActivity;
import com.xing.gfoxdialog.databinding.ActivitySmsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xing.gfox.util.model.SMSBean;

public class SMSActivity extends BaseActivity {
    ActivitySmsBinding binding;
    private SMSAdapter smsAdapter;

    @Override
    protected int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        binding.permissionBtn.setOnClickListener(v -> U_permissions.applyPermission(mActivity, new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                U_Toast.show("權限獲取成功");
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                U_Toast.show("權限獲取失敗");
            }
        }, Manifest.permission.READ_SMS));
        binding.getSMSBtn.setOnClickListener(v -> {
            if (!U_permissions.checkPermission(mActivity, Manifest.permission.READ_SMS)) {
                U_Toast.show("請先獲取權限");
                return;
            }
            smsAdapter = new SMSAdapter(getSmsInPhone());
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            binding.recyclerView.setAdapter(smsAdapter);
        });
    }

    @Override
    public View getLayoutView() {
        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public List<SMSBean> getSmsInPhone() {
        mStatus.setLoading();
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
        final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表

        List<SMSBean> smsBeanList = new ArrayList<>();
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type",};
            Cursor cur = getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信
            // 获取短信中最新的未读短信
            // Cursor cur = getContentResolver().query(uri, projection,
            // "read = ?", new String[]{"0"}, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);
                    SMSBean smsBean = new SMSBean();
                    smsBean.setPhoneNo(strAddress);
                    smsBean.setContent(strbody);
                    smsBean.setLongDate(longDate);
                    smsBean.setType(intType);
                    smsBeanList.add(smsBean);
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
            if (smsBeanList.size() == 0) {
                U_Toast.show("沒有短信");
            }
            mStatus.setFinish();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            mStatus.setFinish();
            U_Toast.show("獲取失敗");
        }
        return smsBeanList;
    }
}
