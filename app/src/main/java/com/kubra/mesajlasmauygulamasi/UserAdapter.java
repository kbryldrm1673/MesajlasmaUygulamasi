package com.kubra.mesajlasmauygulamasi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {
    Context context;
    List<String> userList;
    Activity activity;
    String username;

    public UserAdapter(Context context, List<String> list, Activity activity, String username) {
        this.context = context;
        this.userList = list;
        this.activity = activity;
        this.username = username;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(userList.get(position).toString());
        holder.userAnalayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,ChatActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("othername", userList.get(position).toString());
                activity.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        LinearLayout userAnalayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.userNameChat);
            userAnalayout = itemView.findViewById(R.id.userAnalayout);
        }
    }
}
