package com.ipleiria.selfiechallenge.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;
import com.ipleiria.selfiechallenge.utils.Firebase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateChallengeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateChallengeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX = "index";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateChallengeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreateChallengeFragment newInstance(int index) {
        CreateChallengeFragment fragment = new CreateChallengeFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
        Button btn_create = (Button) view.findViewById(R.id.btn_create_challenge);
        final EditText nameChallenge = (EditText) view.findViewById(R.id.txt_challenge_name);
        final EditText description = (EditText) view.findViewById(R.id.txt_challenge_description);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameChallenge.getText().toString() != null){

                    Challenge newChallenge = new Challenge(nameChallenge.getText().toString(), description.getText().toString(), Instance.getInstance().getCurrentUser());//Instance.getInstance().getCurrentUser());
                    Instance.getInstance().addChallenge(newChallenge);

                    String id = Firebase.dbUserChallenges.push().getKey();

                    Firebase.dbUserChallenges.child(id).setValue(newChallenge);

                    Toast.makeText(getActivity(), "Added with success", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, ChooseChallengeFragment.newInstance(0)).addToBackStack(null).commit();

                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
