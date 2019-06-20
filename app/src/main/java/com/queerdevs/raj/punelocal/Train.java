package com.queerdevs.raj.punelocal;

import java.io.Serializable;

/**
 * Created by RAJ on 1/22/2018.
 */

public class Train  implements Serializable {
    private String arrivalTime;
    private String cars;
    private String departTime;
    private String destst;
    private String speed;
    private String startst;
    private String trainKey;

    public String getTrainKey() {
        return this.trainKey;
    }

    public void setTrainKey(String trainKey) {
        this.trainKey = trainKey;
    }

    public String getStartst() {
        return this.startst;
    }

    public void setStartst(String startst) {
        this.startst = startst;
    }

    public String getDestst() {
        return this.destst;
    }

    public void setDestst(String destst) {
        this.destst = destst;
    }

    public String getCars() {
        return this.cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDepartTime() {
        return this.departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
