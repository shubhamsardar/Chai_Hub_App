package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Managers.Logger
import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.LogInBody
import `in`.co.tripin.chai_hub_app.POJOs.Responces.LogInResponce
import `in`.co.tripin.chai_hub_app.R

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import `in`.co.tripin.chai_hub_app.networking.APIService
import android.app.AlertDialog
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var forgotpin: TextView
    lateinit var login: TextView
    lateinit var mobile: EditText
    lateinit var pin: EditText
    private var dialog: AlertDialog? = null


    lateinit var awesomeValidation: AwesomeValidation
    lateinit var logInBody: LogInBody
    lateinit var apiSetvice: APIService
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mCompositeDisposable = CompositeDisposable()
        apiSetvice = APIService.create()
        preferenceManager = PreferenceManager.getInstance(this)


        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Loading")
                .build()


        title = "Log In"
        init()
        setListners()
    }


    private fun init() {
        forgotpin = findViewById(R.id.forgotpin)
        login = findViewById(R.id.login)
        mobile = findViewById(R.id.mobile)
        pin = findViewById(R.id.pin)

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        addValidations()

    }


    private fun setListners() {
        forgotpin.setOnClickListener {
            val intent = Intent(this, ForgotPinActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {


            if (awesomeValidation.validate()) {
                logInBody = LogInBody()
                logInBody.mobile = mobile.text.toString().trim()
                logInBody.pin = pin.text.toString().trim()
                logInBody.regToken = preferenceManager.fcmId
                Log.v("OnLogin: ",logInBody.toString())
                dialog!!.show()
                mCompositeDisposable?.add( apiSetvice.logInUser(logInBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError))

            }

        }
    }

    private fun addValidations() {

        awesomeValidation.addValidation(this, R.id.mobile, RegexTemplate.NOT_EMPTY, R.string.err_mobile)
        awesomeValidation.addValidation(this, R.id.pin, RegexTemplate.NOT_EMPTY, R.string.error_invalid_password)
        val regexMobile = ".{10,}"
        awesomeValidation.addValidation(this, R.id.mobile, regexMobile, R.string.invalid_mobile)
        val regexPassword = ".{4,}"
        awesomeValidation.addValidation(this, R.id.pin, regexPassword, R.string.error_invalid_password)


    }

    private fun handleResponse(responce: Response<LogInResponce>) {

        Log.v("OnResponceLogin",responce.toString())
        dialog!!.dismiss()


        val loginResponce : LogInResponce? = responce.body()
        if (loginResponce != null) {
            Logger.v("Loginn : ${loginResponce.data.roles}")
            if(loginResponce.data.status == "Logged In"){

                //save data to shared preferences

                var isRole = false
                for(role: LogInResponce.Data.Role in loginResponce.data.roles){
                    if(role.roleType == "10017"){
                        isRole = true
                        preferenceManager.tapriId = role.id
                    }
                }

                if(isRole){
                    preferenceManager.accessToken = responce.headers().get("token")
                    Log.v("token: ",preferenceManager.accessToken)
                    preferenceManager.mobileNo = loginResponce.data.mobile
                    preferenceManager.userName = loginResponce.data.name
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Not A User! Register First",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SpalshActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }


    }

    private fun handleError(error: Throwable) {
        Log.v("OnErrorLogin",error.toString())
        dialog!!.dismiss()
        Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_LONG).show()

    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


}
