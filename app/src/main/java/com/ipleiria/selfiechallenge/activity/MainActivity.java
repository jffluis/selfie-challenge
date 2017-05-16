package com.ipleiria.selfiechallenge.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipleiria.selfiechallenge.Instance;
import com.ipleiria.selfiechallenge.fragments.LeaderboardFragment;
import com.ipleiria.selfiechallenge.fragments.PlacesAPIFragment;
import com.ipleiria.selfiechallenge.utils.Firebase;
import com.ipleiria.selfiechallenge.utils.PhotoUtil;
import com.ipleiria.selfiechallenge.utils.PackageManagerUtils;
import com.ipleiria.selfiechallenge.utils.PermissionUtils;
import com.ipleiria.selfiechallenge.R;
import com.ipleiria.selfiechallenge.fragments.ChooseChallengeFragment;
import com.ipleiria.selfiechallenge.fragments.StartFragment;
import com.ipleiria.selfiechallenge.fragments.TesteAPICloudVisionFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAJqwtryXbw6JwU97MskQbTTpzx_UiiiTU";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    private ProgressDialog progressDialog;
    private boolean isAccepted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__drawer);


        //showDialog();

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.contentContainer, StartFragment.newInstance(0)).addToBackStack(null).commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*new AsyncTask<Void,String,Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... params) {
                try
                {
                    URL url = new URL(Instance.getInstance().getUrlPhoto());
                    InputStream is = new BufferedInputStream(url.openStream());
                    b = BitmapFactory.decodeStream(is);
                } catch(Exception e)
                {
                    e.printStackTrace();
                }

                return b;
            }
            protected void onPostExecute(Bitmap result){

                fotoImageView.setImageBitmap(result);
            }

        }.execute();*/

    }

    @Override
    public void onBackPressed() {
        Log.d("VALOOOOOOOOR:", String.valueOf(getFragmentManager().getBackStackEntryCount()));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {
           super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity__drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

       Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragment = ChooseChallengeFragment.newInstance(0);
        } else if (id == R.id.nav_gallery) {
            fragment = TesteAPICloudVisionFragment.newInstance(0);
        } else if (id == R.id.nav_slideshow) {
            fragment = LeaderboardFragment.newInstance(0);
        }else if (id == R.id.nav_manage) {
            fragment = PlacesAPIFragment.newInstance(0);
        }


        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                R.anim.fade_out).replace(R.id.contentContainer, fragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

   //     if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            //uploadImage(data.getData());

        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +
                    ".provider", PhotoUtil.getCameraFile(this));

            //se passar no cloud vision api adiciona ao firebase e à aplicação


            try {
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri),
                                1200);

                //Instance.getInstance().getCurrentChallenge().addPhoto(bitmap);
                callCloudVision(bitmap);

                //System.out.println(Instance.getInstance().getCurrentChallenge().getName());

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    //startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);



            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private boolean callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Analysing Selfie..");
        progressDialog.show();

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform PhotoUtil key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(5);
                            add(labelDetection);

                            Feature webDetection = new Feature();
                            webDetection.setType("WEB_DETECTION");
                            webDetection.setMaxResults(5);
                            add(webDetection);

                            Feature logoDetection = new Feature();
                            logoDetection.setType("LOGO_DETECTION");
                            logoDetection.setMaxResults(5);
                            add(logoDetection);

                            Feature faceDetection = new Feature();
                            faceDetection.setType("FACE_DETECTION");
                            faceDetection.setMaxResults(5);
                            add(faceDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision PhotoUtil containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make PhotoUtil request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make PhotoUtil request because of other IOException " +
                            e.getMessage());
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                isAccepted = aBoolean;
                if(isAccepted){
                    uploadFileToFirebase(bitmap);
                }else{
                    Toast.makeText(getApplicationContext(), "Not valid :(", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();

        return false;
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

    private boolean convertResponseToString(BatchAnnotateImagesResponse response) {

        List<String> entireResponse = new ArrayList<String>();

        String message = "";
        List<EntityAnnotation> annotations;
        AnnotateImageResponse annotateImageResponse = response.getResponses().get(0);

        message += "\n___\n Web Detection:\n\n";
        Object webDetection = annotateImageResponse.get("webDetection");
        if (webDetection != null) {
            ArrayMap webDetectionAnnotations = (ArrayMap) webDetection;

            Object webEntities = webDetectionAnnotations.get("webEntities");
            if (webEntities != null) {
                ArrayList<ArrayMap> webEntitiesList = (ArrayList<ArrayMap>) webEntities;

                for (ArrayMap webEntity :
                        webEntitiesList) {
                    message += String.format(Locale.US, "%s - score:%s id:%s", webEntity.get("description"), webEntity.get("score"), webEntity.get("entityId"));
                    message += "\n";

                    entireResponse.add((String) webEntity.get("description"));
                }

            }
        }

        message += "\n___\n Labels:\n\n";
        annotations = annotateImageResponse.getLabelAnnotations();
        if (annotations != null) {
            for (EntityAnnotation annotation : annotations) {
                message += String.format(Locale.US, "%.3f: %s", annotation.getScore(), annotation.getDescription());
                message += "\n";

                entireResponse.add(annotation.getDescription());
            }
        } else {
            message += "nothing";
        }

        message += "\n___\n Text:\n\n";
        annotations = annotateImageResponse.getTextAnnotations();
        if (annotations != null) {
            for (EntityAnnotation annotation : annotations) {
                message += String.format(Locale.US, "%.3f: %s", annotation.getScore(), annotation.getDescription());
                message += "\n";

                entireResponse.add(annotation.getDescription());
            }
        } else {
            message += "nothing";
        }

        message += "\n___\n Landmarks:\n\n";
        annotations = annotateImageResponse.getLandmarkAnnotations();
        if (annotations != null) {
            for (EntityAnnotation annotation : annotations) {
                message += String.format(Locale.US, "%.3f: %s - %s", annotation.getScore(), annotation.getDescription(), annotation.getLocations());
                message += "\n";

                entireResponse.add(annotation.getDescription());
            }
        } else {
            message += "nothing";
        }

        message += "\n___\n Logos:\n\n";
        annotations = annotateImageResponse.getLogoAnnotations();
        if (annotations != null) {
            for (EntityAnnotation annotation : annotations) {
                message += String.format(Locale.US, "%.3f: %s", annotation.getScore(), annotation.getDescription());
                message += "\n";

                entireResponse.add(annotation.getDescription());
            }
        } else {
            message += "nothing";
        }

        message += "\n___\n Faces:\n\n";
        List<FaceAnnotation> faceAnnotations = annotateImageResponse.getFaceAnnotations();
        if (faceAnnotations != null) {
            for (FaceAnnotation annotation : faceAnnotations) {
                message += String.format(Locale.US, "position: %s anger:%s joy:%s surprise:%s headwear:%s",
                        annotation.getBoundingPoly(),
                        annotation.getAngerLikelihood(),
                        annotation.getJoyLikelihood(),
                        annotation.getSurpriseLikelihood(),
                        annotation.getHeadwearLikelihood());
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        message += "\n___\n Image Properties:\n\n";
        ImageProperties imagePropertiesAnnotation = annotateImageResponse.getImagePropertiesAnnotation();
        if (imagePropertiesAnnotation != null) {
            message += String.format(Locale.US, "%s", imagePropertiesAnnotation.getDominantColors());
            message += "\n";
        } else {
            message += "nothing";
        }

        String challengeName = (Instance.getInstance().getCurrentChallenge().getName());
        String lastChallengeName = (challengeName.substring(challengeName.lastIndexOf(" ") + 1));

        System.out.println("Cloud vision:  entireResponse");
        System.out.println(entireResponse);

        for(String res: entireResponse){
            if (res != null) {
                if (lastChallengeName.toLowerCase().contains(res.toLowerCase())) {
                    progressDialog.dismiss();
                    return true;
                }
            }
        }

        progressDialog.dismiss();
        return false;
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error");
        builder.setMessage("The picture is not related to the challenge");
        builder.setIcon(R.mipmap.ic_launcher);

        String positiveText = "Try again";
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


    private void uploadFileToFirebase(Bitmap bitmap) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading photo..");
        progressDialog.show();
        Random random = new Random();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final String photoURL = "images/"+Instance.getInstance().getCurrentChallenge().getId()+"/"+System.currentTimeMillis()+ random.nextInt(300)+".jpg";

        StorageReference storageRef = storage.getReferenceFromUrl(Firebase.StorageURL);
        StorageReference mountainImagesRef = storageRef.child(photoURL);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Random random = new Random();
                DatabaseReference objRef = Firebase.dbUserChallenges.child(Instance.getInstance().getCurrentChallenge().getId());
                DatabaseReference photosRef = objRef.child("photos").child(Instance.getInstance().getCurrentUser().getId());
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                photosRef.setValue(String.valueOf(downloadUrl));
                progressDialog.dismiss();
                Log.d("downloadUrl-->", "" + downloadUrl);
            }
        });

    }

}
