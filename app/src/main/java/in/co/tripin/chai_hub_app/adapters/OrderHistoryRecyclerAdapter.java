package in.co.tripin.chai_hub_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import in.co.tripin.chai_hub_app.POJOs.Responces.OrderHistoryResponce;
import in.co.tripin.chai_hub_app.R;


public class OrderHistoryRecyclerAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerAdapter.ViewHolder> {

    public OrderHistoryResponce.Data[] data;
    public Context context;

    public OrderHistoryRecyclerAdapter(Context context, OrderHistoryResponce.Data[] data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        SelectedItemsRecyclerAdapter selectedItemsRecyclerAdapter = new SelectedItemsRecyclerAdapter(data[position].getDetails());
        RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(context);
        holder.mSelctedItemsList.setLayoutManager(layoutManager);
        holder.mSelctedItemsList.setAdapter(selectedItemsRecyclerAdapter);

        //holder.mTapriName.setText(data[position].getTapriId().getName());
        holder.mOrderId.setText("#"+data[position].getShortId().toUpperCase());
        holder.mOrderStatus.setText(data[position].getOrderStatus().toUpperCase());
        String timestamp = data[position].getCreatedAt();
        holder.mDate.setText(timestamp.substring(0,timestamp.indexOf('T')));

        holder.mTotalCost.setText("₹"+data[position].getTotalAmount());
        holder.mAddress.setText(data[position].getAddressId().getFullAddressString());
        holder.mPaymentMethod.setText(data[position].getPaymentType());
        holder.mAddress.setText(data[position].getTapriId().getName());

        if(position==data.length-1){
            holder.mBody.setVisibility(View.VISIBLE);
            holder.mOrderId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp, 0, 0, 0);
        }





    }

    @Override
    public int getItemCount() {
            return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //UI Elements
        public TextView mTapriName;
        public TextView mTotalCost;
        public TextView mAddress;
        public TextView mPaymentMethod;
        public RecyclerView mSelctedItemsList;
        public Switch mRecivedSwitch;
        public TextView mOrderId;
        public TextView mOrderStatus;
        public LinearLayout mHeader;
        public LinearLayout mBody;
        public TextView mDate;


        public ViewHolder(View itemView) {
            super(itemView);
            mTotalCost = itemView.findViewById(R.id.totalcost);
            mAddress = itemView.findViewById(R.id.addressall);
            mPaymentMethod = itemView.findViewById(R.id.paymentmethod);
            mSelctedItemsList = itemView.findViewById(R.id.selected_items_list);
            mOrderId = itemView.findViewById(R.id.orderid);
            mOrderStatus = itemView.findViewById(R.id.orderstatus);
            mHeader = itemView.findViewById(R.id.order_header);
            mBody = itemView.findViewById(R.id.order_footer);
            mDate = itemView.findViewById(R.id.orderdate);


            mHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBody.getVisibility()== View.GONE){
                        mBody.setVisibility(View.VISIBLE);
                        mOrderId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp, 0, 0, 0);


                    }else {
                        mBody.setVisibility(View.GONE);
                        mOrderId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp, 0, 0, 0);
                    }
                }
            });

        }
    }
}
