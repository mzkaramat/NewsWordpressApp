package com.raoul.socailbase.utill;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by mobile_perfect on 30/03/15.
 */
public class Utills {
//     public List<String> cityNames;
//     private void searchCity(String key) {
//        final String searchKey = key.replace(" ", "+");
//        new Thread() {
//            public void run() {
//                String reqUrl = String.format(
//                        GOOGLE_PLACES_AUTOCOMPLETE, searchKey,
//                        PLACES_API_DATA_RESULT_TYPE_GEOCODE);
//                HttpUtility httpUtility = new HttpUtility();
//                HttpResponseData responseData = httpUtility.sendGet(reqUrl);
//                if (responseData != null && responseData.getStatusCode() == 200) {
//                    try {
//                        JSONObject jsonData = new JSONObject(
//                                responseData.getResponseContent());
//                        JSONArray predictions = jsonData
//                                .getJSONArray("predictions");
//                        cityNames.clear();
//                        JSONObject prediction;
//                        for (int i = 0; i < predictions.length(); i++) {
//                            prediction = predictions.getJSONObject(i);
//                            String fullAddress = prediction.getString("description");
//                            int range = fullAddress.lastIndexOf(",");
//                            cityNames.add(fullAddress.substring(0, range));
//                        }
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                cityListAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        // e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
//     public static final String GOOGLE_PLACES_AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&types=%s&components=country:us&key=AIzaSyD478Y5RvbosbO4s34uRaukMwiPkBxJi5A";
//     public static final String PLACES_API_DATA_RESULT_TYPE_GEOCODE = "geocode";
//     public void getLatLongValueFromAddress(String address, final Handler handler) {
//        final String searchKey = address.replace(" ", "+");
//        new Thread() {
//            public void run() {
//                String reqUrl = String.format(
//                        GOOGLE_GEOLOCATION_API_GET_COORDINATES,
//                        searchKey);
//                HttpResponseData responseData = httpUtility.sendGet(reqUrl);
//                if (responseData != null && responseData.getStatusCode() == 200) {
//                    try {
//                        JSONObject jsonData = new JSONObject(
//                                responseData.getResponseContent());
//                        if (!PLACES_API_NO_RESULTS.equals(jsonData
//                                .getString("status"))) {
//                            JSONObject result = jsonData
//                                    .getJSONArray("results").getJSONObject(0);
//                            double lat = result.getJSONObject("geometry")
//                                    .getJSONObject("location").getDouble("lat");
//                            double lng = result.getJSONObject("geometry")
//                                    .getJSONObject("location").getDouble("lng");
//                            Bundle bundle = new Bundle();
//                            bundle.putDouble("latitude", lat);
//                            bundle.putDouble("longitude", lng);
//                            Message message = new Message();
//                            message.what = GET_LATLNG;
//                            message.setData(bundle);
//                            handler.sendMessage(message);
//                        }
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        // e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
//     public static final String GOOGLE_GEOLOCATION_API_GET_COORDINATES = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyD478Y5RvbosbO4s34uRaukMwiPkBxJi5A";
//     public static final String PLACES_API_NO_RESULTS = "ZERO_RESULTS";
//
//


}
