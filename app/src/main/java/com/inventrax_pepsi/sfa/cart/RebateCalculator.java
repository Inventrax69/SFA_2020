/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inventrax_pepsi.sfa.cart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemDiscount;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum OutletType {

    KeyOutlet, DisplayAccount
}

/**
 * @author manikanta
 */
public class RebateCalculator {

    private DatabaseHelper databaseHelper;
    private TableItem tableItem;
    private TableScheme tableScheme;
    private Gson gson;
    private Customer customer;


    public RebateCalculator(Customer customer) {

        this.customer = customer;
        gson = new GsonBuilder().create();
        databaseHelper = DatabaseHelper.getInstance();
        tableItem = databaseHelper.getTableItem();
        tableScheme = databaseHelper.getTableScheme();

    }


   /* public RebateCalculator() {

        gson = new GsonBuilder().create();
        databaseHelper = DatabaseHelper.getInstance();
        tableItem = databaseHelper.getTableItem();
        tableScheme = databaseHelper.getTableScheme();

    }*/

    public List<OrderItem> getItemWithCalculatedPrice(Integer customerGroupId, Item item, List<OrderItem> orderItems) {

        if (customerGroupId != 0 && item != null && orderItems != null && orderItems.size() > 0) {

            boolean isKOL = false;
            boolean isDisplayOL = false;
            int errorCode = 0;
            switch (customerGroupId) {
                case 4:
                    break;
                case 5:
                case 6:
                    isKOL = true;
                    break;
                case 7:
                    isDisplayOL = true;
                    break;
                default:
                    errorCode = 36;
                    break;
            }

            if (errorCode == 0 && isDisplayOL) {

                //Display OUTLETS
                for (OrderItem orderItem : orderItems) {
                    orderItem.setDispatchQty(orderItem.getQuantity());
                    customerBasisDiscountCalculation(orderItem, item, OutletType.DisplayAccount.ordinal());
                }

            } else if (errorCode == 0 && isKOL) {

                //KEY OUTLETS
                for (OrderItem orderItem : orderItems) {
                    orderItem.setDispatchQty(orderItem.getQuantity());
                    customerBasisDiscountCalculation(orderItem, item, OutletType.KeyOutlet.ordinal());
                }

            } else if (errorCode == 0) {

                //GENERAL OUTLET
                for (OrderItem orderItem : orderItems) {
                    orderItem.setDispatchQty(orderItem.getQuantity());
                    generalOutletOrderItemPriceCalculation(orderItem, item);
                }

            } else {
                //some thing is wrong
            }
        }
        return orderItems;
    }

    private List<OrderItem> OrderItemClubingFesibility(List<OrderItem> orderItems) {
        try {

            if (orderItems != null && orderItems.size() > 1) {
                OrderItem orderItem1 = orderItems.get(0);
                OrderItem orderItem2 = orderItems.get(1);
                //24 bottles have to consider as 1 case.
            }

        } catch (Exception e) {

        }
        return null;
    }

    /* Need to find territory price details */
    private void generalOutletOrderItemPriceCalculation(OrderItem orderItem, Item item) {
        if (item.getItemRebateMap() != null && item.getItemRebateMap().size() > 0) {
            if (item.getItemRebateMap().containsKey(1)) {
                // ArrayList<ItemPrice> itemPrices = getSortedItemPrice ((ArrayList) item.getItemRebateMap().get(1));
                ArrayList<ItemPrice> itemPrices = ((ArrayList) item.getItemRebateMap().get(1));
                if (itemPrices != null) {
                    ItemPrice actualItemPrice = null;

                    for (ItemPrice itemPrice : itemPrices) {
                        // && itemPrice.isUserSelected() == true ;
                        //if ( itemPrice.getUoMId() == orderItem.getUoMId() || itemPrice.getUoMCode().equals(orderItem.getUoMCode())) {
                        if ((itemPrice.getUoMId() == orderItem.getUoMId() || itemPrice.getUoMCode().equals(orderItem.getUoMCode())) && itemPrice.isUserSelected()) {
                            actualItemPrice = itemPrice;
                            break;
                        }
                    }

                    if (actualItemPrice != null) {
                        double lineItemPrice = (customer.getBillingPriceTypeId() == 1 ? actualItemPrice.getTradePrice() : actualItemPrice.getmRP()) * orderItem.getQuantity();
                        orderItem.setPrice(lineItemPrice);
                        orderItem.setDerivedPrice(lineItemPrice);
                        orderItem.setItemPrice(customer.getBillingPriceTypeId() == 1 ? actualItemPrice.getTradePrice() : actualItemPrice.getmRP());
                        orderItem.setItemPriceId(actualItemPrice.getItemPriceId());
                        orderItem.setItemPackId(item.getItemPackId());
                        orderItem.setItemBrandId(item.getItemBrandId());

                        //Price is not belongs to customer, so we can apply discounts and schemes.
                        if (item.getItemRebateMap().containsKey(3) && item.getItemRebateMap().get(3) != null) {
                            List<Scheme> schemes = (ArrayList<Scheme>) item.getItemRebateMap().get(3);
                            ApplySchemeOnLineItem(orderItem, schemes);
                        }
                    }
                }
            }
        }
    }

    /* need to find and customer price and apply */
    private void customerBasisDiscountCalculation(OrderItem orderItem, Item item, int outletType) {

        if (item.getItemRebateMap() != null && item.getItemRebateMap().size() > 0) {
            if (item.getItemRebateMap().containsKey(1)) {
                //ArrayList<ItemPrice> itemPrices = getSortedItemPrice ( (ArrayList) item.getItemRebateMap().get(1) );
                ArrayList<ItemPrice> itemPrices = (ArrayList) item.getItemRebateMap().get(1);
                if (itemPrices != null) {
                    ItemPrice actualItemPrice = null;

                    for (ItemPrice itemPrice : itemPrices) {

                        // && itemPrice.isUserSelected() == true ;

                        //if ( itemPrice.getUoMId() == orderItem.getUoMId() || itemPrice.getUoMCode().equals(orderItem.getUoMCode())) {
                        //&& itemPrice.isUserSelected()
                        if ((itemPrice.getUoMId() == orderItem.getUoMId() || itemPrice.getUoMCode().equals(orderItem.getUoMCode())) && itemPrice.isUserSelected()) {
                            actualItemPrice = itemPrice;
                            break;
                        }
                    }

                    if (actualItemPrice != null) {
                        double lineItemPrice = (customer.getBillingPriceTypeId() == 1 ? actualItemPrice.getTradePrice() : actualItemPrice.getmRP()) * orderItem.getQuantity();
                        orderItem.setPrice(lineItemPrice);
                        orderItem.setDerivedPrice(lineItemPrice);
                        orderItem.setItemPrice(customer.getBillingPriceTypeId() == 1 ? actualItemPrice.getTradePrice() : actualItemPrice.getmRP());
                        orderItem.setItemPriceId(actualItemPrice.getItemPriceId());
                        orderItem.setItemBrandId(item.getItemBrandId());
                        orderItem.setItemPackId(item.getItemPackId());

                        //Price is not belongs to customer, so we can apply discounts and schemes.
                        // ApplicableId (3) -> For Customer

                        if (actualItemPrice.getPriceApplicableTypeId() != 3) {


                            if (outletType == OutletType.KeyOutlet.ordinal()) {

                                // KEY Outlet

                                if (item.getItemRebateMap().containsKey(2) && item.getItemRebateMap().get(2) != null) {
                                    ArrayList<CustomerDiscount> customerDisc = (ArrayList<CustomerDiscount>) item.getItemRebateMap().get(2);

                                    if (customerDisc.size() > 0) {
                                        ApplyDiscountOnLineItem(orderItem, customerDisc);
                                    } else if (item.getItemRebateMap().containsKey(3) && item.getItemRebateMap().get(3) != null) {
                                        List<Scheme> schemes = (ArrayList<Scheme>) item.getItemRebateMap().get(3);
                                        ApplySchemeOnLineItem(orderItem, schemes);
                                    }
                                }

                            } else if (outletType == OutletType.DisplayAccount.ordinal()) {

                                /*
                                // Display Outlet

                                // Discounts
                                if (item.getItemRebateMap().containsKey(2) && item.getItemRebateMap().get(2) != null) {
                                    ArrayList<CustomerDiscount> customerDisc = (ArrayList<CustomerDiscount>) item.getItemRebateMap().get(2);
                                    if (customerDisc.size() > 0) {
                                        ApplyDiscountOnLineItem(orderItem, customerDisc);
                                    }
                                }

                                // Schemes
                                if (item.getItemRebateMap().containsKey(3) && item.getItemRebateMap().get(3) != null) {
                                    List<Scheme> schemes = (ArrayList<Scheme>) item.getItemRebateMap().get(3);
                                    ApplySchemeOnLineItem(orderItem, schemes);
                                }*/

                                if (item.getItemRebateMap().containsKey(2) && item.getItemRebateMap().get(2) != null) {
                                    ArrayList<CustomerDiscount> customerDisc = (ArrayList<CustomerDiscount>) item.getItemRebateMap().get(2);

                                    if (customerDisc.size() > 0) {
                                        ApplyDiscountOnLineItem(orderItem, customerDisc);
                                    } else if (item.getItemRebateMap().containsKey(3) && item.getItemRebateMap().get(3) != null) {
                                        List<Scheme> schemes = (ArrayList<Scheme>) item.getItemRebateMap().get(3);
                                        ApplySchemeOnLineItem(orderItem, schemes);
                                    }
                                }


                            }

                        }
                    }
                }
            }
        }
    }


    private SchemeOfferItem GetSelectedSchemeOfferItem(List<SchemeOfferItem> schemeOfferItems, int selectedOfferItemId) {
        if (schemeOfferItems != null && schemeOfferItems.size() > 0 && selectedOfferItemId != 0) {
            for (SchemeOfferItem schemeOfferItem : schemeOfferItems) {
                if (schemeOfferItem.getSchemeItemId() == selectedOfferItemId)
                    return schemeOfferItem;
            }
        }
        return schemeOfferItems.get(0);

    }

    private void ApplySchemeOnLineItem(OrderItem orderItem, List<Scheme> schemes) {

        if (orderItem != null && schemes != null && schemes.size() > 0) {

            for (Scheme scheme : schemes) {

                if ((scheme.getSchemeTargetItems() != null && scheme.getSchemeTargetItems().size() > 0)
                        && ((orderItem.getSelectedSchemeId() != 0 && orderItem.getSelectedSchemeId() == scheme.getSchemeId()) || orderItem.getSelectedSchemeId() == 0)) {

                    if (scheme.getSchemeTargetItems().get(0).getItemId() == orderItem.getItemId()
                            && (scheme.getSchemeTargetItems().get(0).getUoMId() == orderItem.getUoMId()
                                || ((scheme.getSchemeTargetItems().get(0).getUnits() / scheme.getSchemeOfferItems().get(0).getQuantity()) >= 1
                                    && (orderItem.getQuantity() / (scheme.getSchemeTargetItems().get(0).getUnits() / scheme.getSchemeOfferItems().get(0).getQuantity())) >= 1))

                            && ( scheme.getSchemeTargetItems().get(0).getMRP() == orderItem.getMRP() || scheme.getSchemeTargetItems().get(0).getMRP()==0)
                            && scheme.getSchemeTargetItems().get(0).getQuantity() <= orderItem.getQuantity())
                    {

                        orderItem.setOrderItemSchemes(new ArrayList<OrderItemScheme>());
                        if (scheme.isSingleOffer()) {

                            SchemeOfferItem offerItem = GetSelectedSchemeOfferItem(scheme.getSchemeOfferItems(), orderItem.getSelectedOfferItemId());
                            OrderItemScheme orderScheme = new OrderItemScheme();
                            orderScheme.setItemCode(offerItem.getItemCode());
                            orderScheme.setItemId(offerItem.getItemId());
                            orderScheme.setItemName(offerItem.getItemCode());
                            orderScheme.setItemUoMId(offerItem.getItemUoMId());

                            ItemPrice itemPrice = GetItemPrice(offerItem.getItemId(), offerItem.getUoMId());
                            if (itemPrice != null) {
                                orderScheme.setPrice(itemPrice.getTradePrice() * (((int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity())) * offerItem.getQuantity()));
                                orderScheme.setItemPrice(itemPrice.getTradePrice());
                                orderScheme.setItemPriceId(itemPrice.getItemPriceId());
                                orderScheme.setMRP(GetItemMRP(offerItem.getItemId()));
                            }
                            orderScheme.setValue(
                                    scheme.getSchemeTargetItems().get(0).getUoMId() == orderItem.getUoMId()
                                    ?((int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity())) * offerItem.getQuantity()
                                    :Math.floor( (orderItem.getQuantity() / (scheme.getSchemeTargetItems().get(0).getUnits() / scheme.getSchemeOfferItems().get(0).getQuantity())) ));


                            orderScheme.setQuantity(
                                    scheme.getSchemeTargetItems().get(0).getUoMId() == orderItem.getUoMId()
                                    ?(int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity())
                                    :1);

                            orderScheme.setSchemeCode(scheme.getSchemeCode());
                            orderScheme.setSchemeId(scheme.getSchemeId());
                            orderScheme.setUoM(offerItem.getUoM());
                            orderScheme.setUoMId(offerItem.getUoMId());
                            orderScheme.setItemBrand(offerItem.getItemBrand());
                            orderScheme.setItemPack(offerItem.getItemPack());
                            orderScheme.setImageName(offerItem.getImageName());
                            orderScheme.setItemType(offerItem.getItemType());
                            orderItem.getOrderItemSchemes().add(orderScheme);
                            break;
                        } else {
                            if (scheme.getSchemeOfferItems() != null)
                                for (SchemeOfferItem offerItem : scheme.getSchemeOfferItems()) {
                                    OrderItemScheme orderScheme = new OrderItemScheme();

                                    orderScheme.setItemCode(offerItem.getItemCode());
                                    orderScheme.setItemId(offerItem.getItemId());
                                    orderScheme.setItemName(offerItem.getItemCode());
                                    orderScheme.setItemUoMId(offerItem.getItemUoMId());
                                    ItemPrice itemPrice = GetItemPrice(offerItem.getItemId(), offerItem.getUoMId());

                                    if (itemPrice != null) {
                                        orderScheme.setPrice(itemPrice.getTradePrice() * (((int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity())) * offerItem.getQuantity()));
                                        orderScheme.setItemPrice(itemPrice.getTradePrice());
                                        orderScheme.setItemPriceId(itemPrice.getItemPriceId());
                                        orderScheme.setMRP(GetItemMRP(offerItem.getItemId()));
                                    }

                                    orderScheme.setValue( ( (int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity()) ) * offerItem.getQuantity());
                                    orderScheme.setQuantity( (int) (orderItem.getQuantity() / scheme.getSchemeTargetItems().get(0).getQuantity()));
                                    orderScheme.setSchemeCode(scheme.getSchemeCode());
                                    orderScheme.setSchemeId(scheme.getSchemeId());
                                    orderScheme.setUoM(offerItem.getUoM());
                                    orderScheme.setUoMId(offerItem.getUoMId());
                                    orderScheme.setItemBrand(offerItem.getItemBrand());
                                    orderScheme.setItemPack(offerItem.getItemPack());
                                    orderScheme.setImageName(offerItem.getImageName());
                                    orderScheme.setItemType(offerItem.getItemType());
                                    orderItem.getOrderItemSchemes().add(orderScheme);
                                }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void ApplyDiscountOnLineItem(OrderItem orderItem, ArrayList<CustomerDiscount> discounts) {

        boolean discountEnable = false;
        if (discounts != null)
            for (CustomerDiscount discount : discounts) {

                if (orderItem != null && discount != null && discount.getDiscountTypeId() != 0) {

                    if (orderItem.getItemId() == discount.getTargetItemId() && discount.getDiscountValue() != 0) {
                        switch (discount.getDiscountTypeId()) {
                            case 1:
                                getBottleDiscount(orderItem, discount);
                                discountEnable = true;
                                break;
                            case 2:
                                getPercentageDiscount(orderItem, discount);
                                discountEnable = true;
                                break;
                            case 3:
                                getFixedDiscount(orderItem, discount);
                                discountEnable = true;
                                break;
                            case 4:
                                break;
                        }
                        if (discountEnable) {
                            break;
                        }
                    }
                }
            }
    }

    private OrderItemDiscount getBottleDiscount(OrderItem orderItem, CustomerDiscount discount) {

        OrderItemDiscount lineDiscount = new OrderItemDiscount();
        lineDiscount.setDiscountCode(discount.getDiscountCode());
        lineDiscount.setDiscountId(discount.getDiscountId());
        lineDiscount.setDiscountType(discount.getDiscountType());
        lineDiscount.setDiscountTypeId(discount.getDiscountTypeId());
        lineDiscount.setIsDiscountItem(true);
        lineDiscount.setIsSpot(discount.isSpot());
        lineDiscount.setItemCode(discount.getOfferItemCode());
        lineDiscount.setItemId(discount.getOfferItemId());
        lineDiscount.setItemUoMId(discount.getOfferItemUoMId());
        lineDiscount.setUomId(discount.getOfferUoMId());
        lineDiscount.setValue(discount.getOfferQty() * ((int) (orderItem.getQuantity() / discount.getTargetItemQty())));
        lineDiscount.setQuantity((int) (orderItem.getQuantity() / discount.getTargetItemQty()));
        lineDiscount.setUoMCode(discount.getOfferUoMCode());
        lineDiscount.setUoM(discount.getOfferUoMCode());

        //As per the customer requirement; only FC discounts are configured.
        if (orderItem.getUoMId() == 1 && discount.getTargetUoMUnits() != 0) {
            lineDiscount.setValue((int) ((discount.getOfferQty() / discount.getTargetUoMUnits()) * orderItem.getQuantity()));
            lineDiscount.setQuantity(1);
        }

        //need to initialize..
        ItemPrice itemPrice = GetItemPrice(discount.getOfferItemId(), discount.getOfferUoMId());
        if (itemPrice != null) {
            lineDiscount.setDiscountPrice(itemPrice.getTradePrice() * ((int) (orderItem.getQuantity() / discount.getTargetItemQty())));
            lineDiscount.setItemPrice(itemPrice.getTradePrice());
            lineDiscount.setItemPriceId(itemPrice.getItemPriceId());
            lineDiscount.setDiscountValue(discount.getOfferQty() * ((int) (orderItem.getQuantity() / discount.getTargetItemQty())));
            lineDiscount.setMRP(GetItemMRP(discount.getOfferItemId()));
        }

        orderItem.setOrderItemDiscount(lineDiscount);
        return lineDiscount;

    }

    private OrderItemDiscount getPercentageDiscount(OrderItem orderItem, CustomerDiscount discount) {

        if (discount.getDiscountValue() != 0 && discount.getDiscountValue() < 100) {

            OrderItemDiscount lineDiscount = new OrderItemDiscount();
            lineDiscount.setDiscountCode(discount.getDiscountCode());
            lineDiscount.setDiscountId(discount.getDiscountId());
            lineDiscount.setDiscountType(discount.getDiscountType());
            lineDiscount.setDiscountTypeId(discount.getDiscountTypeId());
            lineDiscount.setIsDiscountItem(true);

            if (orderItem.getUoMId() != 1) {
                orderItem.setDiscountPrice((orderItem.getItemPrice() * discount.getTargetItemQty()) * ((int) (orderItem.getQuantity() / discount.getTargetItemQty())) * (discount.getDiscountValue() * 0.01));
                lineDiscount.setDiscountPrice(((orderItem.getItemPrice() * discount.getTargetItemQty()) * ((int) (orderItem.getQuantity() / discount.getTargetItemQty()))) * (discount.getDiscountValue() * 0.01));
            } else {
                orderItem.setDiscountPrice(orderItem.getItemPrice() * orderItem.getQuantity() * (discount.getDiscountValue() * 0.01));
                lineDiscount.setDiscountPrice(orderItem.getItemPrice() * orderItem.getQuantity() * (discount.getDiscountValue() * 0.01));
            }

            if (discount.isSpot())
                orderItem.setDerivedPrice(orderItem.getPrice() - orderItem.getDiscountPrice());
            else
                orderItem.setDerivedPrice(orderItem.getPrice());


            lineDiscount.setValue(lineDiscount.getDiscountPrice());
            lineDiscount.setDiscountValue(discount.getDiscountValue());
            lineDiscount.setIsSpot(discount.isSpot());
            lineDiscount.setItemCode(discount.getTargetItemCode());
            lineDiscount.setItemId(discount.getTargetItemId());
            lineDiscount.setItemUoMId(discount.getTargetItemUoMId());
            lineDiscount.setUomId(discount.getTargetUoMId());
            lineDiscount.setQuantity((int) (orderItem.getQuantity() / discount.getTargetItemQty()));
            lineDiscount.setUoMCode(discount.getTargetUoMCode());
            lineDiscount.setUoM(discount.getTargetUoMCode());
            orderItem.setOrderItemDiscount(lineDiscount);
            lineDiscount.setItemPrice(0);
            lineDiscount.setItemPriceId(0);

            return lineDiscount;
        } else {
            return null;
        }

    }

    private OrderItemDiscount getFixedDiscount(OrderItem orderItem, CustomerDiscount discount) {

        OrderItemDiscount lineDiscount = new OrderItemDiscount();
        lineDiscount.setDiscountCode(discount.getDiscountCode());
        lineDiscount.setDiscountId(discount.getDiscountId());
        lineDiscount.setDiscountType(discount.getDiscountType());
        lineDiscount.setDiscountTypeId(discount.getDiscountTypeId());
        lineDiscount.setIsDiscountItem(true);

        if (orderItem.getUoMId() == 2) {

            orderItem.setDiscountPrice(((int) (orderItem.getQuantity() / discount.getTargetItemQty())) * (discount.getDiscountValue()));
            lineDiscount.setDiscountPrice(((int) (orderItem.getQuantity() / discount.getTargetItemQty())) * (discount.getDiscountValue()));
            lineDiscount.setDiscountValue(discount.getDiscountValue());

        } else {
            orderItem.setDiscountPrice(orderItem.getQuantity() * (discount.getDiscountValue() / discount.getTargetUoMUnits()));
            lineDiscount.setDiscountPrice(orderItem.getQuantity() * (discount.getDiscountValue() / discount.getTargetUoMUnits()));
            lineDiscount.setDiscountValue(discount.getDiscountValue() / discount.getTargetUoMUnits());
        }

        lineDiscount.setIsSpot(discount.isSpot());
        lineDiscount.setItemCode(discount.getTargetItemCode());
        lineDiscount.setItemId(discount.getTargetItemId());
        lineDiscount.setItemUoMId(discount.getTargetItemUoMId());
        lineDiscount.setUomId(discount.getTargetUoMId());
        lineDiscount.setValue(lineDiscount.getDiscountPrice());
        lineDiscount.setQuantity((int) (orderItem.getQuantity() / discount.getTargetItemQty()));
        lineDiscount.setUoMCode(discount.getTargetUoMCode());
        lineDiscount.setUoM(discount.getTargetUoMCode());

        if (discount.isSpot())
            orderItem.setDerivedPrice(orderItem.getPrice() - orderItem.getDiscountPrice());
        else
            orderItem.setDerivedPrice(orderItem.getPrice());

        lineDiscount.setItemPrice(0);
        lineDiscount.setItemPriceId(0);
        orderItem.setOrderItemDiscount(lineDiscount);
        return lineDiscount;
    }

    private ItemPrice GetItemPrice(int itemId, int uomId) {

        com.inventrax_pepsi.database.pojos.Item localItem = tableItem.getItem(itemId);
        if (localItem == null)
            return null;
        List<ItemPrice> itemPrices = gson.fromJson(localItem.getPriceJSON(), new TypeToken<List<ItemPrice>>() {
        }.getType());

        if (itemPrices == null)
            return null;

        for (ItemPrice itemPrice : itemPrices) {
            if (itemPrice.getUoMId() == uomId)
                return itemPrice;
        }

        return null;
    }


    private double GetItemMRP(int itemId) {

        com.inventrax_pepsi.database.pojos.Item localItem = tableItem.getItem(itemId);
        if (localItem == null)
            return 0;
        List<ItemPrice> itemPrices = gson.fromJson(localItem.getPriceJSON(), new TypeToken<List<ItemPrice>>() {
        }.getType());

        if (itemPrices == null || itemPrices.size() == 0)
            return 0;

        Collections.sort((ArrayList) itemPrices, new Comparator<ItemPrice>() {

            @Override
            public int compare(ItemPrice lhs, ItemPrice rhs) {

                return (lhs.getItemPriceId() >= rhs.getItemPriceId()) ? -1 : 1;

            }
        });

        for (ItemPrice itemPrice : itemPrices) {
            if (itemPrice.getUoMCode().equalsIgnoreCase("FB"))
                return itemPrice.getmRP();
        }

        return 0;
    }


    private int getItemFCUnits(List<ItemUoM> uoms) {
        for (ItemUoM uom : uoms) {
            if (uom.getUoMId() == 2)
                return uom.getUnits();
        }
        return 0;
    }


    public Order applyCustomizedSchemeOnOrder(Order order, HashMap<Integer, Item> territoryPrices, HashMap<Integer, List<Scheme>> territorySchemes) {
        try {

            if (order != null && order.getOrderItems() != null && order.getOrderItems().size() > 0 && territoryPrices != null && territoryPrices.size() > 0 && territorySchemes != null && territorySchemes.size() > 0) {

                HashMap<String, Double> packWiseBottles = new HashMap<>();
                HashMap<String, List<Integer>> allowedItems = new HashMap<>();

                for (OrderItem orderItem : order.getOrderItems()) {

                    int FCUnits = getItemFCUnits(territoryPrices.get(orderItem.getItemId()).getItemUoMs());
                    String UniqueKey = FCUnits + "_" + (Math.round(orderItem.getMRP() * 100.0) / 100.0);
                    if (orderItem.getOrderItemDiscount() == null && orderItem.getOrderItemSchemes() == null && orderItem.getUoMId() == 1) {
                        if (!packWiseBottles.containsKey(UniqueKey)) {
                            allowedItems.put(UniqueKey, new ArrayList<Integer>());
                            packWiseBottles.put(UniqueKey, orderItem.getDispatchQty());
                            allowedItems.get(UniqueKey).add(orderItem.getItemId());
                        } else {
                            packWiseBottles.put(UniqueKey, packWiseBottles.get(UniqueKey) + orderItem.getDispatchQty());
                            allowedItems.get(UniqueKey).add(orderItem.getItemId());
                        }
                    }
                }

                if (packWiseBottles.size() > 0) {
                    HashMap<String, OrderItemScheme> customizedSchemes = new HashMap<>();
                    for (Map.Entry<String, Double> entry : packWiseBottles.entrySet()) {
                        boolean isSchemeFound = false;
                        int units = new Integer(entry.getKey().split("_")[0].trim());
                        double schItemPrice = new Double(entry.getKey().split("_")[1].trim());
                        double bottleCnt = entry.getValue();

                        for (Map.Entry<Integer, Item> priceEntry : territoryPrices.entrySet()) {

                            if (!allowedItems.get(entry.getKey()).contains(priceEntry.getValue().getItemId()))
                                continue;

                            for (ItemPrice itemPrice : priceEntry.getValue().getItemPrices()) {
                                String UniqueKey = itemPrice.getUnits() + "_" + (Math.round((itemPrice.getmRP() / itemPrice.getUnits()) * 100.0) / 100.0);

                                if (UniqueKey.trim().equals(entry.getKey())) {
                                    for (ItemUoM itemuom : priceEntry.getValue().getItemUoMs()) {
                                        ////Need to put && condition (for flavour and price)
                                        if (units == itemPrice.getUnits() && itemuom.getUoMId() != 1 && itemuom.getUnits() <= bottleCnt) {
                                            for (Map.Entry<Integer, List<Scheme>> schemeEntry : territorySchemes.entrySet()) {

                                                for (Scheme scheme : schemeEntry.getValue()) {

                                                    if (allowedItems.containsKey(UniqueKey) && allowedItems.get(UniqueKey).contains(scheme.getSchemeTargetItems().get(0).getItemId())
                                                            && itemuom.getUoMId() == scheme.getSchemeTargetItems().get(0).getUoMId()
                                                            && scheme.getSchemeTargetItems().get(0).getQuantity() <= new Double((int) (bottleCnt / itemuom.getUnits()))) {

                                                        if ((territoryPrices.containsKey(scheme.getSchemeOfferItems().get(0).getItemId())
                                                                && schItemPrice == Math.round(getItemPriceForSchemeOfferItem("FB", territoryPrices.get(scheme.getSchemeTargetItems().get(0).getItemId()).getItemPrices(), schItemPrice).getmRP() * 100.0) / 100.0)
                                                                && scheme.getSchemeOfferItems() != null && scheme.getSchemeOfferItems().size() > 0) {

                                                            OrderItemScheme orderItemScheme = new OrderItemScheme();
                                                            orderItemScheme.setItemCode(scheme.getSchemeOfferItems().get(0).getItemCode());
                                                            orderItemScheme.setItemId(scheme.getSchemeOfferItems().get(0).getItemId());
                                                            orderItemScheme.setItemUoMId(scheme.getSchemeOfferItems().get(0).getItemUoMId());
                                                            orderItemScheme.setValue(scheme.getSchemeOfferItems().get(0).getQuantity() * (((int) (bottleCnt / itemuom.getUnits())) / scheme.getSchemeTargetItems().get(0).getQuantity()));
                                                            orderItemScheme.setQuantity(((int) (bottleCnt / itemuom.getUnits())) / scheme.getSchemeTargetItems().get(0).getQuantity());
                                                            orderItemScheme.setSchemeCode(scheme.getSchemeCode());
                                                            orderItemScheme.setUoM(scheme.getSchemeOfferItems().get(0).getUoMCode());
                                                            orderItemScheme.setSchemeId(scheme.getSchemeId());
                                                            orderItemScheme.setUoMId(scheme.getSchemeOfferItems().get(0).getUoMId());
                                                            orderItemScheme.setItemBrand(scheme.getSchemeOfferItems().get(0).getItemBrand());
                                                            orderItemScheme.setItemPack(scheme.getSchemeOfferItems().get(0).getItemPack());
                                                            orderItemScheme.setMixedScheme(true);

                                                            if (territoryPrices.containsKey(scheme.getSchemeOfferItems().get(0).getItemId())) {
                                                                ItemPrice itemPrice1 = getItemPriceForSchemeOfferItem(scheme.getSchemeOfferItems().get(0).getUoMCode(), territoryPrices.get(scheme.getSchemeOfferItems().get(0).getItemId()).getItemPrices(), 0);

                                                                if (itemPrice1 != null) {
                                                                    orderItemScheme.setPrice(itemPrice1.getTradePrice() * orderItemScheme.getValue());
                                                                    orderItemScheme.setItemPrice(itemPrice1.getTradePrice());
                                                                    orderItemScheme.setItemPriceId(itemPrice1.getItemPriceId());
                                                                    orderItemScheme.setMRP(GetItemMRP(scheme.getSchemeOfferItems().get(0).getItemId()));
                                                                }
                                                            }
                                                            customizedSchemes.put(UniqueKey, orderItemScheme);
                                                            isSchemeFound = true;

                                                        }

                                                    }
                                                    if (isSchemeFound)
                                                        break;
                                                }
                                                if (isSchemeFound)
                                                    break;
                                            }
                                        }

                                        if (isSchemeFound)
                                            break;
                                    }
                                }
                                if (isSchemeFound)
                                    break;
                            }
                            if (isSchemeFound)
                                break;
                        }
                    }

                    if (customizedSchemes.size() > 0) {
                        for (Map.Entry<String, OrderItemScheme> schemeEntry : customizedSchemes.entrySet()) {

                            double schItemPrice = Math.round((new Double(schemeEntry.getKey().split("_")[1].trim()) * 100.0) / 100.0);

                            for (OrderItem orderItem : order.getOrderItems()) {
                                if (orderItem.getDispatchQty() > 0 && orderItem.getOrderItemDiscount() == null
                                        && orderItem.getOrderItemSchemes() == null && Math.round((orderItem.getMRP() * 100.0) / 100.0) == schItemPrice
                                        && allowedItems.containsKey(schemeEntry.getKey()) && allowedItems.get(schemeEntry.getKey()).contains(orderItem.getItemId())) {
                                    orderItem.setOrderItemSchemes(new ArrayList<OrderItemScheme>());
                                    orderItem.getOrderItemSchemes().add(schemeEntry.getValue());
                                    break;
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception ex) {
            //write log
            Logger.Log(RebateCalculator.class.getName(), ex);
            return null;
        }
        return order;
    }

    private ItemPrice getItemPriceForSchemeOfferItem(String uomCode, List<ItemPrice> itemPrices, double requiredMRP) {

        itemPrices = getSortedItemPrice((ArrayList<ItemPrice>) itemPrices);

        for (ItemPrice itemPrice : itemPrices) {
            if (itemPrice.getUoM().equals(uomCode)) {
                if (requiredMRP == 0)
                    return itemPrice;
                else if (requiredMRP == itemPrice.getmRP())
                    return itemPrice;
            }
        }
        return new ItemPrice();
    }

    public HashMap<Integer, List<Scheme>> getTerritorySchemes() {

        HashMap<Integer, List<Scheme>> territorySchemesMap = new HashMap<>();

        try {
            List<com.inventrax_pepsi.database.pojos.Item> localItems = tableItem.getAllItems();

            if (localItems == null)
                return null;

            for (com.inventrax_pepsi.database.pojos.Item localItem : localItems) {
                List<com.inventrax_pepsi.database.pojos.Scheme> localItemSchemes = tableScheme.getAllSchemesByItemId(localItem.getItemId());

                List<Scheme> schemes = new ArrayList<>();

                if (localItemSchemes != null)
                    for (com.inventrax_pepsi.database.pojos.Scheme localScheme : localItemSchemes) {

                        schemes.add(gson.fromJson(localScheme.getSchemeJSON(), Scheme.class));
                    }

                territorySchemesMap.put(localItem.getItemId(), schemes);
            }

        } catch (Exception ex) {

            return null;
        }

        return territorySchemesMap;

    }

    public HashMap<Integer, Item> getTerritoryPrices() {

        HashMap<Integer, Item> territoryPricesMap = new HashMap<>();

        try {
            List<com.inventrax_pepsi.database.pojos.Item> localItems = tableItem.getAllItems();

            if (localItems == null)
                return null;

            for (com.inventrax_pepsi.database.pojos.Item localItem : localItems) {
                territoryPricesMap.put(localItem.getItemId(), gson.fromJson(localItem.getItemJSON(), Item.class));
            }

        } catch (Exception ex) {
            return null;
        }

        return territoryPricesMap;
    }

    private ArrayList<ItemPrice> getSortedItemPrice(ArrayList<ItemPrice> itemPrices) {
        try {

            if (itemPrices != null && itemPrices.size() > 0) {
                Collections.sort(itemPrices, new Comparator<ItemPrice>() {
                    @Override
                    public int compare(ItemPrice lhs, ItemPrice rhs) {

                        return (lhs.getItemPriceId() >= rhs.getItemPriceId()) ? -1 : 1;

                    }
                });
            }

            return itemPrices;

        } catch (Exception e) {


        }
        return null;
    }
}
