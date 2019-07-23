package com.billbreaker;

import java.util.List;

class ResponseLine {
    String boundingBox;
    List<ResponseWord> words;

    String getLine(boolean delimited) {
        StringBuilder s = new StringBuilder();
        for (ResponseWord rw : words) {
            if (delimited)
                s.append(rw.text).append(' ');
            else
                s.append(rw.text);
        }
        return s.toString();
    }

    private int getNumberStart(String s) {
        for (int i = 0; i <  s.length(); ++i) {
            if (Character.isDigit(s.charAt(i)))
                return i;
        }
        return -1;
    }

    double getPrice() {
        String line = getLine(false);
        int idx = getNumberStart(line);
        if (idx == -1)
            return 0.0;
        else
            return Double.parseDouble(line.substring(idx));
    }
}
