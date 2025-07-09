package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
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
