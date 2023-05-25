package edu.uw.tcss450.jrdeal.lab1myfirstapp.ui;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.jrdeal.lab1myfirstapp.databinding.FragmentSecondBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {
    private FragmentSecondBinding mBinding;

    FusedLocationProviderClient flpc;
    private final static int REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instantiate the Binding object and Inflate the layout for this fragment
        mBinding = FragmentSecondBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestLocationUpdates();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
        }

        // Get a reference to the SafeArgs object.
        SecondFragmentArgs args = SecondFragmentArgs.fromBundle(getArguments());

        // Set the text color of the label. NOTE no need to cast.
        mBinding.textMessage.setText(args.getMessage());

        flpc = LocationServices.getFusedLocationProviderClient(getActivity());

        mBinding.getLocationBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    private void requestLocationUpdates() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
//            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
            return;
        }

        flpc.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        }).addOnSuccessListener(location -> {
            Log.wtf("uwu", "success, location: " + location);
            if (location != null) {
                Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Log.wtf("uwu", "setting bindings");
                    mBinding.latitude.setText("Latitude: " + addresses.get(0).getLatitude());
                    mBinding.longitude.setText("Longitude: " + addresses.get(0).getLongitude());
                    mBinding.address.setText("Address: " + addresses.get(0).getAddressLine(0));
                    mBinding.city.setText("City: " + addresses.get(0).getLocality());
                    mBinding.country.setText("Country: " + addresses.get(0).getCountryName());
                    Log.wtf("uwu", "bindings set");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.wtf("uwu", "permission check failed");
            askPermission();
        } else {
            Log.wtf("uwu", "attempting to access location");
            flpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.wtf("uwu", "success, location: " + location);
                    if (location != null) {
                        Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                        try {
                            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Log.wtf("uwu", "setting bindings");
                            mBinding.latitude.setText("Latitude: " + addresses.get(0).getLatitude());
                            mBinding.longitude.setText("Longitude: " + addresses.get(0).getLongitude());
                            mBinding.address.setText("Address: " + addresses.get(0).getAddressLine(0));
                            mBinding.city.setText("City: " + addresses.get(0).getLocality());
                            mBinding.country.setText("Country: " + addresses.get(0).getCountryName());
                            Log.wtf("uwu", "bindings set");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
                getCurrentLocation();
            } else {
                Log.wtf("uwu", "permission request failed");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}