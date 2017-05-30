package com.ipleiria.selfiechallenge.fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.activity.MainActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlacesAPIFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlacesAPIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesAPIFragment extends Fragment {
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
    public RVPOIAdapter rvAdapter;
    private RecyclerView rvPOI;
    private GoogleApiClient client;
    private Weather weather;
    private PendingIntent myPendingIntent;
    private PendingIntent myPendingIntent2;
    private PendingIntent myPendingIntent3;

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



        client = new GoogleApiClient.Builder(getActivity())
                .addApi(Awareness.API)
                .build();
        client.connect();


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
                        if(location != null){
                            getPlaces(location);
                        }else {
                            showDialog("Could not get location!");
                        }
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

        getLocation();

        getWeather();

        return view;

    }

    private void getLocation() {
        checkLocationPermission();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Location");
        progressDialog.show();
        Awareness.SnapshotApi.getLocation(client)
                .setResultCallback(new ResultCallback<LocationResult>() {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        if (locationResult.getStatus().isSuccess()) {
                            location = locationResult.getLocation();
                            System.out.println("ENTREI AQUI??");
                            progressDialog.dismiss();
                        }
                    }
                });
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

        String url_string = "";

        if(isNightEarly()){
            url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?location="+location.getLatitude()+","+location.getLongitude()+"&type=restaurant&key=" + Constants.API_KEY;
            showDialog("Since it is dinner time, we recommended you some Restaurants!");
        }else if (isNightLate()){
            url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?location="+location.getLatitude()+","+location.getLongitude()+"&type=bar&key=" + Constants.API_KEY;
            showDialog("Since is party time, we recommended you some Bars!");

        }else if (isDay() && weather.getConditions()[0] == Weather.CONDITION_CLEAR){
            url_string = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?location="+location.getLatitude()+","+location.getLongitude()+"&type=amusement_park&key=" + Constants.API_KEY;
            showDialog("We recommended you some entertainment places");

        }else if (isDay() && weather.getConditions()[0] == Weather.CONDITION_RAINY ||
                weather.getConditions()[0] == Weather.CONDITION_STORMY ||
                weather.getConditions()[0] == Weather.CONDITION_CLOUDY){
            url_string = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?location="+location.getLatitude()+","+location.getLongitude()+"&type=shopping_mall&key=" + Constants.API_KEY;
            showDialog("We recommended you some shopping  places");
        }

        final String final_urlString = url_string;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb;
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(final_urlString);
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
        String url_string = "";

        if(isNightEarly()){
            url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?query="+ cityParsed + "+point+of+interest" + "&type=restaurant&key=" + Constants.API_KEY;
            showDialog("Since it is dinner time, we recommended you some Restaurants!");
        }else if (isNightLate()){
            url_string= "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?query="+ cityParsed + "+point+of+interest" + "&type=bar&key=" + Constants.API_KEY;
            showDialog("Since is party time, we recommended you some Bars!");

        }else if (isDay() && weather.getConditions()[0] == Weather.CONDITION_CLEAR){
            url_string = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?query="+ cityParsed + "+point+of+interest" + "&type=amusement_park&key=" + Constants.API_KEY;
            showDialog("We recommended you some entertainment places");

        }else if (isDay() && weather.getConditions()[0] == Weather.CONDITION_RAINY ||
                weather.getConditions()[0] == Weather.CONDITION_STORMY ||
                weather.getConditions()[0] == Weather.CONDITION_CLOUDY){

            url_string = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                    "json?query="+ cityParsed + "+point+of+interest" + "&type=shopping_mall&key=" + Constants.API_KEY;
            showDialog("We recommended you some shopping  places");
        }

        final String final_urlString = url_string;

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb;
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(final_urlString);
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


        for (int i = 0; i < 3; i++)
        {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                String name = oneObject.getString("name");
                String address = oneObject.getString("formatted_address");
                JSONArray jArrayPhoto = oneObject.optJSONArray("photos");
                JSONObject geometry =  oneObject.getJSONObject("geometry");
                JSONObject loc = geometry.getJSONObject("location");
                Location location = new Location("POI");
                location.setLatitude(loc.getDouble("lat"));
                location.setLongitude(loc.getDouble("lng"));
                String photoURL;
                if(jArrayPhoto != null){
                    String photoReference = jArrayPhoto.getJSONObject(0).getString("photo_reference");
                    photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+
                            photoReference +"&key="+Constants.API_KEY;
                }else {
                    photoURL = "https://image.freepik.com/free-icon/placeholder-on-map-paper-in-perspective_318-61698.jpg";
                }

                Instance.getInstance().addPOI(new POI(name, address, photoURL, location));

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
                createFences();
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


    public void getWeather(){

        checkLocationPermission();
        Awareness.SnapshotApi.getWeather(client)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        if (!weatherResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get weather.");
                            return;
                        }
                        Weather weather = weatherResult.getWeather();
                        setTemperature(weather);
                        showDialog("It seems that the weather is " +
                                weatherConditionToString(weather.getConditions()[0]) +
                                "\nI will suggest you POI based on this condition");
                    }
                });

    }

    public void setTemperature(Weather temp){
        this.weather = temp;
    }

    private void showDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information");
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

    public String weatherConditionToString(int id){

        switch (id){
            case 0:
                return "unknown";
            case 1:
                return "clear";
            case 2:
                return "cloudy";
            case 3:
                return "foggy";
            case 4:
                return "hazy";
            case 5:
                return "icy";
            case 6:
                return "rainy";
            case 7:
                return "snowy";
            case 8:
                return "stormy";
            case 9:
                return "windy";
            default:
                return "eheh";

        }
    }

    private static boolean isDay(){
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int nightHourStart = 7, nightHourEnd = 19;
        return (now.get(Calendar.HOUR_OF_DAY) >= nightHourStart) && (now.get(Calendar.HOUR_OF_DAY) < nightHourEnd);
    }

    private static boolean isNightEarly(){
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int nightHourStart = 20, nightHourEnd = 0;
        return (now.get(Calendar.HOUR_OF_DAY) >= nightHourStart) || (now.get(Calendar.HOUR_OF_DAY) < nightHourEnd);
    }

    private static boolean isNightLate(){
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int nightHourStart = 0, nightHourEnd = 6;
        return (now.get(Calendar.HOUR_OF_DAY) >= nightHourStart) && (now.get(Calendar.HOUR_OF_DAY) < nightHourEnd);
    }


    public void createFences(){
        checkLocationPermission();
        for (POI p: Instance.getInstance().getPOIList()){
            AwarenessFence fence = LocationFence.in(p.getLocation().getLatitude(), p.getLocation().getLongitude(), Constants.DISTANCE_TO_POI, 1);
            Instance.getInstance().getFences().add(fence);
            Intent intent = new Intent(p.getName());
            PendingIntent myPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            registerFence(fence, p.getName(), myPendingIntent);
            MyFenceReceiver myFenceReceiver = new MyFenceReceiver();
            getActivity().registerReceiver(myFenceReceiver, new IntentFilter(p.getName()));
            System.out.println("entrei aqui");
        }
        System.out.println("OKKK");
    }

    public void registerFence(AwarenessFence fence, String nameKey, PendingIntent pendingIntent){

        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(nameKey, fence, pendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Fence was successfully registered.");
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
                        }
                    }
                });

    }

    class MyFenceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FenceState fenceState = FenceState.extract(intent);
            for (POI p : Instance.getInstance().getPOIList()){
                if (TextUtils.equals(fenceState.getFenceKey(), p.getName())) {
                    System.out.println("ENTREI NA CLASS DA FENCE");

                    switch (fenceState.getCurrentState()) {
                        case FenceState.TRUE:
                            Log.i(TAG, "You are in "+Constants.DISTANCE_TO_POI+ "m radius of " + p.getName());
                            showDialog("You are in "+Constants.DISTANCE_TO_POI +"m radius of "+ p.getName());
                            break;
                        case FenceState.FALSE:
                            Log.i(TAG, "You are NOT in " +Constants.DISTANCE_TO_POI+ "m radius of " + p.getName());
                            showDialog("You are NOT in " + Constants.DISTANCE_TO_POI + "m radius of " + p.getName());
                            break;
                        case FenceState.UNKNOWN:
                            Log.i(TAG, "unknown state.");
                            break;
                    }
                }
            }
        }
    }

}
