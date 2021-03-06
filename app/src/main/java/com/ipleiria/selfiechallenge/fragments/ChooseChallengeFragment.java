package com.ipleiria.selfiechallenge.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.adapter.RVAdapter;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;
import com.ipleiria.selfiechallenge.utils.Firebase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseChallengeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseChallengeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX = "index";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "DEBUG: ";
    private RVAdapter rvAdapter;

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private GoogleApiClient mGoogleApiClient;

    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;


    public ChooseChallengeFragment() {
        // Required empty public constructor
    }

    public static ChooseChallengeFragment newInstance(int index) {
        ChooseChallengeFragment fragment = new ChooseChallengeFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getInt(INDEX);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_challenge, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Challenges");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                        .replace(R.id.contentContainer, CreateChallengeFragment.newInstance(0)).addToBackStack(null).commit();
            }
        });
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching challenges..");
        progressDialog.show();

        Firebase.dbUserChallenges.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Instance.getInstance().getChallengesList().clear();
                getAllChallenges(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rvAdapter = new RVAdapter(Instance.getInstance().getChallengesList(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void getAllChallenges(DataSnapshot dataSnapshot){

        for (DataSnapshot ds: dataSnapshot.getChildren()){
            ArrayList<String> photosUrl = new ArrayList<>();
            String nameChallengeFirebase = ds.child("name").getValue(String.class);
            String id = ds.getKey();
            String descriptionChallengeFirebase = ds.child("description").getValue(String.class);
            String userName = ds.child("user").child("name").getValue(String.class);
            String userId = ds.child("user").child("id").getValue(String.class);
            String userphoto = ds.child("user").child("photoURL").getValue(String.class);
            Integer points = ds.child("user").child("points").getValue(Integer.class);

            for(DataSnapshot photo: ds.child("photos").getChildren()){
                String photoURL = photo.getValue(String.class);
                photosUrl.add(photoURL);
            }

            User user =  new User(userId, userName, userphoto, points);
            Challenge challenge = new Challenge(id, nameChallengeFirebase, descriptionChallengeFirebase,user, photosUrl);
            Instance.getInstance().getChallengesList().add(challenge);

        }
        rvAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }


    private void showImageFromFireBaseDataBase(StorageReference storageRef)
    {
        try {
            final File localFile = File.createTempFile("images", "jpg");
            final Bitmap[] bitmap = new Bitmap[1];
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("Test", "success!");
                    bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    //raysImage.setImageBitmap(bitmap[0]);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("Test", "fail :( " + exception.getMessage());
                }
            });
        }catch(IOException e){
            Log.e("ImageView",e.toString());
        }
    }
}
