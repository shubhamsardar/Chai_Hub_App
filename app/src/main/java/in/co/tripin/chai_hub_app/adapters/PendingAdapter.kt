package `in`.co.tripin.chai_hub_app.adapters


import `in`.co.tripin.chai_hub_app.POJOs.Responces.PendingOrdersResponce
import `in`.co.tripin.chai_hub_app.R

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.item_order_pending.view.*

class PendingAdapter(val data: List<PendingOrdersResponce.Datum>,
                     val context :Context,
                     val pendingOrdersInteractionCallback: PendingOrdersInteractionCallback) :
        RecyclerView.Adapter<PendingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_pending, parent, false))
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(data[position])
        val status = data[position].orderStatus

        if(status == "accepted"){
            holder.b2.background = ContextCompat.getDrawable(context,R.drawable.button_main_selector)
            holder.b2.text = "Send Order"
            //holder.b2.setTextColor(ContextCompat.getColor(context,R.color.colorGreyLight))
            holder.b1.text = "Contact Customer"
        }else if(status == "on the way"){
            holder.b2.background = null
            holder.b2.text = "Ready for PickUp!"
            holder.b1.text = "Contact Customer"

        }

        holder.b1.setOnClickListener{

            if(status == "accepted"){
                pendingOrdersInteractionCallback.onCalledCustomer(data[position].userId.mobile)
            }else if(status == "ordered"){
                pendingOrdersInteractionCallback.onOrderRejected(data[position].id)
            }

        }
        holder.b2.setOnClickListener{
            if(status == "accepted"){
                pendingOrdersInteractionCallback.onOrderSent(data[position].id)
            }else if(status == "ordered"){
                pendingOrdersInteractionCallback.onOrderAccepted(data[position].id)
//                holder.b2.background = ContextCompat.getDrawable(context,R.drawable.button_light_selector)
//                holder.b2.text = "Send Order"
//                //holder.b2.setTextColor(ContextCompat.getColor(context,R.color.colorGreyLight))
//                holder.b1.text = "Contact Customer"
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val totalCost = itemView.findViewById<TextView>(R.id.totalcost)

        val b1 = itemView.findViewById<Button>(R.id.b1)
        val b2 = itemView.findViewById<Button>(R.id.b2)


        @SuppressLint("SetTextI18n")
        fun bindItems(order : PendingOrdersResponce.Datum) = with(itemView)  {

            itemView.totalcost.text = "₹"+order.totalAmount.toString()
            itemView.addressall.text = order.tapriId.name
            itemView.orderid.text = "# ${order.shortId.toUpperCase()}"




            val selectedItemsRecyclerAdapter = SelectedItemsRecyclerAdapter(order.details)
            val layoutManager = CustomLinearLayoutManager(context)
            itemView.selected_items_list.layoutManager = layoutManager
            itemView.selected_items_list.adapter = selectedItemsRecyclerAdapter


            itemView.b2.setOnClickListener{}

        }
    }
}