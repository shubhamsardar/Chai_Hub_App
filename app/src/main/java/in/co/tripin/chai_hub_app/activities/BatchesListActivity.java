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
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

    FloatingActionButton addBatchButton;
    private ListView batchListView;
    private PreferenceManager preferenceManager;
    ArrayList<BatchResponce.Data> batchResponcesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_batches_list);
        batchListView = (ListView) findViewById(R.id.batchListView);
        preferenceManager = PreferenceManager.getInstance(BatchesListActivity.this);
        batchResponcesList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addBatchButton = (FloatingActionButton) findViewById(R.id.addBatchButton);
        addBatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BatchesListActivity.this, AddBatchActivity.class), 0);
            }
        });
        setTitle("Batches");
        getBatchesFromApi();

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
        this.getBatchesFromApi();
    }

    public void getBatchesFromApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BatchService batchService = retrofit.create(BatchService.class);
        Call<BatchResponce> call = batchService.getBatches(preferenceManager.getAccessToken());
        call.enqueue(new Callback<BatchResponce>() {
            @Override
            public void onResponse(Call<BatchResponce> call, Response<BatchResponce> response) {
                if (response.isSuccessful()) {

                    BatchResponce batchResponce = response.body();

                    batchResponcesList = (ArrayList<BatchResponce.Data>) batchResponce.getData();

                    CustomAdapter customAdapter = new CustomAdapter(BatchesListActivity.this, android.R.layout.simple_list_item_1, batchResponcesList);
                    batchListView.setAdapter(customAdapter);

                } else {
                    Log.d("onResponse: ", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<BatchResponce> call, Throwable t) {

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
        public View getView(int position, View convertView, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.custom_batch, null);

            TextView tvBatchName = (TextView) view.findViewById(R.id.tvBatchName);
            TextView tvBatchSize = (TextView) view.findViewById(R.id.tvBatchSize);

            tvBatchName.setText(batchResponcesList.get(position).getName());
            tvBatchSize.setText(batchResponcesList.get(position).getSize());

            return view;
        }
    }
}
