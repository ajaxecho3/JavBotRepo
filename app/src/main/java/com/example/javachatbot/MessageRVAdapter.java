package com.example.javachatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessageModal> messageModalArrayList;
    private Context context;

    public MessageRVAdapter(ArrayList<MessageModal> messageModalArrayList, Context context) {
        this.messageModalArrayList = messageModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
                return new BotViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModal messageModal = messageModalArrayList.get(position);
        switch (messageModal.getSender()){
            case "user":
                ((UserViewHolder)holder).userTv.setText(messageModal.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).botMsgTv.setText(messageModal.getMessage());
                break;
        }
    }
    @Override
    public int getItemViewType(int position){
        switch (messageModalArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return messageModalArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTv;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            userTv = itemView.findViewById(R.id.TVUser);
        }

    }
    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botMsgTv;

        public BotViewHolder(@NonNull View itemView){
            super(itemView);
            botMsgTv = itemView.findViewById(R.id.TVBot);
        }

    }



}
