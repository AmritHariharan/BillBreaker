package com.billbreaker;

import android.media.Image;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

class ReceiptProcessor {

    static void getItemsFromImage(Image receipt) { // FIXME: actually return something lmao
        HttpURLConnection conn = null;
        try {
            URL u = new URL("https://westus.api.cognitive.microsoft.com/vision/v2.0/ocr?language=unk&detectOrientation=true");
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Ocp-Apim-Subscription-Key", BuildConfig.AZURE_COGNITION_KEY);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<ReceiptItem> processItems(Image receipt) {
        getItemsFromImage(receipt);
    }
}
