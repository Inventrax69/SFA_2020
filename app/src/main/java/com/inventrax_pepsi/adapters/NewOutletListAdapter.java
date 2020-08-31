package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.sfa.pojos.Customer;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: New Outlet List Adapter
 */


public class NewOutletListAdapter extends RecyclerView.Adapter<NewOutletListViewHolders> {

    private List<Customer> customers;
    private Context context;
    private Resources resources;
    private FragmentActivity fragmentActivity;
    private DatabaseHelper databaseHelper;
    private TableJSONMessage tableJSONMessage;

    public NewOutletListAdapter(Context context, List<Customer> customers,FragmentActivity fragmentActivity) {
        setNewOutletListAdapter(context, customers,fragmentActivity);
    }

    public void setNewOutletListAdapter(Context context, List<Customer> customers,FragmentActivity fragmentActivity) {
        this.customers=customers;
        this.context = context;
        resources = context.getResources();
        this.fragmentActivity=fragmentActivity;

        databaseHelper = DatabaseHelper.getInstance();
        tableJSONMessage = databaseHelper.getTableJSONMessage();
    }

    @Override
    public NewOutletListViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_outlet_list_row, parent, false);

        NewOutletListViewHolders newOutletListViewHolders= new NewOutletListViewHolders(layoutView,fragmentActivity);

        return newOutletListViewHolders;
    }

    @Override
    public void onBindViewHolder(NewOutletListViewHolders holder, int position) {

        try {

            Customer customer = customers.get(position);

            holder.txtOutletCode.setText(""+customer.getCustomerCode());

            holder.txtOutletCode.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_in_process));

            if (tableJSONMessage.getCustomerSyncStatus(customer.getAutoIncId()) == 1)
                holder.txtOutletCode.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_success));


            holder.txtOutletName.setText(customer.getCustomerName());
            holder.txtOwnerName.setText(customer.getContactPersonName());
            holder.txtChannelCode.setText(customer.getOutletProfile()!=null?customer.getOutletProfile().getChannelCode():"");
            holder.txtPhoneNo.setText(""+customer.getMobileNo1());
            holder.txtLandmark.setText( (customer.getAddressBook()!=null && customer.getAddressBook().size()>0)?customer.getAddressBook().get(0).getLandmark():"");

            holder.customer = customer;


        } catch (Exception ex) {
            Logger.Log(NewOutletListAdapter.class.getName(), ex);
            return;
        }

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}
