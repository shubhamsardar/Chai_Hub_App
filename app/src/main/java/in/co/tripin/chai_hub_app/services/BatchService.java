package in.co.tripin.chai_hub_app.services;

import in.co.tripin.chai_hub_app.POJOs.Bodies.BatchRequestBody;
import in.co.tripin.chai_hub_app.POJOs.Responces.BatchResponce;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BatchService {

    @GET("/api/v1/batch?batch=ALL")
    Call<BatchResponce> getBatches(@Header("token")String token);

    @GET("/api/v1/batch/nonEmpty")
    Call<BatchResponce> getNonEmptyBatches(@Header("token")String token);

    @POST("/api/v1/batch")
    Call<BatchRequestBody> toAddBatch(@Header("token")String token, @Body BatchRequestBody batchRequestBody);
}
