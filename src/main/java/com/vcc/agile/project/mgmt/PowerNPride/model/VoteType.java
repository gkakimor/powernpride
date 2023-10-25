package com.vcc.agile.project.mgmt.PowerNPride.model;

import com.vcc.agile.project.mgmt.PowerNPride.exceptions.SpringPowerNPrideException;

import java.util.Arrays;

public enum VoteType {

    UPVOTE(1), DOWNVOTE(-1),
    ;

    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringPowerNPrideException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
