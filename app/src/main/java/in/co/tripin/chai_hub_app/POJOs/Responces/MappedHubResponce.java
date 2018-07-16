package in.co.tripin.chai_hub_app.POJOs.Responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MappedHubResponce {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("hubId")
        @Expose
        private String hubId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private Address address;

        public String getHubId() {
            return hubId;
        }

        public void setHubId(String hubId) {
            this.hubId = hubId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public class Address {

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
            @SerializedName("hubId")
            @Expose
            private String hubId;
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

            public String getHubId() {
                return hubId;
            }

            public void setHubId(String hubId) {
                this.hubId = hubId;
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
}
