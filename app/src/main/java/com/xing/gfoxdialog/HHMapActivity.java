package com.xing.gfoxdialog;//package x.com.dialogdialog;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.example.xxing.amap.AmapController;
//import com.example.xxing.amap.AmapLocation;
//import com.example.xxing.amap.AmapSearch;
//import com.xing.hhmap.HHLocation;
//import com.xing.hhmap.HHSearch;
//import com.xing.hhmap.HHmap;
//import com.xing.hhmap.bean.LatLngBean;
//import com.xing.hhmap.bean.LineBean;
//import com.xing.hhmap.bean.LineType;
//import com.xing.hhmap.bean.LocationBean;
//import com.xing.hhmap.bean.LocationFrom;
//import com.xing.hhmap.bean.LocationType;
//import com.xing.hhmap.listener.OnCameraChangeListener;
//import com.xing.hhmap.listener.OnLocationListener;
//import com.xing.hhmap.listener.OnMapReadyListener;
//
//import x.com.dialogdialog.BaseApp.BaseActivity;
//import x.com.dialogdialog.BaseApp.Config;
//import x.com.log.ViseLog;
//
//
//public class HHMapActivity extends BaseActivity {
//    private HHmap hHmap;
//    private HHLocation hhLocation;
//    private Object marker;
//
//    @Override
//    public void initUI(Bundle savedInstanceState) {
//        LinearLayout ll_mapview = findViewById(R.id.ll_mapview);
//        hHmap = new HHmap(this, new AmapController());
//        hHmap.addMapView(ll_mapview, savedInstanceState, new OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(LocationBean latLngs) {
//                if (latLngs.getFrom() == LocationFrom.MOVEMAP_FINISH) {
////                    ViseLog.d(latLngs);
//                }
//            }
//        }, new OnMapReadyListener() {
//            @Override
//            public void onReady() {
//                hHmap.moveToPosition(new LatLngBean(Config.testLat1, Config.testLng1), new LatLngBean(Config.testLat2, Config.testLng2), 100);
//                hHmap.addMarker(new LineBean().setMarker(new LatLngBean(Config.testLat1, Config.testLng1), R.mipmap.map_icon_p));
//                hHmap.addMarker(new LineBean().setMarker(new LatLngBean(Config.testLat2, Config.testLng2), R.mipmap.map_icon_p));
//            }
//        });
//
//
////        hHmap.searchRoute(RouteSearchType.CAR, new LatLngBean(Config.testLat1, Config.testLng1), new LatLngBean(Config.testLat2, Config.testLng2), Config.city, new OnLocationListener() {
////            @Override
////            public void onCurrentLocation(LocationBean latLngs) {
////                ViseLog.d(latLngs);
////                LineBean lineBean = new LineBean().setLine(latLngs.getPois(), 10);
////                lineBean.setColorList(latLngs.getColorList());
////                hHmap.addMarker(lineBean);
////            }
////
////            @Override
////            public void onError(int code, String errorInfo) {
////
////            }
////        });
//        hhLocation = new HHLocation(getApplicationContext(), new AmapLocation());
//        hhLocation.setOnLocationListener(new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs, boolean isFirstLocation) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//
//            }
//        });
//        hhLocation.startLocation();
////        hHmap.addGoogleMap(false);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_mapview;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        hHmap.onDestroy();
//        hhLocation.stopLocation();
//        hhLocation.releaseLocationListener();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        hHmap.onResume();
//        hhLocation.startLocation();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        hHmap.onPause();
//        hhLocation.stopLocation();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        hHmap.onSaveInstanceState(outState);
//    }
//
//    public void add(View view) {
//        LineBean markerBean = new LineBean();
//        markerBean.setMarker(new LatLngBean(Config.testLat1, Config.testLng1), R.mipmap.map_icon_p);
//        marker = hHmap.addMarker(markerBean);
//    }
//
//    public void set(View view) {
//        LineBean markerBean = new LineBean();
//        markerBean.setMarker(new LatLngBean(Config.testLat1, Config.testLng1), R.mipmap.fx_gengduo);
//        hHmap.setMarker(marker, markerBean);
//    }
//
//    public void remove(View view) {
//        ViseLog.d(hHmap.removeMarker(marker, LineType.MARKER));
//    }
//
//    public void search(View view) {
//        HHSearch hhSearch = new HHSearch(this, new AmapSearch(), new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs, boolean isFirstLocation) {
//                ViseLog.d(latLngs);
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//                ViseLog.d(code);
//
//            }
//        });
//        hhSearch.address2Point("大同路", Config.city);
//        hhSearch.point2address(Config.testLat1, Config.testLng1, 1000, LocationType.AMAP);
//
////        hhSearch.showTips("火车站", Config.city, true, new OnLocationListener() {
////            @Override
////            public void onCurrentLocation(LocationBean latLngs) {
////                ViseLog.d(latLngs);
////            }
////
////            @Override
////            public void onError(int code, String errorInfo) {
////            }
////        });
////        hhSearch.poiSearch("火车站", "", Config.city, new OnLocationListener() {
////            @Override
////            public void onCurrentLocation(LocationBean latLngs) {
////                ViseLog.d(latLngs);
////            }
////
////            @Override
////            public void onError(int code, String errorInfo) {
////            }
////        });
//    }
//}
