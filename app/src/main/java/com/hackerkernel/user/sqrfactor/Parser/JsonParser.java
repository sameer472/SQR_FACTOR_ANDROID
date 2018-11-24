package com.hackerkernel.user.sqrfactor.Parser;

import com.hackerkernel.user.sqrfactor.Constants.SPConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    public static String SimpleParser(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        return obj.getString(SPConstants.MESSAGE);
    }

}
