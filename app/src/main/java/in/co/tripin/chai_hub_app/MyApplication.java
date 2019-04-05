package in.co.tripin.chai_hub_app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import java.util.Calendar;

import in.co.tripin.chai_hub_app.Helper.Constants;

@AcraCore(buildConfigClass = org.acra.BuildConfig.class)
public class MyApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri(Constants.BASE_URL + "acra")
                .setHttpMethod(HttpSender.Method.POST)
                .setEnabled(true);

        ACRA.init(this, builder);

    }

}
