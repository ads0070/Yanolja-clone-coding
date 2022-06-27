package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetPointRes {
    private int id;
    private String name;
    private int point;
    private Timestamp deadline_for_use;
    private Timestamp create_at;
}
