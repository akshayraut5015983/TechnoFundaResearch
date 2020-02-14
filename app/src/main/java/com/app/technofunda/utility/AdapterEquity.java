package com.app.technofunda.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.technofunda.R;

import java.util.List;

public class AdapterEquity extends RecyclerView.Adapter<AdapterEquity.ViewHolder> {

    Context context;
    private List<Company> companyList;

    public AdapterEquity(Context context, List<Company> companyList ) {
        this.context = context;
        this.companyList = companyList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tab_one, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder myViewHolder = new ViewHolder(v);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.textView.setText(seedNames.get(position).toString());
        // holder.itemView.setText()
      //  final Company c = Heroes.get(position);
       Company company = companyList.get(position);
        holder.txtcompname.setText(company.getComapnyname());
        holder.txtprice.setText(String.valueOf(company.getPrice()));
        holder.txtentryprice.setText(String.valueOf(company.getEntryprice()));
        holder.txtstoploss.setText(String.valueOf(company.getStoploss()));
        holder.txtCMP.setText(String.valueOf(company.getCMP()));

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtcompname, txtprice, txtentryprice, txtstoploss, txtCMP ;

      public ViewHolder(View itemView) {
          super(itemView);

          txtcompname = (TextView) itemView.findViewById(R.id.txtcompname);
          txtprice = (TextView) itemView.findViewById(R.id.txtprice);
          txtentryprice = (TextView) itemView.findViewById(R.id.txtentryprice);
          txtstoploss = (TextView) itemView.findViewById(R.id.txtstoploss);
          txtCMP = (TextView) itemView.findViewById(R.id.txtCMP);

      }
  }
}
