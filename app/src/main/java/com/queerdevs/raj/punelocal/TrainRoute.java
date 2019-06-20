package com.queerdevs.raj.punelocal;

import java.io.Serializable;

/**
 * Created by RAJ on 1/22/2018.
 */

public class TrainRoute  implements Serializable {
    private String code;
    private String name;
    private String time;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
