package in.co.tripin.chai_hub_app.POJOs.Responces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HubMenuResponce {

    private String status;

    private Data data;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", data = "+data+"]";
    }


    public class Data implements Serializable
    {
        private Item[] Extra;

        private Item[] Beverages;

        private Item[] Snacks;

        private Item[] Chaihiyeh;

        public Item[] getChaihiyeh() {
            return Chaihiyeh;
        }

        public void setChaihiyeh(Item[] chaihiyeh) {
            Chaihiyeh = chaihiyeh;
        }

        public Item[] getExtra() {
            return Extra;
        }

        public void setExtra(Item[] extra) {
            Extra = extra;
        }

        public Item[] getBeverages() {
            return Beverages;
        }

        public void setBeverages(Item[] beverages) {
            Beverages = beverages;
        }

        public Item[] getSnacks() {
            return Snacks;
        }

        public void setSnacks(Item[] snacks) {
            Snacks = snacks;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Extra = "+Extra+", Beverages = "+Beverages+", Snacks = "+Snacks+"]";
        }

        public class Item implements Serializable
        {
            private int quantity = 0;

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("rate")
            @Expose
            private Double rate;
            @SerializedName("status")
            @Expose
            private Integer status;
            @SerializedName("flag")
            @Expose
            private Integer flag;
            @SerializedName("category")
            @Expose
            private String category;

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

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

            public Double getRate() {
                return rate;
            }

            public void setRate(Double rate) {
                this.rate = rate;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public Integer getFlag() {
                return flag;
            }

            public void setFlag(Integer flag) {
                this.flag = flag;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            @Override
            public String toString()
            {
                return "ClassPojo [category = "+category+", rate = "+rate+", _id = "+id+", status = "+status+", name = "+name+"]";
            }
        }
    }

}
