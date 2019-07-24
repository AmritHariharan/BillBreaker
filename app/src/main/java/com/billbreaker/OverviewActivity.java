package com.billbreaker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {
    private List<PersonalReceiptItem> peopleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OverviewAdaptor mAdapter;
    private CoordinatorLayout coordinatorLayout;


    // Below edittext and button are all exist in the popup dialog view.
    private View popupInputDialogView = null;
    // Contains user name data.
    private EditText nameEditText = null;
    // Contains email data.
    private EditText priceEditText = null;
    // Click this button in popup dialog to save user input data in above three edittext.
    private Button saveUserDataButton = null;
    // Click this button to cancel edit user data.
    private Button cancelUserDataButton = null;
    // This listview is just under above button.
    private ListView userDataListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        mAdapter = new OverviewAdaptor(this, peopleList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep receiptItem_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep receiptItem_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PersonalReceiptItem receiptItem = peopleList.get(position);
                Toast.makeText(getApplicationContext(), receiptItem.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareReceiptItemData();
    }

     private void prepareReceiptItemData() {
        PersonalReceiptItem receiptItem = new PersonalReceiptItem("Mad Max: Fury Road", 2.0);
        peopleList.add(receiptItem);
        PersonalReceiptItem receiptItem2 = new PersonalReceiptItem("Mad Max: Fury Road", 2.0);
        peopleList.add(receiptItem);
        PersonalReceiptItem receiptItem3 = new PersonalReceiptItem("Mad Max: Fury Road", 2.0);
        peopleList.add(receiptItem);
        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }
}
