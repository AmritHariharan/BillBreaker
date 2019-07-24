package com.billbreaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OverviewAdaptor extends RecyclerView.Adapter<OverviewAdaptor.MyViewHolder> {

    private Context context;
    private List<PersonalReceiptItem> peopleList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.debt);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public OverviewAdaptor(Context context, List<PersonalReceiptItem> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_rows, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PersonalReceiptItem personalReceiptItem = peopleList.get(position);
        holder.name.setText(personalReceiptItem.getName());
        String formattedPrice = String.format("$%.2f", personalReceiptItem.getPrice());
        holder.price.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public void removeItem(int position) {
        peopleList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(PersonalReceiptItem item, int position) {
        peopleList.add(position, item);
        notifyItemInserted(position);
    }

    public void editItem(PersonalReceiptItem item, int position) {
        item.setName("SomethingNew");
        item.setPrice(4.5);
        notifyDataSetChanged();
    }

    public void addItem() {
        PersonalReceiptItem e = new PersonalReceiptItem("New Item", 0);
        peopleList.add(e);
        notifyItemInserted(0);
    }
}
