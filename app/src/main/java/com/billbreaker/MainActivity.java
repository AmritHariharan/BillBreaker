package com.billbreaker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.billbreaker.OverviewActivity.TIMESTAMP_KEY;

public class MainActivity extends AppCompatActivity implements ReceiptsAdapter.OnReceiptClickedListener {

    String filePath;
    Uri originalPhotoUri;
    ReceiptDatabase receiptDatabase;
    List<Receipt> receipts;
    private RecyclerView recyclerView;
    public static final String TAX_KEY = "taxKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        receiptDatabase = new ReceiptDatabase(MainActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateReceipts();

        FloatingActionButton newPhoto = findViewById(R.id.new_photo);

        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePhoto();
            }
        });
    }

    private void populateReceipts() {
         receipts = receiptDatabase.getAllReceipts();
//        PersonalReceiptItem shri = new PersonalReceiptItem("shri", 13.34);
//        PersonalReceiptItem ada = new PersonalReceiptItem("ada", 14.57);
//        PersonalReceiptItem amrit = new PersonalReceiptItem("amrit", 9.83);
//        PersonalReceiptItem anna = new PersonalReceiptItem("anna", 11.95);
//        List<PersonalReceiptItem> items = Arrays.asList(shri, ada, amrit, anna);
//        Receipt receipt = new Receipt(items, Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis(), 0, 0, 0);
//
//        receipts = Arrays.asList(receipt, receipt, receipt);

        RecyclerView.Adapter adapter = new ReceiptsAdapter(receipts, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            CropImage.activity(originalPhotoUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),result.getUri());
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                System.out.println(error.toString());
            }
        }
    }

    private void dispatchTakePhoto() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            File originalPhoto = createFile();

            if (originalPhoto != null) {
                filePath = originalPhoto.getAbsolutePath();
                originalPhotoUri = FileProvider.getUriForFile(MainActivity.this, "com.billbreaker.MainActivity", originalPhoto);
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, originalPhotoUri);

                startActivityForResult(takePhoto, 1);
            }
        }
    }

    private File createFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("main log", "Excep: " + e.toString());
        }
        return image;
    }

    @Override
    public void onReceiptClicked(int position) {
        Receipt receipt = receipts.get(position);
        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra(TIMESTAMP_KEY, receipt.getTimestamp());
        startActivity(intent);
    }

    private class AsyncTaskRunner extends AsyncTask<Bitmap, Void, ArrayList<ReceiptItem>> {
        ProgressDialog progressDialog;

        @Override
        protected ArrayList<ReceiptItem> doInBackground(Bitmap... bitmaps) {
            return ReceiptProcessor.processItems(bitmaps[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog", "Processing Image");
        }

        @Override
        protected void onPostExecute(ArrayList<ReceiptItem> receiptItems) {
            super.onPostExecute(receiptItems);
            progressDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, EditItemsActivity.class);
            intent.putParcelableArrayListExtra("receiptItems", receiptItems);
            startActivity(intent);
        }
    }

    public void onSettingsButtonClicked(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Set Default Tax Percentage");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View settingsView = layoutInflater.inflate(R.layout.settings_popup, null);

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(settingsView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button saveButton = settingsView.findViewById(R.id.button_save_user_data);
        Button cancelButton = settingsView.findViewById(R.id.button_cancel_user_data);
        final EditText taxEditText = settingsView.findViewById(R.id.tax_edit);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float tax = Float.parseFloat(taxEditText.getText().toString());
                Context context = getApplicationContext();

                // tax percentage will be stored in sharedPreferences to be easily accessible later
                SharedPreferences sharedPreferences =
                        context.getSharedPreferences(
                                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(TAX_KEY, tax);
                editor.apply();

                Toast.makeText(context, "default tax set as " + tax, Toast.LENGTH_LONG)
                        .show();

                alertDialog.cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }
}
