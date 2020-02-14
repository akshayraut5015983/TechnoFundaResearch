package com.app.technofunda.shopping;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;

/**
 * Created by comp3 on 03-04-2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView countryName;
    public ImageView countryPhoto;
    int position;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
        //countryPhoto.setAlpha(0.5);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = "+getPosition(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        position=getPosition();
        if (position==0)
        {
            Intent intent = new Intent(view.getContext(), grid_categery.class);
            view.getContext().startActivity(intent);

        }
        else if (position==1)
        {
            Intent intent = new Intent(view.getContext(), grid_categery.class);
            view.getContext().startActivity(intent);

        }
        else if (position==2)
        {
            Intent intent = new Intent(view.getContext(), grid_categery.class);
            view.getContext().startActivity(intent);

        }
    }
}