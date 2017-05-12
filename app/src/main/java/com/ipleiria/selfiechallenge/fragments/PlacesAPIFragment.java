package com.ipleiria.selfiechallenge.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.adapter.RVPOIAdapter;
import com.ipleiria.selfiechallenge.model.POI;
import com.ipleiria.selfiechallenge.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlacesAPIFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlacesAPIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesAPIFragment extends Fragment implements
        android.location.LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PLACES: ";
    public static final int LOCATION_REQUEST = 3;
    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LocationManager locationManager;
    private String provider;
    private static View view;
    private Location location;
    private TextView textViewgenerate;
    private Switch switchLocation;
    private Button btnGenerate;
    private boolean searchByLocation = false;
    private String placeToSearch = "";
    private RVPOIAdapter rvAdapter;
    private RecyclerView rvPOI;

    public PlacesAPIFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlacesAPIFragment newInstance(int index) {
        PlacesAPIFragment fragment = new PlacesAPIFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_places_api, container, false);
        } catch (InflateException e) {
    /* map is already there, just return view as it is */
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Challenge");
        getLocation();

        textViewgenerate = (TextView) view.findViewById(R.id.textView3);
        switchLocation = (Switch) view.findViewById(R.id.switch2);
        btnGenerate = (Button) view.findViewById(R.id.btn_generate);
        rvPOI = (RecyclerView) view.findViewById(R.id.rv_poi);
        rvAdapter = new RVPOIAdapter(Instance.getInstance().getPOIList(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPOI.setLayoutManager(mLayoutManager);
        rvPOI.setItemAnimator(new DefaultItemAnimator());
        rvPOI.setAdapter(rvAdapter);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Fetching POIs..");
                progressDialog.show();

                if(searchByLocation){
                    try {
                        getPlaces(location);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else if (!placeToSearch.isEmpty()) {
                    try {
                        getPlaces(placeToSearch);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

            }

        });



        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getActivity(), "Place selected: " + place.getName().toString(), Toast.LENGTH_SHORT).show();
                placeToSearch = place.getName().toString();
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    textViewgenerate.setVisibility(View.GONE);
                    autocompleteFragment.getView().setVisibility(View.GONE);
                    searchByLocation = true;
                }else {
                    textViewgenerate.setVisibility(View.VISIBLE);
                    autocompleteFragment.getView().setVisibility(View.VISIBLE);
                    searchByLocation = false;
                }
            }
        });

        return view;

    }

    private void getLocation() {
        if(checkLocationPermission()) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String locLat = String.valueOf(latitude) + "," + String.valueOf(longitude);

                Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                }

                Log.i(TAG, "Place: " + locLat);

            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        String locLat = String.valueOf(lat)+","+String.valueOf(lng);

        Log.i(TAG, "ON LOCATION CHANGED: " + locLat);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }


    private void getPlaces(Location location) throws MalformedURLException {

        final String url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                "json?query=point+of+interest&location="+location.getLatitude()+","+location.getLongitude()+"&key=" + Constants.API_KEY;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb;
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(url_string);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    sb = new StringBuffer("");
                    while (data != -1) {
                        sb.append((char) data);
                        data = isw.read();
                    }
                    parseResponse(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();

                    }
                }
            }
        }).start();

    }


    private void getPlaces(String cityName) throws MalformedURLException {
        String cityParsed = cityName.replace(" ", "+");
        final String url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                "json?query="+ cityParsed + "+point+of+interest" + "&key=" + Constants.API_KEY;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb;
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(url_string);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    sb = new StringBuffer("");
                    while (data != -1) {
                        sb.append((char) data);
                        data = isw.read();
                    }
                    parseResponse(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();

                    }
                }
            }
        }).start();

    }


    private void parseResponse(String response) throws JSONException {

        Instance.getInstance().getPOIList().clear();
        JSONObject jObject = new JSONObject(response);
        JSONArray jArrayStock = jObject.getJSONArray("results");

        JSONArray jArray = shuffleJsonArray(jArrayStock);


        for (int i=0; i < 3; i++)
        {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                String name = oneObject.getString("name");
                String address = oneObject.getString("formatted_address");
                JSONArray jArrayPhoto = oneObject.getJSONArray("photos");
                String photoURL;
                if(jArrayPhoto.length() > 0){
                    String photoReference = jArrayPhoto.getJSONObject(0).getString("photo_reference");
                    photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+
                            photoReference +"&key="+Constants.API_KEY;
                }else {
                    photoURL = "https://image.freepik.com/free-icon/placeholder-on-map-paper-in-perspective_318-61698.jpg";
                }

                Instance.getInstance().addPOI(new POI(name, address, photoURL));

            } catch (JSONException e) {
                e.printStackTrace();
                // quando da porcaria
            }
        }
        Log.d(TAG, "LISTA POI: " + Instance.getInstance().getPOIList().toString());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rvAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });

    }

    public static JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }


}
