package in.co.tripin.chai_hub_app.POJOs.Bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePinBody {

    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("newPin")
    @Expose
    private String newPin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
