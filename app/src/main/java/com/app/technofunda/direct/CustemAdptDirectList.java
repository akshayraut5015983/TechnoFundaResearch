package com.app.technofunda.direct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.technofunda.R;
import com.app.technofunda.team.TeamActivitys;

public class CustemAdptDirectList extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> usersList;
    private ArrayList<HashMap<String, String>> items;

    public CustemAdptDirectList(Context context, ArrayList<HashMap<String, String>> usersList) {
        this.context = context;
        this.usersList = usersList;
        this.items = usersList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return usersList.indexOf(getItem(position));
    }

    public class ViewHolder {
        TextView directid, dname,dates,posi;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.direct_list_row_activity, parent, false);

        holder.directid = (TextView) convertView.findViewById(R.id.direct_list);
        holder.dname = (TextView) convertView.findViewById(R.id.text_direct_title);
        holder.dates = (TextView) convertView.findViewById(R.id.text_direct_date);
        holder.posi = (TextView) convertView.findViewById(R.id.text_position);

        convertView.setTag(holder);

        holder.directid.setText(usersList.get(position).get(DirectActivitys.TAG_ID));
        holder.dname.setText(usersList.get(position).get(DirectActivitys.TAG_NAME));
        holder.dates.setText(usersList.get(position).get(DirectActivitys.TAG_DATE));
        holder.posi.setText(usersList.get(position).get(DirectActivitys.TAG_POSITION));

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
                if (items == null)
                    items = usersList;
                if (constraint != null) {
                    if (items != null && items.size() > 0) {
                        for (final HashMap<String, String> g : items) {
                            if (g.get(TeamActivitys.TAG_NAME).toLowerCase()
                                    .contains(constraint.toString())||g.get(TeamActivitys.TAG_ID).toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                usersList = (ArrayList<HashMap<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
