package com.p3pi27.quarters.response.user;

public class AccountBalance {

    private long quarters;
    private String formattedQuarters;
    private long ethers;
    private String formattedEthers;

    public long getQuarters() {

        return quarters;
    }

    public String getQuartersFormatted() {

        return formattedQuarters;
    }

    public long getEthers() {

        return ethers;
    }

    public String getEthersFormatted() {

        return formattedEthers;
    }
}