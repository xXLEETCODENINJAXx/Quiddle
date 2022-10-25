package com.quiddle.quiddleApplication.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";

    private Object data;
    private String message;
    private String status;
}
