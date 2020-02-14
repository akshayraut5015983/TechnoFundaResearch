package com.app.technofunda.newsupdate;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;

public class SingleMenuItemActivity3 extends BaseAdapter
{
    public String TEST;
    public Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private HashMap<String, String> song;
    private ViewHolderVideo holder;
    Animation animBlink;
    public SingleMenuItemActivity3(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data=d;
    }

    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        holder = new ViewHolderVideo();
        View v;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_row_newsupdates,null);
            v = convertView;

        } else
        {
            v = convertView;
        }

        final TextView newsid = (TextView) v.findViewById(R.id.text_news_idno);
        holder.newsdate = (TextView) v.findViewById(R.id.text_news_date);
        holder.newstitle = (TextView) v.findViewById(R.id.text_news_title);
        holder.newsbtnview = (Button) v.findViewById(R.id.but_news_view);

        ReadNewsUpdate(position);

        animBlink = AnimationUtils.loadAnimation(activity, R.anim.blink);

        newsid.setText(song.get(NewsUpdateActivity.KEY_NEWSID));
        holder.newsdate.setText(song.get(NewsUpdateActivity.KEY_NEWSDATE));
        holder.newstitle.setText(song.get(NewsUpdateActivity.KEY_NEWSTITLE));
        holder.newstitle.setVisibility(View.VISIBLE);
        holder.newstitle.startAnimation(animBlink);

        newsid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TEST = newsid.getText().toString();
                Log.e("Text Id", TEST);
            }
        });

        holder.newsbtnview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TEST = newsid.getText().toString();
                Log.e("Adv Get No:", TEST);
                Intent inten = new Intent(activity, NewsUpdateDetialsActivity.class);
                inten.putExtra("STRING_I_NEED", TEST);
                activity.startActivity(inten);
            }
        });

        return convertView;
    }

    private void ReadNewsUpdate(int position)
    {
        song = new HashMap<String, String>();
        song = data.get(position);
        Log.e("User Id", String.valueOf(song));
    }
}

class ViewHolderVideo
{
    TextView newsdate, newstitle;
    Button newsbtnview;

}
