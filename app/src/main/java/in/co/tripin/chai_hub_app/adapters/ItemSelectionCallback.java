package in.co.tripin.chai_hub_app.adapters;

public interface ItemSelectionCallback {

    public void onitemAdded(Double cost, int quant);

    public void onItemRemoved(Double cost, int quant);
}
