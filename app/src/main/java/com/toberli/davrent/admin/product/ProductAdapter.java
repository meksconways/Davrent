package com.toberli.davrent.admin.product;

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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{


    private final ProductSelectedListener listener;
    private final List<Data> data = new ArrayList<>();

    public ProductAdapter(ProductViewModel viewModel, LifecycleOwner owner, ProductSelectedListener listener) {
        this.listener = listener;
        viewModel.getData().observe(owner, dataList -> {
            data.clear();
            if (dataList != null){
                data.addAll(dataList);
            }
            notifyDataSetChanged();
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
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

    static final class ProductViewHolder extends RecyclerView.ViewHolder{

        Data data;
        @BindView(R.id.txt_productname)
        TextView productName;
        @BindView(R.id.txt_productdesc)
        TextView productDesc;
        @BindView(R.id.txt_productprice)
        TextView prPrice;
        @BindView(R.id.txt_productcat)
        TextView prCat;

        ProductViewHolder(@NonNull View itemView, ProductSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                listener.setSelectedProduct(data);
            });
        }
        void bind(Data data){
            this.data = data;
            productName.setText(data.title);
            productDesc.setText(data.description);
            prPrice.setText(String.format("Fiyat: %s₺",data.price));
            prCat.setText(String.format("Kategori: %s",data.category.name));
        }
    }

}
