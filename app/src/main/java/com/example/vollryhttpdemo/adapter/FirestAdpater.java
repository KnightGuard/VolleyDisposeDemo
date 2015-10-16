package com.example.vollryhttpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vollryhttpdemo.R;
import com.example.vollryhttpdemo.VolleyApplication;
import com.example.vollryhttpdemo.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 斌斌 on 2015/10/15.
 */
public class FirestAdpater extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Item> info;
    private Context context;
    private int resource;

    public FirestAdpater(Context context, int resource) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        info = new ArrayList<>();
        this.context = context;
        this.resource = resource;
        this.info = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return info == null ? 0 : info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(List<Item> info) {
        this.info.addAll(info);
        notifyDataSetChanged();
    }

    public void setItem(List<Item> info) {
        this.info.clear();
        addItem(info);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            holder.countryName= (TextView) convertView.findViewById( R.id.countryName);
            holder.countryImage= (ImageView) convertView.findViewById( R.id.countryImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.countryName.setText(info.get(position).getTitle());
        VolleyApplication.getInstance().settingImg(info.get(position).getPicUrl(), holder.countryImage,null);
        return convertView;
    }
    class ViewHolder {
        TextView countryName;
        ImageView countryImage;
    }
}
