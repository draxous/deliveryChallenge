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
import android.widget.TextView;

import com.easy.delivery.deazy.BaseApplication;
import com.easy.delivery.deazy.R;
import com.easy.delivery.deazy.presenter.MainActivityPresenter;
import com.easy.delivery.deazy.model.DeliveryItem;
import com.easy.delivery.deazy.ui.adapter.DeliveryListAdapter;
import com.easy.delivery.deazy.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, MainActivityView {

    public static String DELIVERY_ITEM;
    @BindView(R.id.rec_delivery_list)
    RecyclerView mRecDeliveryList;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeToRefresh;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Inject
    Retrofit retrofit;

    MainActivityPresenter mainActivityPresenter;

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
        }

        mainActivityPresenter = new MainActivityPresenter(getApplication(), this);
        loadDeliveryData(offset);

        mRecDeliveryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                totalItemCount = mLinearLayoutManager.getItemCount();
                lastVisibleItem = mLinearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    addLoadingMoreProgress();
                    loadDeliveryData(offset);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadDeliveryData(final int offset) {
        if (NetworkUtils.isNetworkConnected(this)) {

            mainActivityPresenter.loadDeliveries(offset);

        } else {
            showErrorMessage(getString(R.string.no_connection));
        }
    }

    private void addLoadingMoreProgress() {
        if (moviesList.size() > 0) {

            moviesList.add(null);
            adapter.addToDeliveryList(moviesList);
            adapter.notifyItemInserted(moviesList.size());
            adapter.notifyDataSetChanged();
        }

    }

    private void removeLoadingMoreProgress() {
        moviesList.remove(moviesList.size() - 1);
        adapter.addToDeliveryList(moviesList);
        adapter.notifyItemRemoved(moviesList.size());
    }

    @Override
    public void onRefresh() {
        loading = false;
        offset = 0;
        mSwipeToRefresh.setRefreshing(true);
        adapter.clearDeliveryList();
        moviesList.clear();
        loadDeliveryData(offset);

    }

    @Override
    public void displayDeliveries(List<DeliveryItem> deliveryItemsList) {

        if (deliveryItemsList == null || deliveryItemsList.size() == 0) {
            stopRefreshing();
            stopLoading();
            return;
        }

        offset += deliveryItemsList.size();

        stopLoading();
        stopRefreshing();

        moviesList.addAll(deliveryItemsList);
        adapter.addToDeliveryList(moviesList);
        adapter.notifyDataSetChanged();
    }

    private void stopLoading() {

        if (loading) {
            removeLoadingMoreProgress();
            //since paging isn't available in api making sure it doesn't update offset wrongly
            loading = false;

        }
    }

    @Override
    public void displayErrors(String message) {
        loading = false;
        showErrorMessage(message);
        stopRefreshing();

    }

    private void stopRefreshing() {
        if (mSwipeToRefresh.isRefreshing()) {
            mSwipeToRefresh.setRefreshing(false);
        }
    }

    private void showErrorMessage(String message) {
        stopRefreshing();
        Snackbar snackbar = Snackbar.make(mRecDeliveryList, message, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadDeliveryData(offset);
                    }
                });

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}

