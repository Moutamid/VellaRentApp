package com.moutimid.vellarentapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Home.VillaDetailsActivity;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.VillaModel;

import java.util.ArrayList;
import java.util.List;

public class AllVillaAdapter extends RecyclerView.Adapter<AllVillaAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<VillaModel> productModels;

    public AllVillaAdapter(Context ctx, List<VillaModel> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.all_villa, parent, false);
        return new GalleryPhotosViewHolder(view);
    }
    public void filterList(ArrayList<VillaModel> filterlist) {

        productModels = filterlist;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        VillaModel VillaModel = productModels.get(position);
        holder.resturant_name.setText(VillaModel.getName());
        holder.resturant_discription.setText(VillaModel.getShort_description());
        Glide.with(ctx).load(VillaModel.getImage_url()).into(holder.image);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, VillaDetailsActivity.class);
            Stash.put(Config.currentModel, VillaModel);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView resturant_discription, resturant_name;
        ImageView image;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            resturant_discription = itemView.findViewById(R.id.resturant_discription);
            resturant_name = itemView.findViewById(R.id.resturant_name);
            image = itemView.findViewById(R.id.image);

        }
    }
}
