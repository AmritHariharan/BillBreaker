package com.billbreaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AssignItemsActivity extends AppCompatActivity {

    private List<ReceiptItem> receiptItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignItemsAdaptor adaptor;
    private TabLayout tabLayout;
    private String currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_items);

        // Setup tab bar on top
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
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Setup main recyclerview
        recyclerView = findViewById(R.id.recycler_view);

//        receiptItemList = intent.getParcelableArrayListExtra("receiptItems");
        receiptItemList.add(new ReceiptItem("yeet", 2.31));
        receiptItemList.add(new ReceiptItem("oh, wow", 23.1));
        receiptItemList.add(new ReceiptItem("neato", 033.231));

        adaptor = new AssignItemsAdaptor(this, receiptItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adaptor);
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
