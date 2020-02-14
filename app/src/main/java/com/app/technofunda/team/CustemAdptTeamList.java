package com.app.technofunda.team;

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

public class CustemAdptTeamList extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> usersList;
    private ArrayList<HashMap<String, String>> items;

    public CustemAdptTeamList(Context context, ArrayList<HashMap<String, String>> usersList) {
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
        TextView teamid, teamtitle,teampv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.team_list_row_activity, parent, false);

         holder.teamid = (TextView) convertView.findViewById(R.id.team_id_list);
         holder.teamtitle = (TextView) convertView.findViewById(R.id.text_teams_title);
         holder.teampv = (TextView) convertView.findViewById(R.id.text_teams_count);
         convertView.setTag(holder);

        holder.teamid.setText(usersList.get(position).get(TeamActivitys.TAG_ID));
        holder.teamtitle.setText(usersList.get(position).get(TeamActivitys.TAG_NAME));
        holder.teampv.setText(usersList.get(position).get(TeamActivitys.TAG_PV));

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
