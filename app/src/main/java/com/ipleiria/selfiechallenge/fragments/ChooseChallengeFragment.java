package com.ipleiria.selfiechallenge.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.adapter.RVAdapter;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;
import com.ipleiria.selfiechallenge.utils.Firebase;

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

    private ArrayList<Challenge> listChallenges = new ArrayList<>();

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

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching challenges..");
        progressDialog.show();

        Firebase.dbUserChallenges.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String nameChallengeFirebase = dataSnapshot.child("name").getValue(String.class);
                    String descriptionChallengeFirebase = dataSnapshot.child("description").getValue(String.class);
                    String userName = dataSnapshot.child("user").child("name").getValue(String.class);
                    Integer points = dataSnapshot.child("user").child("points").getValue(Integer.class);
                    User user =  new User(userName, points);

                    Challenge challenge = new Challenge(nameChallengeFirebase, descriptionChallengeFirebase,user);
                    listChallenges.add(challenge);
                    rvAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listChallenges = Instance.getInstance().getChallengesList();
        rvAdapter = new RVAdapter(listChallenges, getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvAdapter);

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if(autocompleteFragment == null) {
            autocompleteFragment = (SupportPlaceAutocompleteFragment) SupportPlaceAutocompleteFragment.instantiate(getActivity(), "com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment");
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    Toast.makeText(getActivity(), place.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Status status) {

                }
            });

            getChildFragmentManager().beginTransaction().replace(R.id.autocomplete_fragment, autocompleteFragment).commit();
        }

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



}
