package `in`.co.tripin.chai_hub_app.activities


import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Models.Item
import `in`.co.tripin.chai_hub_app.POJOs.Models.OrderSummeryPOJO
import `in`.co.tripin.chai_hub_app.POJOs.Models.UserAddress
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubItemsPojo
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubMenuResponce
import `in`.co.tripin.chai_hub_app.POJOs.Responces.MappedHubResponce
import `in`.co.tripin.chai_hub_app.adapters.ItemsListAdapter
import `in`.co.tripin.chai_hub_app.networking.APIService
import `in`.co.tripin.chai_hub_app.R
import `in`.co.tripin.chai_hub_app.adapters.ItemSelectionCallback


import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_stock.*
import java.util.ArrayList
import java.util.HashMap

class NewStockActivity : AppCompatActivity() {


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


    private var mSnacksAdapter: ItemsListAdapter? = null
    private var mBeveragesAdapter: ItemsListAdapter? = null
    private var mExtrasAdapter: ItemsListAdapter? = null
    private var mChahiyehAdapter: ItemsListAdapter? = null

    //private TapriMenuResponce.Data.Item[] mSnacks,mBeverages,mExtras,mChaihiyeh;

    private var mItemsToggleHeader: LinearLayout? = null
    private var mAddresseHeader: LinearLayout? = null

    private var mChaihiyehll: LinearLayout? = null
    private var mBeveragesll:LinearLayout? = null
    private var mSnacksll:LinearLayout? = null
    private var mExtrasll:LinearLayout? = null
    private var mWalletInfo:LinearLayout? = null
    private var mItemsToggleText: TextView? = null
    private var mItemsList: LinearLayout? = null
    private var dialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_stock)
        title = "Order New Stock"
        queue = Volley.newRequestQueue(this)
        gson = Gson()
        preferenceManager = PreferenceManager.getInstance(this)

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Fetching Menu")
                .build()

        mCompositeDisposable = CompositeDisposable()
        apiService = APIService.create()

        init()
        setUpView()
        setListners()

        //get hub and id from api

        hitHubtemsListAPI()

    }



    private fun setListners() {
        mItemsToggleHeader!!.setOnClickListener {
            if (mItemsList!!.visibility != View.GONE) {
                mItemsList!!.visibility = View.GONE
                mItemsToggleText!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp, 0, 0, 0)
            } else {
                mItemsList!!.visibility = View.VISIBLE
                mItemsToggleText!!.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp, 0, 0, 0)

            }
        }


        mProceedToPay!!.setOnClickListener {
            val mItems = ArrayList<Item>()
            var cost = 0.0



            for (i in 0 until mChahiyehAdapter!!.data.size) {

                if (mChahiyehAdapter!!.data[i].quantity !== 0) {

                    mItems.add(mChahiyehAdapter!!.data[i])
                    var rate: Double? = 0.0
                    try {
                        rate = mChahiyehAdapter!!.data[i].rate.toDouble()
                    } catch (e: NumberFormatException) {
                        Logger.v("Rate Invalid: cant convert to double")
                    }

                    cost = cost + mChahiyehAdapter!!.data[i].quantity * rate!!
                }

            }
//            for (i in 0 until mExtrasAdapter!!.data.size) {
//
//                if (mExtrasAdapter!!.data[i].quantity !== 0) {
//
//                    mItems.add(mExtrasAdapter!!.data[i])
//                    var rate: Double? = 0.0
//                    try {
//                        rate = mExtrasAdapter!!.data[i].rate.toDouble()
//                    } catch (e: NumberFormatException) {
//                        Logger.v("Rate Invalid: cant convert to double")
//                    }
//
//                    cost = cost + mExtrasAdapter!!.data[i].quantity * rate!!
//                }
//            }
//            for (i in 0 until mSnacksAdapter!!.data.size) {
//
//                if (mSnacksAdapter!!.data[i].quantity !== 0) {
//
//                    mItems.add(mSnacksAdapter!!.data[i])
//                    var rate: Double? = 0.0
//                    try {
//                        rate = mSnacksAdapter!!.data[i].rate.toDouble()
//                    } catch (e: NumberFormatException) {
//                        Logger.v("Rate Invalid: cant convert to double")
//                    }
//
//                    cost = cost + mSnacksAdapter!!.data[i].quantity * rate!!
//                }
//            }
//            for (i in 0 until mBeveragesAdapter!!.data.size) {
//
//                if (mBeveragesAdapter!!.data[i].quantity !== 0) {
//
//                    mItems.add(mBeveragesAdapter!!.data[i])
//                    var rate: Double? = 0.0
//                    try {
//                        rate = mBeveragesAdapter!!.data[i].rate.toDouble()
//                    } catch (e: NumberFormatException) {
//                        Logger.v("Rate Invalid: cant convert to double")
//                    }
//
//                    cost += mBeveragesAdapter!!.data[i].quantity * rate!!
//                }
//            }


//            if (address == null) {
//                Toast.makeText(applicationContext, "Address Required!", Toast.LENGTH_LONG).show()
//            } else {

                val paymentMethod = "COD"
                val orderSummeryPOJO = OrderSummeryPOJO(hubId,
                        tapriName,
                        java.lang.Double.toString(cost),
                        address,
                        paymentMethod,
                        mItems,hubAddressId)

                if (!orderSummeryPOJO.getmItems().isEmpty()) {

                        val i = Intent(this@NewStockActivity, OrderSummeryActivity::class.java)
                        i.putExtra("ordersummery", orderSummeryPOJO)
                        startActivity(i)


                } else {
                    Toast.makeText(applicationContext, "Select Some Items!", Toast.LENGTH_LONG).show()

                }


        }



    }


    private fun init() {
        Logger.v("setting UI..")

        mBeveragesList = findViewById(R.id.beverages)
        mSnacksList = findViewById(R.id.snacks)
        mExtrasList = findViewById(R.id.extras)
        mChaihiyehList = findViewById(R.id.chaihiyeh)

        mAddressNick = findViewById(R.id.addressnick)
        mAddressNick!!.text = "Your Default Address"
        mAddressFull = findViewById(R.id.fulladdress)


        mBeveragesll = findViewById(R.id.beveragesll)
        mSnacksll = findViewById(R.id.snacksll)
        mChaihiyehll = findViewById(R.id.chaihiyehll)
        mExtrasll = findViewById(R.id.extrasll)

        mItemsToggleHeader = findViewById(R.id.itrms_header)
        mAddresseHeader = findViewById(R.id.address_header)

        mItemsList = findViewById(R.id.items_lists)
        mItemsToggleText = findViewById(R.id.items_title)
        mAddressInclude = findViewById(R.id.address_include)
        mAddressCancel = findViewById(R.id.remove)

        mProceedToPay = findViewById(R.id.proceedtopay)

        mNoItems = findViewById(R.id.tv_noitems)

        mMainScroll = findViewById(R.id.mainscroll)


    }

    private fun setUpView() {

        //        mSnacks = new TapriMenuResponce.Data.Item[]{};
        //        mBeverages = new TapriMenuResponce.Data.Item[]{};
        //        mExtras = new TapriMenuResponce.Data.Item[]{};


        val mLayoutManager = LinearLayoutManager(applicationContext)
        mSnacksList!!.layoutManager = mLayoutManager
        val mLayoutManager1 = LinearLayoutManager(applicationContext)
        mBeveragesList!!.layoutManager = mLayoutManager1
        val mLayoutManager2 = LinearLayoutManager(applicationContext)
        mExtrasList!!.layoutManager = mLayoutManager2
        val mLayoutManager3 = LinearLayoutManager(applicationContext)
        mChaihiyehList!!.layoutManager = mLayoutManager3




    }

    fun setAddress(){
        mAddressCancel!!.visibility = View.GONE
        if (preferenceManager.hubAddress == null) {
            mAddressInclude!!.visibility = View.GONE
        } else {
            mAddressInclude!!.visibility = View.VISIBLE
            address = gson!!.fromJson(preferenceManager!!.getHubAddress(), UserAddress.Data::class.java)
            mAddressFull!!.text = address!!.fullAddressString

        }
    }

    private fun hitHubtemsListAPI() {

        Logger.v("getting menu...")
        dialog!!.show()
        val url = "http://192.168.1.21:3055/api/v1/vendor/items"

        val getRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    // display response
                    //Toast.makeText(getApplicationContext(), "List Fetched!", Toast.LENGTH_SHORT).show();
                    Logger.v("Response: " + response.toString())

                    //val dummyres = ""
                    hubsItemPojo = Gson().fromJson(response.toString(), HubItemsPojo::class.java)
                    setItems(hubsItemPojo!!.data)
                    proceedtopay.visibility = View.VISIBLE
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


//        mBeveragesAdapter = ItemsListAdapter(this, data.beverages, object : ItemSelectionCallback {
//            override fun onitemAdded(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! + cost!! * quant
//                updateTotalCostUI()
//            }
//
//            override fun onItemRemoved(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! - cost!! * quant
//                updateTotalCostUI()
//
//            }
//        })
//        mExtrasAdapter = ItemsListAdapter(this, data.extra, object : ItemSelectionCallback {
//            override fun onitemAdded(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! + cost!! * quant
//                updateTotalCostUI()
//
//            }
//
//            override fun onItemRemoved(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! - cost!! * quant
//                updateTotalCostUI()
//
//            }
//        })
//        mSnacksAdapter = ItemsListAdapter(this, data.snacks, object : ItemSelectionCallback {
//            override fun onitemAdded(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! + cost!! * quant
//                updateTotalCostUI()
//
//            }
//
//            override fun onItemRemoved(cost: Double?, quant: Int) {
//                mTotalCost = mTotalCost!! - cost!! * quant
//                updateTotalCostUI()
//
//            }
//        })
        mChahiyehAdapter = ItemsListAdapter(this, data.chaihiyeh, object : ItemSelectionCallback {
            override fun onitemAdded(cost: Double?, quant: Int) {
                mTotalCost = mTotalCost!! + cost!! * quant
                updateTotalCostUI()

            }

            override fun onItemRemoved(cost: Double?, quant: Int) {
                mTotalCost = mTotalCost!! - cost!! * quant
                updateTotalCostUI()

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
//        if (!data.beverages.isEmpty()) {
//            mBeveragesll!!.visibility = View.VISIBLE
//            isNoItemsTexttobeHidden = false
//        }
        if (!data.chaihiyeh.isEmpty()) {
            mChaihiyehll!!.visibility = View.VISIBLE
            isNoItemsTexttobeHidden = false
        }
//        if (!data.extra.isEmpty()) {
//            mExtrasll!!.visibility = View.VISIBLE
//            isNoItemsTexttobeHidden = false
//        }
//        if (!data.snacks.isEmpty()) {
//            mSnacksll!!.visibility = View.VISIBLE
//            isNoItemsTexttobeHidden = false
//        }
        if (isNoItemsTexttobeHidden) {
            mNoItems!!.visibility = View.GONE
        }
    }

//    // Call Back method  to get the Message form other Activity
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
//        super.onActivityResult(requestCode, resultCode, intent)
//
//        Logger.v("On Activity Result : result code: $resultCode request code:$requestCode")
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 1) {
//                Toast.makeText(applicationContext, "Address Selected!", Toast.LENGTH_SHORT).show()
//
//                address = intent.getSerializableExtra("address") as UserAddress.Data
//                val fulladdressstring = (address!!.landmark + ", "
//                        + address!!.flatSociety + ", "
//                        + address!!.addressLine1 + ", "
//                        + address!!.addressLine2 + ", "
//                        + address!!.city + ", "
//                        + address!!.country)
//
//                mAddressFull!!.text = fulladdressstring
//                mAddressNick!!.text = address!!.nickname
//                preferenceManager!!.setHubAddress(gson!!.toJson(address))
//                mAddressInclude!!.visibility = View.VISIBLE
//                mAddressInclude!!.background = ContextCompat.getDrawable(applicationContext, R.color.colorHighlight)
//                Logger.v("selected address is: $fulladdressstring")
//            }
//        } else {
//            Toast.makeText(applicationContext, "Address not selected!", Toast.LENGTH_SHORT).show()
//        }
//
//
//    }

    private fun updateTotalCostUI() {
        proceedtopay.text = "Proceed: ₹$mTotalCost"
    }


}
