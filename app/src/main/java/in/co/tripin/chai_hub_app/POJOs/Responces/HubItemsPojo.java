package in.co.tripin.chai_hub_app.POJOs.Responces;

import java.io.Serializable;

import in.co.tripin.chai_hub_app.POJOs.Models.Item;


public class HubItemsPojo implements Serializable {
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
        private Item[] Chaihiyeh;

        private Item[] Extra;

        private Item[] Beverages;

        private Item[] Snacks;

        public Item[] getChaihiyeh ()
        {
            return Chaihiyeh;
        }

        public void setChaihiyeh (Item[] Chaihiyeh)
        {
            this.Chaihiyeh = Chaihiyeh;
        }

        public Item[] getExtra ()
        {
            return Extra;
        }

        public void setExtra (Item[] Extra)
        {
            this.Extra = Extra;
        }

        public Item[] getBeverages ()
        {
            return Beverages;
        }

        public void setBeverages (Item[] Beverages)
        {
            this.Beverages = Beverages;
        }

        public Item[] getSnacks ()
        {
            return Snacks;
        }

        public void setSnacks (Item[] Snacks)
        {
            this.Snacks = Snacks;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Chaihiyeh = "+Chaihiyeh+", Extra = "+Extra+", Beverages = "+Beverages+", Snacks = "+Snacks+"]";
        }


    }
}
