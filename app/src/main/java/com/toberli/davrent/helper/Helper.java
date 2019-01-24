package com.toberli.davrent.helper;

import org.json.JSONArray;
import org.json.JSONObject;

public class Helper {

    public static String getApiBadRequestError(String data){

        String error_msg = "";

        if (data == null){
            return "";
        }
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            JSONArray errors = jsonObject.getJSONArray("errors");
            error_msg = errors.get(0).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return error_msg;

    }

    public static String apikey = "2LrRl4IypvS1nXuuLHFi2afHTFlO5LvCvZl0KGvIAev8Q64F";

}
