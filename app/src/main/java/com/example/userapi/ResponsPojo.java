package com.example.userapi;

public class ResponsPojo {
    private String message;
    private String error;
    private String exits;

    public ResponsPojo(String message, String error, String exits) {
        this.message = message;
        this.error = error;
        this.exits = exits;
    }

    public String getExits() {
        return exits;
    }

    public void setExits(String exits) {
        this.exits = exits;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
