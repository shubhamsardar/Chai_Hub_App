package in.co.tripin.chai_hub_app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import in.co.tripin.chai_hub_app.Helper.Constants;
import in.co.tripin.chai_hub_app.Managers.PreferenceManager;
import in.co.tripin.chai_hub_app.POJOs.Responces.BatchResponce;
import in.co.tripin.chai_hub_app.R;
import in.co.tripin.chai_hub_app.services.BatchService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BatchesListActivity extends AppCompatActivity {


    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_ORDER_ID = "order_id";

    FloatingActionButton addBatchButton, selectBatchesButton;
    private ListView batchListView;
    private PreferenceManager preferenceManager;
    ArrayList<BatchResponce.Data> batchResponcesList;
    private String orderId;
    private int orderQuantity;
    private int totalAvailableSize = 0;
    private HashMap<String, BatchResponce.Data> batchesMapper = new HashMap<>();
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_batches_list);
        batchListView = (ListView) findViewById(R.id.batchListView);
        preferenceManager = PreferenceManager.getInstance(BatchesListActivity.this);
        batchResponcesList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addBatchButton = (FloatingActionButton) findViewById(R.id.addBatchButton);
        selectBatchesButton = (FloatingActionButton) findViewById(R.id.selectBatchesButton);
        addBatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BatchesListActivity.this, AddBatchActivity.class), 0);
            }
        });
        selectBatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putCharSequenceArrayListExtra(MainActivity.KEY_BATCH_IDS, new ArrayList<CharSequence>(batchesMapper.keySet()));
                i.putExtra(KEY_ORDER_ID, orderId);
                setResult(200, i);
                finish();

            }
        });
        setTitle("Batches");

        orderId = getIntent().getStringExtra(KEY_ORDER_ID);
        orderQuantity = getIntent().getIntExtra(KEY_QUANTITY, 0);

        if (orderId == null) {
            getBatchesFromApi(false);
        } else {
            getBatchesFromApi(true);
            addBatchButton.setVisibility(View.GONE);
        }

        selectBatchesButton.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                //
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_order_history, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (orderId == null) {
            getBatchesFromApi(false);
        } else {
            getBatchesFromApi(true);
        }
    }

    public void getBatchesFromApi(boolean shouldShowNonEmptyBatchesOnly) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BatchService batchService = retrofit.create(BatchService.class);

        Call<BatchResponce> call = null;
        if (shouldShowNonEmptyBatchesOnly) {
            call = batchService.getNonEmptyBatches(preferenceManager.getAccessToken());
        } else {
            call = batchService.getBatches(preferenceManager.getAccessToken());
        }

        call.enqueue(new Callback<BatchResponce>() {
            @Override
            public void onResponse(Call<BatchResponce> call, Response<BatchResponce> response) {

                Log.d("TAG", response.body().toString());

                if (response.isSuccessful()) {

                    BatchResponce batchResponce = response.body();

                    batchResponcesList = (ArrayList<BatchResponce.Data>) batchResponce.getData();

                    customAdapter = new CustomAdapter(BatchesListActivity.this, android.R.layout.simple_list_item_1, batchResponcesList);
                    batchListView.setAdapter(customAdapter);

                } else {
                    Log.d("onResponse: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<BatchResponce> call, Throwable t) {
                Log.d("TAG", String.valueOf(t.getMessage()));
            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<BatchResponce.Data> {

        ArrayList<BatchResponce.Data> batchResponcesList;
        Context context;
        View view;

        public CustomAdapter(Context context, int resource, ArrayList<BatchResponce.Data> batchResponceList) {
            super(context, resource, batchResponceList);
            this.context = context;
            this.batchResponcesList = batchResponceList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.custom_batch, null);

            TextView tvBatchName = (TextView) view.findViewById(R.id.tvBatchName);
            TextView tvBatchSize = (TextView) view.findViewById(R.id.tvBatchSize);
            CheckBox checkbox = view.findViewById(R.id.checkbox);

            final BatchResponce.Data data = batchResponcesList.get(position);

            if (orderId == null) {
                checkbox.setVisibility(View.GONE);
            } else if (orderId != null && totalAvailableSize >= orderQuantity && !batchesMapper.containsKey(data.get_id())) {

                checkbox.setVisibility(View.INVISIBLE);

            } else {
                checkbox.setChecked(batchesMapper.containsKey(data.get_id()));
            }

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        batchesMapper.put(data.get_id(), data);
                    else
                        batchesMapper.remove(data.get_id());

                    checkIfDemandIsMet();

                }
            });

            tvBatchName.setText(data.getName());
            tvBatchSize.setText("" + (data.getTotalSize() - data.getRequestedSize()));

            return view;
        }
    }

    private void checkIfDemandIsMet() {

        Iterator<String> iterator = batchesMapper.keySet().iterator();

        totalAvailableSize = 0;
        while (iterator.hasNext()) {

            String batchId = iterator.next();
            BatchResponce.Data batch = batchesMapper.get(batchId);

            int availableSize = batch.getTotalSize() - batch.getRequestedSize();
            totalAvailableSize = totalAvailableSize + availableSize;

        }

        customAdapter.notifyDataSetChanged();
        if(totalAvailableSize >= orderQuantity) {
            selectBatchesButton.setVisibility(View.VISIBLE);
        }

    }


}
