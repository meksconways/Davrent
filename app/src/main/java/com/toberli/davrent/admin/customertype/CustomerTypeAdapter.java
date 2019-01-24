package com.toberli.davrent.admin.customertype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.customertype.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTypeAdapter extends RecyclerView.Adapter<CustomerTypeAdapter.CustomerTypeViewHolder>{

    private final List<Data> data = new ArrayList<>();
    private final CustomerTypeSelectedListener listener;

    public CustomerTypeAdapter(CustomerTypeViewModel viewModel, LifecycleOwner owner,CustomerTypeSelectedListener listener) {
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
    public CustomerTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_type,parent,false);
        return new CustomerTypeViewHolder(view,listener);
    }
    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerTypeViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static final class CustomerTypeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_header)
        TextView header;
        @BindView(R.id.txt_desc)
        TextView desc;
        @BindView(R.id.txt_discountname)
        TextView discName;
        @BindView(R.id.txt_percent)
        TextView perc;

        Data data;

        CustomerTypeViewHolder(@NonNull View itemView, CustomerTypeSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                listener.setSelectedCustomerType(data);
            });
        }
        void bind(Data data){
            this.data = data;
            header.setText(String.format("Müşteri Tipi: %s", data.title));
            desc.setText(data.description);
            discName.setText(String.format("Sahip Olduğu İndirim: %s",data.discount.title));
            perc.setText(String.format("İndirim Miktarı: %%%s",data.discount.percent));
        }
    }


}
