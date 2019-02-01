package com.toberli.davrent.admin.customer.customerdetail.rentproduct;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.product.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RentProductAdapter extends RecyclerView.Adapter<RentProductAdapter.RentProductViewHolder>{

    private final List<Data> data = new ArrayList<>();

    RentProductAdapter(RentProductViewModel viewModel, LifecycleOwner owner) {

        viewModel.getProductData().observe(owner, dataList -> {
            if (dataList != null){
                data.add(dataList);
                notifyDataSetChanged();
            }

        });

    }

    @NonNull
    @Override
    public RentProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rentproduct,parent,false);

        return new RentProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentProductViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void clearAll() {
        data.clear();
    }


    static final class RentProductViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.txt_productname)
        TextView productName;

        RentProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bind(Data data){
            productName.setText(data.title);
        }
    }


}
