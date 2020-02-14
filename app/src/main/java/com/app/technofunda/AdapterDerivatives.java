package com.app.technofunda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.technofunda.utility.Company1;

import java.util.List;

public class AdapterDerivatives extends RecyclerView.Adapter<AdapterDerivatives.ViewHolder>{

    Context context;
    private List<Company1> companyList1;

    public AdapterDerivatives(Context context,List<Company1> companyList1 ) {
        this.context = context;
        this.companyList1 = companyList1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tab_two, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder myViewHolder = new ViewHolder (v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Company1 company = companyList1.get(position);
        holder.txtcompname.setText(company.getComapnyname());
        holder.txtprice.setText(String.valueOf(company.getPrice()));
        holder.txtentryprice.setText(String.valueOf(company.getEntryprice()));
        holder.txtstoploss.setText(String.valueOf(company.getStoploss()));
        holder.txtCMP.setText(String.valueOf(company.getCMP()));
    }

    @Override
    public int getItemCount() {
        return companyList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

