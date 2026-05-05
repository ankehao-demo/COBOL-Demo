package com.coboldemo.redefines;

/**
 * Corporate-shaped {@link Customer}: holds a company name.
 */
public class CorpCustomer extends Customer {

    private String corpName;

    public CorpCustomer() {
        super(Type.CORP);
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
