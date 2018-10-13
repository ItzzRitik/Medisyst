package com.medisyst.medisyst;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<History> history;
    private HomeActivity homeActivity;
    private Random random;
    private int[] thumb={R.drawable.h1,R.drawable.h2,R.drawable.h3,R.drawable.h4,
            R.drawable.h5,R.drawable.h6,R.drawable.h7,R.drawable.h8};
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,prof_name,solution;
        LinearLayout cardItem;
        ImageView thumbnail;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            name.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            date = view.findViewById(R.id.date);
            date.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            prof_name = view.findViewById(R.id.prof_name);
            prof_name.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            solution = view.findViewById(R.id.solution);
            solution.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            thumbnail = view.findViewById(R.id.thumbnail);
            cardItem = view.findViewById(R.id.cardItem);
        }
    }
    HistoryAdapter(HomeActivity homeActivity, List<History> history) {
        this.history = history;
        this.homeActivity = homeActivity;
        random = new Random();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_people, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final History item = history.get(position);
        holder.thumbnail.setImageDrawable(homeActivity.getDrawable(thumb[random.nextInt(thumb.length-1)]));
        holder.name.setText(item.getName());
        holder.date.setText(item.getDate());
        holder.prof_name.setText(item.getProfName());
        holder.solution.setText(item.getSolution());
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    @Override
    public int getItemCount() {
        return history.size();
    }
}