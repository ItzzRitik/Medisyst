package com.medisyst.medisyst;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.MyViewHolder> {
    private List<Permission> permission;
    private HomeActivity homeActivity;
    private Random random;
    private int[] thumb={R.drawable.h1,R.drawable.h2,R.drawable.h3,R.drawable.h4,
            R.drawable.h5,R.drawable.h6,R.drawable.h7,R.drawable.h8};
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,key,accept,reject;
        LinearLayout cardItem;
        ImageView thumbnail;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            name.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2_bold.otf"));
            key = view.findViewById(R.id.key);
            key.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            accept = view.findViewById(R.id.accept);
            accept.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            reject = view.findViewById(R.id.reject);
            reject.setTypeface(Typeface.createFromAsset(homeActivity.getAssets(), "fonts/exo2.ttf"));
            thumbnail = view.findViewById(R.id.thumbnail);
            cardItem = view.findViewById(R.id.cardItem);
        }
    }
    PermissionAdapter(HomeActivity homeActivity, List<Permission> Permission) {
        this.permission = Permission;
        this.homeActivity = homeActivity;
        random = new Random();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_permission, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Permission item = permission.get(position);
        holder.thumbnail.setImageDrawable(homeActivity.getDrawable(thumb[random.nextInt(thumb.length-1)]));
        holder.name.setText(item.getName());
        holder.key.setText(item.getKey());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://medisyst-adityabhardwaj.c9users.io/allow").newBuilder();
                urlBuilder.addQueryParameter("key",item.getKey());
                Request request = new Request.Builder().url(urlBuilder.build().toString()).get()
                        .addHeader("Content-Type", "application/json").build();
                homeActivity.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("key", e.getMessage());
                        call.cancel();
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        if(Integer.parseInt(Objects.requireNonNull(response.body()).string())==1 && response.isSuccessful()){
                            Log.i("key","Request Accepted");
                        }
                        else{
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    homeActivity.listRefresh();
                                }
                            });
                        }
                    }
                });
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://medisyst-adityabhardwaj.c9users.io/reject").newBuilder();
                urlBuilder.addQueryParameter("key",item.getKey());
                Request request = new Request.Builder().url(urlBuilder.build().toString()).get()
                        .addHeader("Content-Type", "application/json").build();
                homeActivity.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("key", e.getMessage());
                        call.cancel();
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        if(Integer.parseInt(Objects.requireNonNull(response.body()).string())==1 && response.isSuccessful()){
                            Log.i("key","Request Rejected");
                        }
                        else{
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    homeActivity.listRefresh();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return permission.size();
    }
}