package com.ipleiria.selfiechallenge.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.ipleiria.selfiechallenge.R;


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

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private GoogleApiClient mGoogleApiClient;

    private OnFragmentInteractionListener mListener;

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

        return inflater.inflate(R.layout.fragment_choose_challenge, container, false);
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

}
