package com.billbreaker;

import android.graphics.Bitmap;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

class ReceiptProcessor {

    private static ResponseBody getItemsFromImage(Bitmap receipt) {
        HttpClient httpclient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        try {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/vision/v2.0/ocr");

            // Request header
            builder.setParameter("language", "unk");
            builder.setParameter("detectOrientation", "true");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
//            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", BuildConfig.AZURE_COGNITION_KEY);

            // Process Bitmap to binary array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            receipt.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] receiptBytes = stream.toByteArray();

            // Request body
//            StringEntity reqEntity = new StringEntity(Arrays.toString(receiptBytes));
            StringEntity reqEntity = new StringEntity("{\"url\":\"https://i.imgur.com/dpTVdcZ.jpg\"}");
            request.setEntity(reqEntity);

            // Response
            HttpResponse httpResponse = httpclient.execute(request);
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

            return mapper.readValue(jsonResponse, ResponseBody.class);
        } catch (Exception e) {
            Log.d("ReceiptProcessor", Objects.requireNonNull(e.getMessage()));
            return new ResponseBody();
        }
    }

    static List<ReceiptItem> processItems(Bitmap receipt) {
        ResponseBody responseBody = getItemsFromImage(receipt);

        if (responseBody.regions.size() != 2)
            throw new RuntimeException("Incorrect number of regions in response body, should be 2");

        List<ReceiptItem> items = new ArrayList<>();
        List<ResponseLine> names = responseBody.regions.get(0).lines;
        List<ResponseLine> prices = responseBody.regions.get(1).lines;

        for (int i = 0; i < Math.min(names.size(), prices.size()); ++i)
            items.add(new ReceiptItem(names.get(i).getLine(true), prices.get(i).getPrice()));

        Log.d("ReceiptProcessor", items.toString());
        return items;
    }
}
