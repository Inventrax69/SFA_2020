package com.inventrax_pepsi.interfaces;

/**
 * Created by Naresh on 04-Apr-16.
 */
public interface DeliveryView {

    void showPrintStatus(int status,String message);

    void showProgress();

    void closeProgress();

}
