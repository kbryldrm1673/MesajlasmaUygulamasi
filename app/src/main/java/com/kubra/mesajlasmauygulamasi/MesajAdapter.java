package com.kubra.mesajlasmauygulamasi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.ViewHolder> {
    Context context;
    List<MesajModel> list;
    Activity activity;
    String username;
    Boolean state;
    int viewSend=1,viewReceive=2;

    public MesajAdapter(Context context, List<MesajModel> list, Activity activity, String username) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.username = username;
        state=false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==viewSend) {
            view = LayoutInflater.from(context).inflate(R.layout.send_layout, parent, false);
            return new ViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.received_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getText().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            if(state==true){
                textView = (TextView) itemView.findViewById(R.id.sendTexView);
            }else{
                textView = (TextView) itemView.findViewById(R.id.receiveTexView);
            }
        }

    }

    @Override
    public int getItemViewType(int position){
        if(list.get(position).getFrom().equals(username)){
            state = true;
            return viewSend;
        }
        else {
            state=false;
            return viewReceive;
        }
    }
}
