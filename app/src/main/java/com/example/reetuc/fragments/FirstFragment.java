package com.example.reetuc.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.reetuc.App;
import com.example.reetuc.R;
import com.example.reetuc.dialogs.SetDateDialogFragment;
import com.example.reetuc.dialogs.SetTimeDialogFragment;
import com.example.reetuc.models.ServerResponse;
import com.example.reetuc.network.ApiConfig;
import com.example.reetuc.network.ClientService;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

public class FirstFragment extends Fragment implements SetTimeDialogFragment.DialogListener,SetDateDialogFragment.DialogListener {

private static final String TAG = FirstFragment.class.getSimpleName();







  private RadioGroup radioSexGroup;
  private RadioButton radioSexFemale;
  private RadioButton radioSexMale;

  private TextInputLayout userNameLayout,emailLayout,phoneLayout,locationLayout,passwordLayout,passwordConfirmLayout;
  private EditText userName ,email ,phone   ,password ,passwordConfirm;
  private AutoCompleteTextView location;

  private Button setTime, setDate;
  private FloatingActionButton uploadBtn;
  private ImageView userImage;
  private int selectedId =-1;

  private  String filePath="";
  private ArrayList<String> permissionsToRequest;
  private ArrayList<String> permissionsRejected = new ArrayList<>();
  private ArrayList<String> permissions = new ArrayList<>();

  private final static int ALL_PERMISSIONS_RESULT = 107;
  private final static int IMAGE_RESULT = 200;
  private String selectedPlace;
  AutocompleteSupportFragment autocompleteFragment;
  View view;

  Uri picUri;


  public static final int REQUEST_IMAGE_CAPTURE = 0;
  public static final int REQUEST_GALLERY_IMAGE = 1;
  String[] locationList={"Ara","Baruni","Begusarai","Bettiah","Bhagalpur","Bihar Sharif","Bodh Gaya","Buxar","Chapra","Darbhanga","Dehri","Dinapur Nizamat","Gaya","Hajipur","Jamalpur","Katihar","Madhubani","Motihari","Munger","Muzaffarpur","Patna","Purnia","Pusa","Saharsa","Samastipur","Sasaram","Sitamarhi","Siwan","Chandi"};


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_first, container, false);
    return view ;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    uploadBtn = view.findViewById(R.id.floatingActionButton);
    uploadBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
      }
    });
    userImage = view.findViewById(R.id.imageview_account_profile);
    permissions.add(CAMERA);
    permissions.add(WRITE_EXTERNAL_STORAGE);
    permissions.add(READ_EXTERNAL_STORAGE);
    permissionsToRequest = findUnAskedPermissions(permissions);



    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


      if (permissionsToRequest.size() > 0)
        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
    }



    userName = view.findViewById(R.id.userName);
    userNameLayout = view.findViewById(R.id.userNameLayout);

    email = view.findViewById(R.id.email);
    emailLayout = view.findViewById(R.id.emailLayout);

    phone= view.findViewById(R.id.phone);
    phoneLayout = view.findViewById(R.id.phoneLayout);


    location = view.findViewById(R.id.location);
    ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,locationList);
    location.setAdapter(adapter);
    location.setThreshold(1);
    locationLayout = view.findViewById(R.id.locationLayout);

    PlacesClient placesClient = Places.createClient(getContext());
     autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
    autocompleteFragment.setHint("Search Location");


    // Set up a PlaceSelectionListener to handle the response.
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(@NotNull Place place) {
        // TODO: Get info about the selected place.
        selectedPlace = place.getName();

        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
      }


      @Override
      public void onError(@NotNull Status status) {
        // TODO: Handle the error.
        selectedPlace="";
        Log.i(TAG, "An error occurred: " + status);
      }
    });
/**/

    password= view.findViewById(R.id.password);
    passwordLayout = view.findViewById(R.id.passwordLayout);

    passwordConfirm= view.findViewById(R.id.passwordConfirm);
    passwordConfirmLayout = view.findViewById(R.id.passwordConfirmLayout);

    radioSexFemale = view.findViewById(R.id.radioFemale);
    radioSexMale = view.findViewById(R.id.radioMale);
    radioSexGroup = view.findViewById(R.id.radioSex);


    radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int selectedRadio) {
        selectedId =selectedRadio;
        radioSexFemale.setError(null);
        radioSexMale.setError(null);

      }
    });

    setTime= view.findViewById(R.id.set_time);
    setTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


        SetTimeDialogFragment dialogFragment = new SetTimeDialogFragment();
        dialogFragment.setTargetFragment(FirstFragment.this, 0);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
          ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");

      }
    });


    setDate= view.findViewById(R.id.set_date);
    setDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SetDateDialogFragment dialogFragment = new SetDateDialogFragment();
        dialogFragment.setTargetFragment(FirstFragment.this, 0);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
          ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
      }
    });


    view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (hasError()) {
          File file = new File(filePath);
          RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
          MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", file.getName(), requestFile);

          RequestBody textSample = RequestBody.create(MediaType.parse("text/plain"), userName.getText().toString());

          Call<ServerResponse> call = App.getEndPoints().uploadFile(4,body, textSample);
          call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
              ServerResponse res = response.body();
              Log.e(TAG, response.errorBody()+"");
              NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            Log.e(TAG, t.getLocalizedMessage());

            }
          });

        }
        //else{
          //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
       // }

      }
    });
  }

  @Override
  public void onFinishEditDialog(String inputText) {
    setTime.setText(inputText);
    setTime.setError(null);
  }

  @Override
  public void onFinishEditDateDialog(String inputText) {
    setDate.setText(inputText);
    setDate.setError(null);
  }



  public Intent getPickImageChooserIntent() {

    Uri outputFileUri = getCaptureImageOutputUri();

    List<Intent> allIntents = new ArrayList<>();
    PackageManager packageManager = getActivity().getPackageManager();

    Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
    for (ResolveInfo res : listCam) {
      Intent intent = new Intent(captureIntent);
      intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
      intent.setPackage(res.activityInfo.packageName);
      if (outputFileUri != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
      }
      allIntents.add(intent);
    }

    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
    galleryIntent.setType("image/*");
    List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
    for (ResolveInfo res : listGallery) {
      Intent intent = new Intent(galleryIntent);
      intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
      intent.setPackage(res.activityInfo.packageName);
      allIntents.add(intent);
    }

    Intent mainIntent = allIntents.get(allIntents.size() - 1);
    for (Intent intent : allIntents) {
      if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
        mainIntent = intent;
        break;
      }
    }
    allIntents.remove(mainIntent);

    Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

    return chooserIntent;
  }


  private Uri getCaptureImageOutputUri() {
    Uri outputFileUri = null;
    File getImage = getActivity().getExternalFilesDir("");
    if (getImage != null) {
      outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
    }
    return outputFileUri;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {


    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_RESULT) {

        filePath = getImageFilePath(data);
        if (filePath != null) {
          Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
          userImage.setImageBitmap(selectedImage);
        }
      }

    }

  }


  private String getImageFromFilePath(Intent data) {
    boolean isCamera = data == null || data.getData() == null;

    if (isCamera) return getCaptureImageOutputUri().getPath();
    else return getPathFromURI(data.getData());

  }

  public String getImageFilePath(Intent data) {
    return getImageFromFilePath(data);
  }

  private String getPathFromURI(Uri contentUri) {
    String[] proj = {MediaStore.Audio.Media.DATA};
    Cursor cursor =getActivity(). getContentResolver().query(contentUri, proj, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
  }



  private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
    ArrayList<String> result = new ArrayList<String>();

    for (String perm : wanted) {
      if (!hasPermission(perm)) {
        result.add(perm);
      }
    }

    return result;
  }

  private boolean hasPermission(String permission) {
    if (canMakeSmores()) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
      }
    }
    return true;
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(getContext())
      .setMessage(message)
      .setPositiveButton("OK", okListener)
      .setNegativeButton("Cancel", null)
      .create()
      .show();
  }

  private boolean canMakeSmores() {
    return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    switch (requestCode) {

      case ALL_PERMISSIONS_RESULT:
        for (String perms : permissionsToRequest) {
          if (!hasPermission(perms)) {
            permissionsRejected.add(perms);
          }
        }

        if (permissionsRejected.size() > 0) {



          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
              showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                      requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                    }
                  }
                });
              return;
            }
          }

        }

        break;
    }

  }
private boolean hasError(){
Log.i(TAG,"validation");
  String inputName =userName.getText().toString();
  if(inputName.isEmpty()|| inputName.length()<3 ){
    userNameLayout.setError("Invalid User Name");
    return  false;
  }else{

    userNameLayout.setError(null);
  }
  Log.i(TAG," pass User Name");

  String inputEmail =email.getText().toString();
  if(inputEmail.isEmpty()|| (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches())){
    emailLayout.setError("Invalid Email");
    return  false;
  }else{

    emailLayout.setError(null);
  }
  Log.i(TAG," pass email");
  String inputPhone =phone.getText().toString();
  if(inputPhone.isEmpty() ){
    phoneLayout.setError("Invalid Phone");
    return  false;
  }else{

    phoneLayout.setError(null);
  }
  Log.i(TAG," pass phone ");

  if(selectedId==-1){
    radioSexFemale.setError("Please Select Item");
    radioSexMale.setError("Please Select Item");
    passwordConfirmLayout.setError("password does not match ");
    return  false;
  }else{
    radioSexFemale.setError(null);
    radioSexMale.setError(null);

  }
  Log.i(TAG," pass gender ");

  if(setDate.getText().toString().equalsIgnoreCase("set date")){
    setDate.setError("Please Set Date");
    return  false;
  }else{
    setDate.setError(null);
  }
  Log.i(TAG," pass date");
  if(setTime.getText().toString().equalsIgnoreCase("set time")){
    setTime.setError("Please Set Time");
  }else{
    setTime.setError(null);
  }

  Log.i(TAG,"pass time ");
  String inputlocation =selectedPlace;
  if(inputlocation.isEmpty() ){


    Snackbar snackbar = Snackbar.make(view, "Please enter location", Snackbar.LENGTH_LONG)
      .setAction("Action", null);
    View sbView = snackbar.getView();
    sbView.setBackgroundColor(Color.BLACK);
    snackbar.show();

    return  false;
  }else{

    locationLayout.setError(null);

  }
  /*Log.i(TAG,"pass location");*/



  String inputPassword =password.getText().toString();
  if(inputPassword.isEmpty()|| inputPassword.length()<6 ){
    passwordLayout.setError("Invalid password");
    return  false;
  }else{
    passwordLayout.setError(null);

  }
  Log.i(TAG," pass password");


  String inputPasswordConfirm =passwordConfirm.getText().toString();
  if(!inputPasswordConfirm.equals(inputPassword)){
    passwordConfirmLayout.setError("password does not match ");
    return  false;
  }else{
    passwordConfirmLayout.setError(null);
  }
  Log.i(TAG," end validation");

    return true;

}
public boolean validLocation(String inputlocation){
  for (String loca : locationList) {
    if(loca.equalsIgnoreCase(inputlocation)){
      locationLayout.setError(null);
      return true;
    }
  }
    return false;
}

}
