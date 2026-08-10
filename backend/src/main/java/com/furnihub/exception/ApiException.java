package com.furnihub.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}
