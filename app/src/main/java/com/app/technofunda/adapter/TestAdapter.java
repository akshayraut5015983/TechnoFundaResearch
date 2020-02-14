package com.app.technofunda.adapter;

/**
 * Created by Diu on 10/20/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.technofunda.R;
import com.app.technofunda.model.RequestPojo;

import java.util.List;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {


    private Context context;


    private List<RequestPojo> superHeroes;


    public TestAdapter(List<RequestPojo> superHeroes, Context context) {
        super();

        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final RequestPojo m = superHeroes.get(position);
        holder.textViewId.setText("Time : " + m.getBanerTitle());

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
        public TextView textViewId;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewId =   itemView.findViewById(R.id.tv);

        }
    }


}
