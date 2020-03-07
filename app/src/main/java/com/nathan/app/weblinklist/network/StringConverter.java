package com.nathan.app.weblinklist.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class StringConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(@NonNull ResponseBody value) throws IOException {
        return value.string();
    }
}
