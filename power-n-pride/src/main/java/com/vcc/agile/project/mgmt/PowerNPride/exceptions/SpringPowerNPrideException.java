package com.vcc.agile.project.mgmt.PowerNPride.exceptions;

public class SpringPowerNPrideException  extends RuntimeException{

    public SpringPowerNPrideException(String exMessage, Exception exception) {

        super(exMessage, exception);
    }

    public SpringPowerNPrideException(String exMessage) {

        super(exMessage);
    }
}
