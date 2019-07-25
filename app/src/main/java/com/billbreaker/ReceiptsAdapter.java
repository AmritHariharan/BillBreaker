package com.billbreaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptsViewHolder> {

    private List<Receipt> receipts;
    private OnReceiptClickedListener onReceiptClickedListener;

    ReceiptsAdapter(List<Receipt> receipts, OnReceiptClickedListener onReceiptClickedListener) {
        this.receipts = receipts;
        this.onReceiptClickedListener = onReceiptClickedListener;
    }

    @NonNull
    @Override
    public ReceiptsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_layout, null);
        return new ReceiptsViewHolder(view, onReceiptClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);

        DateFormat timestampFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.US);
        Date date = new Date(receipt.getTimestamp());

        holder.timestamp.setText(timestampFormat.format(date));

        String nameAndPrice;

        if (receipt.getPersonalReceiptItems().size() == 0) {
            nameAndPrice = "Receipt not split";
        } else {
            String name = receipt.getPersonalReceiptItems().get(0).getName();
            double price = receipt.getPersonalReceiptItems().get(0).getPrice();

            nameAndPrice = name + " - $" + price;
        }

        holder.nameAndPrice.setText(nameAndPrice);
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ReceiptsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView timestamp;
        TextView nameAndPrice;
        OnReceiptClickedListener onReceiptClickedListener;

        ReceiptsViewHolder(View view, OnReceiptClickedListener onReceiptClickedListener) {
            super(view);
            timestamp = view.findViewById(R.id.receiptTimestamp);
            nameAndPrice = view.findViewById(R.id.receiptNameAndPrice);

            this.onReceiptClickedListener = onReceiptClickedListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onReceiptClickedListener.onReceiptClicked(getAdapterPosition());
        }
    }

    public interface OnReceiptClickedListener {
        void onReceiptClicked(int position);
    }
}
