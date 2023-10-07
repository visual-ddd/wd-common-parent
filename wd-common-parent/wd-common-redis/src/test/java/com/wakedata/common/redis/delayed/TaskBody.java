package com.wakedata.common.redis.delayed;

import java.io.Serializable;

public class TaskBody implements Serializable {
    private String name;
    private String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}