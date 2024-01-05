package com.han.gateway.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ExceptionResponse {
    private String message;
    private int statusCode;
}
