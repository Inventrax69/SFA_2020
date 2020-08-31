package com.inventrax_pepsi.sfa.cart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.InvoiceItem;
import com.inventrax_pepsi.sfa.pojos.InvoiceOrder;
import com.inventrax_pepsi.sfa.pojos.InvoiceVAT;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.pojos.Taxation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by android on 4/22/2016.
 */

public class BillingManager {

    private DatabaseHelper databaseHelper;
    private TableItem tableItem;
    private Gson gson;

    public BillingManager() {

        databaseHelper=DatabaseHelper.getInstance();
        tableItem=databaseHelper.getTableItem();
        gson = new  GsonBuilder().create();

    }

    public Invoice prepareInvoice(Order order) {
        try {
            if (order != null) {
                Invoice invoice = new Invoice();

                invoice.setInvoiceNo(order.getOrderCode());
                invoice.setCustomerId(order.getCustomerId());
                invoice.setAuditInfo(order.getAuditInfo());
                invoice.setCustomerName(order.getCustomerName());
                invoice.setGeneratedDate(order.getCreatedOn());
                invoice.setInvoiceItems(new ArrayList<InvoiceItem>());
                invoice.setNoOfBottles(order.getNoOfBottles());
                invoice.setNoOfCases(order.getNoOfCases());

                invoice.setInvoiceOrders(new ArrayList<InvoiceOrder>());

                InvoiceOrder invoiceOrder=new InvoiceOrder();
                invoiceOrder.setOrderCode(order.getOrderCode());
                invoiceOrder.setOrderId(order.getOrderId());
                invoiceOrder.setCreatedOn(order.getCreatedOn());

                invoice.getInvoiceOrders().add(invoiceOrder);

                double discountAmount = 0;
                double netPrice = 0;
                double grossPrice = 0;

                for (OrderItem orderItem : order.getOrderItems()) {

                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setDerivedPrice(orderItem.getDerivedPrice());
                    invoiceItem.setPrice(orderItem.getPrice());
                    invoiceItem.setDiscountPrice(orderItem.getDiscountPrice());
                    invoiceItem.setIsDiscountItem(false);
                    invoiceItem.setItemBrand(orderItem.getItemBrand());
                    invoiceItem.setItemPack(orderItem.getItemPack());
                    invoiceItem.setItemUoMId(orderItem.getItemUOMId());
                    invoiceItem.setItemCode(orderItem.getItemCode());
                    invoiceItem.setItemId(orderItem.getItemId());
                    invoiceItem.setItemPrice(orderItem.getItemPrice());
                    invoiceItem.setUomId(orderItem.getUoMId());
                    invoiceItem.setUom(orderItem.getUoM());
                    invoiceItem.setUoMCode(orderItem.getUoMCode());

                    invoiceItem.setMRP(orderItem.getMRP());

                    invoice.getInvoiceItems().add(invoiceItem);



                    invoiceItem.setQuantity(orderItem.getQuantity());

                    netPrice += orderItem.getDerivedPrice();
                    grossPrice += orderItem.getPrice();

                    if (orderItem.getOrderItemSchemes() != null) {
                        for (OrderItemScheme orderScheme : orderItem.getOrderItemSchemes()) {
                            InvoiceItem schemeLine = new InvoiceItem();
                            schemeLine.setUom(orderScheme.getUoM());
                            schemeLine.setItemId(orderScheme.getItemId() );
                            schemeLine.setUomId(orderScheme.getUoMId());
                            schemeLine.setItemUoMId(orderScheme.getItemUoMId());
                            schemeLine.setSchemeItem(true);
                            schemeLine.setDiscountPrice(orderScheme.getPrice());
                            schemeLine.setPrice(orderScheme.getPrice());
                            schemeLine.setDerivedPrice(0);
                            schemeLine.setItemCode(orderScheme.getItemCode());
                            schemeLine.setQuantity(orderScheme.getValue());
                            schemeLine.setUoMCode(orderScheme.getUoM());
                            schemeLine.setItemBrand(orderScheme.getItemBrand());
                            schemeLine.setItemPack(orderScheme.getItemPack());

                            schemeLine.setMRP(orderScheme.getMRP());

                            grossPrice += orderScheme.getPrice();
                            invoice.getInvoiceItems().add(schemeLine);
                        }
                    }

                    if (orderItem.getOrderItemDiscount() != null) {

                        if (orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot())
                        {
                            InvoiceItem discountLine = new InvoiceItem();

                            discountLine.setMRP(orderItem.getOrderItemDiscount().getMRP());

                            discountLine.setItemBrand(orderItem.getOrderItemDiscount().getItemCode());
                            discountLine.setItemCode(orderItem.getOrderItemDiscount().getItemCode());
                            discountLine.setItemId(orderItem.getOrderItemDiscount().getItemId());
                            discountLine.setUom(orderItem.getOrderItemDiscount().getUoM());
                            discountLine.setUoMCode(orderItem.getOrderItemDiscount().getUoM());
                            discountLine.setDiscountPrice(orderItem.getOrderItemDiscount().getDiscountPrice());
                            discountLine.setPrice(orderItem.getOrderItemDiscount().getDiscountPrice());
                            discountLine.setUomId(orderItem.getOrderItemDiscount().getUomId());
                            discountLine.setDerivedPrice(0);
                            discountLine.setIsDiscountItem(true);
                            discountLine.setItemPrice(orderItem.getOrderItemDiscount().getItemPrice());
                            discountLine.setQuantity(orderItem.getOrderItemDiscount().getValue());
                            discountLine.setItemCode(orderItem.getOrderItemDiscount().getItemCode());
                            discountAmount += orderItem.getOrderItemDiscount().getDiscountPrice();
                            grossPrice += orderItem.getOrderItemDiscount().getDiscountPrice();



                        } else if (orderItem.getOrderItemDiscount().isSpot()) {
                            discountAmount += orderItem.getOrderItemDiscount().getDiscountPrice();
                        }

                        // Modified By Santosh
                        if (orderItem.getOrderItemDiscount().getDiscountTypeId() != 1 && orderItem.getOrderItemDiscount().isSpot()) {
                            InvoiceItem discountLine = new InvoiceItem();
                            discountLine.setItemBrand(orderItem.getOrderItemDiscount().getItemCode());
                            discountLine.setItemCode(orderItem.getOrderItemDiscount().getItemCode());
                            discountLine.setItemId(orderItem.getOrderItemDiscount().getItemId());
                            discountLine.setUom(orderItem.getOrderItemDiscount().getUoM());
                            discountLine.setUoMCode(orderItem.getOrderItemDiscount().getUoM());
                            discountLine.setDiscountPrice(orderItem.getOrderItemDiscount().getDiscountPrice());
                            discountLine.setPrice(orderItem.getOrderItemDiscount().getDiscountPrice());
                            discountLine.setUomId(orderItem.getOrderItemDiscount().getUomId());
                            discountLine.setDerivedPrice(0);
                            discountLine.setIsDiscountItem(true);
                            discountLine.setItemPrice(orderItem.getOrderItemDiscount().getItemPrice());
                            discountLine.setQuantity(orderItem.getOrderItemDiscount().getValue());
                            discountLine.setItemCode(orderItem.getOrderItemDiscount().getItemCode());
                            discountAmount += orderItem.getOrderItemDiscount().getDiscountPrice();
                            grossPrice += orderItem.getOrderItemDiscount().getDiscountPrice();

                            for (InvoiceItem item : invoice.getInvoiceItems()) {
                                if( ( item.getItemId() == discountLine.getItemId() ) && item.getUoMCode().equals(discountLine.getUoMCode()) ) {
                                    item.setSpotDiscountPrice(orderItem.getOrderItemDiscount().getValue());
                                    orderItem.getOrderItemDiscount().getDiscountType();
                                }
                            }
                        }


                    }
                }
                invoice.setNetAmount(netPrice);
                invoice.setGrossAmount(grossPrice);
                invoice.setDiscountAmount(discountAmount);
                //vat calculation..
                calculateVATOnInvoice(invoice);

                return invoice;
            }
        } catch (Exception e) {

            //need to write the log..

            e.printStackTrace();
        }
        return null;
    }


    private void calculateVATOnInvoice(Invoice invoice) {
        try {
            HashMap<Integer, Taxation> itemTaxRateMap = new HashMap<>();

            List<Item> localDBItems =  tableItem.getAllItems();

            if (localDBItems==null || gson==null )
                return;

            if (localDBItems.size()>0){

                for (Item localItem:localDBItems){
                    com.inventrax_pepsi.sfa.pojos.Item item = gson.fromJson(localItem.getItemJSON(), com.inventrax_pepsi.sfa.pojos.Item.class);
                    itemTaxRateMap.put(item.getItemId(),new Taxation(item.getTaxationId(),item.getvAT()));
                }
            }


            //initialize item tax information.
            double invoiceVAT = 0;
            HashMap<Integer, InvoiceVAT> invoiceVATMap = new HashMap<>();

            if (invoice != null && invoice.invoiceItems != null && invoice.invoiceItems.size() > 0)
            {
                for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {

                    if (itemTaxRateMap.containsKey(invoiceItem.getItemId())) {

                        if (!invoiceVATMap.containsKey(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId())) {
                            invoiceVATMap.put(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId(), new InvoiceVAT());
                            invoiceVATMap.get(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId()).setInvoiceNo(invoice.getInvoiceNo());
                            invoiceVATMap.get(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId()).setTaxationId(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId());
                            invoiceVATMap.get(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId()).setvAT(itemTaxRateMap.get(invoiceItem.getItemId()).getVAT());
                        }

                        double vatAmt = invoiceVATMap.get(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId()).getvATAmount();
                        double vat = itemTaxRateMap.get(invoiceItem.getItemId()).getVAT();

                        double payableAmt = invoiceItem.getPrice()-invoiceItem.getDiscountPrice();
                        invoiceVATMap.get(itemTaxRateMap.get(invoiceItem.getItemId()).getTaxationId()).setvATAmount(vatAmt + ( payableAmt - (payableAmt * ( 100 / (100 + vat) ))));
                        invoiceVAT +=  ( payableAmt - (payableAmt * ( 100 / (100 + vat) )) );
                    }
                }
            }

            invoice.setvAT(invoiceVAT);
            invoice.setInvoiceVATs(new ArrayList<InvoiceVAT>(invoiceVATMap.values()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
