package com.example.reetuc.models;

public class ServerResponse {
  private boolean error;
  private String url;
  private String id;
  private String message;

  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Boolean getError() {
    return error;
  }

  public void setError(Boolean error) {
    this.error = error;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
