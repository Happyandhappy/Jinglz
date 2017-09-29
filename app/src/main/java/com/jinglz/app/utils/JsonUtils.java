package com.jinglz.app.utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public final class JsonUtils {

    /**
     * Constructs sole JsonUtils constructor.
     */
    private JsonUtils() {

    }

    /**
     * This method is used to convert string json data to JSONObject.
     * it may throw JSONException if parse fails or {@code json} does not
     * contains JSONObject.
     *
     * @param json a JSON-encoded string containing an object
     * @return Returns JSONObject if parsed successfully.
     * @throws JSONException if parse fails or {@code json} does not
     * contains JSONObject.
     */
    public static JSONObject parseSave(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bundle jsonStringToBundle(String jsonString){
        try {
            JSONObject jsonObject = toJsonObject(jsonString);
            return jsonToBundle(jsonObject);
        } catch (JSONException ignored) {

        }
        return null;
    }
    public static JSONObject toJsonObject(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }
    public static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = jsonObject.getString(key);
            bundle.putString(key,value);
        }
        return bundle;
    }
}
