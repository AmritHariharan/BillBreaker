package com.billbreaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssignItemsAdaptor extends RecyclerView.Adapter<AssignItemsAdaptor.AssignItemsViewHolder> {

    private Context context;
    private List<ReceiptItem> receiptItemsList;

    @NonNull
    @Override
    public AssignItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assign_items_list_row, parent, false);

        return new AssignItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignItemsViewHolder holder, int position) {
        ReceiptItem receiptItem = receiptItemsList.get(position);
        holder.name.setText(receiptItem.getName());
        String formattedPrice = String.format("$%.2f", receiptItem.getPrice());
        holder.price.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return receiptItemsList.size();
    }

    public static class AssignItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;

        public AssignItemsViewHolder(@NonNull View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.price = view.findViewById(R.id.price);
        }
    }

    public AssignItemsAdaptor(Context context, List<ReceiptItem> receiptItemsList) {
        this.context = context;
        this.receiptItemsList = receiptItemsList;
    }
}
