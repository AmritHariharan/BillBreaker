package com.billbreaker;

import android.media.Image;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

class ReceiptProcessor {

    private static ResponseBody getItemsFromImage(Image receipt) { // FIXME: actually return something lmao
        HttpClient httpclient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
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
            StringEntity reqEntity = new StringEntity("{body}"); // TODO: this...
            request.setEntity(reqEntity);

            // Response
            HttpResponse httpResponse = httpclient.execute(request);
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

            return mapper.readValue(jsonResponse, ResponseBody.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static List<ReceiptItem> processItems(Image receipt) {
        ResponseBody responseBody = getItemsFromImage(receipt);

        if (responseBody.regions.size() != 2) {
            throw new RuntimeException("Incorrect number of regions in response body, should be 2");
        }

        List<ReceiptItem> items = null;

        Iterator name = responseBody.regions.get(0).lines.iterator();
        Iterator price = responseBody.regions.get(1).lines.iterator();

        while (name.hasNext() && price.hasNext()) {
            items.add(new ReceiptItem(name.next().toString(), (double)price.next()));
        }

        return items;
    }
}