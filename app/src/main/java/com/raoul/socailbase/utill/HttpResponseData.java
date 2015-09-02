package com.raoul.socailbase.utill;

public class HttpResponseData {

 private int statusCode;
 private String responseData;
 
 public HttpResponseData(int statusCode, String responseData){
  this.statusCode = statusCode;
  this.responseData = responseData;
 }
 
 public int getStatusCode(){
  return statusCode;
 }
 
 public String getResponseContent(){
  return responseData;
 }
}