package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchRes {
    private int id;
    private int user_id;
    private String word;
    private int adult;
    private int child;
    private Timestamp start_date;
    private Timestamp end_date;
    private Timestamp create_at;
}
