package in.co.tripin.chai_hub_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.tripin.chai_hub_app.POJOs.Models.Item;
import in.co.tripin.chai_hub_app.R;


public class ItemsManageAdapter extends RecyclerView.Adapter<ItemsManageAdapter.ViewHolder> {

    public ArrayList<Item> data;
    public Context context;
    public ItemManageCallback itemManageCallback;

    public ItemsManageAdapter(Context context, ArrayList<Item> data, ItemManageCallback itemManageCallback) {
        this.data = data;
        this.context = context;
        this.itemManageCallback = itemManageCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.itemName.setText(data.get(position).getName());
        holder.itemRate.setText("₹" + data.get(position).getRate());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyItemRemoved(position);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemManageCallback.onitemEdit(position,data.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemRate;
        public ImageButton edit,remove;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemRate = itemView.findViewById(R.id.itemrate);
            edit = itemView.findViewById(R.id.edit);
            remove = itemView.findViewById(R.id.remove);



        }

    }
}
