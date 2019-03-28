package in.co.tripin.chai_hub_app.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatHelper {

    private static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String getISOString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_ISO);
// simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        return simpleDateFormat.format(date);
    }

}
