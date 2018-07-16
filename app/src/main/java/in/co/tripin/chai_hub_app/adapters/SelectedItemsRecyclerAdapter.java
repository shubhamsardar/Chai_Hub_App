package in.co.tripin.chai_hub_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.co.tripin.chai_hub_app.POJOs.Models.Item;
import in.co.tripin.chai_hub_app.POJOs.Responces.OrderHistoryResponce;
import in.co.tripin.chai_hub_app.POJOs.Responces.PendingOrdersResponce;
import in.co.tripin.chai_hub_app.R;


public class SelectedItemsRecyclerAdapter extends RecyclerView.Adapter<SelectedItemsRecyclerAdapter.ViewHolder> {


    public List<PendingOrdersResponce.Datum.Detail> data;
    public OrderHistoryResponce.Data.Details[] details;
    private ArrayList<Item> mItems;


    public SelectedItemsRecyclerAdapter(List<PendingOrdersResponce.Datum.Detail> data) {
        this.data = data;
    }

    public SelectedItemsRecyclerAdapter(OrderHistoryResponce.Data.Details[] details) {
        this.details = details;
    }

    public SelectedItemsRecyclerAdapter(ArrayList<Item> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (data != null) {
            holder.name.setText(data.get(position).getItemName());
            holder.quant.setText(data.get(position).getQuantity() + "");
            holder.rate.setText("₹" + data.get(position).getAmount());
        } else {
            if (details != null) {
                holder.name.setText(details[position].getItemName());
                holder.quant.setText(details[position].getQuantity() + "");
                holder.rate.setText("₹" + details[position].getAmount());
            } else if (mItems != null) {
                holder.name.setText(mItems.get(position).getName());
                holder.quant.setText(mItems.get(position).getQuantity() + "");
                holder.rate.setText("₹" + mItems.get(position).getRate());
            }
        }
    }




    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            if (details != null) {
                return details.length;
            } else {
                if (mItems != null) {
                    return mItems.size();
                } else {
                    return 0;
                }
            }
        }
    }

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView quant;
    public TextView rate;

    public ViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.item_name);
        quant = itemView.findViewById(R.id.item_quant);
        rate = itemView.findViewById(R.id.item_rate);
    }
}
}
