package com.queerdevs.raj.punelocal;

import java.io.Serializable;

/**
 * Created by RAJ on 1/23/2018.
 */

public class Share implements Serializable {
    private String destStation;
    private  int flagfordest;
    private  int flagforsource;
    private  String sourceStation;

    public String getDestStation() {
        return destStation;
    }

    public void setDestStation(String destStation) {
        this.destStation = destStation;
    }

    public int getFlagfordest() {
        return flagfordest;
    }

    public void setFlagfordest(int flagfordest) {
        this.flagfordest = flagfordest;
    }

    public int getFlagforsource() {
        return flagforsource;
    }

    public void setFlagforsource(int flagforsource) {
        this.flagforsource = flagforsource;
    }

    public  String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }
}

