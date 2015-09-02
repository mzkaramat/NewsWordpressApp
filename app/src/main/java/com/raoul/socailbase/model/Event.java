package com.raoul.socailbase.model;

/**
 * Created by mobile_perfect on 20/03/15.
 */
public class Event {
    String eventID;
    String eventNmae;
    String eventTimestart;
    String eventTimeend;
    String eventType;
    String eventContente;
    String eventLocation;
    String eventPosition;
    String eventUserimage;
    Double position_latitude;
    Double position_longitude;
    String userid;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setPosition_latitude(Double position_latitude) {
        this.position_latitude = position_latitude;
    }

    public void setPosition_longitude(Double position_longitude) {
        this.position_longitude = position_longitude;
    }

    public Double getPosition_latitude() {
        return position_latitude;
    }

    public Double getPosition_longitude() {
        return position_longitude;
    }

    public void setEventContente(String eventContente) {
        this.eventContente = eventContente;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventNmae(String eventNmae) {
        this.eventNmae = eventNmae;
    }

    public void setEventPosition(String eventPosition) {
        this.eventPosition = eventPosition;
    }

    public void setEventTimeend(String eventTimeend) {
        this.eventTimeend = eventTimeend;
    }

    public void setEventTimestart(String eventTimestart) {
        this.eventTimestart = eventTimestart;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventUserimage(String eventUserimage) {
        this.eventUserimage = eventUserimage;
    }

    public String getEventContente() {
        return eventContente;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventNmae() {
        return eventNmae;
    }

    public String getEventPosition() {
        return eventPosition;
    }

    public String getEventTimeend() {
        return eventTimeend;
    }

    public String getEventTimestart() {
        return eventTimestart;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventUserimage() {
        return eventUserimage;
    }
}
