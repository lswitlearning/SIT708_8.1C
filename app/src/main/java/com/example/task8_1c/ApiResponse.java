package com.example.task8_1c;

import com.google.gson.annotations.SerializedName;


//The response from an API call.
public class ApiResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}

