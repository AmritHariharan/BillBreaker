package com.billbreaker;

import android.media.Image;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class ReceiptProcessor {

    private static void getItemsFromImage(Image receipt) { // FIXME: actually return something lmao
        HttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/vision/v2.0/ocr");

            // Request header
            builder.setParameter("language", "unk");
            builder.setParameter("detectOrientation", "true");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", BuildConfig.AZURE_COGNITION_KEY);

            // Request body
            StringEntity reqEntity = new StringEntity("{body}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<ReceiptItem> processItems(Image receipt) {
        getItemsFromImage(receipt);
        return new ArrayList<ReceiptItem>();
    }
}
