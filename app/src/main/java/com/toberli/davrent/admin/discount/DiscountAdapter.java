package com.toberli.davrent.admin.discount;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.discount.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{

    private final List<Data> data = new ArrayList<>();
    private final DiscountItemClickEvent listener;

    public DiscountAdapter(DiscountViewModel viewModel, LifecycleOwner owner,DiscountItemClickEvent listener) {
        this.listener = listener;
        viewModel.getDataList().observe(owner, dataList -> {
            data.clear();
            if (dataList != null){
                data.addAll(dataList);
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
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount,parent,false);
        return new DiscountViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class DiscountViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_header)
        TextView txtHeader;
        @BindView(R.id.txt_percent)
        TextView txtPercent;
        @BindView(R.id.txt_desc)
        TextView txtDesc;

        Data model;

        DiscountViewHolder(@NonNull View itemView,DiscountItemClickEvent listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                listener.onClickDiscountItem(model);
            });
        }
        void bind(Data model){
            this.model = model;
           txtHeader.setText(model.title);
           txtDesc.setText(model.desc);
           txtPercent.setText(String.format("%%%s", model.percent));
        }
    }

}
