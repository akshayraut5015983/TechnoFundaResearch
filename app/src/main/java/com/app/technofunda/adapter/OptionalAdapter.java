package com.app.technofunda.adapter;

/**
 * Created by Diu on 10/20/2016.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.model.EquityResponce;

import java.util.List;


public class OptionalAdapter extends RecyclerView.Adapter<OptionalAdapter.ViewHolder> {


    private Context context;


    private List<EquityResponce> superHeroes;


    public OptionalAdapter(List<EquityResponce> superHeroes, Context context) {
        super();

        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_optional, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return new ViewHolder(v);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final EquityResponce m = superHeroes.get(position);
        holder.tvSegment.setText(m.getSegment());
        holder.tvRecommendation.setText(m.getRecommendation());
        holder.tvSymbol.setText(m.getSymbol());
        holder.tvEntryPrice.setText(String.valueOf(m.getEntryPrice()));
        holder.tvSl.setText("SL - " + String.valueOf(m.getSL()));
        holder.tvTG1.setText("TG 1 - " + String.valueOf(m.getTG1()));
        holder.tvTG2.setText("TG 2 - " + String.valueOf(m.getTG2()));
        holder.tvQuantity.setText("Quantity - " + String.valueOf(m.getQuantity()));
        holder.tvExit_value.setText(String.valueOf(m.getExitValue()));
        holder.tvPinname.setText(String.valueOf(m.getProfitLoss()));
        holder.tvTime.setText(m.getTime());

        if (String.valueOf(m.getRecommendation()).equals("Sell")) {
            holder.tvBuy.setText("Sell");
        } else {
            holder.tvBuy.setText("Buy");
        }

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
        public TextView tvProfitOrLoss, tvSegment, tvRecommendation, tvSymbol, tvEntryPrice, tvSl, tvTG1, tvTG2, tvQuantity, tvExit_value, tvPinname, tvTime, tvBuy;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProfitOrLoss = itemView.findViewById(R.id.tvProfitOrLoss);
            tvSegment = itemView.findViewById(R.id.tvSegment);
            tvRecommendation = itemView.findViewById(R.id.tvRecommendation);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvEntryPrice = itemView.findViewById(R.id.tvEntryPrice);
            tvSl = itemView.findViewById(R.id.tvSl);
            tvTG1 = itemView.findViewById(R.id.tvTG1);
            tvTG2 = itemView.findViewById(R.id.tvTG2);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExit_value = itemView.findViewById(R.id.tvExit_value);
            tvPinname = itemView.findViewById(R.id.tvPinname);
            tvTime = itemView.findViewById(R.id.tvTime);

            tvBuy = itemView.findViewById(R.id.tvBuy);
        }
    }


}
