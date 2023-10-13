package com.ead.train_management.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class booking {

     @SerializedName("referenceId")
     @Expose
     private String  reference_id;
     @SerializedName("travallerName")
     @Expose
     private String  traveler_name;

     @SerializedName("travallerProfile")
     @Expose
     private String  travelerId;

     @SerializedName("phoneNumber")
     @Expose
     private String  phone_number;

     @SerializedName("trainName")
     @Expose
     private String  train_name;


     @SerializedName("noOfPassenger")
     @Expose
     private int  passengerCount;

     @SerializedName("emailAddress")
     @Expose
     private String  email;

     @SerializedName("reservationDate")
     @Expose
     private String  reservation_date;

     @SerializedName("isCancelled")
     @Expose
     private boolean  res_status;


     public String getReferenceId() {
          return reference_id;
     }

     public void setReferenceId(String reference_id) {
          this.reference_id = reference_id;
     }

     public String getTravelerName() { return traveler_name; }

     public void setTravelerName(String traveler_name) {
          this.traveler_name = traveler_name;
     }

     public String getTravelerId() {
          return travelerId;
     }

     public void setTravelerId(String travelerId) {
          this.travelerId = travelerId;
     }

     public String getPhoneNumber() {
          return phone_number;
     }

     public void setPhoneNumber(String phone_number) {
          this.phone_number = phone_number;
     }

     public String getTrainName() {
          return train_name;
     }

     public void setTrainName(String train_name) {
          this.train_name = train_name;
     }

     public int getPassengerCount() {
          return passengerCount;
     }

     public void setPassengerCount(int passengerCount) {
          this.passengerCount = passengerCount;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getReservationDate() {
          return reservation_date;
     }

     public void setReservationDate(String date) {
          this.reservation_date = reservation_date;
     }

     public boolean isStatus() {
          return res_status;
     }

     public void setStatus(boolean res_status) {
          this.res_status = res_status;
     }
}
