package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.R
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate

class ForgotPinActivity : AppCompatActivity() {

    lateinit var gotoresetpin : TextView
    lateinit var awesomeValidation: AwesomeValidation
    lateinit var mobile : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pin)
        title = "Forgot Pin"
        init()
        setListners()

    }

    private fun init() {
        gotoresetpin = findViewById(R.id.submit)
        mobile = findViewById(R.id.mobile)
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        val regexPassword = ".{10,}"
        awesomeValidation.addValidation(this, R.id.pin, regexPassword, R.string.error_invalid_password)
        awesomeValidation.addValidation(this, R.id.mobile, RegexTemplate.NOT_EMPTY, R.string.err_mobile)

    }

    private fun setListners() {
        gotoresetpin.setOnClickListener {

            if(awesomeValidation.validate()){
                val intent = Intent(this, ResetPinActivity::class.java)
                intent.putExtra("mobile",mobile.text.toString().trim())
                startActivity(intent)
            }

        }

    }
}
