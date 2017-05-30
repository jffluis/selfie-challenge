package com.ipleiria.selfiechallenge.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.utils.Constants;
import com.ipleiria.selfiechallenge.utils.Firebase;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private Challenge challenge;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private String photoURL;
    private LikeButton likeButton;
    private TextView likeCounter;
    private int likes;
    private int currentPage = 0;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  Challenge challenge) {
        this.activity = activity;
        this.challenge = challenge;
    }

    @Override
    public int getCount() {
        return this.challenge.getPhotos().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        photoURL = challenge.getPhotos().get(position);


        System.out.println("ehehe : " + ((ViewPager) container).getCurrentItem());
        try {
            isLiked();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        likeButton = (LikeButton) viewLayout.findViewById(R.id.like_button);
        likeCounter = (TextView) viewLayout.findViewById(R.id.likes_counter);
        likeCounter.setText("0");

;

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                System.out.println("liked");

                try {
                    postToggleLike();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TODO
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                System.out.println("disliked");

                try {
                    postToggleLike();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TODO

            }
        });

        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        final ProgressBar progressBar = (ProgressBar) viewLayout.findViewById(R.id.progress);

        Glide
                .with(activity)
                .load(photoURL)
                .crossFade()
                .animate(R.anim.fade_in).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(imgDisplay);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) activity).getSupportFragmentManager().popBackStack();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    private void post(String urlStr) throws IOException, JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL = urlStr;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);

                JSONObject json = null;
                try {
                    json = new JSONObject(response);

                    int likes = json.getInt("likes");
                    boolean isLiked = json.getBoolean("hasLike");
                    toggleCallback(likes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("user_id", Instance.getInstance().getCurrentUser().getId());
                headers.put("photo_id", photoURL);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void likeCallback(int likes, boolean isLiked){
        likeButton.setLiked(isLiked);
        likeCounter.setText(String.valueOf(likes));
    }

    private void toggleCallback(int likes){
        likeCounter.setText(String.valueOf(likes));
    }

    private void isLiked() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL = Constants.BACKEND_SERVER + "/getLikes";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);

                    int likes = json.getInt("likes");
                    boolean isLiked = json.getBoolean("hasLike");
                    likeCallback(likes, isLiked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("user_id", Instance.getInstance().getCurrentUser().getId());
                headers.put("photo_id", photoURL);
                return headers;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void postToggleLike() throws IOException {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    post(Constants.BACKEND_SERVER + "/toggleLike");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void changePage(int currentItem) throws JSONException {
        photoURL = challenge.getPhotos().get(currentItem);
        currentPage = currentItem;
        isLiked();
    }
}