package com.divitngoc.android.helpme_stackoverflow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private List<Item> data;
    private Context mContext;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item
        @BindView(R.id.title)
        TextView mTitleTextView;
        @BindView(R.id.views)
        TextView mViewsTextView;
        @BindView(R.id.score)
        TextView mScoreTextView;
        @BindView(R.id.card_view)
        CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public CustomRecyclerAdapter(Context context, List<Item> myData) {
        this.data = myData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(listItemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item currentQ = data.get(position);
        holder.mTitleTextView.setText(currentQ.getTitle());
        String votesStr = String.valueOf(currentQ.getScore()) + "\nVotes";
        String viewsStr = "Views: " + String.valueOf(currentQ.getViewCount());
        holder.mScoreTextView.setText(votesStr);
        holder.mViewsTextView.setText(viewsStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentQ.getLink()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public int getItemCount() {
        return (data != null ? data.size() : 0);
    }
}
