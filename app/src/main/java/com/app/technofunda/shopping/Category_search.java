package com.app.technofunda.shopping;

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
import com.app.technofunda.addvertise.AdvertiseActivitys;

public class Category_search extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>>  imagesArraynamelist;
    private ArrayList<HashMap<String, String>> items;

    public Category_search(Context context, ArrayList<HashMap<String, String>> usersList) {
        this.context = context;
        this. imagesArraynamelist = imagesArraynamelist;
        this.items = usersList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return  imagesArraynamelist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return  imagesArraynamelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return  imagesArraynamelist.indexOf(getItem(position));
    }

    public class ViewHolder {
        TextView list_element;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.category_row_list, parent, false);

        holder.list_element = (TextView) convertView.findViewById(R.id.text_idno);


        convertView.setTag(holder);

        holder.list_element.setText( imagesArraynamelist.get(position).get(category_list.TAG_ID));

        return convertView;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
                if (items == null)
                    items =  imagesArraynamelist;
                if (constraint != null) {
                    if (items != null && items.size() > 0) {
                        for (final HashMap<String, String> g : items) {
                            if (g.get(AdvertiseActivitys.TAG_NAME).toLowerCase()
                                    .contains(constraint.toString())||g.get(category_list.TAG_ID).toLowerCase()
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
                imagesArraynamelist = (ArrayList<HashMap<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
