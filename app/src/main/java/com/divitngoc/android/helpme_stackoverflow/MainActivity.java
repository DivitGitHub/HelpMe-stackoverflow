package com.divitngoc.android.helpme_stackoverflow;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    final static String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.empty_view)
    TextView emptyView;

    private StackOverFlowAPI stackOverFlowAPI;
    private List<Item> data;
    private CustomRecyclerAdapter customRecyclerAdapter;

    private static final String BASE_URL = "https://api.stackexchange.com";
    public String userQuery = BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        data = new ArrayList<>();

        // Create api object from retrofit
        stackOverFlowAPI = createStackOverFlowAPI();

        customRecyclerAdapter = new CustomRecyclerAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customRecyclerAdapter);
    }


    /**
     * Retrofit2 network call
     */
//    Callback<ListWrapper> questionsCallback = new Callback<ListWrapper>() {
//        @Override
//        public void onResponse(Call<ListWrapper> call, Response<ListWrapper> response) {
//            if (response.isSuccessful()) {
//                data = new ArrayList<>();
//                data.addAll(response.body().getItems());
//                recyclerView.setAdapter(new CustomRecyclerAdapter(MainActivity.this, data));
//            }
//            loadingIndicator.setVisibility(GONE);
//        }
//
//        @Override
//        public void onFailure(Call<ListWrapper> call, Throwable t) {
//            t.printStackTrace();
//        }
//    };
    private StackOverFlowAPI createStackOverFlowAPI() {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))// Serialize/deserialize endpoint response to/from java objects(POGO)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Turn retrofit endpoints to Observable streams
                .build();

        return stackOverFlowAPI = retrofit.create(StackOverFlowAPI.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!isNetworkConnected()) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Gets user input query
                userQuery = query.trim();

                //hides empty_view
                emptyView.setVisibility(GONE);

                // Shows loading indicator(progressbar) after query is submitted
                loadingIndicator.setVisibility(View.VISIBLE);

                // fetches and displays data
                fetchData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private boolean isNetworkConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * RxJava2
     * Add data to list and populate the recycler view
     */
    private void fetchData() {
        data.clear(); // clears any previous data
        stackOverFlowAPI.getQuestionsRx(userQuery).subscribeOn(Schedulers.io()) // Set up async background thread
                .observeOn(AndroidSchedulers.mainThread()) // Delivers to mainthread
                .subscribe(new DisposableObserver<ListWrapper>() {

                    @Override
                    public void onNext(@NonNull ListWrapper listWrapper) {
                        data.addAll(listWrapper.getItems());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(LOG_TAG, getString(R.string.coudnt_fetch_data), e);
                    }

                    @Override
                    public void onComplete() {
                        loadingIndicator.setVisibility(GONE); // hides loading indicator after data is fetched
                        recyclerView.setAdapter(customRecyclerAdapter); // display data
                        dispose(); // Free up memory since we already displayed the data through our data ArrayList
                    }
                });
    }
}
