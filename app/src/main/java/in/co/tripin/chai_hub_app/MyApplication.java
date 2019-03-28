package in.co.tripin.chai_hub_app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.util.Calendar;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setEnabled(true)
                .setReportFileName("CrashReport_" + Calendar.getInstance().getTime().toString() + ".json")
                .setMailTo("clyde.mendonca@weaverbirds.in,siddharth@weaverbirds.in,amar@weaverbirds.in");

        // The following line triggers the initialization of ACRA
        ACRA.init(this, builder);

    }

}
