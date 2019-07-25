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
    private List<ReceiptItemBreakdown> itemBreakdownsList;

    @NonNull
    @Override
    public AssignItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assign_items_list_row, parent, false);

        return new AssignItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignItemsViewHolder holder, int position) {
        ReceiptItemBreakdown receiptItem = itemBreakdownsList.get(position);
        holder.name.setText(receiptItem.getName());
        String formattedPrice = String.format("$%.2f", receiptItem.getPrice());
        holder.price.setText(formattedPrice);
        holder.people.setText(receiptItem.getPeopleString());
    }

    @Override
    public int getItemCount() {
        return itemBreakdownsList.size();
    }

    public static class AssignItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, people;

        public AssignItemsViewHolder(@NonNull View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.price = view.findViewById(R.id.price);
            this.people = view.findViewById(R.id.people_list_text);
        }
    }

    public AssignItemsAdaptor(Context context, List<ReceiptItemBreakdown> itemBreakdownsList) {
        this.context = context;
        this.itemBreakdownsList = itemBreakdownsList;
    }
}
