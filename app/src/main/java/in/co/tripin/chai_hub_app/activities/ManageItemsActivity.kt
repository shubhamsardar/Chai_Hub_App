package `in`.co.tripin.chai_hub_app.activities


import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Models.UserAddress
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubItemsPojo
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubMenuResponce
import `in`.co.tripin.chai_hub_app.POJOs.Responces.MappedHubResponce
import `in`.co.tripin.chai_hub_app.R
import `in`.co.tripin.chai_hub_app.adapters.ItemToggleCallback
import `in`.co.tripin.chai_hub_app.adapters.ItemsToggleAdapter
import `in`.co.tripin.chai_hub_app.networking.APIService
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

class ManageItemsActivity : AppCompatActivity() {

    lateinit var manageItems : TextView

    lateinit var preferenceManager: PreferenceManager
    lateinit var apiService: APIService
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var hubsData: MappedHubResponce
    lateinit var hubsItemPojo : HubItemsPojo



    private var hubId: String? = ""
    private var hubAddressId: String? = ""
    private var tapriName: String? = ""
    private var address: UserAddress.Data? = null
    private var queue: RequestQueue? = null
    private var tapriMenuResponce: HubMenuResponce? = null
    private var gson: Gson? = null
    private var mTotalCost: Double? = 0.0
    private var mAvailableBalance: Double? = 0.0
    internal var mMoneyTobeAdded: Double? = 0.0



    private var mSnacksList: RecyclerView? = null
    private var mBeveragesList: RecyclerView? = null
    private var mExtrasList: RecyclerView? = null
    private var mChaihiyehList: RecyclerView? = null
    private var mAddressInclude: View? = null
    private var mAddressCancel: ImageView? = null

    private var mAddressNick: TextView? = null
    private var mAddressFull: TextView? = null
    private var mProceedToPay: TextView? = null
    private var mBalance: TextView? = null
    private var mAddMoney: TextView? = null
    private var mPaymentHeader: TextView? = null
    private var mPaymentType: RadioGroup? = null
    private var mMainScroll: ScrollView? = null
    private var mNoItems: TextView? = null


    private var mSnacksAdapter: ItemsToggleAdapter? = null
    private var mBeveragesAdapter: ItemsToggleAdapter? = null
    private var mExtrasAdapter: ItemsToggleAdapter? = null
    private var mChahiyehAdapter: ItemsToggleAdapter? = null

    //private TapriMenuResponce.Data.Item[] mSnacks,mBeverages,mExtras,mChaihiyeh;

    private var mItemsToggleHeader: LinearLayout? = null
    private var mAddresseHeader: LinearLayout? = null

    private var mChaihiyehll: LinearLayout? = null
    private var mBeveragesll: LinearLayout? = null
    private var mSnacksll: LinearLayout? = null
    private var mExtrasll: LinearLayout? = null
    private var mWalletInfo: LinearLayout? = null
    private var mItemsToggleText: TextView? = null
    private var mItemsList: LinearLayout? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_items)
        title = "Items Availability"
        init()
        setListners()

        queue = Volley.newRequestQueue(this)
        gson = Gson()
        preferenceManager = PreferenceManager.getInstance(this)

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .build()

        mCompositeDisposable = CompositeDisposable()
        apiService = APIService.create()


    }

    override fun onStart() {
        super.onStart()
        if(preferenceManager.tapriId!=null){
            hitTapritemsListAPI(preferenceManager.tapriId)
        }else{
            Toast.makeText(applicationContext,"Tapri ID null",Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        manageItems = findViewById(R.id.managelist)

        mBeveragesList = findViewById(R.id.beverages)
        mSnacksList = findViewById(R.id.snacks)
        mExtrasList = findViewById(R.id.extras)
        mChaihiyehList = findViewById(R.id.chaihiyeh)

        mBeveragesll = findViewById(R.id.beveragesll)
        mSnacksll = findViewById(R.id.snacksll)
        mChaihiyehll = findViewById(R.id.chaihiyehll)
        mExtrasll = findViewById(R.id.extrasll)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        mSnacksList!!.layoutManager = mLayoutManager
        val mLayoutManager1 = LinearLayoutManager(applicationContext)
        mBeveragesList!!.layoutManager = mLayoutManager1
        val mLayoutManager2 = LinearLayoutManager(applicationContext)
        mExtrasList!!.layoutManager = mLayoutManager2
        val mLayoutManager3 = LinearLayoutManager(applicationContext)
        mChaihiyehList!!.layoutManager = mLayoutManager3
    }


    private fun setListners() {
        manageItems.setOnClickListener {
            val intent = Intent(this, ManageItemsListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hitTapritemsListAPI(tapriId : String) {

        Logger.v("getting menu...")
        dialog!!.show()
        val url = "http://192.168.1.21:3055/api/v1/hub/items/all"

        val getRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    // display response
                    //Toast.makeText(getApplicationContext(), "List Fetched!", Toast.LENGTH_SHORT).show();
                    Logger.v("Response: " + response.toString())

                    //val dummyres = ""
                    hubsItemPojo = Gson().fromJson(response.toString(), HubItemsPojo::class.java)
                    setItems(hubsItemPojo!!.data)
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

    private fun hitToggleItemAPI(tapriId : String, operation:String) {

        Logger.v("Toggle $tapriId : $operation")
        dialog!!.show()
        val url = "http://192.168.1.21:3055/api/v1/hub/items/$tapriId/$operation"

        val getRequest = object : JsonObjectRequest(Request.Method.PATCH, url, null,
                Response.Listener { response ->
                    // display response
                    //Toast.makeText(getApplicationContext(), "List Fetched!", Toast.LENGTH_SHORT).show();
                    dialog!!.dismiss()
                    Toast.makeText(applicationContext, "Item $operation", Toast.LENGTH_SHORT).show()

                    Logger.v("ResponseToggle: " + response.toString())

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

    private fun setItems(data: HubItemsPojo.Data) {

        setListVisiblity(data)


        mBeveragesAdapter = ItemsToggleAdapter(this, data.beverages, object : ItemToggleCallback {

            override fun onItemInactive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"active")
                }
            }

            override fun onitemActive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"inactive")
                }
            }
        })
        mExtrasAdapter = ItemsToggleAdapter(this, data.extra, object : ItemToggleCallback {

            override fun onItemInactive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"active")
                }

            }

            override fun onitemActive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"inactive")
                }

            }
        })
        mSnacksAdapter = ItemsToggleAdapter(this, data.snacks, object : ItemToggleCallback {

            override fun onItemInactive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"active")
                }

            }

            override fun onitemActive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"inactive")
                }
            }
        })

        mChahiyehAdapter = ItemsToggleAdapter(this, data.chaihiyeh,object : ItemToggleCallback {

            override fun onItemInactive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"active")
                }

            }

            override fun onitemActive(itemId: String?) {
                if (itemId != null) {
                    hitToggleItemAPI(itemId,"inactive")
                }
            }
        })

        //Logger.v("in Extras: "+mExtras[0].toString());

        mBeveragesList!!.adapter = mBeveragesAdapter
        mSnacksList!!.adapter = mSnacksAdapter
        mExtrasList!!.adapter = mExtrasAdapter
        mChaihiyehList!!.adapter = mChahiyehAdapter


        //        mSnacksAdapter.notifyDataSetChanged();
        //        mBeveragesAdapter.notifyDataSetChanged();
        //        mExtrasAdapter.notifyDataSetChanged();


        dialog!!.dismiss()


    }



    private fun setListVisiblity(data: HubItemsPojo.Data) {

        mBeveragesll!!.visibility = View.GONE
        mChaihiyehll!!.visibility = View.GONE
        mSnacksll!!.visibility = View.GONE
        mExtrasll!!.visibility = View.GONE
        var isNoItemsTexttobeHidden = true
        if (!data.beverages.isEmpty()) {
            mBeveragesll!!.visibility = View.VISIBLE
            isNoItemsTexttobeHidden = false
        }
        if (!data.chaihiyeh.isEmpty()) {
            mChaihiyehll!!.visibility = View.VISIBLE
            isNoItemsTexttobeHidden = false
        }
        if (!data.extra.isEmpty()) {
            mExtrasll!!.visibility = View.VISIBLE
            isNoItemsTexttobeHidden = false
        }
        if (!data.snacks.isEmpty()) {
            mSnacksll!!.visibility = View.VISIBLE
            isNoItemsTexttobeHidden = false
        }
        if (isNoItemsTexttobeHidden) {
            mNoItems!!.visibility = View.GONE
        }
    }

    private fun updateTotalCostUI() {
        //proceedtopay.text = "Proceed: ₹$mTotalCost"
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
            hitTapritemsListAPI(preferenceManager.tapriId)
        }

        return super.onOptionsItemSelected(item)
    }


}
