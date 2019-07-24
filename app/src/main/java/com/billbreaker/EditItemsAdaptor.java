package com.billbreaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditItemsAdaptor extends RecyclerView.Adapter<EditItemsAdaptor.MyViewHolder> {

    private Context context;
    private List<ReceiptItem> receiptItemsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public EditItemsAdaptor(Context context, List<ReceiptItem> receiptItemsList) {
        this.context = context;
        this.receiptItemsList = receiptItemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReceiptItem receiptItem = receiptItemsList.get(position);
        holder.name.setText(receiptItem.getName());
        String formattedPrice = String.format("$%.2f", receiptItem.getPrice());
        holder.price.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return receiptItemsList.size();
    }

    public void removeItem(int position) {
        receiptItemsList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ReceiptItem item, int position) {
        receiptItemsList.add(position, item);
        notifyItemInserted(position);
    }

    public void editItem(ReceiptItem item, int position) {
        item.setName("SomethingNew");
        item.setPrice(4.5);
        notifyDataSetChanged();
    }

    public void addItem() {
        ReceiptItem e = new ReceiptItem("New Item", 0);
        receiptItemsList.add(e);
        notifyItemInserted(0);
    }
}
