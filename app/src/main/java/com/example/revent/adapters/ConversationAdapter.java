package com.example.revent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revent.R;
import com.example.revent.models.ConversationList;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.MemoryData;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

    private  List<ConversationList> conversationLists;
    private final Context context;

    private String user_id;

    String uid = new FireBaseWrapper.Auth().getUid();

    public ConversationAdapter(List<ConversationList> conversationLists, Context context) {
        this.conversationLists = conversationLists;
        this.context = context;
        this.user_id = uid;
    }

    @NonNull
    @Override
    public ConversationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MyViewHolder holder, int position) {
        ConversationList list2 = conversationLists.get(position);

        user_id = uid;

        if(list2.getUid().equals(user_id)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate()+" "+list2.getTime());
        }
        else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return conversationLists.size();
    }

    public void updateConversationList(List<ConversationList> conversationLists) {
        this.conversationLists = conversationLists;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private  TextView oppoTime, myTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}