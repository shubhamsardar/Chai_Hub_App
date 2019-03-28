package in.co.tripin.chai_hub_app.POJOs.Responces;

public class AddBatchResponse {

    private String status;
    private Batch data;

    public AddBatchResponse(String status, Batch data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Batch getData() {
        return data;
    }

    public void setData(Batch data) {
        this.data = data;
    }

    public class Batch {

        private String _id, updatedAt, createdAt, name, size, date;
        private BatchResponce.HubId hubId;
        private String __v, flag;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public BatchResponce.HubId getHubId() {
            return hubId;
        }

        public void setHubId(BatchResponce.HubId hubId) {
            this.hubId = hubId;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    public class HubId {
        private String _id, name;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
