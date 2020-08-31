package com.inventrax_pepsi.sfa.invoice_print;

/**
 * Created by santosh on 4/25/2016.
 */
public class TaxBreakup {
    private double VAT;
    private double vatAmount;

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }
}
