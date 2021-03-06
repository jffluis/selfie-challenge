package com.ipleiria.selfiechallenge.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.activity.MainActivity;
import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;
import com.ipleiria.selfiechallenge.utils.Firebase;

import static android.R.attr.category;


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
        final String[] nameChallenge = {""};
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "cliquei no " + adapterView.getSelectedItem()
                        , Toast.LENGTH_SHORT).show();
                nameChallenge[0] = "Take a selfie " + adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final EditText description = (EditText) view.findViewById(R.id.txt_challenge_description);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameChallenge[0].toString() != null){

                    Challenge newChallenge = new Challenge(nameChallenge[0], description.getText().toString(), Instance.getInstance().getCurrentUser());//Instance.getInstance().getCurrentUser());
                    Instance.getInstance().addChallenge(newChallenge);
                    String id = Firebase.dbUserChallenges.push().getKey();
                    Firebase.dbUserChallenges.child(id).setValue(newChallenge);


                   DatabaseReference pointsRef = Firebase.dbUsers.child(Instance.getInstance().getCurrentUser().getId()+"/points");
                    pointsRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Integer currentValue = mutableData.getValue(Integer.class);
                            if (currentValue == null) {
                                mutableData.setValue(0);
                            } else {
                                mutableData.setValue(currentValue + 10);
                            }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            System.out.println("Transaction completed");
                        }
                    });


                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                            .replace(R.id.contentContainer, ChooseChallengeFragment.newInstance(0)).addToBackStack(null).commit();

                    showDialog("You earned 10 points for creating a challenge!");
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

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Congratulations!");
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_star);
        String positiveText = "OK";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
