package com.cobol.serialization.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java equivalent of the COBOL ws-customer structure when ws-customer-type-corp is true.
 * Represents a corporate customer with a single company name field.
 * This demonstrates the REDEFINES clause where ws-corp-name redefines ws-customer-name.
 * 
 * COBOL structure (corporate view):
 * 05  ws-corp-name redefines ws-customer-name pic x(30).
 */
@XmlRootElement(name = "ws-customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class CorporateCustomer extends Customer {

    private static final int CORP_NAME_LENGTH = 30;

    @XmlElement(name = "ws-corp-name")
    @JsonProperty("ws-corp-name")
    private String corpName;

    public CorporateCustomer() {
        super();
        this.customerType = TYPE_CORP;
    }

    public CorporateCustomer(String corpName, Address address) {
        super(TYPE_CORP, address);
        this.corpName = padOrTruncate(corpName, CORP_NAME_LENGTH);
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = padOrTruncate(corpName, CORP_NAME_LENGTH);
    }

    @Override
    public String getDisplayName() {
        return corpName != null ? corpName.trim() : "";
    }

    @JsonIgnore
    public String getRawNameData() {
        return corpName != null ? String.format("%-" + CORP_NAME_LENGTH + "s", corpName) : 
               String.format("%" + CORP_NAME_LENGTH + "s", "");
    }

    private String padOrTruncate(String input, int length) {
        if (input == null) {
            return null;
        }
        if (input.length() > length) {
            return input.substring(0, length);
        }
        return input;
    }

    @Override
    public String toString() {
        return String.format("CorporateCustomer[corpName=%s, address=%s]",
                corpName, address);
    }
}
