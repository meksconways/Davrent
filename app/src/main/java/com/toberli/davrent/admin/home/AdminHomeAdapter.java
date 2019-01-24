package com.toberli.davrent.admin.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toberli.davrent.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminHomeAdapter extends RecyclerView.Adapter<AdminHomeAdapter.AdminHomeAdapterVH>{

    private final List<AdminHomeModel> data = new ArrayList<>();


    public AdminHomeAdapter(AdminHomeViewModel viewModel, LifecycleOwner lifecycleOwner) {

        viewModel.getModel().observe(lifecycleOwner, newsList -> {
            data.clear();
            if (newsList != null){
                data.addAll(newsList);
            }
            notifyDataSetChanged();
        });
        setHasStableIds(true);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public AdminHomeAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_menu,parent,false);
        return new AdminHomeAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminHomeAdapterVH holder, int position) {
        holder.bind(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class AdminHomeAdapterVH extends RecyclerView.ViewHolder{

        @BindView(R.id.img_menu)
        ImageView photo;
        @BindView(R.id.txt_menu)
        TextView text;

        AdminHomeAdapterVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
        void bind(AdminHomeModel model){
            Glide.with(text.getContext())
                    .load(model.photo)
                    .into(photo);
            text.setText(model.name);
        }
    }



}
