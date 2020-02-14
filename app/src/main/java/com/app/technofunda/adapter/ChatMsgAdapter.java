package com.app.technofunda.adapter;

/**
 * Created by Diu on 10/20/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.model.ChatMsgModel;

import java.util.List;


public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {


    private Context context;


    private List<ChatMsgModel> superHeroes;


    public ChatMsgAdapter(List<ChatMsgModel> superHeroes, Context context) {
        super();

        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatmsg, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ChatMsgModel m = superHeroes.get(position);

        if (m.getName().equals("Admin")) {
          /*  holder.tvId.setTextColor(Color.parseColor("#ff3300"));
            holder.tvId.setGravity(Gravity.LEFT);
            holder.tvMsg.setGravity(Gravity.LEFT);*/
            holder.layout.setVisibility(View.VISIBLE);
            holder.layoutNew.setVisibility(View.GONE);
        } else {
           /* holder.tvId.setGravity(Gravity.RIGHT);
            holder.tvMsg.setGravity(Gravity.RIGHT);
            holder.tvId.setTextColor(Color.parseColor("#000099"));*/
            holder.layout.setVisibility(View.GONE);
            holder.layoutNew.setVisibility(View.VISIBLE);
        }
        holder.tvId.setText(m.getName());
        holder.tvIdNew.setText(m.getName());

        String str = m.getSubject();
        String newSub = str.replaceAll("_", " ");
        holder.tvSub.setText("Subject-  " + newSub);
        String strNew = m.getMessage();

        String newMsg = strNew.replaceAll("_", " ");
        holder.tvMsg.setText(newMsg);
        holder.tvMsgNew.setText(newMsg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.hold, android.R.anim.fade_out);*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvSub, tvMsg, tvIdNew, tvMsgNew;
        public LinearLayout layout, layoutNew;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            layoutNew = itemView.findViewById(R.id.layoutnew);
            tvId = itemView.findViewById(R.id.tvID);
            tvSub = itemView.findViewById(R.id.tvSub);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvIdNew = itemView.findViewById(R.id.tvIDnew);
            tvMsgNew = itemView.findViewById(R.id.tvMsgnew);

        }
    }


}
