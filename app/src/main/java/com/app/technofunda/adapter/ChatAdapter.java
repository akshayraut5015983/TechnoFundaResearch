package com.app.technofunda.adapter;

/**
 * Created by Diu on 10/20/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.technofunda.ChatDetailsActivity;
import com.app.technofunda.R;
import com.app.technofunda.model.ChatModel;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private Context context;


    private List<ChatModel> superHeroes;


    public ChatAdapter(List<ChatModel> superHeroes, Context context) {
        super();

        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ChatModel m = superHeroes.get(position);
        holder.tvId.setText(m.getTicketID());
        String str = m.getSubject();
        String newstr = str.replaceAll("_", " ");
        holder.tvSub.setText(newstr);
        holder.tvDate.setText(m.getCreatedDate());
        holder.tvStatus.setText(m.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tiketID", String.valueOf(holder.tvId.getText().toString()));
                intent.putExtra("subject", String.valueOf(holder.tvSub.getText().toString()));
                Log.d("adapterId", holder.tvId.getText().toString());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
            }
        });
    }


    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvSub, tvDate, tvStatus;


        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvID);
            tvSub = itemView.findViewById(R.id.tvSub);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }


}
