package com.billbreaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AssignItemsActivity extends AppCompatActivity {

    private List<ReceiptItem> receiptItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignItemsAdaptor adaptor;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_items);

//        ViewPager viewPager = findViewById(R.id.pager);
//        CollectionItemPagerAdapter myPagerAdapter = new CollectionItemPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(myPagerAdapter);

        tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);

        recyclerView = findViewById(R.id.recycler_view);

//        receiptItemList = intent.getParcelableArrayListExtra("receiptItems");
        receiptItemList.add(new ReceiptItem("yeet", 2.31));
        receiptItemList.add(new ReceiptItem("oh, wow", 23.1));
        receiptItemList.add(new ReceiptItem("neato", 033.231));
        receiptItemList.add(new ReceiptItem("yeet", 2.31));
        receiptItemList.add(new ReceiptItem("oh, wow", 23.1));
        receiptItemList.add(new ReceiptItem("neato", 033.231));

        adaptor = new AssignItemsAdaptor(this, receiptItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adaptor);
    }
}
