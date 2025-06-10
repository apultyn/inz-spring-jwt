package com.pultyn.spring_jwt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewBookRequest {
    private String title;
    private String author;
}
