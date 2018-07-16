package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Models.Item
import `in`.co.tripin.chai_hub_app.POJOs.Models.UserAddress
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubItemsPojo
import `in`.co.tripin.chai_hub_app.POJOs.Responces.HubMenuResponce
import `in`.co.tripin.chai_hub_app.POJOs.Responces.MappedHubResponce
import `in`.co.tripin.chai_hub_app.adapters.ItemManageCallback
import `in`.co.tripin.chai_hub_app.adapters.ItemsManageAdapter
import `in`.co.tripin.chai_hub_app.R



import `in`.co.tripin.chai_hub_app.networking.APIService
import android.app.AlertDialog
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.HashMap

class ManageItemsListActivity : AppCompatActivity() {

    lateinit var manageItems: TextView
    lateinit var addBeverage: TextView
    lateinit var addSnack: TextView
    lateinit var addExtra: TextView
    lateinit var mRequestBody : String


    lateinit var preferenceManager: PreferenceManager
    lateinit var apiService: APIService
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var hubsData: MappedHubResponce
    lateinit var hubsItemPojo: HubItemsPojo
    lateinit var editDialog : Dialog


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


    private var mSnacksAdapter: ItemsManageAdapter? = null
    private var mBeveragesAdapter: ItemsManageAdapter? = null
    private var mExtrasAdapter: ItemsManageAdapter? = null
    private var mChahiyehAdapter: ItemsManageAdapter? = null

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
        setContentView(R.layout.activity_manage_items_list)
        title = "Manage Items List"
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
        if (preferenceManager.tapriId != null) {
            hitTapritemsListAPI()
        } else {
            Toast.makeText(applicationContext, "Tapri ID null", Toast.LENGTH_SHORT).show()
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

        addBeverage = findViewById(R.id.new_bev)
        addSnack = findViewById(R.id.new_snacks)
        addExtra = findViewById(R.id.new_extra)


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

            getDataFromList()

        }

        addExtra.setOnClickListener {
            var item : Item = Item()
            item.category = "Extra"
            createEditDialog(mExtrasAdapter!!.data.size, item)
        }

        addSnack.setOnClickListener {
            var item : Item = Item()
            item.category = "Snacks"
            createEditDialog(mSnacksAdapter!!.data.size, item)        }

        addBeverage.setOnClickListener {
            var item : Item = Item()
            item.category = "Beverages"
            createEditDialog(mBeveragesAdapter!!.data.size, item)        }
    }

    private fun hitTapritemsListAPI() {

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


    private fun setItems(data: HubItemsPojo.Data) {

        setListVisiblity(data)

        val bevs:ArrayList<Item> = ArrayList()
        for(item : Item in data.beverages){
            bevs.add(item)
        }
        mBeveragesAdapter = ItemsManageAdapter(this, bevs, ItemManageCallback { position, item ->
            createEditDialog(position,item)
        })

        val snacks:ArrayList<Item> = ArrayList()
        for(item : Item in data.snacks){
            snacks.add(item)
        }
        mSnacksAdapter = ItemsManageAdapter(this, snacks, ItemManageCallback { position, item ->
            createEditDialog(position,item)
        })

        val extras:ArrayList<Item> = ArrayList()
        for(item : Item in data.extra){
            extras.add(item)
        }
        mExtrasAdapter = ItemsManageAdapter(this, extras, ItemManageCallback { position, item ->
            createEditDialog(position,item)
        })
        val chaihiyeh:ArrayList<Item> = ArrayList()
        for(item : Item in data.chaihiyeh){
            chaihiyeh.add(item)
        }
        mChahiyehAdapter = ItemsManageAdapter(this, chaihiyeh, ItemManageCallback{ position, item ->
            createEditDialog(position,item)
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

        mChaihiyehll!!.visibility = View.GONE

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
            hitTapritemsListAPI()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createEditDialog(i : Int, item : Item) {
        // custom dialog
        editDialog = Dialog(this)
        editDialog.setContentView(R.layout.edititem_dialog)
        editDialog.setTitle("Sending OTP")
        editDialog.setCancelable(false)
        val window = editDialog.window
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val okButton = editDialog.findViewById<TextView>(R.id.ok)
        val cancelButton = editDialog.findViewById<TextView>(R.id.cancel)
        val iname = editDialog.findViewById<EditText>(R.id.name)
        val iprice = editDialog.findViewById<EditText>(R.id.price)

        if(item.name!=null){
            iname.setText(item.name)
            iname.setSelection(iname.text.toString().length)
        }
        if(item.rate!=null){
            iprice.setText(item.rate)
            iprice.setSelection(iprice.text.toString().length)
        }

        cancelButton.setOnClickListener {
            editDialog.dismiss()
        }

        okButton.setOnClickListener {

            if(!iname.text.toString().isEmpty()&&!iprice.text.toString().isEmpty()){
                //
                item.name = iname.text.toString()
                item.rate = iprice.text.toString()

                when(item.category){
                    "Snacks"->{
                        if(mSnacksAdapter!!.data.size==i){
                            mSnacksAdapter!!.data.add(item)
                            mSnacksAdapter!!.notifyItemInserted(i)
                        }else{
                            mSnacksAdapter!!.data[i] = item
                            mSnacksAdapter!!.notifyItemChanged(i)
                        }


                    }
                    "Beverages"->{
                        if(mBeveragesAdapter!!.data.size==i){
                            mBeveragesAdapter!!.data.add(item)
                            mBeveragesAdapter!!.notifyItemInserted(i)
                        }else{
                            mBeveragesAdapter!!.data[i] = item
                            mBeveragesAdapter!!.notifyItemChanged(i)
                        }


                    }
                    "Extra"->{
                        if(mExtrasAdapter!!.data.size==i){
                            mExtrasAdapter!!.data.add(item)
                            mExtrasAdapter!!.notifyItemInserted(i)
                        }else{
                            mExtrasAdapter!!.data[i] = item
                            mExtrasAdapter!!.notifyItemChanged(i)
                        }


                    }
                }
                editDialog.dismiss()

                manageItems.background = ContextCompat.getDrawable(applicationContext,R.drawable.button_main_selector)

            }else{
                Toast.makeText(applicationContext,"Name or Prise cannot be blank",Toast.LENGTH_LONG).show()
            }

        }

        editDialog.show()

    }

    private fun getDataFromList() {

        val jsonBody = JSONObject()
        try {
            val jsonArrey = JSONArray()

            for(item : Item in mBeveragesAdapter!!.data){
                val jsonItem = JSONObject()
                jsonItem.put("name",item.name)
                jsonItem.put("rate",item.rate)
                jsonItem.put("category",item.category)
                jsonItem.put("status",item.status)

                if(item._id!=null)
                jsonItem.put("_id",item._id)

                jsonArrey.put(jsonItem)
            }
            for(item : Item in mExtrasAdapter!!.data){
                val jsonItem = JSONObject()
                jsonItem.put("name",item.name)
                jsonItem.put("rate",item.rate)
                jsonItem.put("category",item.category)
                jsonItem.put("status",item.status)

                if(item._id!=null)
                    jsonItem.put("_id",item._id)

                jsonArrey.put(jsonItem)
            }

            for(item : Item in mSnacksAdapter!!.data){
                val jsonItem = JSONObject()
                jsonItem.put("name",item.name)
                jsonItem.put("rate",item.rate)
                jsonItem.put("category",item.category)
                jsonItem.put("status",item.status)

                if(item._id!=null)
                    jsonItem.put("_id",item._id)

                jsonArrey.put(jsonItem)
            }

            jsonBody.put("items", jsonArrey)
            mRequestBody = jsonBody.toString()
            Logger.v("RequestBody: $mRequestBody")
            HitSaveChangesAPI()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun HitSaveChangesAPI() {
        Logger.v("saving changes")
        dialog!!.show()
        val url = "http://192.168.1.21:3055/api/v1/hub/items/edit"
        val getRequest = object : JsonObjectRequest(Request.Method.PATCH, url, null,
                Response.Listener { response ->
                    // display response
                    dialog!!.dismiss()
                    Toast.makeText(applicationContext, "Changes Saved!", Toast.LENGTH_LONG).show()
                    Log.d("Response", response.toString())
                    finish()
                },
                Response.ErrorListener { error ->
                    dialog!!.dismiss()
                    Log.d("Error.Response", error.toString())
                    Toast.makeText(applicationContext, "Try Again!", Toast.LENGTH_LONG).show()
                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["token"] = preferenceManager.accessToken
                return params
            }


            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray? {
                try {
                    return mRequestBody?.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8")
                    return null
                }

            }
        }
        queue!!.add(getRequest)
    }
}
