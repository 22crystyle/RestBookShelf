package org.example.dto.response;

import lombok.Getter;

import java.util.Objects;

@Getter
public class RestErrorResponse {
    private final String code;
    private final String message;

    public RestErrorResponse(
            String code,
            String message
    ) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RestErrorResponse) obj;
        return Objects.equals(this.code, that.code) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "ErrorResponse[" +
                "code=" + code + ", " +
                "message=" + message + ']';
    }

}
