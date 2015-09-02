package com.raoul.socailbase.utill;

/**
 * Created by mobile_perfect on 30/03/15.
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtility {
    DefaultHttpClient httpClient;
    HttpContext localContext;
    private String ret;

    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;

    public HttpUtility() {
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 50000);
        HttpConnectionParams.setSoTimeout(myParams, 50000);
        httpClient = new DefaultHttpClient(myParams);
        localContext = new BasicHttpContext();
    }

    synchronized public HttpResponseData sendGet(String url) {
        return sendGet(url, null);
    }

    synchronized public HttpResponseData sendGet(String url, String authorization) {
        httpGet = new HttpGet(url);
        if (authorization != null)
            httpGet.setHeader("Authorization", authorization);

        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            ret = "";
        }

        return new HttpResponseData(response.getStatusLine().getStatusCode(),
                ret);
    }

    public String sendPostData(String url, List<NameValuePair> nameValuePairs)
            throws Exception {
        // Getting the response handler for handling the post response
        ResponseHandler<String> res = new BasicResponseHandler();
        HttpPost postMethod = new HttpPost(url);
        postMethod.addHeader("Keep-Alive", "115");
        postMethod.addHeader("Connection", "Keep-Alive");
        // Setting the data that is to be sent
        postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        // Execute HTTP Post Request
        String response = httpClient.execute(postMethod, res);
        return response;
    }

    public HttpResponseData sendPostJsonData(String url, JSONObject jsonObj)
            throws Exception {
        return sendPostJsonData(url, null, jsonObj);
    }

    public HttpResponseData sendPostJsonData(String url, String authorization,
                                             JSONObject jsonObj) {
        // Getting the response handler for handling the post response
        // ResponseHandler<String> res = new BasicResponseHandler();
        HttpPost postMethod = new HttpPost(url);
        postMethod.addHeader("Keep-Alive", "115");
        postMethod.addHeader("Connection", "Keep-Alive");
        postMethod.setHeader("Content-type", "application/json");
        if (authorization != null)
            postMethod.setHeader("Authorization", authorization);
        // Setting the data that is to be sent
        StringEntity se;
        try {
            se = new StringEntity(jsonObj.toString());

            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            postMethod.setEntity(se);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            // e1.printStackTrace();
            return null;
        }
        // Execute HTTP Post Request
        try {
            response = httpClient.execute(postMethod);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            ret = "";
        }
        return new HttpResponseData(response.getStatusLine().getStatusCode(),
                ret);
    }

}
