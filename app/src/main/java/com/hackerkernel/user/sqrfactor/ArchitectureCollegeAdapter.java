package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArchitectureCollegeAdapter extends RecyclerView.Adapter<ArchitectureCollegeAdapter.MyViewAdapter> {
    private ArrayList<ArchitectureCollegeClass> architectureCollegeClassArrayList;
    private Context context;

    public ArchitectureCollegeAdapter(ArrayList<ArchitectureCollegeClass> architectureCollegeClassArrayList, Context context) {
        this.architectureCollegeClassArrayList = architectureCollegeClassArrayList;
        this.context = context;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.architecture_college_adapter, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter holder, int position) {

        final ArchitectureCollegeClass architectureCollegeClass = architectureCollegeClassArrayList.get(position);
        holder.name.setText(architectureCollegeClass.getName());
        Glide.with(context).load(UtilsClass.baseurl1 + architectureCollegeClass.getProfile())
                .into(holder.profileImage);

        holder.collegeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserProfileActivity.class);
                intent.putExtra("User_id",architectureCollegeClass.getId());
                intent.putExtra("ProfileUserName",architectureCollegeClass.getUser_name());
                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return architectureCollegeClassArrayList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView name,college;
        ImageView profileImage;
        LinearLayout collegeLinear;

        public MyViewAdapter(View itemView) {
            super(itemView);

            profileImage=(ImageView) itemView.findViewById(R.id.architectureCollege_image);
            name=(TextView)itemView.findViewById(R.id.architectureCollege_name);
            college =(TextView)itemView.findViewById(R.id.architectureCollege_college);
            collegeLinear = (LinearLayout)itemView.findViewById(R.id.college_linear);


        }
    }
}
