package com.toberli.davrent.admin.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toberli.davrent.R;
import com.toberli.davrent.admin.staff.model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminStaffAdapter extends RecyclerView.Adapter<AdminStaffAdapter.AdminStaffViewHolder>{

    private final StaffSelectedListener listener;
    private final List<Data> data = new ArrayList<>();

    AdminStaffAdapter(AdminStaffViewModel viewModel, LifecycleOwner owner, StaffSelectedListener listener) {
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

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    @NonNull
    @Override
    public AdminStaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staffs,parent,false);
        return new AdminStaffViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminStaffViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


     static final class AdminStaffViewHolder extends RecyclerView.ViewHolder{

        Data data;
        @BindView(R.id.txt_name)
        TextView name;
        @BindView(R.id.txt_phone)
        TextView phone;
        @BindView(R.id.txt_totalrenting)
        TextView totalRenting;
        @BindView(R.id.txt_totaladdedcustomer)
        TextView totalAddedCustomer;

        AdminStaffViewHolder(@NonNull View itemView, StaffSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> listener.setSelectedStaff(data));
        }

        void bind(Data data){
            this.data = data;
            name.setText(data.nameSurname);
            phone.setText(data.phone);
            totalRenting.setText(String.format("Toplam Kiralama: %s",data.totalRents));
            totalAddedCustomer.setText(String.format("Toplam Eklenen Müşteri: %s",data.totalAddedCustomer));

        }
    }

}
