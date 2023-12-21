package com.moutimid.vellarentapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.dailogues.CalenderDialogClass;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.Villa;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllVillaAdapter extends RecyclerView.Adapter<AllVillaAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<Villa> productModels;

    public AllVillaAdapter(Context ctx, List<Villa> productModels) {
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

    public void filterList(ArrayList<Villa> filterlist) {
        productModels = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        Villa villa = productModels.get(position);
        holder.villa_name.setText(villa.getName());
        holder.user_name.setText(villa.getUserName());
        holder.villa_discription.setText(villa.getBill() + " $/month");
        if (villa.isBills_included()) {
            holder.bill_included.setText("Included");
        } else {
            holder.bill_included.setText("Not Included");

        }
        Glide.with(ctx).load(villa.getImage()).into(holder.image);

        Glide.with(ctx).load(villa.getUserImage()).into(holder.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (villa.available.equals("available")) {
                    Stash.put(Config.currentModel, villa);
                    CalenderDialogClass cdd = new CalenderDialogClass(ctx);
                    cdd.show();
                }
                else
                {
                    Toast.makeText(ctx, "Villa is not available yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView villa_discription, villa_name, bill_included, user_name;
        ImageView image;
        CircleImageView user_image;


        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            villa_discription = itemView.findViewById(R.id.bill);
            villa_name = itemView.findViewById(R.id.villa_name);
            image = itemView.findViewById(R.id.image);
            bill_included = itemView.findViewById(R.id.bill_included);
            user_name = itemView.findViewById(R.id.user_name);
            user_image = itemView.findViewById(R.id.user_image);

        }
    }
}
