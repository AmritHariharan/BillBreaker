package com.billbreaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AssignItemsActivity extends AppCompatActivity {

    private List<ReceiptItemBreakdown> receiptItemBreakdowns = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignItemsAdaptor adaptor;
    private TabLayout tabLayout;
    private String currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_items);

        createRecyclerView();
        createTabLayout();
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);

        Intent intent = this.getIntent();
        List<ReceiptItem> receiptItems = intent.getParcelableArrayListExtra("receiptItems");
        for (int i = 0; i < receiptItems.size(); ++i)
            receiptItemBreakdowns.add(new ReceiptItemBreakdown(receiptItems.get(i).getName(), receiptItems.get(i).getPrice()));
        Log.d("EditItemsActivity", receiptItemBreakdowns.toString());

        adaptor = new AssignItemsAdaptor(this, receiptItemBreakdowns);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ReceiptItemBreakdown itemBreakdown = receiptItemBreakdowns.get(position);
                itemBreakdown.editPerson(currentPerson);
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));

        recyclerView.setAdapter(adaptor);
    }

    private void createTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("+"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == tabLayout.getTabCount()-1) // Last tab, add a new tab
                    getNameDialog();
                else // select person
                    currentPerson = tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == tabLayout.getTabCount()-1)
                    getNameDialog();
            }
        });
    }

    private void getNameDialog() {
        final EditText input = new EditText(AssignItemsActivity.this); // TODO: add padding
        new AlertDialog.Builder(AssignItemsActivity.this)
                .setTitle("Person Name")
                .setMessage("Add a new person's name")
                .setView(input)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentPerson = input.getText().toString();
                        tabLayout.addTab(tabLayout.newTab().setText(currentPerson), tabLayout.getTabCount()-1);
                        tabLayout.getTabAt(tabLayout.getTabCount()-2).select();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
