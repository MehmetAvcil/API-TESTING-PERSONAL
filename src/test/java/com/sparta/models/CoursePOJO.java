package com.sparta.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoursePOJO {
    private StreamPOJO stream;
    private String name;

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String trainer;

    public CoursePOJO() {
    }

    public StreamPOJO getStream() {
        return stream;
    }

    public void setStream(StreamPOJO stream) {
        this.stream = stream;
    }
}
