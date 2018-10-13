package com.medisyst.medisyst;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<History> history;
    private HomeActivity homeActivity;
    private int[] thumb={R.drawable.h1,R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5,R.drawable.h6,R.drawable.h7,R.drawable.h8};
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,prof_name,solution;
        LinearLayout cardItem;
        ImageView thumbnail;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            name.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            date = view.findViewById(R.id.views);
            date.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            prof_name = view.findViewById(R.id.date);
            prof_name.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            solution = view.findViewById(R.id.date);
            solution.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            thumbnail = view.findViewById(R.id.thumbnail);
            cardItem = view.findViewById(R.id.cardItem);
        }
    }
    HistoryAdapter(HomeActivity homeActivity, List<History> history) {
        this.history = history;
        this.homeActivity = homeActivity;

        Random rnd = ThreadLocalRandom.current();
        for (int i=thumb.length-1;i>0;i--)
        {
            int index = rnd.nextInt(i+1);
            int a = thumb[index];
            thumb[index] = thumb[i];
            thumb[i] = a;
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Schemes scheme = schemes.get(position);
        holder.title.setText(scheme.getTitle());
        holder.last_date.setText(daysLeft(scheme.getDate()));
        holder.views.setText(scheme.getViews());
        Picasso.get().load(scheme.getThumbnail()).into(holder.thumbnail);

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(homeActivity,SchemeActivity.class);
                Log.i("email",email);
                home.putExtra("email",email);
                home.putExtra("aadhaar",aadhaar);
                home.putExtra("name",scheme.getTitle());
                home.putExtra("date",scheme.getDate());
                home.putExtra("days",daysLeft(scheme.getDate()));
                home.putExtra("details",scheme.getDetails());
                home.putExtra("img",scheme.getThumbnail());
                homeActivity.startActivity(home);
                homeActivity.overridePendingTransition(R.anim.fade_in,0);
            }
        });
    }
    @Override
    public int getItemCount() {
        return schemes.size();
    }
    private String daysLeft(String date)
    {
        long diffInMillies=0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Calendar c = Calendar.getInstance();
            Date firstDate = sdf.parse(sdf.format(c.getTime()));
            Date secondDate = sdf.parse(date);
            diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        }
        catch (Exception ignored){}
        return((TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)+1)+" Days\nRemaining");
    }
}