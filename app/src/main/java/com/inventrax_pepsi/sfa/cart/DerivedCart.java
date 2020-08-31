/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inventrax_pepsi.sfa.cart;

import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.interfaces.SKUListView;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manikanta
 */
public class DerivedCart {

    private HashMap<Integer, List<OrderItem>> cartItems;
    private Integer outletId;
    private Integer noOfSku;
    private double noOfCases;
    private double noOfBottles;
    private double noOfFreesInCase;
    private double noOfFreesInBottles;
    private double derivedPrice;
    private double discountPrice;
    private double price;
    private SKUListView skuListView;
    private DatabaseHelper databaseHelper;
    private TableVehicleStock tableVehicleStock;
    private Customer customer;

    public DerivedCart(Customer customer) {

        this.customer=customer;
        cartItems = new HashMap<>();
        outletId = 0;
        noOfSku = 0;
        derivedPrice = 0.0f;
        discountPrice = 0.0f;
        price = 0.0f;
        noOfCases = 0.0f;
        noOfBottles = 0.0f;
        noOfFreesInCase = 0.0f;
        noOfFreesInBottles = 0.0f;
        databaseHelper = DatabaseHelper.getInstance();
        tableVehicleStock = databaseHelper.getTableVehicleStock();

    }

    public void setSkuListView(SKUListView skuListView) {
        this.skuListView = skuListView;
    }

    public double getNoOfCases() {
        return noOfCases;
    }

    public void setNoOfCases(double noOfCases) {
        this.noOfCases = noOfCases;
    }

    public double getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(double noOfBottles) {
        this.noOfBottles = noOfBottles;
    }

    public double getNoOfFreesInCase() {
        return noOfFreesInCase;
    }

    public void setNoOfFreesInCase(double noOfFreesInCase) {
        this.noOfFreesInCase = noOfFreesInCase;
    }

    public double getNoOfFreesInBottles() {
        return noOfFreesInBottles;
    }

    public void setNoOfFreesInBottles(double noOfFreesInBottles) {
        this.noOfFreesInBottles = noOfFreesInBottles;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public HashMap<Integer, List<OrderItem>> getCartItems() {
        return cartItems;
    }

    public void setCartItems(HashMap<Integer, List<OrderItem>> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getOutletId() {
        return outletId;
    }

    public void setOutletId(Integer outletId) {
        this.outletId = outletId;
    }

    public Integer getNoOfSku() {
        return noOfSku;
    }

    public void setNoOfSku(Integer noOfSku) {
        this.noOfSku = noOfSku;
    }

    public double getDerivedPrice() {
        return derivedPrice;
    }

    public void setDerivedPrice(double derivedPrice) {
        this.derivedPrice = derivedPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }


    private boolean isPackUoM(List<ItemUoM> itemUoMs) {

        if (itemUoMs != null && itemUoMs.size() > 0) {
            for (ItemUoM itemUoM : itemUoMs) {
                if (itemUoM.getUomCode().trim().equals("PACK")) {
                    return true;
                }
            }

        }
        return false;

    }



    public void AddItemToCart(Item item, double fullCaseQty, double fullBottleQty, int customerGroupId,SchemeOfferItem schemeOfferItem) {
        try {

            double selectedMRP=0;

            if(item.getItemPrices()!=null)
            {
                for (ItemPrice itemPrice:item.getItemPrices())
                {
                    if (itemPrice.isUserSelected() && itemPrice.getUoMCode().equalsIgnoreCase("FB"))
                    {
                        selectedMRP=itemPrice.getmRP();
                        break;
                    }
                }
            }

            //need to prepare two order items with two quantities.
            RebateCalculator rebateCal = new RebateCalculator(customer);

            List<OrderItem> orderItems = null;
            boolean isPack = isPackUoM(item.getItemUoMs());

            if (!cartItems.containsKey(item.getItemId())) {
                orderItems = new ArrayList<>();

                if (fullBottleQty > 0) {

                    OrderItem orderItem = new OrderItem();

                    orderItem.setItemId(item.getItemId());
                    orderItem.setUoMCode("FB");
                    orderItem.setUoM("FB");
                    orderItem.setUoMId(1);
                    orderItem.setQuantity(fullBottleQty);
                    orderItem.setItemBrand(item.getItemBrand());
                    orderItem.setItemPack(item.getItemPack());
                    orderItem.setItemType(item.getItemType());
                    orderItem.setItemTypeId(item.getItemTypeId());
                    orderItem.setImageName(item.getImageName());
                    orderItem.setSelectedOfferItemId((schemeOfferItem !=null)?schemeOfferItem.getSchemeItemId():0);
                    orderItem.setSelectedSchemeId((schemeOfferItem !=null)?schemeOfferItem.getSchemeId():0);
                    orderItem.setItemCode(item.getItemCode());

                    orderItem.setMRP(selectedMRP);

                    orderItems.add(orderItem);

                }

                if (fullCaseQty > 0) {

                    OrderItem orderItem = new OrderItem();

                    orderItem.setItemId(item.getItemId());
                    orderItem.setUoMCode(isPack ? "PACK" : "FC");
                    orderItem.setUoM(isPack ? "PACK" : "FC");
                    orderItem.setUoMId((isPack ? 3 : 2));
                    orderItem.setQuantity(fullCaseQty);
                    orderItem.setItemBrand(item.getItemBrand());
                    orderItem.setItemPack(item.getItemPack());
                    orderItem.setItemType(item.getItemType());
                    orderItem.setItemTypeId(item.getItemTypeId());
                    orderItem.setImageName(item.getImageName());

                    orderItem.setItemCode(item.getItemCode());

                    orderItem.setSelectedOfferItemId((schemeOfferItem !=null)?schemeOfferItem.getSchemeItemId():0);
                    orderItem.setSelectedSchemeId((schemeOfferItem !=null)?schemeOfferItem.getSchemeId():0);

                    orderItem.setMRP(selectedMRP);

                    orderItems.add(orderItem);

                }

                cartItems.put(item.getItemId(), orderItems);

            } else {

                orderItems = cartItems.get(item.getItemId());
                boolean hadCartEntry = false;

                for (OrderItem orderItem : orderItems) {

                    if (fullCaseQty > 0 && orderItem.getUoMCode().equals(isPack ? "PACK" : "FC")) {
                        orderItem.setQuantity(orderItem.getQuantity() + fullCaseQty);
                        orderItem.setOrderItemDiscount(null);
                        orderItem.setOrderItemSchemes(null);
                        orderItem.setSelectedOfferItemId((schemeOfferItem !=null)?schemeOfferItem.getSchemeItemId():0);
                        orderItem.setSelectedSchemeId((schemeOfferItem !=null)?schemeOfferItem.getSchemeId():0);
                        hadCartEntry=true;

                        orderItem.setMRP(selectedMRP);

                    } else if (fullBottleQty > 0 && orderItem.getUoMCode().equals("FB")) {

                        orderItem.setQuantity(orderItem.getQuantity() + fullBottleQty);
                        orderItem.setOrderItemDiscount(null);
                        orderItem.setOrderItemSchemes(null);
                        orderItem.setSelectedOfferItemId((schemeOfferItem !=null)?schemeOfferItem.getSchemeItemId():0);
                        orderItem.setSelectedSchemeId((schemeOfferItem !=null)?schemeOfferItem.getSchemeId():0);
                        hadCartEntry=true;

                        orderItem.setMRP(selectedMRP);
                    }
                }

                hadCartEntry = (fullBottleQty > 0 && fullCaseQty > 0 && orderItems.size() < 2) ? false : hadCartEntry;

                if (!hadCartEntry) {
                    String uomCode = (orderItems.get(0).getUoMCode().equals("FB") ? (isPack ? "PACK" : "FC") : "FB");
                    double qty = (orderItems.get(0).getUoMCode().equals((isPack ? "PACK" : "FC")) ? fullBottleQty : fullCaseQty);
                    OrderItem mOrderItem = new OrderItem();
                    mOrderItem.setItemId(item.getItemId());
                    mOrderItem.setUoMCode(uomCode);
                    mOrderItem.setUoM(uomCode);
                    mOrderItem.setUoMId((orderItems.get(0).getUoMCode().equals("FB") ? (isPack ? 3 : 2) : 1));
                    mOrderItem.setQuantity(qty);
                    mOrderItem.setItemBrand(item.getItemBrand());
                    mOrderItem.setItemPack(item.getItemPack());
                    mOrderItem.setItemType(item.getItemType());
                    mOrderItem.setItemTypeId(item.getItemTypeId());
                    mOrderItem.setImageName(item.getImageName());
                    mOrderItem.setSelectedOfferItemId((schemeOfferItem !=null)?schemeOfferItem.getSchemeItemId():0);
                    mOrderItem.setSelectedSchemeId((schemeOfferItem !=null)?schemeOfferItem.getSchemeId():0);

                    mOrderItem.setItemCode(item.getItemCode());

                    mOrderItem.setMRP(selectedMRP);

                    orderItems.add(mOrderItem);

                }

            }


            //this check only for ready selling.
            //don't require stock check for pre selling.
            if ((AppController.getUser().getUserTypeId() != 7) || new OrderUtil().findEnoughStockAvailOrNot(orderItems,selectedMRP))
            {
                List<OrderItem> orderItemsDerived = rebateCal.getItemWithCalculatedPrice(customerGroupId, item, orderItems);

                if (orderItemsDerived != null && orderItemsDerived.size() > 0)
                {

                    this.setNoOfSku(cartItems.size());
                    this.setDiscountPrice(0);
                    this.setDerivedPrice(0);
                    this.setPrice(0);
                    this.setNoOfBottles(0);
                    this.setNoOfCases(0);
                    this.setNoOfFreesInCase(0);
                    this.setNoOfFreesInBottles(0);

                    for (Map.Entry<Integer, List<OrderItem>> entry : cartItems.entrySet()) {

                        for (OrderItem orderItem : entry.getValue())
                        {

                            if (orderItem.getPrice() > 0)
                            {
                                if (orderItem.getUoMCode().equals("FB")) {
                                    this.setNoOfBottles(this.getNoOfBottles() + orderItem.getQuantity());
                                } else {
                                    this.setNoOfCases(this.getNoOfCases() + orderItem.getQuantity());
                                }

                                if (orderItem.getOrderItemSchemes() != null) {
                                    for (OrderItemScheme orderItemScheme : orderItem.getOrderItemSchemes()) {
                                        if (orderItemScheme.getUoM().equals("FB")) {
                                            this.setNoOfFreesInBottles(this.getNoOfFreesInBottles() + orderItemScheme.getValue());
                                        } else {
                                            this.setNoOfFreesInCase(this.getNoOfFreesInCase() + orderItemScheme.getValue());
                                        }
                                    }
                                }

                                // Discount  Type Id : 1 --> Bottle Discount
                                if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot())
                                {
                                    if (orderItem.getOrderItemDiscount().getUoM().equalsIgnoreCase("FB")) {
                                        this.setNoOfFreesInBottles(this.getNoOfFreesInBottles() + orderItem.getOrderItemDiscount().getValue());
                                    } else {
                                        this.setNoOfFreesInCase(this.getNoOfFreesInCase() + orderItem.getOrderItemDiscount().getValue());
                                    }
                                }

                                this.setPrice(this.getPrice() + orderItem.getPrice());

                                if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().isSpot()) {
                                    this.setDerivedPrice(this.getDerivedPrice() + orderItem.getDerivedPrice());
                                    this.setDiscountPrice(this.getDiscountPrice() + orderItem.getDiscountPrice());
                                } else {
                                    this.setDerivedPrice(this.getDerivedPrice() + orderItem.getPrice());
                                }

                                skuListView.showMessage(item.getItemBrand() + " " + item.getItemPack()   + " added to the cart",false);

                            }
                        }
                    }
                }
            } else
            {


                //write some message on it.
                //VehicleStock vanStock = tableVehicleStock.getVehicleStock(item.getItemId());
                VehicleStock vanStock = tableVehicleStock.getVehicleStock(item.getItemId(),selectedMRP);

                if (skuListView != null && vanStock != null) {

                    // Remove SKU from cart
                    for (ItemUoM itemUoM : item.getItemUoMs()) {
                        deleteCartItem(item.getItemId(), itemUoM.getUomCode());
                    }

                    if ((vanStock.getCaseQuantity() == 0) && (vanStock.getBottleQuantity() == 0)) {

                        skuListView.showMessage("No Stock Available",true);

                    } else {
                        skuListView.showMessage("Available Stock " + (((vanStock.getCaseQuantity() != 0 ? " Cases : " + (int) vanStock.getCaseQuantity() : "") + (vanStock.getBottleQuantity() != 0 ? " Bottles : " + (int) vanStock.getBottleQuantity() : ""))),true);
                    }

                } else {

                    for (ItemUoM itemUoM : item.getItemUoMs()) {
                        deleteCartItem(item.getItemId(), itemUoM.getUomCode());
                    }

                    skuListView.showMessage("No Stock Available",true);
                }
            }
        } catch (Exception ex) {
            Logger.Log(DerivedCart.class.getName(), ex);
            return;
        }

    }


    public void deleteCartItem(int itemId, String uomCode) {

        if (itemId != 0 && uomCode != null) {

            if (cartItems.containsKey(itemId))
            {
                int index = 0;
                boolean found = false;
                for (OrderItem orderItem : cartItems.get(itemId)) {
                    if (orderItem.getUoMCode().equals(uomCode)) {
                        found = true;
                        break;
                    }
                    index++;
                }

                if (found) {

                    OrderItem orderItem = cartItems.get(itemId).get(index);

                    this.setDerivedPrice(this.getDerivedPrice() - orderItem.getDerivedPrice());
                    this.setDiscountPrice(this.getDiscountPrice() - orderItem.getDiscountPrice());
                    this.setPrice(this.getPrice() - orderItem.getPrice());

                    if (uomCode.trim().equals("FB")) {
                        this.setNoOfBottles(this.getNoOfBottles() - orderItem.getQuantity());
                    } else {
                        this.setNoOfCases(this.getNoOfCases() - orderItem.getQuantity());
                    }

                    //applied scheme items need to remove from the cart.
                    if (orderItem.getOrderItemSchemes() != null && orderItem.getOrderItemSchemes().size() > 0) {
                        for (OrderItemScheme itemScheme : orderItem.getOrderItemSchemes()) {
                            if (itemScheme.getUoM().trim().equals("FB")) {
                                this.setNoOfFreesInBottles(this.getNoOfFreesInBottles() - itemScheme.getValue());
                            } else {
                                this.setNoOfFreesInCase(this.getNoOfFreesInCase() - itemScheme.getValue());
                            }
                        }
                    }

                    //applied discounts need to remove from the cart.
                    if (orderItem.getOrderItemDiscount() != null) {

                        if (orderItem.getOrderItemDiscount().getDiscountTypeId() == 1) {
                            if (orderItem.getOrderItemDiscount().getUoMCode().trim().equals("FB")) {
                                this.setNoOfFreesInBottles(this.getNoOfFreesInBottles() - orderItem.getOrderItemDiscount().getValue());
                            } else {
                                this.setNoOfFreesInCase(this.getNoOfFreesInCase() - orderItem.getOrderItemDiscount().getValue());
                            }
                        }
                    }

                    if (cartItems.get(itemId).size() == 1) {
                        cartItems.remove(itemId);
                        //this.setNoOfSku(this.getNoOfSku() - 1); //Commented by naresh

                        // Added by naresh
                        this.setNoOfSku(cartItems.size());
                    } else {

                        cartItems.get(itemId).remove(index);

                        // Added by naresh
                        this.setNoOfSku(cartItems.size());

                    }

                }
            }
        }
    }


    public  DerivedCart getRecalculatedCart(DerivedCart derivedCart )
    {

        if (derivedCart != null && derivedCart.getCartItems().size() > 0)
        {

            derivedCart.setNoOfSku(derivedCart.getCartItems().size());
            derivedCart.setDiscountPrice(0);
            derivedCart.setDerivedPrice(0);
            derivedCart.setPrice(0);
            derivedCart.setNoOfBottles(0);
            derivedCart.setNoOfCases(0);
            derivedCart.setNoOfFreesInCase(0);
            derivedCart.setNoOfFreesInBottles(0);

            for (Map.Entry<Integer, List<OrderItem>> entry : derivedCart.getCartItems().entrySet()) {

                for (OrderItem orderItem : entry.getValue())
                {

                    if (orderItem.getPrice() > 0)
                    {
                        if (orderItem.getUoMCode().equals("FB")) {
                            derivedCart.setNoOfBottles(derivedCart.getNoOfBottles() + orderItem.getQuantity());
                        } else {
                            derivedCart.setNoOfCases(derivedCart.getNoOfCases() + orderItem.getQuantity());
                        }

                        if (orderItem.getOrderItemSchemes() != null) {
                            for (OrderItemScheme orderItemScheme : orderItem.getOrderItemSchemes()) {
                                if (orderItemScheme.getUoM().equals("FB")) {
                                    derivedCart.setNoOfFreesInBottles(derivedCart.getNoOfFreesInBottles() + orderItemScheme.getValue());
                                } else {
                                    derivedCart.setNoOfFreesInCase(derivedCart.getNoOfFreesInCase() + orderItemScheme.getValue());
                                }
                            }
                        }

                        // Discount  Type Id : 1 --> Bottle Discount
                        if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot())
                        {
                            if (orderItem.getOrderItemDiscount().getUoM().equalsIgnoreCase("FB")) {
                                derivedCart.setNoOfFreesInBottles(derivedCart.getNoOfFreesInBottles() + orderItem.getOrderItemDiscount().getValue());
                            } else {
                                derivedCart.setNoOfFreesInCase(derivedCart.getNoOfFreesInCase() + orderItem.getOrderItemDiscount().getValue());
                            }
                        }

                        derivedCart.setPrice(derivedCart.getPrice() + orderItem.getPrice());
                        if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().isSpot()) {
                            derivedCart.setDerivedPrice(derivedCart.getDerivedPrice() + orderItem.getDerivedPrice());
                            derivedCart.setDiscountPrice(derivedCart.getDiscountPrice() + orderItem.getDiscountPrice());
                        } else {
                            derivedCart.setDerivedPrice(derivedCart.getDerivedPrice() + orderItem.getPrice());
                        }
                    }
                }
            }
        }


        return derivedCart;

    }

}
