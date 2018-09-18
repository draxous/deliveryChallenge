package com.easy.delivery.deazy.ui;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.easy.delivery.deazy.BaseApplication;
import com.easy.delivery.deazy.R;
import com.easy.delivery.deazy.api.RestApi;
import com.easy.delivery.deazy.api.model.DeliveryItem;
import com.easy.delivery.deazy.ui.adapter.DeliveryListAdapter;
import com.easy.delivery.deazy.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static String DELIVERY_ITEM;
    @BindView(R.id.rec_delivery_list)
    RecyclerView mRecDeliveryList;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeToRefresh;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Inject
    Retrofit retrofit;

    private DeliveryListAdapter adapter;
    private int offset = 0;
    private LinearLayoutManager mLinearLayoutManager;

    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private static List<DeliveryItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);



        ((BaseApplication) getApplication()).getNetworkComponent().inject(this);

        mSwipeToRefresh.setOnRefreshListener(this);
        adapter = new DeliveryListAdapter(MainActivity.this);

        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecDeliveryList.setLayoutManager(mLinearLayoutManager);
        mRecDeliveryList.setAdapter(adapter);
        mRecDeliveryList.setHasFixedSize(true);

        if (moviesList == null) {
            moviesList = new ArrayList<>();
            RefreshDeliveryList();
        }


        mRecDeliveryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                totalItemCount = mLinearLayoutManager.getItemCount();
                lastVisibleItem = mLinearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    LoadMoreDeliveryItems();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void RefreshDeliveryList() {

        if (NetworkUtils.isNetworkConnected(this)) {

            mSwipeToRefresh.setRefreshing(true);


            Call<List<DeliveryItem>> posts = retrofit.create(RestApi.class).getPosts(0, 20);

            //Enque the call
            posts.enqueue(new Callback<List<DeliveryItem>>() {
                @Override
                public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
                    if (response != null && response.body() != null && response.body().size() > 0) {
                        adapter.clearDeliveryList();
                        moviesList.clear();
                        moviesList.addAll(response.body());
                        adapter.addToDeliveryList(moviesList);
                        adapter.notifyDataSetChanged();

                        //since paging isn't available in api making sure it doesn't update offset wrongly
                        //offset += response.body().size();
                    }
                    mSwipeToRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
                    mSwipeToRefresh.setRefreshing(false
                    );
                }
            });

        } else {
            Snackbar.make(mRecDeliveryList, getString(R.string.no_connection), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RefreshDeliveryList();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();

        }


    }

    public void LoadMoreDeliveryItems() {

        if (NetworkUtils.isNetworkConnected(this)) {

            addLoadingMoreProgress();

            if (moviesList.size() > 0) {
                offset = moviesList.size();
            }

            Call<List<DeliveryItem>> posts = retrofit.create(RestApi.class).getPosts(offset, 20);

            //Enque the call
            posts.enqueue(new Callback<List<DeliveryItem>>() {
                @Override
                public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
                    if (response != null && response.body() != null) {

                        removeLoadingMoreProgress();

                        if(response.body().size() == 0){
                            return;
                        }

                        moviesList.addAll(response.body());
                        adapter.addToDeliveryList(moviesList);
                        adapter.notifyDataSetChanged();

                        //since paging isn't available in api making sure it doesn't update offset wrongly
                        offset += response.body().size();
                        loading = false;
                    } else {
                        removeLoadingMoreProgress();
                        loading = false;
                        Snackbar.make(mRecDeliveryList, getString(R.string.loading_error), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        RefreshDeliveryList();
                                    }
                                })
                                .setActionTextColor(Color.YELLOW)
                                .show();
                    }


                }

                @Override
                public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
                }
            });

        } else {
            Snackbar.make(mRecDeliveryList, getString(R.string.no_connection), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LoadMoreDeliveryItems();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        }


    }

    private void addLoadingMoreProgress() {
        moviesList.add(null);
        adapter.addToDeliveryList(moviesList);
        adapter.notifyItemInserted(moviesList.size() + 1);
    }

    private void removeLoadingMoreProgress() {
        moviesList.remove(moviesList.size() - 1);
        adapter.addToDeliveryList(moviesList);
        adapter.notifyItemRemoved(moviesList.size());
    }

    @Override
    public void onRefresh() {
        RefreshDeliveryList();
    }

}

