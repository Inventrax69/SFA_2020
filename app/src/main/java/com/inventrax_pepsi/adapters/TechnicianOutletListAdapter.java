package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.model.TechnicianOutletList;

import java.util.List;

/**
 * Created by padmaja on 22/07/19.
 */
public class TechnicianOutletListAdapter extends RecyclerView.Adapter<TechnicianOutletListAdapter.ViewHolder> {
    private List<TechnicianOutletList> items;
    private Context context;
    OnItemClickListener listener;


    public TechnicianOutletListAdapter(Context applicationContext, List<TechnicianOutletList> itemArrayList, OnItemClickListener mlistener) {
        this.context = applicationContext;
        this.items = itemArrayList;
        listener = mlistener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_technician_outletlist, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        TechnicianOutletList outletList = (TechnicianOutletList) items.get(i);
        viewHolder.txtCustmerName.setText((outletList.getOutletName()));
        viewHolder.txtPersonName.setText((outletList.getCustomerCode()));
        viewHolder.txtPlace.setText((outletList.getCustomerName()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCustmerName, txtPersonName, txtPlace;


        public ViewHolder(View view) {
            super(view);
            // Initializing Views
            txtCustmerName = (TextView) view.findViewById(R.id.txtCustmerName);
            txtPersonName = (TextView) view.findViewById(R.id.txtPersonName);
            txtPlace = (TextView) view.findViewById(R.id.txtPlace);


            //on item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onItemClick(pos);
                        }
                    }
                }

            });


        }
    }

    // Item click listener interface
    public interface OnItemClickListener {
        void onItemClick(int pos);


    }
}
