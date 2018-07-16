package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ChangePinBody
import `in`.co.tripin.chai_hub_app.POJOs.Responces.ChangePinResponce
import `in`.co.tripin.chai_hub_app.R

import `in`.co.tripin.chai_hub_app.networking.APIService
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ChangePinActivity : AppCompatActivity() {

    lateinit var awesomeValidation: AwesomeValidation

    lateinit var old : EditText
    lateinit var new : EditText
    lateinit var renew : EditText
    lateinit var submit : Button

    lateinit var apiService: APIService
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        mCompositeDisposable = CompositeDisposable()
        apiService = APIService.create()
        title = "Change PIN"
        addValidations()
        init()
        setListners()
    }

    private fun init() {
        old = findViewById(R.id.oldpin)
        new = findViewById(R.id.newpin)
        renew = findViewById(R.id.reenternewpin)
        submit = findViewById(R.id.submit)
        preferenceManager = PreferenceManager.getInstance(this)


    }
    private fun setListners() {

        submit.setOnClickListener{
            if(awesomeValidation.validate()){
                val changePinBody = ChangePinBody()
                changePinBody.pin = old.text.toString().trim()
                changePinBody.newPin = new.text.toString().trim()

                mCompositeDisposable?.add(apiService.changePIN(preferenceManager.accessToken,"application/json",changePinBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError))
            }
        }

    }


    private fun handleResponse(responce: ChangePinResponce) {

        Log.v("OnResponcePending: ",responce.status)

        Toast.makeText(applicationContext,responce.data.message,Toast.LENGTH_LONG).show()
        preferenceManager.clearLoginPreferences()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun handleError(error: Throwable) {
        Log.v("OnErrorPending",error.toString())
        Toast.makeText(applicationContext,"Error",Toast.LENGTH_LONG).show()

    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }



    private fun addValidations() {
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        awesomeValidation.addValidation(this, R.id.oldpin, RegexTemplate.NOT_EMPTY, R.string.error_invalid_password)
        awesomeValidation.addValidation(this, R.id.newpin, RegexTemplate.NOT_EMPTY, R.string.error_invalid_password)
        awesomeValidation.addValidation(this, R.id.reenternewpin, R.id.newpin, R.string.err_password_confirmation)    }
}
