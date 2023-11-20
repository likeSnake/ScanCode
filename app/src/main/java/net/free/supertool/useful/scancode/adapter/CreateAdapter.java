package net.free.supertool.useful.scancode.adapter;

import static net.free.supertool.useful.scancode.util.MyUtil.byteArrayToBitmap;
import static net.free.supertool.useful.scancode.util.MyUtil.convertTimestampToDate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.CreateBean;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;
import net.free.supertool.useful.scancode.ui.CreateAct;


import java.util.ArrayList;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private QRCodeDao qrCodeDao;
    private ArrayList<CreateBean> createBeans;


    public CreateAdapter(Context context, ArrayList<CreateBean> createBeans) {
        this.context = context;
        this.createBeans = createBeans;

        qrCodeDao = new QRCodeDao(context);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text_format;
        private ImageView ic_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            text_format = itemView.findViewById(R.id.text_type);
            ic_icon = itemView.findViewById(R.id.ic_icon);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_item,parent,false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {

        CreateBean createBean = createBeans.get(position);
        Bitmap bitmap = byteArrayToBitmap(createBean.getBitmap());
        String content = createBean.getContent();

        holder.text_format.setText(content);
        holder.ic_icon.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateAct.class);
                String JsQR = new Gson().toJson(createBean);
                MMKV.defaultMMKV().encode("createBean",JsQR);
                ((Activity)context).startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return createBeans.size();
    }


}
