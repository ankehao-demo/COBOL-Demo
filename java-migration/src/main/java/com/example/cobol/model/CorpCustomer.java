package com.example.cobol.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "corp_customer")
public class CorpCustomer extends Customer {
    private String corpName;

    public CorpCustomer() {
        super();
        this.customerType = 2;
    }

    public CorpCustomer(String corpName, String streetAddress, String state, int zipCode) {
        super(streetAddress, state, zipCode, 2);
        this.corpName = truncate(corpName, 30);
    }

    @XmlElement(name = "corp_name")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = truncate(corpName, 30);
    }

    @Override
    public String toString() {
        return String.format("Customer Type: CORP\nCompany name: %s\nAddress:\n%s\n%s, %05d",
                corpName, streetAddress, state, zipCode);
    }
}
