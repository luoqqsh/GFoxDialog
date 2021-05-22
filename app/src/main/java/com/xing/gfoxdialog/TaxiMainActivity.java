package com.xing.gfoxdialog;//package x.com.dialogdialog;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import com.example.xxing.amap.AmapController;
//import com.example.xxing.amap.AmapLocation;
//import com.example.xxing.amap.AmapSearch;
//import com.xing.hhmap.HHLocation;
//import com.xing.hhmap.HHSearch;
//import com.xing.hhmap.HHmap;
//import com.xing.hhmap.bean.LocationBean;
//import com.xing.hhmap.bean.LocationFrom;
//import com.xing.hhmap.bean.LocationType;
//import com.xing.hhmap.listener.OnCameraChangeListener;
//import com.xing.hhmap.listener.OnLocationListener;
//
//import x.com.base.activity.HLBaseActivity;
//import x.com.log.ViseLog;
//
//public class TaxiMainActivity extends HLBaseActivity {
//    private HHmap hHmap;
//    private HHLocation hhLocation;
//    private HHSearch hhSearch;
//    private TextView startAddressText;
//
//    @Override
//    public void initUI(Bundle savedInstanceState) {
//        mTitle.setTitleBg(R.mipmap.icon_image_select);
//        mTitle.setTitle2Text("厦门市");
//        startAddressText = findViewById(R.id.startAddressText);
//        FrameLayout taxiMapLayout = findViewById(R.id.taxiMapLayout);
//        View callTaxiBtn = findViewById(R.id.callTaxiBtn);
//        callTaxiBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hhLocation.startLocation();
//            }
//        });
//        hHmap = new HHmap(mActivity, new AmapController());
//        hHmap.addMapView(taxiMapLayout, savedInstanceState, new OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(LocationBean latLngs) {
//                if (latLngs.getFrom() == LocationFrom.MOVEMAP_FINISH) {
//                    hhSearch.point2address(latLngs.getLat(), latLngs.getLng(), 100, LocationType.AMAP);
//                }
//            }
//        });
//        hhLocation = new HHLocation(mActivity, new AmapLocation());
//        hhLocation.setOnLocationListener(new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs, boolean isFirstLocation) {
//                if (isFirstLocation) {
//                    hHmap.moveToPosition(latLngs.getLatlngBean(), 16);
//                    startAddressText.setText(latLngs.getAddress());
////                    ViseLog.d(latLngs);
//                }
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//                ViseLog.d(errorInfo);
//            }
//        });
//        hhLocation.startLocation();
//        hhSearch = new HHSearch(mActivity, new AmapSearch(), new OnLocationListener() {
//            @Override
//            public void onCurrentLocation(LocationBean latLngs, boolean isFirstLocation) {
//            }
//
//            @Override
//            public void onError(int code, String errorInfo) {
//
//            }
//        });
//
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_main_taxi;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        hHmap.onDestroy();
//        hhLocation.releaseLocationListener();
//    }
//}
