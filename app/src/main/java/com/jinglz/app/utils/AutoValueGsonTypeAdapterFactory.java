package com.jinglz.app.utils;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class AutoValueGsonTypeAdapterFactory implements TypeAdapterFactory {

    // Static factory method to access the package
    // private generated implementation

    /**
     * Return new instance of AutoValueGson_AutoValueGsonTypeAdapterFactory.
     *
     * @return AutoValueGson_AutoValueGsonTypeAdapterFactory object
     */
    public static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueGsonTypeAdapterFactory();
    }
}
