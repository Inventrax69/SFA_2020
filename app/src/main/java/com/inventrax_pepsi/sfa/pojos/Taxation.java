package com.inventrax_pepsi.sfa.pojos;

/**
 * Created by android on 4/22/2016.
 */
public class Taxation {


    public Taxation(int taxationId, double VAT) {
        this.taxationId = taxationId;
        this.VAT = VAT;
    }

    public int getTaxationId() {
        return taxationId;
    }

    public void setTaxationId(int taxationId) {
        this.taxationId = taxationId;
    }

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    private  int taxationId;
    private  double VAT;

}
