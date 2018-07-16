package in.co.tripin.chai_hub_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import in.co.tripin.chai_hub_app.POJOs.Models.Item;
import in.co.tripin.chai_hub_app.R;


public class ItemsToggleAdapter extends RecyclerView.Adapter<ItemsToggleAdapter.ViewHolder> {

    public Item[] data;
    public Context context;
    public ItemToggleCallback itemToggleCallback;

    public ItemsToggleAdapter(Context context, Item[] data, ItemToggleCallback itemToggleCallback) {
        this.data = data;
        this.context = context;
        this.itemToggleCallback = itemToggleCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_toggle_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.itemName.setText(data[position].getName());
        holder.itemRate.setText("₹" + data[position].getRate());
        if(data[position].getCategory().equals("Chaihiyeh")){
            //check flag
            if(data[position].getFlag().equals("1")){
                holder.toggle.setChecked(true);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreyLight));
            }else {
                holder.toggle.setChecked(false);
            }

        }else {
            //check status
            if(data[position].getStatus().equals("1")){
                holder.toggle.setChecked(true);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreyLight));
            }else {
                holder.toggle.setChecked(false);
            }

        }
        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreyLight));
                    itemToggleCallback.onItemInactive(data[position].get_id());

                }else {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGreyWhite));
                    itemToggleCallback.onitemActive(data[position].get_id());
                }
            }
        });



    }



    @Override
    public int getItemCount() {
        if (data != null) {
            return data.length;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemRate;
        public Switch toggle;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemRate = itemView.findViewById(R.id.itemrate);
            toggle = itemView.findViewById(R.id.toggle);


        }

    }
}
