package in.co.tripin.chai_hub_app.POJOs.Responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingOrdersResponce {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("userId")
        @Expose
        private UserId userId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("paymentType")
        @Expose
        private String paymentType;
        @SerializedName("totalAmount")
        @Expose
        private Double totalAmount;
        @SerializedName("tapriId")
        @Expose
        private TapriId tapriId;
        @SerializedName("addressId")
        @Expose
        private AddressId addressId;
        @SerializedName("shortId")
        @Expose
        private String shortId;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("orderStatus")
        @Expose
        private String orderStatus;
        @SerializedName("paymentStatus")
        @Expose
        private String paymentStatus;
        @SerializedName("details")
        @Expose
        private List<Detail> details = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public TapriId getTapriId() {
            return tapriId;
        }

        public void setTapriId(TapriId tapriId) {
            this.tapriId = tapriId;
        }

        public AddressId getAddressId() {
            return addressId;
        }

        public void setAddressId(AddressId addressId) {
            this.addressId = addressId;
        }

        public String getShortId() {
            return shortId;
        }

        public void setShortId(String shortId) {
            this.shortId = shortId;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public List<Detail> getDetails() {
            return details;
        }

        public void setDetails(List<Detail> details) {
            this.details = details;
        }

        public class Detail {

            @SerializedName("itemName")
            @Expose
            private String itemName;
            @SerializedName("amount")
            @Expose
            private Double amount;
            @SerializedName("itemId")
            @Expose
            private String itemId;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;
            @SerializedName("_id")
            @Expose
            private String id;

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public Double getAmount() {
                return amount;
            }

            public void setAmount(Double amount) {
                this.amount = amount;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

        }

        public class UserId {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("fullName")
            @Expose
            private String fullName;
            @SerializedName("mobile")
            @Expose
            private String mobile;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

        }

        public class AddressId {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("updatedAt")
            @Expose
            private String updatedAt;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("nickname")
            @Expose
            private String nickname;
            @SerializedName("flatSociety")
            @Expose
            private String flatSociety;
            @SerializedName("addressLine1")
            @Expose
            private String addressLine1;
            @SerializedName("addressLine2")
            @Expose
            private String addressLine2;
            @SerializedName("country")
            @Expose
            private String country;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("landmark")
            @Expose
            private String landmark;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("__v")
            @Expose
            private Integer v;
            @SerializedName("flag")
            @Expose
            private Integer flag;
            @SerializedName("location")
            @Expose
            private Location location;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFlatSociety() {
                return flatSociety;
            }

            public void setFlatSociety(String flatSociety) {
                this.flatSociety = flatSociety;
            }

            public String getAddressLine1() {
                return addressLine1;
            }

            public void setAddressLine1(String addressLine1) {
                this.addressLine1 = addressLine1;
            }

            public String getAddressLine2() {
                return addressLine2;
            }

            public void setAddressLine2(String addressLine2) {
                this.addressLine2 = addressLine2;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getLandmark() {
                return landmark;
            }

            public void setLandmark(String landmark) {
                this.landmark = landmark;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public Integer getV() {
                return v;
            }

            public void setV(Integer v) {
                this.v = v;
            }

            public Integer getFlag() {
                return flag;
            }

            public void setFlag(Integer flag) {
                this.flag = flag;
            }

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public class Location {

                @SerializedName("coordinates")
                @Expose
                private List<Object> coordinates = null;

                public List<Object> getCoordinates() {
                    return coordinates;
                }

                public void setCoordinates(List<Object> coordinates) {
                    this.coordinates = coordinates;
                }

            }

        }

        public class TapriId {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

    }
}
