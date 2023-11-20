package net.free.supertool.useful.scancode.fragment;


import static net.free.supertool.useful.scancode.util.MyUtil.bitmapToByteArray;
import static net.free.supertool.useful.scancode.util.MyUtil.decodeBitmapFromResource;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.adapter.CreateAdapter;
import net.free.supertool.useful.scancode.bean.CreateBean;
import net.free.supertool.useful.scancode.ui.MainAct;

import java.util.ArrayList;


public class CreateFragment extends Fragment implements View.OnClickListener{
    private Activity activity;

    private ArrayList<CreateBean> createBeans = new ArrayList<>();
    private CreateAdapter createAdapter;
    private RecyclerView recyclerView;

    public CreateFragment(Activity activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor, container, false);
        initView(rootView,savedInstanceState);
        initDate();
        initListener();
        initTimer();
        return rootView;
    }

    public void initView(View rootView,Bundle savedInstanceState){
        recyclerView = rootView.findViewById(R.id.create_recyclerView);

    }

    public void initDate(){

        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_contacts)),"contacts"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_mail)),"e-mail"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_phone)),"phone"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_location)),"location"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"maxicode"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_note)),"sms"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_text)),"txt"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_link)),"url"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"data_matrix"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"aztec"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"upc_e"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"upc_a"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"pdf_417"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"itf"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"ean_8"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"ean_13"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"code_39"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"code_93"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"code_128"));
        createBeans.add(new CreateBean(bitmapToByteArray(decodeBitmapFromResource( activity,R.drawable.ic_code)),"codabar"));

        startAdapter(createBeans);
    }

    public void initListener(){}
    public void initTimer(){

    }



    @Override
    public void onClick(View v) {

    }

    public void startAdapter(ArrayList<CreateBean> createBeans){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        createAdapter = new CreateAdapter(activity,createBeans);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createAdapter);
    }
}