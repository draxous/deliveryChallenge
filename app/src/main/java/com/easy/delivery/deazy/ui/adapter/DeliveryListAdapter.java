package com.easy.delivery.deazy.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.easy.delivery.deazy.R;
import com.easy.delivery.deazy.api.model.Delivery;
import com.easy.delivery.deazy.api.model.DeliveryItem;
import com.easy.delivery.deazy.ui.MainActivity;
import com.easy.delivery.deazy.ui.MapsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final Context mContext;
    private List<DeliveryItem> moviesList;

    public DeliveryListAdapter(Context context) {
        mContext = context;
        moviesList = new ArrayList<>();
    }

    public void addToDeliveryList(List<DeliveryItem> deliveryItems) {
        if (moviesList == null) {
            moviesList = new ArrayList<>();
        } else {
            moviesList.clear();
            moviesList.addAll(deliveryItems);
        }

        notifyDataSetChanged();
    }

    public void clearDeliveryList() {
        if (moviesList != null) {
            moviesList.clear();
        }

        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.deliveries_layout, parent, false);

            viewHolder = new DeliverItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_layout, parent, false);

            viewHolder = new ProgressViewHolder(v);
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DeliverItemViewHolder) {
            final DeliveryItem deliveryItem = (DeliveryItem) moviesList.get(position);

            final DeliverItemViewHolder viewHolder = ((DeliverItemViewHolder) holder);

            Glide.with(mContext).load(moviesList.get(position).getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.mMovieBanner) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    viewHolder.mMovieBanner.setImageDrawable(circularBitmapDrawable);
                }
            });

            viewHolder.mDeliveryDescription.setText(deliveryItem.getDescription());
            viewHolder.mDeliveryAddress.setText(deliveryItem.getLocation().getAddress());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra(((MainActivity) mContext).DELIVERY_ITEM, deliveryItem);
                    mContext.startActivity(intent);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList == null ? 0 : moviesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return moviesList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public class DeliverItemViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        @BindView(R.id.img_banner)
        ImageView mMovieBanner;
        @BindView(R.id.txt_delivery_des)
        TextView mDeliveryDescription;

        @BindView(R.id.txt_address)
        TextView mDeliveryAddress;


        public DeliverItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }
    }
}
