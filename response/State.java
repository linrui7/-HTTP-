package com.github7.response;

public enum State {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    METHOD_NOT_ALLOWED(405,"Not_Allowed"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
    Temporarily_Moved(302, "Temporarily Moved");

    private int code;
    private String reason;

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    State(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
