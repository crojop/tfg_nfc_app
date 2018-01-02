package com.example.cristina.tfgapp.controller_view;

/**
 * Created by Cristina on 16/09/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristina.tfgapp.R;

public class AdapterGraph extends RecyclerView.Adapter <AdapterGraph.GraphViewHolder>
        implements View.OnClickListener {
    //Adaptador específico de los recycler view

    private View.OnClickListener listener;
    private String[] graph_titles;
    private int[] graph_image;

    public AdapterGraph(String[] titles, int[] images) {
        this.graph_titles = titles;
        this.graph_image = images;
    }

    @Override
    public GraphViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_graph_adapter, viewGroup, false);
        itemView.setMinimumHeight(viewGroup.getMeasuredHeight()/2);
        itemView.setMinimumWidth(viewGroup.getMeasuredWidth());
        itemView.setOnClickListener(this);

        return new GraphViewHolder(itemView, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(GraphViewHolder viewHolder, int pos) {
        viewHolder.bindCardElement(graph_titles[pos], graph_image[pos]);
    }

    @Override
    public int getItemCount() {
        return graph_titles.length;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public static class GraphViewHolder
            extends RecyclerView.ViewHolder {

        private Context context;
        private TextView name;
        private ImageView photo;

        public GraphViewHolder(View itemView, Context c) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.graphName);
            photo = (ImageView)itemView.findViewById(R.id.graphImage);
            context = c;
        }

        public void bindCardElement(String title, int image) {
            name.setText(title);
            photo.setImageResource(image);
            if (title==context.getString(R.string.SHOP)) photo.setPadding(12, 24, 32, 40);
            if (title==context.getString(R.string.MOVEMENTS)) photo.setPadding(20, 24, 20, 22);
            if (title==context.getString(R.string.SETTINGS)) photo.setPadding(32, 32, 32, 28);
            photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

}
