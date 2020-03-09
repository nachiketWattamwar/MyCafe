package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String data1[],data2[];
    Context context;

    public MyAdapter(Context ct,String s1[],String s2[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        Log.d("demo========", String.valueOf(data2[0]));
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.t1.setText(data1[position]);
        holder.t2.setText("Rating is "+data2[position]);


    }

    @Override
    public int getItemCount() {

        return data1.length;
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        TextView t1,t2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.textView);
            t2 = itemView.findViewById(R.id.textViewRating);
        }
    }
}
