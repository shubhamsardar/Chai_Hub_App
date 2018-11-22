package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Helper.Constants
import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Responces.OrderHistoryResponce
import `in`.co.tripin.chai_hub_app.R
import `in`.co.tripin.chai_hub_app.adapters.MarkRecivedCallback
import `in`.co.tripin.chai_hub_app.adapters.StockHistoryRecyclerAdapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import org.json.JSONObject
import java.util.HashMap

class StockHistoryActivity : AppCompatActivity() {
    private var orderHistoryRecyclerAdapter: StockHistoryRecyclerAdapter? = null
    private var dialog: AlertDialog? = null
    private var orderHistoryResponce: OrderHistoryResponce? = null
    private var gson: Gson? = null
    private var queue: RequestQueue? = null
    private var preferenceManager: PreferenceManager? = null
    private var mContext: Context? = null

    private var mOderHistoryList: RecyclerView? = null
    private var mLoadingMsg: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_history)
        title = "Stock Order History"
        mContext = this
        gson = Gson()
        queue = Volley.newRequestQueue(this)
        preferenceManager = PreferenceManager.getInstance(this)

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Loading")
                .build()

        mOderHistoryList = findViewById(R.id.orderhistorylist)
        mLoadingMsg = findViewById(R.id.loadingtv)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.stackFromEnd = true
        mLayoutManager.reverseLayout = true
        mOderHistoryList!!.layoutManager = mLayoutManager

        callOrderHistoryAPI()

    }

    private fun callOrderHistoryAPI() {

        Logger.v("fetch List of Order History..")
        dialog!!.show()
        val url = Constants.BASE_URL+"api/v2/hub/stock"
        val getRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    // display response
                    Log.d("Response", response.toString())
                    orderHistoryResponce = gson!!.fromJson(response.toString(), OrderHistoryResponce::class.java)
                    if (orderHistoryResponce != null) {
                        setAdapter(orderHistoryResponce!!.data)
                        dialog!!.dismiss()
                        if (orderHistoryResponce!!.data.isNotEmpty()) {
                            mLoadingMsg!!.visibility = View.GONE
                        } else {
                            mLoadingMsg!!.visibility = View.VISIBLE
                            mLoadingMsg!!.text = "Empty Order History, Get some orders!"
                        }

                    }
                },
                Response.ErrorListener { error ->
                    dialog!!.dismiss()
                    Logger.v("Error.Response: " + error.toString())
                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
                }
        ) {

            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["token"] = preferenceManager!!.accessToken
                return params
            }
        }

        queue!!.add(getRequest)


    }

    private fun setAdapter(data: Array<OrderHistoryResponce.Data>) {

        Logger.v("adapter set")
        orderHistoryRecyclerAdapter = StockHistoryRecyclerAdapter(this, data, MarkRecivedCallback {
            hitToggleItemAPI(it,"recieved")
        })
        mOderHistoryList!!.adapter = orderHistoryRecyclerAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(this@StockHistoryActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }




    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_order_history, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_refresh) {
            callOrderHistoryAPI()
        }
        //        } else if (id == R.id.action_call) {
        //            //call to enquiry
        //        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitToggleItemAPI(orderId : String, operation:String) {

        Logger.v("Toggle $orderId : $operation")
        dialog!!.show()
        val url = Constants.BASE_URL+"api/v2/order/$orderId/status/$operation"

        val getRequest = object : JsonObjectRequest(Request.Method.PATCH, url, null,
                Response.Listener { response ->
                    // display response
                    //Toast.makeText(getApplicationContext(), "List Fetched!", Toast.LENGTH_SHORT).show();
                    dialog!!.dismiss()
                    Toast.makeText(applicationContext, "Item $operation", Toast.LENGTH_SHORT).show()
                    Logger.v("ResponseToggle: " + response.toString())
                    callOrderHistoryAPI()

                },
                Response.ErrorListener { error ->
                    Logger.v("Error.Response: " + error.message.toString())
                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()
                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["token"] = preferenceManager!!.accessToken
                return params
            }


            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }


        }
        queue!!.add(getRequest)
    }
}
