package com.billbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OverviewActivity extends AppCompatActivity {
    private List<PersonalReceiptItem> peopleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OverviewAdaptor mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private long timestamp;
    public static final String TIMESTAMP_KEY = "timestampTest";
    private double tipTotal;
    private double taxTotal;
    private double subtotal;


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

        Intent intent = this.getIntent();
        timestamp = intent.getLongExtra(TIMESTAMP_KEY, 0);
        Log.d("OverviewActivity", peopleList.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.overview_toolbar);
        setSupportActionBar(toolbar);

        DateFormat timestampFormat = new SimpleDateFormat("MM-dd-yyyy, HH:mm:ss", Locale.US);
        Date date = new Date(timestamp);

        setTitle(timestampFormat.format(date));

        ReceiptDatabase receiptDatabase = new ReceiptDatabase(this);
        Receipt receipt = receiptDatabase.getReceipt(timestamp);
        peopleList = receipt.getPersonalReceiptItems();

        tipTotal = receipt.getTip();
        taxTotal = receipt.getTax();
        subtotal = receipt.getSubtotal();
        addTipAndTaxByPercentage();

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
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addTipAndTaxByPercentage() {

        double tipOwed, percentageOwed, taxOwed;

        for (PersonalReceiptItem e : peopleList) {
            percentageOwed = subtotal > 0 ? e.getPrice() / subtotal : 0;
            tipOwed = percentageOwed * tipTotal;
            taxOwed = percentageOwed * taxTotal;
            e.setPrice(e.getPrice() + tipOwed + taxOwed);
        }
    }
}
