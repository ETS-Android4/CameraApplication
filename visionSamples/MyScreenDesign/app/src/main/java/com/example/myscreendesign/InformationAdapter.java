package com.example.myscreendesign;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.informationViewHolder>{
    List<InformationModel> list=new ArrayList<>();
    Context context;

    public InformationAdapter(List<InformationModel> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public informationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        informationViewHolder viewHolder=new informationViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String cardViewId = String.valueOf(v.getId());
             context.startActivity(new Intent(context,Tutorial.class));

//                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//                String item = mList.get(itemPosition);
//                Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull informationViewHolder holder, int position) {
      InformationModel informationModel=list.get(position);
//      final String id=informationModel.getId();
     // holder.image.setImageDrawable(informationModel.getImage());
        holder.userName.setText(informationModel.getUserName());
//        holder.message.setText(informationModel.getMessage());
//        holder.samay.setText(informationModel.getSamay());
//        holder.count.setText(informationModel.getMessageCount());

    }




    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class informationViewHolder  extends RecyclerView.ViewHolder
    {
//        ImageView image;
        TextView userName,message,count,samay;
        public informationViewHolder(@NonNull View itemView) {
            super(itemView);
//            image=itemView.findViewById(R.id.imageView);
            userName=itemView.findViewById(R.id.text_user);
//            message=itemView.findViewById(R.id.text_msg);
//            count=itemView.findViewById(R.id.text_count);
//            samay=itemView.findViewById(R.id.text_time);


        }
    }
}
