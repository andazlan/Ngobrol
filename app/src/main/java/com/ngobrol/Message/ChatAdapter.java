package com.ngobrol.Message;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ngobrol.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by azlan on 7/7/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private ArrayList<Chat> mChats;
    private FirebaseUser mUser;
    private PrettyTime mPrettyTime;

    public ChatAdapter(ArrayList<Chat> chats, FirebaseUser user){
        this.mChats = chats;
        this.mUser = user;
        this.mPrettyTime = new PrettyTime();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.segment_chat_mine, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.segment_chat_other, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = getChats().get(position);
        holder.name.setText(chat.getName().toString().trim());
        holder.message.setText(chat.getBody().toString().trim());
        String dateFormated = mPrettyTime.format(chat.getDate());
        holder.date.setText(dateFormated);
    }

    @Override
    public int getItemCount() {
        return getChats().size();
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = getChats().get(position);
        if (chat.getUid().equals(mUser.getUid())){
            return 1;
        }
        return 0;
    }

    public ArrayList<Chat> getChats() {
        return mChats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.mChats = chats;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.txt_name) TextView name;
        @Bind(R.id.txt_message) TextView message;
        @Bind(R.id.txt_date) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
