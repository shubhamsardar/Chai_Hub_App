package in.co.tripin.chai_hub_app.Managers;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {

    public static final String PREF_MOBILE_NUMBER = "registered_mobile_no";
    public static final String PREF_USER_NAME = "user_name";
    public static final String PREF_TAPRI_ID = "tapri_id";
    public static final String PREF_LOGIN_STATUS = "login_status";
    private static final String PREF_ACCESS_TOKEN = "partner_access_token";
    private static final String PREF_USER_ID = "partner_user_id";
    private static final String PREF_FCM_ID = "user_fcm_id";
    private static final String PREF_TAPRI_NAME = "tapri_name";
    private static final String PREF_FILE_NAME = "file_name";
    private static final String PREF_HUB_ADDRESS = "hub_address";
    private static final String PREF_HUB_AID = "hub_id";




    private static SharedPreferences sInstance;
    private static SharedPreferences.Editor editor;
    private static PreferenceManager mSPreferenceManager;

    private PreferenceManager() {
    }

    /**
     * @param context
     * @return
     */
    public static synchronized PreferenceManager getInstance(Context context) {
        if (mSPreferenceManager == null) {
//            sInstance = PreferenceManager.getDefaultSharedPreferences(context);
            sInstance = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
            editor = sInstance.edit();
            mSPreferenceManager = new PreferenceManager();
        }
        return mSPreferenceManager;
    }

    public String getValueFromSharedPreference(String key, String value) {
        String retrivedValue = sInstance.getString(key, value);
        return retrivedValue;
    }

    public String getValueFromSharedPreference(String key) {
        return getValueFromSharedPreference(key, "");
    }

    public void setValueToSharedPreference(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void clearAllDataFromSharedPrefernces() {
        editor.clear();
        editor.commit();
    }

    /**
     * @return
     */
    public String getAccessToken() {
        String accessToken = sInstance.getString(PREF_ACCESS_TOKEN, null);
        return accessToken;
    }

    /**
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    /**
     * @return
     */
    public String getHubAddress() {
        String accessToken = sInstance.getString(PREF_HUB_ADDRESS, null);
        return accessToken;
    }

    /**
     * @param accessToken
     */
    public void setHubAddress(String accessToken) {
        editor.putString(PREF_HUB_ADDRESS, accessToken);
        editor.commit();
    }

    /**
     * @return
     */
    public String getHubAId() {
        String accessToken = sInstance.getString(PREF_HUB_AID, null);
        return accessToken;
    }

    /**
     * @param id
     */
    public void setHubId(String id) {
        editor.putString(PREF_HUB_AID, id);
        editor.commit();
    }


    /**
     * @return
     */
    public String getUserId() {
        String userId = sInstance.getString(PREF_USER_ID, null);
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        editor.putString(PREF_USER_ID, userId);
        editor.commit();
    }


    public boolean isLogin() {
        String tapriId = sInstance.getString(PREF_ACCESS_TOKEN, null);

        if (tapriId == null || tapriId.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public String getMobileNo() {
        String mobileNo = sInstance.getString(PREF_MOBILE_NUMBER, null);
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        editor.putString(PREF_MOBILE_NUMBER, mobileNo);
        editor.commit();
    }

    public String getUserName() {
        String userName = sInstance.getString(PREF_USER_NAME, null);
        return userName;
    }

    public void setUserName(String userName) {
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public String getTapriId() {
        String tapriId = sInstance.getString(PREF_TAPRI_ID, null);
        return tapriId;
    }

    public void setTapriId(String tripinId) {
        editor.putString(PREF_TAPRI_ID, tripinId);
        editor.commit();
    }

    public String getFCMId() {
        String fcmId = sInstance.getString(PREF_FCM_ID, "Dummy FCM");
        return fcmId;
    }

    public void setFCMId(String fcmId) {
        editor.putString(PREF_FCM_ID, fcmId);
        editor.commit();
    }

    public String getTapriName() {
        String tripinId = sInstance.getString(PREF_TAPRI_NAME, null);
        return tripinId;
    }

    public void setTapriName(String tripinId) {
        editor.putString(PREF_TAPRI_NAME, tripinId);
        editor.commit();
    }


    public void clearLoginPreferences() {
        //setting FCM id before clearing since it would be required when user sign's in again.
        String fcmId = getFCMId();
        editor.clear();
        setFCMId(fcmId);
        editor.commit();
    }

}
