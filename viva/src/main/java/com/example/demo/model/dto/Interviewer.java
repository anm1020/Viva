package com.example.demo.model.dto;

public class Interviewer {
    private final String id;
    private final String name;
    

    public Interviewer(String id, String name) {
        this.id   = id;
        this.name = name;
    }
    public String getId()   { return id; }
    public String getName() { return name; }
}