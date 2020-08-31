package com.inventrax_pepsi.application;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.model.OutletMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 17/03/2016 11:03 AM
 * Purpose	: Outlet Menu Factory
 */

public class OutletMenuFactory {

    public static List<OutletMenuItem> getOutletMenuItemsByUserType(int userType,boolean isAssetAvailable){

        List<OutletMenuItem> outletMenuItemList=new ArrayList<>();

        switch (userType)
        {
            // CEO
            case 1:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // VP
            case 2:{


                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // GM
            case 3:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // AGM
            case 4:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // TDM
            case 5:{

                //outletMenuItemList.add(new OutletMenuItem(R.drawable.discount, "Discounts"));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.outlet_update,"Update Outlet"));

                //outletMenuItemList.add(new OutletMenuItem(R.drawable.complaint,"Complaint"));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_audit,"Asset Audit"));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // CE
            case 6:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.discount, "Discounts"));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.outlet_update,"Update Outlet"));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,"Asset Request"));


                if (isAssetAvailable) {
                    addAssetMenus(outletMenuItemList);
                }

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            // RA
            case 7:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.book_order,AbstractApplication.get().getString(R.string.title_book_order)));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.delivery,AbstractApplication.get().getString(R.string.title_delivery_list)));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,AbstractApplication.get().getString(R.string.title_customer_returns)));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.order_history,"Today's Orders"));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.discount,  AbstractApplication.get().getString(R.string.title_discount_list)));
               // outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,"Asset Info"));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,AbstractApplication.get().getString(R.string.title_asset_request)));

                if (isAssetAvailable) {
                    addAssetMenus(outletMenuItemList);
                }

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)));

            }break;

            //PSR
            case 8:{

                outletMenuItemList.add(new OutletMenuItem(R.drawable.book_order,AbstractApplication.get().getString(R.string.title_book_order)));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.discount, AbstractApplication.get().getString(R.string.title_discount_list)));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.order_history,AbstractApplication.get().getString(R.string.title_order_history)));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,"Asset Info"));
                //outletMenuItemList.add(new OutletMenuItem(R.drawable.outlet_update,"Update Outlet"));
                outletMenuItemList.add(new OutletMenuItem(R.drawable.asset_request,AbstractApplication.get().getString(R.string.title_asset_request)));

                if (isAssetAvailable) {
                    addAssetMenus(outletMenuItemList);
                }

                outletMenuItemList.add(new OutletMenuItem(R.drawable.check_out, AbstractApplication.get().getString(R.string.sku_list_btn_checkout_text)) );


            }break;

        }

        return outletMenuItemList;
    }


    private static void addAssetMenus(List<OutletMenuItem> outletMenuItems){
        outletMenuItems.add(new OutletMenuItem(R.drawable.complaint, AbstractApplication.get().getString(R.string.title_asset_complaint)));
        outletMenuItems.add(new OutletMenuItem(R.drawable.asset_audit, AbstractApplication.get().getString(R.string.title_asset_audit)));
        outletMenuItems.add(new OutletMenuItem(R.drawable.asset_request, AbstractApplication.get().getString(R.string.title_pullout_form)));
    }


}
