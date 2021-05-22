package com.xing.gfox.hl_study.Hermes;

import android.content.Context;

public class Hermes {
    private Context mContext;
    private TypeCenter typeCenter;
    private ServiceConnectionManager serviceConnectionManager;

    private static final Hermes ourInstance = new Hermes();

    public Hermes() {
        serviceConnectionManager = ServiceConnectionManager.getInstance();
        typeCenter = TypeCenter.getInstance();
    }

    public static Hermes getDefault(){
        return ourInstance;
    }

    public void init(Context context){
        this.mContext =  context.getApplicationContext();
    }

    //----------------------------服务端-------------------------
    //注册观察者
    public void register(Class<UserManager> clazz) {
        typeCenter.register(clazz);
    }




    //----------------------------客户端-------------------------
    public void connect(Context context,
                        Class<HermesService> hermesServiceClass) {
        connectApp(context, null, hermesServiceClass);
    }

    private void connectApp(Context context, String packageName, Class<HermesService> hermesServiceClass) {
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, hermesServiceClass);
    }
}
