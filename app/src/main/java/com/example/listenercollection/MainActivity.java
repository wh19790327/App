package com.example.listenercollection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.darkhorse.baseframe.utils.SPUtils;
import com.example.listenercollection.bean.MessageWrap;
import com.example.listenercollection.bean.PayBean;
import com.example.listenercollection.constants.GlobalVars;
import com.example.listenercollection.retrofit.API;
import com.example.listenercollection.retrofit.HttpObserver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RcvPayAdapter mAdapter;
    private TextView tv_date;
    private TextView tv_wsum;
    private TextView tv_zsum;
    private Button btn_scan;
    private Button btn_unbind;
    private Button btn_time;
    private TimePickerView pvTime;
    private String time = "";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        time = simpleDateFormat.format(new Date());

        pvTime = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                time = simpleDateFormat.format(date);
                update();
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .build();
        tv_date = findViewById(R.id.tv_date);
        tv_wsum = findViewById(R.id.tv_wsum);
        tv_zsum = findViewById(R.id.tv_zsum);
        tv_date.setText("日期：" + time);
        btn_time = findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter = new RcvPayAdapter();
        mRecyclerView.setAdapter(mAdapter);

        btn_unbind = findViewById(R.id.btn_unbind);
        btn_unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("是否解除绑定？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                API.getInstance().getService().unbindDevices("disbinddevice", GlobalVars.getBindId(), GlobalVars.getDeviceId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new HttpObserver() {
                                            @Override
                                            public void onSuccess(String msg) {
                                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                GlobalVars.setBindId("");
                                                SPUtils.INSTANCE.put("list", "");
                                                btn_scan.setVisibility(View.VISIBLE);
                                                btn_unbind.setVisibility(View.GONE);
                                                mRecyclerView.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onFailure(String msg) {
                                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 123);
                }
            }
        });

        if (GlobalVars.getBindId().isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            btn_unbind.setVisibility(View.GONE);
            btn_scan.setVisibility(View.VISIBLE);
        }

        if (!isNotificationListenerEnabled(this)) {
            openNotificationListenSettings();
        }

        update();
    }

    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        ArrayList<PayBean> payBeans = new ArrayList<>();
        String json = SPUtils.INSTANCE.getString("list", "");
        float wsum = 0;
        float zsum = 0;
        if (!json.isEmpty()) {
            ArrayList<PayBean> arrayList = new Gson().fromJson(json, new TypeToken<ArrayList<PayBean>>() {
            }.getType());
            for (PayBean payBean : arrayList) {
                if (payBean.getTime().contains(time)) {
                    payBeans.add(payBean);
                    String num = payBean.getPay().substring(0, payBean.getPay().length() - 1);
                    if (payBean.getType().equals("微信")) {
                        wsum += Float.parseFloat(num);
                    } else {
                        zsum += Float.parseFloat(num);
                    }
                }
            }
            Collections.reverse(payBeans);
        }

        DecimalFormat decimalFormat =new DecimalFormat("#.##");
        tv_date.setText("日期：" + time);
        tv_wsum.setText(decimalFormat.format(wsum) + "元");
        tv_zsum.setText(decimalFormat.format(zsum) + "元");
        mAdapter.setNewData(payBeans);
    }

    // 判断是否打开了通知监听权限
    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                final String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                if (result.contains("ZXZNCV01")) {
                    final String userid = result.replace("ZXZNCV01", "");
                    API.getInstance().getService().bindDevices("binddevice", userid, GlobalVars.getDeviceId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new HttpObserver() {
                                @Override
                                public void onSuccess(String msg) {
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    GlobalVars.setBindId(userid);
                                    btn_scan.setVisibility(View.GONE);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    btn_unbind.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(String msg) {
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "信息错误，请重新操作", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        update();
    }
}
