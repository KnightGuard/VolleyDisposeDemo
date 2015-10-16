package com.example.vollryhttpdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vollryhttpdemo.R;
import com.example.vollryhttpdemo.VolleyApplication;
import com.example.vollryhttpdemo.fragment.FirstFragment;
import com.example.vollryhttpdemo.model.GroupImage;
import com.example.vollryhttpdemo.model.Item;
import com.example.vollryhttpdemo.utils.Contants;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private List<GroupImage> applications;
    private int rowLayout;
    private FirstFragment mAct;

    public ApplicationAdapter(List<GroupImage> applications, int rowLayout, FirstFragment act) {
        this.applications = applications;
        this.rowLayout = rowLayout;
        this.mAct = act;
    }


    public void clearApplications() {
        int size = this.applications.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                applications.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(List<GroupImage> applications) {
        this.applications.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final GroupImage appInfo = applications.get(i);
        viewHolder.name.setText(appInfo.getTitle());
        VolleyApplication.getInstance().settingImg(Contants.PREFIX+ appInfo.getImg(), viewHolder.image,null);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.animateActivity(appInfo, viewHolder.image,appInfo.getImg());
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.countryName);
            image = (ImageView) itemView.findViewById(R.id.countryImage);
        }

    }
}
