package com.divitngoc.android.helpme_stackoverflow;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackOverFlowAPI {

    // url would be (BASE_URL + "/2.2/search?order=desc&pagesize=15&sort=relevance&site=stackoverflow")
    @GET("/2.2/search?order=desc&pagesize=15&sort=relevance&site=stackoverflow")
    Observable<ListWrapper> getQuestionsRx(@Query("intitle") String parameter);
}
