package `in`.co.tripin.chai_hub_app.activities

import `in`.co.tripin.chai_hub_app.Managers.PreferenceManager
import `in`.co.tripin.chai_hub_app.R
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SpalshActivity : AppCompatActivity() {

    lateinit var login: TextView
    lateinit var calltoreg: TextView
    lateinit var preferenceManager: PreferenceManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)
        preferenceManager = PreferenceManager.getInstance(this)
        if(preferenceManager.isLogin){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        init()
        setListners()
    }

    private fun init() {
        login = findViewById(R.id.login)
        calltoreg = findViewById(R.id.calltoreg)


    }

    @SuppressLint("MissingPermission")
    private fun setListners() {

        login.setOnClickListener {
            // Go to login Screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        calltoreg.setOnClickListener {
            //call to admin
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8394876737"))
            startActivity(intent)
        }

    }
}
