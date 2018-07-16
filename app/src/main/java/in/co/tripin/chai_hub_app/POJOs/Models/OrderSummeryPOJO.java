package in.co.tripin.chai_hub_app.POJOs.Models;

import java.io.Serializable;
import java.util.ArrayList;



public class OrderSummeryPOJO implements Serializable {

    private String mTapriId;
    private String mHubAddressId;
    private String mTapriName;
    private String mTotalCost;
    private UserAddress.Data mAddress;
    private String mPaymentMethod;
    private ArrayList<Item> mItems;

    public OrderSummeryPOJO(String mTapriId,
                            String mTapriName,
                            String mTotalCost,
                            UserAddress.Data mAddress,
                            String mPaymentMethod,
                            ArrayList<Item> mItems,
                            String mHubAddressId
                            ) {
        this.mTapriId = mTapriId;
        this.mTapriName = mTapriName;
        this.mTotalCost = mTotalCost;
        this.mAddress = mAddress;
        this.mPaymentMethod = mPaymentMethod;
        this.mItems = mItems;
        this.mHubAddressId = mHubAddressId;
    }

    public String getmHubAddressId() {
        return mHubAddressId;
    }

    public void setmHubAddressId(String mHubAddressId) {
        this.mHubAddressId = mHubAddressId;
    }

    public String getmPaymentMethod() {
        return mPaymentMethod;
    }

    public void setmPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public ArrayList<Item> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }

    public String getmTapriId() {
        return mTapriId;
    }

    public void setmTapriId(String mTapriId) {
        this.mTapriId = mTapriId;
    }

    public String getmTapriName() {
        return mTapriName;
    }

    public void setmTapriName(String mTapriName) {
        this.mTapriName = mTapriName;
    }

    public String getmTotalCost() {
        return mTotalCost;
    }

    public void setmTotalCost(String mTotalCost) {
        this.mTotalCost = mTotalCost;
    }


    public UserAddress.Data getmAddress() {
        return mAddress;
    }

    public void setmAddress(UserAddress.Data mAddress) {
        this.mAddress = mAddress;
    }

    public String getFullAddressString(){
        return getmAddress().getLandmark()+", "
                +getmAddress().getFlatSociety()+", "
                +getmAddress().getAddressLine1()+", "
                +getmAddress().getAddressLine2()+", "
                +getmAddress().getCity()+", "
                +getmAddress().getCountry();
    }


}
