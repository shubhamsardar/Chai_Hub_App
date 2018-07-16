package `in`.co.tripin.chai_hub_app.activities


import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ForgotPassOTPBody
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ResetPassBody
import `in`.co.tripin.chai_hub_app.POJOs.Responces.ForgotPassResponce
import `in`.co.tripin.chai_hub_app.POJOs.Responces.ResetPassResponce
import `in`.co.tripin.chai_hub_app.R
import `in`.co.tripin.chai_hub_app.networking.APIService
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.chaos.view.PinView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reset_pin.*
import retrofit2.Response

class ResetPinActivity : AppCompatActivity() {

    lateinit var submit:TextView
    lateinit var otpguide:TextView
    lateinit var resend:TextView
    lateinit var otp:PinView
    lateinit var pin:EditText
    lateinit var repin:EditText



    lateinit var awesomeValidation: AwesomeValidation



    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var apiService: APIService
    var mobile: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pin)
        mCompositeDisposable = CompositeDisposable()
        apiService = APIService.create()

        title = "Reset Pin"
        init()
        setListners()
        mobile = intent.extras.getString("mobile")

        if(mobile != null){
            sendOTP()
        }else{
            finish()
        }

    }

    private fun sendOTP() {
        val body = ForgotPassOTPBody()
        body.mobile = mobile
        body.type = "forget_password_otp"
        mCompositeDisposable?.add(apiService.sendOTPforgotpassword(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }


    private fun init() {
        submit = findViewById(R.id.submit)
        otpguide = findViewById(R.id.otpguide)
        resend = findViewById(R.id.resend)
        otp = findViewById(R.id.pinViewOTP)
        pin = findViewById(R.id.pin)
        repin = findViewById(R.id.repin)


        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        addValidations()



    }

    private fun addValidations() {

        val regexPassword = ".{4,}"
        awesomeValidation.addValidation(this, R.id.pin, regexPassword, R.string.error_invalid_password)
        val regexOTP = ".{4,}"
        awesomeValidation.addValidation(this, R.id.pinViewOTP, regexOTP, R.string.error_invalid_otp)
        awesomeValidation.addValidation(this, R.id.repin, R.id.pin, R.string.error_invalid_password)


    }



    private fun setListners() {
        submit.setOnClickListener {

            if(awesomeValidation.validate()){
                val resetBody : ResetPassBody = ResetPassBody()
                resetBody.mobile = mobile
                resetBody.otp = pinViewOTP.text.toString()
                resetBody.password = pin.text.toString()
                mCompositeDisposable?.add(apiService.resetPassword(resetBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResetResponse, this::handleResetError))

            }

        }

        resend.setOnClickListener{
            sendOTP()
        }
    }

    private fun handleResponse(responce: Response<ForgotPassResponce>) {

        Log.v("OnResponceOTPSent",responce.toString())
        otpguide.setText("an OTP is sent to $mobile")


    }

    private fun handleError(error: Throwable) {
        Log.v("OnErrorOTP",error.toString())
        otpguide.setText("Error Occured! Please Resend")

    }

    private fun handleResetResponse(responce: ResetPassResponce) {

        Log.v("OnResetResponce: ",responce.data.message)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun handleResetError(error: Throwable) {
        Log.v("OnErrorReset",error.toString())
        otpguide.setText("Error Occured! Please Retry!")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


}
