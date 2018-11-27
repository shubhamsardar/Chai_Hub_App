package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Helper.Constants
import `in`.co.tripin.chai_hub_app.Helper.SingleShotLocationProvider
import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Responces.PendingOrdersResponce
import `in`.co.tripin.chai_hub_app.R
import `in`.co.tripin.chai_hub_app.R.id.drawer_layout
import `in`.co.tripin.chai_hub_app.R.id.pendinglist
import `in`.co.tripin.chai_hub_app.adapters.PendingOrdersInteractionCallback

import `in`.co.tripin.chai_hub_app.adapters.PendingAdapter
import `in`.co.tripin.chai_hub_app.networking.APIService
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONObject
import java.util.HashMap

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PendingOrdersInteractionCallback {


    lateinit var switch: Switch
    private var dialog: AlertDialog? = null
    lateinit var mContext: Context

    lateinit var apiService: APIService
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var preferenceManager: PreferenceManager
    private var queue: RequestQueue? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var hubname: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mContext = this
        mCompositeDisposable = CompositeDisposable()
        apiService = APIService.create()
        preferenceManager = PreferenceManager.getInstance(this)
        queue = Volley.newRequestQueue(this)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        pendinglist.layoutManager = linearLayoutManager
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Loading")
                .build()
        title = "Fetching..."
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        fab.setOnClickListener {
            fetchPendingOrders()
        }


        if (preferenceManager.userName != null) {
            hubname = nav_view.getHeaderView(0).findViewById(R.id.hubname)
            hubname.text = preferenceManager.userName.toUpperCase()
        }


    }

    override fun onStart() {
        super.onStart()
        fetchPendingOrders()
    }

    private fun fetchPendingOrders() {

        title = "Fetching..."
        mCompositeDisposable?.add(apiService.getPendingOrders(preferenceManager.accessToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_pending -> {
                // Handle the camera action
            }
            R.id.nav_history -> {
                val intent = Intent(this, OrderHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_stocknew -> {
                val intent = Intent(this, NewStockActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_stockhistory -> {
                val intent = Intent(this, StockHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_manage -> {
                val intent = Intent(this, ManageItemsActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_changepin -> {
                val intent = Intent(this, ChangePinActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_logout -> {
                preferenceManager.clearLoginPreferences()
                val intent = Intent(this, SpalshActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.nav_rate -> {
                rateApp()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleResponse(responce: PendingOrdersResponce) {

        Log.v("OnResponcePending: ", responce.status)
        pendinglist.adapter = PendingAdapter(responce.data, this, this)
        val size: Int = responce.data.size
        when (size) {
            0 -> {
                title = "No Orders for you"
            }
            1 -> {
                title = "$size Order Pending"
            }
            else -> {
                title = "$size Orders Pending"
            }
        }

    }

    private fun handleError(error: Throwable) {
        Log.v("OnErrorPending", error.toString())
    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    override fun onOrderAccepted(mOrderId: String?) {
        callEditOrderAPI(mOrderId, "accepted")

    }

    override fun onOrderRejected(mOrderId: String?) {
        callEditOrderAPI(mOrderId, "rejected")
    }

    override fun onOrderSent(mOrderId: String?) {
        callEditOrderAPI(mOrderId, "sent")
    }

    @SuppressLint("MissingPermission")
    override fun onCalledCustomer(mMobile: String?) {
        //call to admin
        //call to admin
        if (isPermissionGranted()) {
            call_action(mMobile)
        }
    }

    private fun callEditOrderAPI(mOrderId: String?, mOperation: String) {

        Logger.v("Marking Order Recived")
        dialog!!.show()
        val url = Constants.BASE_URL + "api/v2/order/$mOrderId/status/$mOperation"
        val getRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                com.android.volley.Response.Listener<JSONObject> { response ->
                    // display response
                    dialog!!.dismiss()
                    Logger.v("ResponseEdit :" + response.toString())
                    Toast.makeText(this, mOperation, Toast.LENGTH_LONG).show()
                    fetchPendingOrders()

                },
                com.android.volley.Response.ErrorListener { error ->
                    dialog!!.dismiss()
                    Logger.d("Error.Response: " + error.toString())
                    Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
                }
        ) {

            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["token"] = preferenceManager.accessToken
                return params
            }
        }
        queue!!.add(getRequest)
    }


    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted")
                return true
            } else {

                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    @SuppressLint("MissingPermission")
    fun call_action(mMobile: String?) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$mMobile")
        startActivity(callIntent)
    }

    internal fun rateApp() {
        val uri = Uri.parse("market://details?id=" + mContext.getPackageName())
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())))
        }

    }

    fun foo(context: Context) {
        // when you need location
        // if inside activity context = this;

        SingleShotLocationProvider.requestSingleUpdate(context
        ) { location ->
            Log.d("Location", "my location is " + location.toString())

        }
    }
}
