package co.deepmindz.adminorghierservice.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomHttpResponse {
    public static ResponseEntity<Object> responseBuilder(String message, HttpStatus httpStatus, Object responseObject){
        Map<String, Object> response = new HashMap<>();
        response.put("data", responseObject); // Directly use "responseObject" as the "data" value
        response.put("httpStatus", httpStatus);
        response.put("message", message);

        return new ResponseEntity<>(response, httpStatus);
    }
}
