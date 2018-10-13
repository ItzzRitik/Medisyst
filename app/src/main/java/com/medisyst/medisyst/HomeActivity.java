package com.medisyst.medisyst;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomergoldst.tooltips.ToolTipsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout logo_div,splash_cover,diagnosis;
    ImageView ico_splash,menu,search;
    TextView page_tag;
    Animator animator;
    CardView data_div;
    ObjectAnimator startAnim;
    Point screenSize;
    ToolTipsManager toolTip;
    RecyclerView display;
    List<History> history;
    double diagonal;
    OkHttpClient client;
    SwipeRefreshLayout refresh;
    FloatingActionButton add;
    String Email="",Aadhaar="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        diagonal=Math.sqrt((screenSize.x*screenSize.x) + (screenSize.y*screenSize.y));
        splash_cover=findViewById(R.id.splash_cover);
        logo_div=findViewById(R.id.logo_div);
        data_div=findViewById(R.id.data_div);
        toolTip = new ToolTipsManager();
        client = new OkHttpClient();
        Email=getIntent().getStringExtra("email");
        Aadhaar=getIntent().getStringExtra("aadhaar");

        page_tag=findViewById(R.id.page_tag);
        page_tag.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/exo2.ttf"));

        ico_splash=findViewById(R.id.ico_splash);
        ico_splash.setScaleType(ImageView.ScaleType.CENTER);

        menu=findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                    }
                }
        );

        diagnosis=findViewById(R.id.diagnosis);

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float CurrentX = add.getX();
                float CurrentY = add.getY();
                float FinalX = (data_div.getWidth()/2)-(add.getWidth()/2);
                float FinalY = (data_div.getHeight()/2)-(add.getHeight()/2);
                Path path = new Path();
                path.moveTo(CurrentX, CurrentY);
                path.quadTo(CurrentX*4/3, (CurrentY+FinalY)*2/5, FinalX, FinalY);
                startAnim = ObjectAnimator.ofFloat(add, View.X, View.Y, path);
                startAnim.setDuration(300);
                startAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                startAnim.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                    @Override public void onAnimationEnd(Animator animator) {
                        diagnosis.setVisibility(View.VISIBLE);
                        int cx = data_div.getWidth()/2;
                        int cy = data_div.getHeight()/2;
                        int finalRadius = Math.max(data_div.getWidth(), data_div.getHeight());
                        animator=ViewAnimationUtils.createCircularReveal(diagnosis, cx, cy, add.getWidth(), finalRadius);
                        animator.setDuration(300);
                        animator.start();

                        int colorFrom = getResources().getColor(R.color.colorAccent);
                        int colorTo = getResources().getColor(R.color.colorPrimary);
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(300);
                        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                diagnosis.setBackgroundColor((int) animator.getAnimatedValue());
                            }
                        });
                        colorAnimation.start();
                    }
                });
                startAnim.start();
            }
        });

        history = new ArrayList<>();
        display=findViewById(R.id.display);
        prepareHistory();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        display.setLayoutManager(mLayoutManager);
        display.addItemDecoration(new GridSpacingItemDecoration(1,dptopx(10),true));
        display.setItemAnimator(new DefaultItemAnimator());

        if(getIntent().getBooleanExtra("isProfile",false))
        {
            splash_cover.setVisibility(View.GONE);
            logo_div.setVisibility(View.VISIBLE);
            page_tag.setVisibility(View.VISIBLE);
            menu.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            scaleY(data_div,getIntent().getIntExtra("divHeight",0),0,new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(1000);
                    display.setVisibility(View.VISIBLE);display.startAnimation(anims);
                }},800);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // SignUp Animation

                    splash_cover.setVisibility(View.GONE);
                    logo_div.setVisibility(View.VISIBLE);

                    float CurrentX = ico_splash.getX();
                    float CurrentY = ico_splash.getY();
                    float FinalX = 0;
                    float FinalY = 35;
                    Path path = new Path();
                    path.moveTo(CurrentX, CurrentY);
                    path.quadTo(CurrentX*4/3, (CurrentY+FinalY)/4, FinalX, FinalY);

                    startAnim = ObjectAnimator.ofFloat(ico_splash, View.X, View.Y, path);
                    startAnim.setDuration(800);
                    startAnim.setInterpolator(new AccelerateDecelerateInterpolator());

                    AlphaAnimation anims = new AlphaAnimation(0.6f,1);
                    anims.setDuration(1000);
                    anims.setFillAfter(true);
                    anims.setInterpolator(new AccelerateDecelerateInterpolator());
                    logo_div.startAnimation(anims);
                    startAnim.start();
                    ico_splash.animate().scaleX(0f).scaleY(0f).setDuration(1000).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scaleY(data_div,pxtodp(splash_cover.getHeight())-85,800,new AccelerateDecelerateInterpolator());
                            AlphaAnimation anims = new AlphaAnimation(1,0);anims.setDuration(700);anims.setFillAfter(true);
                            ico_splash.startAnimation(anims);
                        }},10);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(400);
                            page_tag.setVisibility(View.VISIBLE);page_tag.startAnimation(anims);
                            menu.setVisibility(View.VISIBLE);menu.startAnimation(anims);
                            search.setVisibility(View.VISIBLE);search.startAnimation(anims);
                        }},400);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlphaAnimation anims = new AlphaAnimation(0,1);anims.setDuration(1000);
                            display.setVisibility(View.VISIBLE);display.startAnimation(anims);
                        }},800);

                }},1500);
        }
    }
    public void prepareHistory(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://nodeexercise-adityabhardwaj.c9users.io/schemes").newBuilder();
        urlBuilder.addQueryParameter("email",getIntent().getStringExtra("email"));
        Request request = new Request.Builder().url(urlBuilder.build().toString()).get()
                .addHeader("Content-Type", "application/json").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("failure", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String mMessage = Objects.requireNonNull(response.body()).string();
                refresh.setRefreshing(false);
                if (response.isSuccessful()){
                    try {
                        JSONArray postsArray = new JSONArray(mMessage);
                        history = new ArrayList<>();
                        for (int i = 0; i < postsArray.length(); i++) {
                            JSONObject pO = postsArray.getJSONObject(i);
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(Email==null){Email="aditya.aastha@gmail.com";}
                                if(Aadhaar==null){Aadhaar="123456789123";}

                            }
                        });
                    }
                    catch (JSONException e) {
                        Log.w("error", e.toString());
                    }
                }
            }
        });
    }
    public void scaleX(final View view,int x,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(),dptopx(x));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(t);anim.start();
    }
    public void scaleY(final View view,int y,int t, Interpolator interpolator)
    {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(),dptopx(y));anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);view.invalidate();
            }
        });
        anim.setDuration(t);anim.start();
    }
    public int dptopx(float dp)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int pxtodp(float px)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void vibrate(int ms)
    {
        ((Vibrator) Objects.requireNonNull(this.getSystemService(Context.VIBRATOR_SERVICE))).vibrate(ms);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}