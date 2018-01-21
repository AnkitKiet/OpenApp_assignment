package edu.openapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.openapp.R;
import edu.openapp.activity.EditMemberActivity;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 19/01/18.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.RecyclerViewHolder> {

    private List<MemberModel> MemberModelList;
    private Context context;

    public MemberListAdapter(Context context, List<MemberModel> MemberModelList) {
        this.MemberModelList = MemberModelList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_members, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        holder.txtName.setText(MemberModelList.get(position).getName());
        Date ludate = MemberModelList.get(position).getTimestamp();
        int yr = ludate.getYear();
        if (yr > 100) {
            yr = ludate.getYear() - 100;
        }

        String ldate = ludate.getDate() + "/" + ludate.getMonth() + "/" + yr;
        holder.txtLUD.setText(ldate);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = (new Intent(context, EditMemberActivity.class));
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", MemberModelList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "file" + MemberModelList.get(position).getImage() + ".jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(context).load(stream.toByteArray()).asBitmap().into(holder.profilepic);
        holder.profilepic.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return MemberModelList.size();
    }


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtLUD;
        CircleImageView profilepic;
        private RelativeLayout rl;
        private Button btnEdit;

        RecyclerViewHolder(View view) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtLUD = (TextView) view.findViewById(R.id.txtLUD);
            profilepic = (CircleImageView) view.findViewById(R.id.profile_image);
            btnEdit = (Button) view.findViewById(R.id.btnEdit);

        }
    }


}