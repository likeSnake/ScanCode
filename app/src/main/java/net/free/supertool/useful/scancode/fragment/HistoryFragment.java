package net.free.supertool.useful.scancode.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.adapter.HistoryAdapter;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;
import net.free.supertool.useful.scancode.util.MyUtil;

import java.util.ArrayList;


public class HistoryFragment extends Fragment implements View.OnClickListener{
    private Activity activity;
    private ArrayList<QRCodeBean> qrCodeBeans;
    private HistoryAdapter adapter;
    private RecyclerView history_recyclerView;
    private Dialog dialog;
    private Boolean isFirst = true;

    public HistoryFragment(Activity activity){
        this.activity = activity;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // Fragment 不可见

        } else {
            // Fragment 可见
            System.out.println("---**---:"+"onHiddenChanged刷新列表");
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("---**---:"+"onResume刷新列表");
        if (!isFirst) {
            initDate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initView(rootView);
        initLoadingDialog();
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){

        history_recyclerView = rootView.findViewById(R.id.history_recyclerView);
    }

    public void initDate(){
     //   dialog.show();
        new QRCodeDao(activity).findAllAsync(new QRCodeDao.QueryCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onQueryComplete(ArrayList<QRCodeBean> findQrCodeBeans) {
                qrCodeBeans =findQrCodeBeans;

                activity.runOnUiThread(()->{
                    if (!isFirst) {
                        if (adapter!=null) {
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        isFirst = false;
                    }
                 //   dialog.cancel();
                    if (qrCodeBeans!=null&&!qrCodeBeans.isEmpty()){
                        startAdapter(qrCodeBeans);
                    }
                });

            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void initListener(){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

    }
    public void startAdapter(ArrayList<QRCodeBean> qrCodeBeans){
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,true);
        adapter = new HistoryAdapter(activity, qrCodeBeans);
        manager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        history_recyclerView.setLayoutManager(manager);
        history_recyclerView.setAdapter(adapter);
    }

    public void initLoadingDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);

    }
}