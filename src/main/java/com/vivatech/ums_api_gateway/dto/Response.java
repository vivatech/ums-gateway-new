package com.vivatech.ums_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Response {

    String result;
    String error;
    Integer errorcode;
    String message;

   Object dto;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Response() {
        this.result = "SUCCESS";
        this.error = "";
        this.errorcode = 0;
    }

    public Response(String string, String message) {
        this.result = string;
        this.error = message;
        this.errorcode = -1;
   }

    public Response(String string, String message, Object dto) {
        this.result = string;
        this.error = message;
        this.dto = dto;
    }

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String s, String noofseats, int countAllottedSeats) {
    }
}
