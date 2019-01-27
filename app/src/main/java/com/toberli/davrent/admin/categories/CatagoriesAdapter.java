package com.toberli.davrent.admin.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.categories.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CatagoriesAdapter extends RecyclerView.Adapter<CatagoriesAdapter.CategoriesViewHolder>{


    private final List<Data> data = new ArrayList<>();
    private final CategoriesSelectedListener listener;

    public CatagoriesAdapter(AdminCategoriesViewModel viewModel, LifecycleOwner owner, CategoriesSelectedListener listener) {
        this.listener = listener;
        viewModel.getData().observe(owner, dataList -> {
            data.clear();
            if (dataList != null) {
                data.addAll(dataList);
            }
            notifyDataSetChanged();

        });
        setHasStableIds(true);

    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories,parent,false);
        return new CategoriesViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    static final class CategoriesViewHolder extends RecyclerView.ViewHolder{


        Data data;
        @BindView(R.id.txt_catname)
        TextView catName;
        @BindView(R.id.txt_catdesc)
        TextView catDesc;


        CategoriesViewHolder(@NonNull View itemView, CategoriesSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                listener.setSelectedCategory(data);
            });
        }

        void bind(Data data){
            this.data = data;
            catName.setText(data.name);
            catDesc.setText(data.description);
        }
    }

}
