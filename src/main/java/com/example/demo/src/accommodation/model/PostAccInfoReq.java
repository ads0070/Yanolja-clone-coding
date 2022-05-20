package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostAccInfoReq {
    private String name;
    private String store_phone_number;
    private String main_category;
    private String sub_category;
    private LocalDateTime create_at;
}
