package com.ipleiria.selfiechallenge.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.share.internal.LikeButton;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.adapter.FullScreenImageAdapter;
import com.like.OnLikeListener;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoViewerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX = "index";
    private static final String ARG_PARAM2 = "param2";

    private static int pos;
    private String mParam2;

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public PhotoViewerFragment() {
        // Required empty public constructor
    }

    public static PhotoViewerFragment newInstance(int index) {
        PhotoViewerFragment fragment = new PhotoViewerFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo_viewer, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new FullScreenImageAdapter(getActivity(),
                Instance.getInstance().challengeToSee);
        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(Instance.getInstance().selectedPhotoPos);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    adapter.changePage(position);
                } catch (JSONException e) {


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
