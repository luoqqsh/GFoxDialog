package com.xing.gfox.rxHttp.li.livedata;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.e("TAG", "rawType = " +rawType.toString());

        if (!(observableType instanceof ParameterizedType)) {
            Log.e("TAG", "rawType = resource must be parameterized" + rawType.toString());
//            throw new IllegalArgumentException("resource must be parameterized");
        }

        return new LiveDataCallAdapter<String>(observableType);
    }
}
