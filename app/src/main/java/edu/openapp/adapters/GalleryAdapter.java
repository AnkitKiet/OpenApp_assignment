package edu.openapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.openapp.R;

/**
 * Created by Ankit on 21/01/18.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecyclerViewHolder> {

    private List<File> mList;
    private Context context;

    public GalleryAdapter(Context context, List<File> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public GalleryAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryAdapter.RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.RecyclerViewHolder holder, final int position) {

        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(position).getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(context).load(stream.toByteArray()).asBitmap().into(holder.profilepic);
        holder.profilepic.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilepic;

        RecyclerViewHolder(View view) {
            super(view);
            profilepic = (CircleImageView) view.findViewById(R.id.imgView);

        }
    }


}
