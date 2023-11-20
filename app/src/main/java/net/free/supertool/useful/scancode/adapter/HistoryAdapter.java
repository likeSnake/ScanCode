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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.free.supertool.useful.scancode.R;
import net.free.supertool.useful.scancode.bean.QRCodeBean;
import net.free.supertool.useful.scancode.db.QRCodeDao;
import net.free.supertool.useful.scancode.ui.EditAct;
import net.free.supertool.useful.scancode.ui.ResultAct;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private QRCodeDao qrCodeDao;
    private ArrayList<QRCodeBean> qrCodeBeans;


    public HistoryAdapter(Context context, ArrayList<QRCodeBean> qrCodeBeans) {
        this.context = context;
        this.qrCodeBeans = qrCodeBeans;

        qrCodeDao = new QRCodeDao(context);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text_format,code_type,code_time,text_context;
        private ImageView ic_icon,ic_scan,ic_detete,ic_pen;

        public ViewHolder(View itemView) {
            super(itemView);
            text_format = itemView.findViewById(R.id.text_format);
            code_type = itemView.findViewById(R.id.code_type);
            code_time = itemView.findViewById(R.id.code_time);
            text_context = itemView.findViewById(R.id.code_content);
            ic_icon = itemView.findViewById(R.id.ic_text);
            ic_scan = itemView.findViewById(R.id.ic_scan);
            ic_detete = itemView.findViewById(R.id.ic_detete);
            ic_pen = itemView.findViewById(R.id.ic_pen);



        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {

        QRCodeBean qrCodeBean = qrCodeBeans.get(position);

        String content = qrCodeBean.getCode_content();
        String format = qrCodeBean.getCode_format();
        Bitmap icon = byteArrayToBitmap(qrCodeBean.getCode_icon());
        String time = convertTimestampToDate(Long.parseLong(qrCodeBean.getCode_time()));
        String type = qrCodeBean.getCode_type();

        holder.text_format.setText(format!=null? format:"");
        holder.code_type.setText(type!=null? type:"");
        holder.code_time.setText(time);
        holder.text_context.setText(content!=null? content:"");
        holder.ic_icon.setImageBitmap(icon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ResultAct.class);
                String JsQR = new Gson().toJson(qrCodeBean);
                MMKV.defaultMMKV().encode("qrCodeBean",JsQR);
                ((Activity)context).startActivity(intent);

            }
        });

        holder.ic_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(qrCodeBean,position);
            }
        });

        holder.ic_detete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeleteDialog(qrCodeBean,position);
            }
        });

        holder.ic_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAct.class);
                String JsQR = new Gson().toJson(qrCodeBean);
                MMKV.defaultMMKV().encode("qrCodeBean",JsQR);
                ((Activity)context).startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return qrCodeBeans.size();
    }


    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public interface ItemOnClickListener{
        void OnClick();
    }


    public void startDialog(QRCodeBean qrCodeBean,int position) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_type);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);

        TextView textView = dialog.findViewById(R.id.text_type);
        textView.setText(qrCodeBean.getCode_type());

        dialog.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.text_suer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                qrCodeBean.setCode_type(textView.getText().toString());
                notifyItemChanged(position);
                qrCodeDao.update(qrCodeBean.getId(),qrCodeBean);
            }
        });
        dialog.show();
    }

    public void startDeleteDialog(QRCodeBean qrCodeBean,int position) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.dialog_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new QRCodeDao(context).deleteQRCodeById(qrCodeBean.getId());
                notifyItemChanged(position);
                qrCodeBeans.remove(position);
                notifyItemRemoved(position);

                //notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
