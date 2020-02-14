package com.app.technofunda.adapter;

/**
 * Created by Diu on 10/20/2016.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.model.TradingMainResponce;
import com.app.technofunda.util.Config;

import java.util.List;


public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.ViewHolder> {


    SharedPreferences pref;
    private Context context;
    String mobilenumber = "", loginid = "";

    private List<TradingMainResponce> superHeroes;


    public PerformanceAdapter(List<TradingMainResponce> superHeroes, Context context) {
        super();

        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_performance, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return new ViewHolder(v);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TradingMainResponce m = superHeroes.get(position);
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
        if (m.getProfitLoss() >= 0) {
            holder.tvProfitOrLoss.setText("Profit");
            holder.tvPinname.setTextColor(Color.parseColor("#ff669900"));
        } else {
            holder.tvProfitOrLoss.setText("Loss");
            holder.tvPinname.setTextColor(Color.parseColor("#ffcc0000"));
        }
        if (m.getRecommendation().equals("Sell")) {
            holder.tvBuy.setText("S");
        } else {
            holder.tvBuy.setText("B");
        }

        pref = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(context,android.R.style.Theme_Material_Dialog);

                builder2.setTitle("Where do you want to share...?");
                builder2.setPositiveButton("Facebook", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Name -" + loginid + "\n " + "Mobile -" + mobilenumber + "\n " + "Segment -" + m.getSegment() + "\n " + "Recommendation" + m.getRecommendation() + "\n " + "Symbol " + m.getSymbol() + "\n " + "Quantity " + m.getQuantity() + "\n " + "Entry_Prise " + m.getEntryPrice() + "\n " + "Exit_Price" + m.getExitValue() + "\n " + "Profit/Loss" + m.getProfitLoss() + "\n " + "Date " + m.getTime());
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.facebook.orca");
                        try {
                            context.startActivity(sendIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "facebook have not been installed.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("Whatsapp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Name -" + loginid + "\n " + "Mobile -" + mobilenumber + "\n " + "Segment -" + m.getSegment() + "\n " + "Recommendation" + m.getRecommendation() + "\n " + "Symbol " + m.getSymbol() + "\n " + "Quantity " + m.getQuantity() + "\n " + "Entry_Prise " + m.getEntryPrice() + "\n " + "Exit_Price" + m.getExitValue() + "\n " + "Profit/Loss" + m.getProfitLoss() + "\n " + "Date " + m.getTime());
                        //  whatsappIntent.putExtra(Intent.EXTRA_TEXT, m.getSegment());
                        try {
                            context.startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder2.show();


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
