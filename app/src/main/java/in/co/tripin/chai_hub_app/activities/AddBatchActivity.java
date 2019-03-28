package in.co.tripin.chai_hub_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import in.co.tripin.chai_hub_app.Helper.Constants;
import in.co.tripin.chai_hub_app.Helper.DateFormatHelper;
import in.co.tripin.chai_hub_app.Managers.PreferenceManager;
import in.co.tripin.chai_hub_app.POJOs.Bodies.BatchRequestBody;
import in.co.tripin.chai_hub_app.R;
import in.co.tripin.chai_hub_app.services.BatchService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBatchActivity extends AppCompatActivity {

    private EditText editTextBatchName, editTextBatchNo;
    private Button buttonSave;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferenceManager = PreferenceManager.getInstance(this);
        setTitle("Add Batch");
        editTextBatchName = (EditText) findViewById(R.id.editTextBatchName);
        editTextBatchNo = (EditText) findViewById(R.id.editTextBatchNo);
        buttonSave = (Button) findViewById(R.id.buttonSave);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBatchThroughApi();
            }
        });
    }

    public void addBatchThroughApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BatchRequestBody batchRequestBody = new BatchRequestBody(editTextBatchName.getText().toString(), editTextBatchNo.getText().toString(), DateFormatHelper.getISOString(new Date()));

        BatchService batchService = retrofit.create(BatchService.class);
        Call<BatchRequestBody> call = batchService.toAddBatch(preferenceManager.getAccessToken(), batchRequestBody);

        call.enqueue(new Callback<BatchRequestBody>() {
            @Override
            public void onResponse(Call<BatchRequestBody> call, Response<BatchRequestBody> response) {
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(Call<BatchRequestBody> call, Throwable t) {

            }
        });

    }
}
