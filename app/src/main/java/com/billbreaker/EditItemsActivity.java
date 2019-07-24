package com.billbreaker;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

public class EditItemsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private List<ReceiptItem> receiptItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditItemsAdaptor mAdapter;
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
        setContentView(R.layout.edit_items_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        Intent intent = this.getIntent();
        receiptItemList = intent.getParcelableArrayListExtra("receiptItems");
        Log.d("EditItemsActivity", receiptItemList.toString());

        mAdapter = new EditItemsAdaptor(this, receiptItemList);

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
                ReceiptItem receiptItem = receiptItemList.get(position);
                showEditItemsPopup(position, receiptItem);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackRemove = new RecyclerItemTouchHelper(0, LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallbackRemove).attachToRecyclerView(recyclerView);

        // Remove by swiping right to LEFT
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackLeft = new ItemTouchHelper.SimpleCallback(0, LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallbackLeft).attachToRecyclerView(recyclerView);

        final Button addButton = (Button) findViewById(R.id.add_item_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAdapter.addItem();
                showEditItemsPopup(receiptItemList.size() - 1, receiptItemList.get(receiptItemList.size() - 1));
            }
        });
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof EditItemsAdaptor.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = receiptItemList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final ReceiptItem modifiedItem = receiptItemList.get(viewHolder.getAdapterPosition());
            final int modifiedIndex = viewHolder.getAdapterPosition();
            
            String undoMessageText = "";

            if (direction == LEFT) {
                // remove the item from recycler view
                mAdapter.removeItem(viewHolder.getAdapterPosition());
                undoMessageText = " removed from cart!";
            }
            
//            if (direction == RIGHT) {
//                showEditItemsPopup(modifiedIndex, modifiedItem);
////                mAdapter.removeItem(modifiedIndex + 1);
//                undoMessageText = " edited!";
//            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + undoMessageText, Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(modifiedItem, modifiedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void showEditItemsPopup(final int index, final ReceiptItem itemBeingEdited) {

        // Create a AlertDialog Builder.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditItemsActivity.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("EDIT RECEIPT ITEM");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(false);

        // Init popup dialog view and it's ui controls.
        initPopupViewControls(itemBeingEdited);

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // When user click the save user data button in the popup dialog.
        saveUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user data from popup dialog editeext.
                String newName = nameEditText.getText().toString();
                double newPrice = Double.parseDouble(priceEditText.getText().toString());

                itemBeingEdited.setName(newName);
                itemBeingEdited.setPrice(newPrice);
                mAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });

        cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    /* Get current user data from listview and set them in the popup dialog edittext controls. */
    private void initEditTextUserDataInPopupDialog(ReceiptItem itemBeingEdited)
    {
        List<String> userDataList = getExistUserDataInListView(userDataListView);

        if(userDataList.size() == 3)
        {
            String name = userDataList.get(0);

            String price = userDataList.get(1);

            if(nameEditText != null)
            {
                nameEditText.setText(name);
            }

            if(priceEditText != null)
            {
                priceEditText.setText(price);
            }
        }
        nameEditText.setText(itemBeingEdited.getName());
        priceEditText.setText(String.valueOf(itemBeingEdited.getPrice()));
    }

    /* If user data exist in the listview then retrieve them to a string list. */
    private List<String> getExistUserDataInListView(ListView listView)
    {
        List<String> ret = new ArrayList<String>();

        if(listView != null)
        {
            ListAdapter listAdapter = listView.getAdapter();

            if(listAdapter != null) {

                int itemCount = listAdapter.getCount();

                for (int i = 0; i < itemCount; i++) {
                    Object itemObject = listAdapter.getItem(i);
                    HashMap<String, String> itemMap = (HashMap<String, String>)itemObject;

                    Set<String> keySet = itemMap.keySet();

                    Iterator<String> iterator = keySet.iterator();

                    String key = iterator.next();

                    String value = itemMap.get(key);

                    ret.add(value);
                }
            }
        }

        return ret;
    }

    /* Initialize popup dialog view and ui controls in the popup dialog. */
    private void initPopupViewControls(ReceiptItem e)
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(EditItemsActivity.this);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);

        // Get user input edittext and button ui controls in the popup dialog.
        nameEditText = (EditText) popupInputDialogView.findViewById(R.id.name_edits);
        priceEditText = (EditText) popupInputDialogView.findViewById(R.id.price_edits);

        saveUserDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
        cancelUserDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);

        // Display values from the main activity list view in user input edittext.
        initEditTextUserDataInPopupDialog(e);
    }
}
