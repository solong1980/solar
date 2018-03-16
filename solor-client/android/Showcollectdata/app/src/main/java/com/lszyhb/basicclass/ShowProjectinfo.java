package com.lszyhb.basicclass;

import java.io.Serializable;

/**
 * Created by kkk8199 on 3/12/18.
 */

public class ShowProjectinfo extends ShowVCode  implements Serializable {
    private Long id;
    private int capability;
    private long projectTotalPChg;
    private int emissionStandards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getCapability() {
        return capability;
    }

    public void setCapability(int capability) {
        this.capability = capability;
    }

    public int getEmissionStandards() {
        return emissionStandards;
    }

    public void setEmissionStandards(int emissionStandards) {
        this.emissionStandards = emissionStandards;
    }

    public Long getprojectTotalPChg() {
        return projectTotalPChg;
    }

    public void setprojectTotalPChg(int projectTotalPChg) {
        this.projectTotalPChg = projectTotalPChg;
    }
}
