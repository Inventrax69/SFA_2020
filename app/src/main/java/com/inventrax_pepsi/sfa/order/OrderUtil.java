package com.inventrax_pepsi.sfa.order;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetCapture;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.AssetCapture;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.sfa.cart.DerivedCart;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.InvoiceItem;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.LoadItem;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;
import com.inventrax_pepsi.sfa.scheme.SchemeUtil;
import com.inventrax_pepsi.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Naresh on 20-Mar-16.
 */
public class OrderUtil {

    private Gson gson;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableVehicleStock tableVehicleStock;
    private TableJSONMessage tableJSONMessage;
    private TableScheme tableScheme;

    public OrderUtil() {

        gson = new GsonBuilder().create();
        databaseHelper = DatabaseHelper.getInstance();

        if (databaseHelper==null){
            Logger.Log(OrderUtil.class.getName()+" :-> Database Helper object not initialized ");
            return;
        }

        tableOrder = databaseHelper.getTableOrder();
        tableVehicleStock = databaseHelper.getTableVehicleStock();
        tableJSONMessage = databaseHelper.getTableJSONMessage();
        tableScheme= databaseHelper.getTableScheme();
    }


    public Order generateOrder(Customer customer, DerivedCart derivedCart, List<OrderItem> orderItemList) {

        Order order = null;

        try {

            order = new Order();

            order.setRouteId(customer.getOutletProfile().getRouteId());
            order.setCustomerId(customer.getCustomerId());
            order.setCustomerCode(customer.getCustomerCode());
            order.setCustomerGroupId(customer.getCustomerGroupId());
            order.setDerivedPrice(derivedCart.getDerivedPrice());
            order.setDiscountPrice(derivedCart.getDiscountPrice());

            order.setOrderCode(DateUtils.getDate("yyyyMMdd") + "" + customer.getCustomerId() + "" + DateUtils.getHourMinuteSeconds());

            order.setOrderItems(orderItemList);
            order.setCustomerName(customer.getCustomerName());

            // Order Type 2 for ready sale , 1 for pre-sale
            // User Type Id 7 for Route Agent
            order.setOrderTypeId((AppController.getUser().getUserTypeId() == 7 ? 2 : 1));
            order.setOrderStatus(((order.getOrderTypeId() == 2) ? OrderStatus.InProcess.name() : OrderStatus.Initiated.name()));
            order.setOrderStatusId(((order.getOrderTypeId() == 2) ? OrderStatus.InProcess.getStatus() : OrderStatus.Initiated.getStatus()));
            order.setPaymentStatus(false);
            order.setOrderPrice(derivedCart.getDerivedPrice());
            order.setNoOfBottles(derivedCart.getNoOfBottles());
            order.setNoOfCases(derivedCart.getNoOfCases());
            order.setNoOfSku(derivedCart.getNoOfSku());
            order.setNoOfFreesInBottles(derivedCart.getNoOfFreesInBottles());
            order.setNoOfFreesInCase(derivedCart.getNoOfFreesInCase());

            AuditInfo auditInfo=new AuditInfo();
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());

            order.setAuditInfo(auditInfo);

        } catch (Exception ex) {
            Logger.Log(OrderUtil.class.getName(), ex);
            return null;
        }

        return order;
    }

    public boolean createOrderInLocalDB(Order order, Customer customer) {

        boolean status = false;
        long local_order_auto_ic_id = 0;

        try {

            com.inventrax_pepsi.database.pojos.Order localOrder = new com.inventrax_pepsi.database.pojos.Order();

            localOrder.setRouteId(order.getRouteId());
            localOrder.setCustomerCode(order.getCustomerCode());
            localOrder.setCustomerId(order.getCustomerId());
            localOrder.setDerivedPrice(order.getDerivedPrice());
            localOrder.setOrderCode(order.getOrderCode());
            localOrder.setOrderPrice(order.getOrderPrice());
            localOrder.setOrderType(order.getOrderTypeId());
            localOrder.setPaymentStatus(0);
            localOrder.setRouteCode(customer.getOutletProfile().getRouteCode());
            localOrder.setOrderJSON(gson.toJson(order));
            localOrder.setOrderStatus(order.getOrderStatusId());
            localOrder.setOrderQuantity(order.getNoOfCases());

            local_order_auto_ic_id = tableOrder.createOrder(localOrder);

            if (local_order_auto_ic_id != 0) {

                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(localOrder.getOrderJSON());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId((int) local_order_auto_ic_id);
                jsonMessage.setNotificationTypeId(1); // For Order Notification Type Id is 1

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    localOrder.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    localOrder.setAutoIncId((int) local_order_auto_ic_id);

                    tableOrder.updateOrder(localOrder);

                    status = true;
                }

            }

        } catch (Exception ex) {
            Logger.Log(OrderUtil.class.getName(), ex);
            return false;
        }

        return status;
    }

    public boolean issueStock(Order order) {

        try {



            TableItem tableItem = databaseHelper.getTableItem();

            if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {

                List<ItemUoM> itemUoMs = null;

                if (!checkItemAvailability(order,tableItem))
                    return false;

                for (OrderItem orderItem : order.getOrderItems()) {

                    com.inventrax_pepsi.database.pojos.Item localItem = tableItem.getItem(orderItem.getItemId());

                    if (localItem != null)
                        itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();
                    else
                        return false;

                    if (reduceVanInventory(itemUoMs, orderItem.getItemId(), orderItem.getUoMCode(), orderItem.getQuantity(),orderItem.getMRP())) {

                        if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot()) {

                            localItem = tableItem.getItem(orderItem.getOrderItemDiscount().getItemId());

                            if (localItem != null)
                                itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();
                            else
                                return false;

                            if (!reduceVanInventory(itemUoMs, orderItem.getOrderItemDiscount().getItemId(), orderItem.getOrderItemDiscount().getUoMCode(), orderItem.getOrderItemDiscount().getValue(),orderItem.getOrderItemDiscount().getMRP())) {
                                return false;
                            }

                        } else if (orderItem.getOrderItemSchemes() != null && orderItem.getOrderItemSchemes().size() > 0) {

                            for (OrderItemScheme scheme : orderItem.getOrderItemSchemes()) {

                                localItem = tableItem.getItem(scheme.getItemId());

                                if (localItem != null)
                                    itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();
                                else
                                    return false;

                                if (!reduceVanInventory(itemUoMs, scheme.getItemId(), scheme.getUoM(), scheme.getValue(),scheme.getMRP())) {
                                    return false;
                                }

                            }

                        }

                    } else {

                        return false;

                    }

                }

            }

            return true;

        }catch (Exception ex){

            Logger.Log(OrderUtil.class.getName(),ex);
            return false;

        }

    }


    private boolean checkItemAvailability(Order order, TableItem tableItem){

        try
        {

            List<ItemUoM> itemUoMs = null;

            Map<String, CustomObject> customObjectMap = new HashMap<>();

            for (OrderItem orderItem : order.getOrderItems()) {

                CustomObject customObject = null;

                com.inventrax_pepsi.database.pojos.Item localItem = null;

                if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot()) {

                    customObject = new CustomObject();

                    localItem = tableItem.getItem(orderItem.getOrderItemDiscount().getItemId());

                    if (localItem != null)
                        itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                    double bottleQty = 0, caseQty = 0;

                    if (orderItem.getOrderItemDiscount().getUoMCode().equalsIgnoreCase("FC") || orderItem.getOrderItemDiscount().getUoMCode().equalsIgnoreCase("PACK")) {
                        caseQty = orderItem.getOrderItemDiscount().getValue();
                    } else {
                        bottleQty = orderItem.getOrderItemDiscount().getValue();
                    }

                    customObject.setItemId(orderItem.getOrderItemDiscount().getItemId());
                    customObject.setItemUoMList(itemUoMs);
                    customObject.setBottleQuantity(bottleQty);
                    customObject.setCaseQuantity(caseQty);
                    customObject.setItemMRP(orderItem.getMRP());

                    String UniqueKey=customObject.getItemId()+"_"+customObject.getItemMRP();

                    if (customObjectMap.containsKey(UniqueKey)) {

                        customObject.setBottleQuantity(bottleQty + customObjectMap.get(UniqueKey).getBottleQuantity());
                        customObject.setCaseQuantity(caseQty + customObjectMap.get(UniqueKey).getCaseQuantity());

                        customObjectMap.put(UniqueKey, customObject);

                    } else {
                        customObjectMap.put(UniqueKey, customObject);
                    }

                } else if (orderItem.getOrderItemSchemes() != null && orderItem.getOrderItemSchemes().size() > 0) {

                    for (OrderItemScheme scheme : orderItem.getOrderItemSchemes()) {

                        customObject = new CustomObject();

                        localItem = tableItem.getItem(scheme.getItemId());

                        if (localItem != null)
                            itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                        double bottleQty = 0, caseQty = 0;

                        if (scheme.getUoM().equalsIgnoreCase("FC") || scheme.getUoM().equalsIgnoreCase("PACK")) {
                            caseQty = scheme.getValue();
                        } else {
                            bottleQty = scheme.getValue();
                        }

                        customObject.setItemId(scheme.getItemId());
                        customObject.setItemUoMList(itemUoMs);
                        customObject.setBottleQuantity(bottleQty);
                        customObject.setCaseQuantity(caseQty);
                        customObject.setItemMRP(scheme.getMRP());

                        String UniqueKey=customObject.getItemId()+"_"+customObject.getItemMRP();

                        if (customObjectMap.containsKey(UniqueKey)) {

                            customObject.setBottleQuantity(bottleQty + customObjectMap.get(UniqueKey).getBottleQuantity());
                            customObject.setCaseQuantity(caseQty + customObjectMap.get(UniqueKey).getCaseQuantity());

                            customObjectMap.put(UniqueKey, customObject);

                        } else {
                            customObjectMap.put(UniqueKey, customObject);
                        }
                    }

                }

                localItem = tableItem.getItem(orderItem.getItemId());

                if (localItem != null)
                    itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();
                else
                    return false;

                customObject = new CustomObject();

                customObject.setItemId(orderItem.getItemId());
                customObject.setItemUoMList(itemUoMs);
                customObject.setItemMRP(orderItem.getMRP());
                if (orderItem.getUoMCode().equalsIgnoreCase("FB"))
                    customObject.setBottleQuantity(orderItem.getQuantity());
                else
                    customObject.setCaseQuantity(orderItem.getQuantity());

                String UniqueKey=customObject.getItemId()+"_"+customObject.getItemMRP();

                if (customObjectMap.containsKey(UniqueKey)) {

                    customObject.setBottleQuantity(customObject.getBottleQuantity() + customObjectMap.get(UniqueKey).getBottleQuantity());
                    customObject.setCaseQuantity(customObject.getCaseQuantity() + customObjectMap.get(UniqueKey).getCaseQuantity());

                    customObjectMap.put(UniqueKey, customObject);

                } else {

                    customObjectMap.put(UniqueKey, customObject);

                }

                for (CustomObject customObject1 : customObjectMap.values()) {

                    if (!availStockCheck(customObject1.getItemUoMList(), customObject1.getItemId(),customObject1.getItemMRP(), customObject1.getCaseQuantity(), customObject1.getBottleQuantity()))
                        return false;

                }
            }

            return true;

        }catch (Exception ex){
            Logger.Log(OrderUtil.class.getName(),ex);
            return false;
        }
    }

    private boolean reduceVanInventory(List<ItemUoM> uoMs, int itemId, String uomCode, double qty) {

        try {

            //special update..
            double caseUnits = 0;

            if (uoMs != null && uoMs.size() > 0) {
                for (ItemUoM uom : uoMs) {
                    if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                        caseUnits = uom.getUnits();
                        break;
                    }
                }
            }

            if (caseUnits != 0) {

                double bottleQty = uomCode.trim().equalsIgnoreCase("FB") ? qty : qty * caseUnits;
                VehicleStock vanStock = tableVehicleStock.getVehicleStock(itemId);

                if (vanStock != null) {

                    double vanBottleStock = vanStock.getBottleQuantity() + (vanStock.getCaseQuantity() * caseUnits);

                    //stock issue/reduce.
                    if (bottleQty <= vanBottleStock) {

                        vanBottleStock = vanBottleStock - bottleQty;
                        tableVehicleStock.updateVanInventory(itemId, (int) (vanBottleStock / caseUnits), (int) (vanBottleStock % caseUnits));
                        return true;

                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            } else {
                return false;
            }

        }catch (Exception ex){

            Logger.Log(OrderUtil.class.getName(),ex);
            return false;

        }
    }

    private boolean reduceVanInventory(List<ItemUoM> uoMs, int itemId, String uomCode, double qty,double itemMRP) {

        try {

            //special update..
            double caseUnits = 0;

            if (uoMs != null && uoMs.size() > 0) {
                for (ItemUoM uom : uoMs) {
                    if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                        caseUnits = uom.getUnits();
                        break;
                    }
                }
            }

            if (caseUnits != 0) {

                double bottleQty = uomCode.trim().equalsIgnoreCase("FB") ? qty : qty * caseUnits;
                VehicleStock vanStock = tableVehicleStock.getVehicleStock(itemId,itemMRP);

                if (vanStock != null) {

                    double vanBottleStock = vanStock.getBottleQuantity() + (vanStock.getCaseQuantity() * caseUnits);

                    //stock issue/reduce.
                    if (bottleQty <= vanBottleStock) {

                        vanBottleStock = vanBottleStock - bottleQty;
                        tableVehicleStock.updateVanInventory(itemId,itemMRP, (int) (vanBottleStock / caseUnits), (int) (vanBottleStock % caseUnits));
                        return true;

                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            } else {
                return false;
            }

        }catch (Exception ex){

            Logger.Log(OrderUtil.class.getName(),ex);
            return false;

        }
    }

    //stock checking before issue.
    private boolean availStockCheck(List<ItemUoM> uoMs, int itemId, double caseQty, double bottleQty)
    {
        //special update..
        double caseUnits = 0;
        if (uoMs != null && uoMs.size() > 0) {
            for (ItemUoM uom : uoMs) {
                if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                    caseUnits = uom.getUnits();
                    break;
                }
            }
        }

        double requiredQty = (caseQty*caseUnits) + bottleQty;

        return  getAvailStock(itemId,uoMs) >= requiredQty;

    }

    private boolean availStockCheck(List<ItemUoM> uoMs, int itemId,double itemMRP, double caseQty, double bottleQty)
    {
        //special update..
        double caseUnits = 0;
        if (uoMs != null && uoMs.size() > 0) {
            for (ItemUoM uom : uoMs) {
                if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                    caseUnits = uom.getUnits();
                    break;
                }
            }
        }

        double requiredQty = (caseQty*caseUnits) + bottleQty;

        return  getAvailStock(itemId,uoMs,itemMRP) >= requiredQty;

    }


    public boolean findEnoughStockAvailOrNot(List<OrderItem> orderItems)
    {
        TableItem tableItem = databaseHelper.getTableItem();

        if(orderItems !=null && orderItems.size() <= 2)
        {
            List<ItemUoM> itemUoMs=null;
            double caseQty=0;
            double bottleQty=0;

            com.inventrax_pepsi.database.pojos.Item localItem = null;

            /*if (orderItems.get(0).getOrderItemDiscount() != null && orderItems.get(0).getOrderItemDiscount().getDiscountTypeId() == 1 && orderItems.get(0).getOrderItemDiscount().isSpot()) {

                localItem=tableItem.getItem(orderItems.get(0).getOrderItemDiscount().getItemId());

                if (localItem != null)
                    itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                bottleQty=0;caseQty=0;

                if (orderItems.get(0).getOrderItemDiscount().getUoMCode().equalsIgnoreCase("FC") || orderItems.get(0).getOrderItemDiscount().getUoMCode().equalsIgnoreCase("PACK") )
                {
                    caseQty = orderItems.get(0).getOrderItemDiscount().getValue();
                }else {
                    bottleQty = orderItems.get(0).getOrderItemDiscount().getValue();
                }

                if(!availStockCheck(itemUoMs,orderItems.get(0).getOrderItemDiscount().getItemId(),caseQty,bottleQty))
                    return  false;

            } else if (orderItems.get(0).getOrderItemSchemes() != null && orderItems.get(0).getOrderItemSchemes().size() > 0) {

                for (OrderItemScheme scheme : orderItems.get(0).getOrderItemSchemes()) {

                    localItem=tableItem.getItem(scheme.getItemId());

                    if (localItem != null)
                        itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                    bottleQty=0;caseQty=0;

                    if (scheme.getUoM().equalsIgnoreCase("FC") || scheme.getUoM().equalsIgnoreCase("PACK") )
                    {
                        caseQty = scheme.getValue();
                    }else {
                        bottleQty = scheme.getValue();
                    }

                    if (!availStockCheck(itemUoMs,scheme.getItemId(),caseQty,bottleQty))
                        return false;

                }

            }*/

            localItem = tableItem.getItem(orderItems.get(0).getItemId());

            if (localItem != null)
                itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

            for(OrderItem orderItem : orderItems){
                if(orderItem.getUoMId() ==1){
                    bottleQty +=orderItem.getQuantity();
                }
                else{
                    caseQty += orderItem.getQuantity();
                }
            }

            return  availStockCheck(itemUoMs,orderItems.get(0).getItemId(),caseQty,bottleQty);

        }else{
            return  false;
        }
    }

    public boolean findEnoughStockAvailOrNot(List<OrderItem> orderItems,double itemMRP)
    {
        TableItem tableItem = databaseHelper.getTableItem();

        if(orderItems !=null && orderItems.size() <= 2)
        {
            List<ItemUoM> itemUoMs=null;
            double caseQty=0;
            double bottleQty=0;

            com.inventrax_pepsi.database.pojos.Item localItem = null;

            /*if (orderItems.get(0).getOrderItemDiscount() != null && orderItems.get(0).getOrderItemDiscount().getDiscountTypeId() == 1 && orderItems.get(0).getOrderItemDiscount().isSpot()) {

                localItem=tableItem.getItem(orderItems.get(0).getOrderItemDiscount().getItemId());

                if (localItem != null)
                    itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                bottleQty=0;caseQty=0;

                if (orderItems.get(0).getOrderItemDiscount().getUoMCode().equalsIgnoreCase("FC") || orderItems.get(0).getOrderItemDiscount().getUoMCode().equalsIgnoreCase("PACK") )
                {
                    caseQty = orderItems.get(0).getOrderItemDiscount().getValue();
                }else {
                    bottleQty = orderItems.get(0).getOrderItemDiscount().getValue();
                }

                if(!availStockCheck(itemUoMs,orderItems.get(0).getOrderItemDiscount().getItemId(),caseQty,bottleQty))
                    return  false;

            } else if (orderItems.get(0).getOrderItemSchemes() != null && orderItems.get(0).getOrderItemSchemes().size() > 0) {

                for (OrderItemScheme scheme : orderItems.get(0).getOrderItemSchemes()) {

                    localItem=tableItem.getItem(scheme.getItemId());

                    if (localItem != null)
                        itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

                    bottleQty=0;caseQty=0;

                    if (scheme.getUoM().equalsIgnoreCase("FC") || scheme.getUoM().equalsIgnoreCase("PACK") )
                    {
                        caseQty = scheme.getValue();
                    }else {
                        bottleQty = scheme.getValue();
                    }

                    if (!availStockCheck(itemUoMs,scheme.getItemId(),caseQty,bottleQty))
                        return false;

                }

            }*/

            localItem = tableItem.getItem(orderItems.get(0).getItemId());

            if (localItem != null)
                itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();

            for(OrderItem orderItem : orderItems){
                if(orderItem.getUoMId() ==1){
                    bottleQty +=orderItem.getQuantity();
                }
                else{
                    caseQty += orderItem.getQuantity();
                }
            }

            return  availStockCheck(itemUoMs,orderItems.get(0).getItemId(),itemMRP,caseQty,bottleQty);

        }else{
            return  false;
        }
    }

    private double getAvailStock(int itemId,List<ItemUoM> uoMs)
    {
        //special update..
        double caseUnits = 0;
        if (uoMs != null && uoMs.size() > 0) {
            for (ItemUoM uom : uoMs) {
                if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                    caseUnits = uom.getUnits();
                    break;
                }
            }
        }

        if (caseUnits != 0) {
            VehicleStock vanStock = tableVehicleStock.getVehicleStock(itemId);
            return (vanStock != null) ? vanStock.getBottleQuantity() + (vanStock.getCaseQuantity() * caseUnits):0;
        }

        return 0;
    }

    private double getAvailStock(int itemId,List<ItemUoM> uoMs,double MRP)
    {
        //special update..
        double caseUnits = 0;
        if (uoMs != null && uoMs.size() > 0) {
            for (ItemUoM uom : uoMs) {
                if (uom.getUoMId() != 1 && !uom.getUoM().trim().equalsIgnoreCase("FB")) {
                    caseUnits = uom.getUnits();
                    break;
                }
            }
        }

        if (caseUnits != 0) {
            VehicleStock vanStock = tableVehicleStock.getVehicleStock(itemId,MRP);
            return (vanStock != null) ? vanStock.getBottleQuantity() + (vanStock.getCaseQuantity() * caseUnits):0;
        }

        return 0;
    }

    public boolean issueStock(Invoice invoice) {

        TableItem tableItem = databaseHelper.getTableItem();

        if (invoice.getInvoiceItems() != null && invoice.getInvoiceItems().size() > 0) {

            List<ItemUoM> itemUoMs = null;

            for (InvoiceItem invoiceItem: invoice.getInvoiceItems()) {

                com.inventrax_pepsi.database.pojos.Item localItem = tableItem.getItem(invoiceItem.getItemId());

                if (localItem != null)
                    itemUoMs = gson.fromJson(localItem.getItemJSON(), Item.class).getItemUoMs();
                else
                    return false;

                if(!reduceVanInventory(itemUoMs, invoiceItem.getItemId(), invoiceItem.getUoMCode(), invoiceItem.getQuantity(),invoiceItem.getMRP())){
                    return false;
                }

            }
        }
        return true;
    }

    public List<LoadItem> getLoadItemsFromOrder(List<OrderItem> orderItems){

        List<LoadItem> loadItemList=null;

        try
        {
            loadItemList=new ArrayList<>();

            LoadItem loadItem=null;

            for (OrderItem orderItem:orderItems){

                loadItem=new LoadItem();

                loadItem.setItemId(orderItem.getItemId());
                loadItem.setItemCode(orderItem.getItemCode());
                loadItem.setItemName(orderItem.getItemBrand()+""+orderItem.getItemPack());
                loadItem.setItemTypeId(orderItem.getItemTypeId());
                loadItem.setUoMId(orderItem.getUoMId());
                loadItem.setQuantity(orderItem.getQuantity());
                loadItem.setImageName(orderItem.getImageName());

                loadItemList.add(loadItem);


            }

            return loadItemList;

        }catch (Exception ex){
            Logger.Log(OrderUtil.class.getName(),ex);
            return null;
        }
    }

    public List<LoadItem> getLoadItemsFromInvoice(List<InvoiceItem> invoiceItems){

        List<LoadItem> loadItemList=null;

        try
        {
            loadItemList=new ArrayList<>();

            LoadItem loadItem=null;

            for (InvoiceItem invoiceItem:invoiceItems){

                loadItem=new LoadItem();

                loadItem.setItemId(invoiceItem.getItemId());
                loadItem.setItemCode(invoiceItem.getItemCode());
                loadItem.setItemName(invoiceItem.getItemName());
                loadItem.setItemTypeId(invoiceItem.getItemTypeId());
                loadItem.setUoMId(invoiceItem.getUomId());
                loadItem.setQuantity(invoiceItem.getQuantity());
                loadItem.setImageName(invoiceItem.getImageName());


                loadItemList.add(loadItem);


            }

            return loadItemList;

        }catch (Exception ex){
            Logger.Log(OrderUtil.class.getName(),ex);
            return null;
        }

    }

    public void postVanInventory(List<LoadItem> loadItems){

        try
        {

            if (tableVehicleStock==null)
                return;

            VehicleStock vehicleStock=null;

            for (LoadItem loadItem:loadItems){

                vehicleStock=new VehicleStock();

                vehicleStock.setItemId(loadItem.getItemId());
                vehicleStock.setItemCode(loadItem.getItemCode());
                vehicleStock.setItemName(loadItem.getItemName());
                vehicleStock.setItemTypeId(loadItem.getItemTypeId());
                vehicleStock.setBottleQuantity((loadItem.getUoMId()==1)?loadItem.getQuantity():0);
                vehicleStock.setCaseQuantity((loadItem.getUoMId()!=1)?loadItem.getQuantity():0);
                vehicleStock.setTransactionType(1);
                vehicleStock.setImageName(loadItem.getImageName());

                VehicleStock localVehicleStock=tableVehicleStock.getVehicleStock(loadItem.getItemId());

                if (localVehicleStock!=null)
                {
                    double caseQty = localVehicleStock.getCaseQuantity();
                    double bottleQty = localVehicleStock.getBottleQuantity();
                    if(loadItem.getUoMId()!=1)
                        caseQty += loadItem.getQuantity();
                    else
                        bottleQty += loadItem.getQuantity();

                    tableVehicleStock.updateVanInventory(loadItem.getItemId(),caseQty,bottleQty);
                }
                else
                {
                    tableVehicleStock.createVehicleStock(vehicleStock);
                }

            }

        }catch (Exception ex){
            Logger.Log(OrderUtil.class.getName(),ex);
            return;
        }
    }

    public Order getOrderWithFrees(Order order){

        try
        {

            if (order.getOrderItems() == null)
                return order;

            order.setNoOfSku(order.getOrderItems().size());

            for (OrderItem orderItem:order.getOrderItems()){

                if (orderItem.getPrice() > 0)
                {

                    if (orderItem.getUoMCode().equals("FB")) {

                        order.setNoOfBottles( order.getNoOfBottles() + orderItem.getQuantity());

                    } else {

                        order.setNoOfCases( order.getNoOfCases() + orderItem.getQuantity());

                    }

                    if (orderItem.getOrderItemSchemes() != null) {

                        for (OrderItemScheme orderItemScheme : orderItem.getOrderItemSchemes()) {

                            if (orderItemScheme.getUoM().equals("FB")) {

                                order.setNoOfFreesInBottles( order.getNoOfFreesInBottles() + orderItemScheme.getValue());

                            } else {

                                order.setNoOfFreesInCase( order.getNoOfFreesInCase() + orderItemScheme.getValue());

                            }

                        }

                    }

                    // Discount  Type Id : 1 --> Bottle Discount
                    if (orderItem.getOrderItemDiscount() != null && orderItem.getOrderItemDiscount().getDiscountTypeId() == 1 && orderItem.getOrderItemDiscount().isSpot())
                    {
                        if (orderItem.getOrderItemDiscount().getUoM().equalsIgnoreCase("FB")) {
                            order.setNoOfFreesInBottles( order.getNoOfFreesInBottles() +  orderItem.getOrderItemDiscount().getValue());
                        } else {
                            order.setNoOfFreesInCase( order.getNoOfFreesInCase()  + orderItem.getOrderItemDiscount().getValue());
                        }
                    }


                }

            }




        }catch (Exception ex){
            return order;
        }

        return order;
    }

    public List<SchemeOfferItem> getSchemeOfferItem(Item item,double caseQty,double bottleQty){

        try {

            ArrayList<Scheme> schemes=null;
            ArrayList<SchemeOfferItem> schemeOfferItems=new ArrayList<>();

            if (item.getItemRebateMap() != null)
            {
                for (Map.Entry<Integer, Object> entry : item.getItemRebateMap().entrySet()) {
                    switch (entry.getKey()) {

                        // Schemes
                        case 3: {

                            if ((ArrayList<Scheme>) entry.getValue() != null && ((ArrayList<Scheme>) entry.getValue()).size() > 0) {

                                schemes = (ArrayList<Scheme>) entry.getValue();

                            }

                        }
                        break;

                    }

                }
            }

            if (schemes == null)
                return null;

            SchemeUtil.getSortedSchemes(schemes);

            //for (Scheme scheme : schemes) {

                Scheme scheme = ( schemes!=null && schemes.size()>0 ) ? schemes.get(0) :null;

                if (scheme!=null) {

                    for (SchemeOfferItem schemeOfferItem : scheme.getSchemeOfferItems()) {
                        schemeOfferItem.setSchemeId(scheme.getSchemeId());
                    }

                    schemeOfferItems.addAll((ArrayList) scheme.getSchemeOfferItems());

                }

            //}

            return schemeOfferItems;


        }catch (Exception ex){
            Logger.Log(OrderUtil.class.getName(),ex);
            return null;
        }



    }

    public boolean checkOrderCap(double orderCap,int customerId,double currentOrderCaseQty){

        try
        {
            double orderQty=currentOrderCaseQty;

           List<com.inventrax_pepsi.database.pojos.Order> localOrders= tableOrder.getAllOrdersByCustomerId(customerId);

            for (com.inventrax_pepsi.database.pojos.Order order:localOrders){
                if(order.getOrderStatus() <= OrderStatus.Partial.getStatus() || order.getOrderStatus() == OrderStatus.Completed.getStatus())
                    orderQty +=  order.getOrderQuantity() ;
            }

            if ( orderQty > orderCap )
                return true;

        }catch (Exception ex){
            return false;
        }

        return false;
    }

    public double checkCustomerCreditLimit(Customer customer,double currentCredit,double currentOrderCreditAmount){

        double creditAmount=0;

        try
        {
            // currentCredit + currentOrderCreditAmount + previous orders credit amount cannot exceed customer credit limit

            creditAmount = currentCredit + currentOrderCreditAmount + getPayableAmountByCustomerId(customer.getCustomerId());

            if ( creditAmount > customer.getCreditLimit() ){

                return creditAmount;

            }

            return 0;


        }catch (Exception ex){

            return 0;
        }

    }

    private double getPayableAmountByCustomerId(int customerId){

        double todayTotalCreditAmount=0;

        try
        {

            List<com.inventrax_pepsi.database.pojos.Order> localOrders= tableOrder.getAllOrdersByCustomerId(customerId);

            for (com.inventrax_pepsi.database.pojos.Order localOrder:localOrders) {

                Order order= gson.fromJson(localOrder.getOrderJSON(),Order.class);

                if (order.getOrderStatusId() == OrderStatus.Completed.getStatus()) {

                     if (order.getPaymentInfo()!=null && ( order.getPaymentInfo().getMode()==1 || order.getPaymentInfo().getPaymentTypeId()==1 ) )
                     {
                         todayTotalCreditAmount+=(order.getDerivedPrice()-order.getPaymentInfo().getAmount());
                     }

                }
            }

            return todayTotalCreditAmount;

        }catch (Exception ex){

            return 0;
        }
    }

    public List<CustomObject> getSKUWiseOrdersByRouteCode(String routeCode){

        try
        {
            Map<String, CustomObject> customObjectMap = new HashMap<>();

            CustomObject customObject=null;

            List<com.inventrax_pepsi.database.pojos.Order> localOrders=tableOrder.getAllOrdersByRouteCode(routeCode);

            if (localOrders==null || localOrders.size()==0)
                return null;

            for (com.inventrax_pepsi.database.pojos.Order localOrder:localOrders)
            {
                  Order order=  gson.fromJson(localOrder.getOrderJSON(),Order.class);

                  if (order!=null && order.getOrderItems() !=null && order.getOrderStatusId() != OrderStatus.Cancelled.getStatus() && order.getOrderItems().size()>0)
                  {
                      for (OrderItem orderItem:order.getOrderItems())
                      {

                          customObject = new CustomObject();

                          customObject.setItemId(orderItem.getItemId());
                          customObject.setBrandName(orderItem.getItemBrand());
                          customObject.setItemPack(orderItem.getItemPack());
                          customObject.setItemMRP(orderItem.getMRP());

                          if (orderItem.getUoMCode().equalsIgnoreCase("FB"))
                              customObject.setBottleQuantity(orderItem.getQuantity());
                          else
                              customObject.setCaseQuantity(orderItem.getQuantity());

                          String UniqueKey=customObject.getItemId()+"_"+customObject.getItemMRP();

                          if (customObjectMap.containsKey(UniqueKey)) {

                              customObject.setBottleQuantity(customObject.getBottleQuantity() + customObjectMap.get(UniqueKey).getBottleQuantity());
                              customObject.setCaseQuantity(customObject.getCaseQuantity() + customObjectMap.get(UniqueKey).getCaseQuantity());

                              customObjectMap.put(UniqueKey, customObject);

                          } else {

                              customObjectMap.put(UniqueKey, customObject);

                          }


                      }
                  }

            }

            List<CustomObject> customObjects=new ArrayList<CustomObject>(customObjectMap.values());


            Collections.sort(customObjects, new Comparator<CustomObject>() {
                @Override
                public int compare(CustomObject lhs, CustomObject rhs) {

                    return  ( Double.parseDouble(lhs.getItemPack().replaceAll("[^\\d.]", "")) >=  Double.parseDouble(rhs.getItemPack().replaceAll("[^\\d.]", "")) ? 1 : -1 ) ;
                }
            });

            return customObjects;


        }catch (Exception ex){

            ex.printStackTrace();
            return null;
        }

    }


    public boolean checkIsAssetAuditCompleted(Customer customer){

        try
        {
            TableAssetCapture tableAssetCapture= databaseHelper.getTableAssetCapture();

            List<AssetCapture> assetCaptures=tableAssetCapture.getAllAssetCapturesByCustomerId(customer.getCustomerId());

            if (assetCaptures==null || assetCaptures.size()==0 )
                return false;

            if ( customer.getCustomerAssets() != null && assetCaptures.size() >=  customer.getCustomerAssets().size()  )
            {
                return true;

            }else {

                return false;

            }

        }catch (Exception ex){

            return false;

        }
    }



}
