package com.toberli.davrent.admin.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.customer.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {


    private final List<Data> data = new ArrayList<>();
    private final CustomerSelectedListener listener;

    public CustomerAdapter(CustomerSelectedListener listener, CustomerViewModel viewModel, LifecycleOwner owner) {
        this.listener = listener;
        viewModel.getData().observe(owner, dataList -> {
            data.clear();
            if (dataList != null){
                data.addAll(dataList);
            }
            notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer,parent,false);
        return new CustomerViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
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

    static final class CustomerViewHolder extends RecyclerView.ViewHolder{

        Data data;
        @BindView(R.id.txt_name)
        TextView name;
        @BindView(R.id.txt_phone)
        TextView phone;
        @BindView(R.id.txt_totalrenting)
        TextView rentingCount;

        CustomerViewHolder(@NonNull View itemView, CustomerSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                listener.customerSelectedListener(data);
            });

        }

        void bind(Data data){
            this.data = data;
            name.setText(data.nameSurname);
            phone.setText(data.phone);
            rentingCount.setText(String.format("Toplam Kiralama: %s",data.rents.size()));
        }
    }

}
