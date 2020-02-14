package com.app.technofunda.shopping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.ethiqs.frontview;
import com.app.technofunda.util.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CurrentLocation extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    EditText full_address, locality_address, locality_pincode;
    Button getlocation, continuse_address;
    EditText et_amc_purchase_date;
    private Calendar calendar;
    public int year, month, day;
    List<String> select_available;
    List<String> select_mypayment;
    Spinner invoice_available,select_payment_mode;
    SharedPreferences pref;
   // TextView purchasePoint,redeemPoint;
    String addressProcessingUrl,stringResponce2,redeemPointUrl, stringResponce,str_select_payment_mode,str_select_payment_mode2,str_purchasePoint,purchasePointUrl;
    private ProgressDialog pDialog;
    String str_redeemPoint;
    String str_amc_purchase_date, str_amc_purchase_date2, str_amc_select_time, str_amc_select_time2, addressProcessing, user_id, user_mobile, password;
    String str_full_address,stringResponce3, str_full_address2, str_locality_address, str_locality_address2, str_locality_pincode, str_locality_pincode2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        full_address = (EditText) findViewById(R.id.full_address);
        locality_address = (EditText) findViewById(R.id.locality_address);
        locality_pincode = (EditText) findViewById(R.id.locality_pincode);
        getlocation = (Button) findViewById(R.id.getlocation);
        continuse_address = (Button) findViewById(R.id.continuse_address);
        et_amc_purchase_date = (EditText) findViewById(R.id.et_amc_purchase_date);
     //   invoice_available = (Spinner) findViewById(R.id.invoice_available);
        select_payment_mode=(Spinner)findViewById(R.id.select_payment_mode);
      //  purchasePoint = (TextView) findViewById(R.id.purchasePoint);
      //  redeemPoint = (TextView) findViewById(R.id.redeemPoint);

//        GpsTrack gt = new GpsTrack(getApplicationContext());
//        Location l = gt.getLocation();
//        if (l == null) {
//            //loading = ProgressDialog.show(MainActivity.this,"Loading Data", "Please wait...",false,false);
//            Toast.makeText(getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
//        } else {
//            turnGPSOn(); // method to turn on the GPS if its in off state.
//            getMyCurrentLocation();//this is
//        }

        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            user_id = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            user_mobile = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            password = pref.getString(Config.KEY_PASSWORD, "");
        }


        calendar = Calendar.getInstance();
        // String.format(Locale.US,"%tB",calendar);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

//        getMyCurrentLocation();
//        getlocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GpsTrack gt = new GpsTrack(getApplicationContext());
//                Location l = gt.getLocation();
//                if (l == null) {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLocation.this);
//                    builder.setTitle("Please Unable GPS");
//                    builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                            //init();
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.setCancelable(false);
//                    builder.show();
//                    //loading = ProgressDialog.show(MainActivity.this,"Loading Data", "Please wait...",false,false);
//                    // Toast.makeText(getApplicationContext(),"GPS Unable to Get Value", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    getMyCurrentLocation();
//                }
//            }
//        });


        select_available = new ArrayList<String>();
        select_available.add("Select Time Schedule");
        select_available.add("09:00AM");
        select_available.add("09:30AM");
        select_available.add("10:00AM");
        select_available.add("10:30AM");
        select_available.add("11:00AM");
        select_available.add("11:30AM");
        select_available.add("12:00PM");
        select_available.add("12:30PM");
        select_available.add("01:00PM");
        select_available.add("01:30PM");
        select_available.add("02:00PM");
        select_available.add("02:30PM");
        select_available.add("03:00PM");
        select_available.add("03:30PM");
        select_available.add("04:00PM");
        select_available.add("04:30PM");
        select_available.add("05:00PM");
        select_available.add("05:30PM");
        select_available.add("06:00PM");
        ArrayAdapter<String> stateAdapter3 = new ArrayAdapter<String>(CurrentLocation.this, android.R.layout.simple_spinner_item, select_available);
        stateAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
   //     invoice_available.setAdapter(stateAdapter3);


        select_mypayment = new ArrayList<String>();
        select_mypayment.add("Select Payment Mode");
        select_mypayment.add("ByCash");
        select_mypayment.add("MyWallet");
        select_mypayment.add("Paytm");
        select_mypayment.add("NetBanking");

        ArrayAdapter<String> stateAdapter4 = new ArrayAdapter<String>(CurrentLocation.this, android.R.layout.simple_spinner_item, select_mypayment);
        stateAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_payment_mode.setAdapter(stateAdapter4);



//        str_purchasePoint = "PurchasePoint";
//        purchasePointUrl = Config.URL + "/api/customer.aspx?Mobile=" + String.valueOf(user_mobile) + "&pass=" + String.valueOf(password) + "&msg=" + String.valueOf(str_purchasePoint) + "%20" + String.valueOf(user_mobile);
//        DownloadWebPageTask2 task2 = new DownloadWebPageTask2();
//        task2.execute(new String[]{String.valueOf(purchasePointUrl)});
//        pDialog = new ProgressDialog(CurrentLocation.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();


     //   str_redeemPoint = "RedeemPoint";
    //    redeemPointUrl = Config.URL + "/api/customer.aspx?Mobile=" + String.valueOf(user_mobile) + "&pass=" + String.valueOf(password) + "&msg=" + String.valueOf(str_redeemPoint) + "%20" + String.valueOf(user_mobile);
    //    DownloadWebPageTask3 task3 = new DownloadWebPageTask3();
     //   task3.execute(new String[]{String.valueOf(redeemPointUrl)});

        continuse_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_full_address = full_address.getText().toString().trim();
                str_locality_address = locality_address.getText().toString().trim();
                str_locality_pincode = locality_pincode.getText().toString().trim();
                str_amc_purchase_date = et_amc_purchase_date.getText().toString().trim();
             //   str_amc_select_time = invoice_available.getSelectedItem().toString().trim();
                str_select_payment_mode = select_payment_mode.getSelectedItem().toString().trim();
                boolean check = true;
                Validate validate = new Validate();

                if (validate.isNotEmpty(str_full_address)) {
                    str_full_address = full_address.getText().toString().trim();
                    str_full_address2 = str_full_address.replaceAll(" ", "-");
                } else {
                    str_full_address = "";
                    check = false;
                    full_address.setError("Please Full Address");
                }

                if (validate.isNotEmpty(str_locality_address)) {
                    str_locality_address = locality_address.getText().toString().trim();
                    str_locality_address2 = str_locality_address.replaceAll(" ", "-");
                } else {
                    str_locality_address = "";
                    check = false;
                    locality_address.setError("Please Enter Locality");
                }
                if (validate.isNotEmpty(str_locality_pincode)) {
                    str_locality_pincode = locality_pincode.getText().toString().trim();
                    str_locality_pincode2 = str_locality_pincode.replaceAll(" ", "-");
                } else {
                    str_locality_pincode = "";
                    check = false;
                    locality_pincode.setError("Please Enter Pin Code");
                }
                if (validate.isNotEmpty(str_amc_purchase_date)) {
                    str_amc_purchase_date = et_amc_purchase_date.getText().toString().trim();
                    str_amc_purchase_date2 = str_amc_purchase_date.replaceAll("/", "-");
                } else {
                    str_amc_purchase_date = "";
                    check = false;
                    et_amc_purchase_date.setError("Please Select Date");
                }
//                str_amc_select_time = invoice_available.getSelectedItem().toString().trim();
//                if (str_amc_select_time.equals("Select Time")) {
//                    Toast.makeText(CurrentLocation.this, "Plz Select Time", Toast.LENGTH_LONG).show();
//                    check = false;
//                } else {
//                    str_amc_select_time2 = str_amc_select_time.replaceAll(" ", "-");
//
//                }
                str_select_payment_mode = select_payment_mode.getSelectedItem().toString().trim();
                if (str_select_payment_mode.equals("Select Payment Mode")) {
                    Toast.makeText(CurrentLocation.this, "Plz Select Payment Mode", Toast.LENGTH_LONG).show();
                    check = false;
                } else {
                    str_select_payment_mode2 = str_select_payment_mode.replaceAll(" ", "-");

                }

                if (check) {
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        addressProcessing = "PlaceOrder";
                        addressProcessingUrl = Config.URL + "/api/customer.aspx?Mobile=" + String.valueOf(user_mobile) + "&pass=" + String.valueOf(password) + "&msg=" + String.valueOf(addressProcessing) + "%20" + String.valueOf(user_mobile) + "%20" + String.valueOf(str_full_address2) + "%20" + String.valueOf(str_locality_address2) + "%20" + String.valueOf(str_locality_pincode2) + "%20" + String.valueOf(str_amc_purchase_date2) + "%20" + String.valueOf(str_amc_select_time2)+ "%20" + String.valueOf(str_select_payment_mode2);
                        DownloadWebPageTask task1 = new DownloadWebPageTask();
                        task1.execute(new String[]{String.valueOf(addressProcessingUrl)});
                        pDialog = new ProgressDialog(CurrentLocation.this);
                        pDialog.setMessage("Please Wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        //  Toast.makeText(NewRegistration.this,""+url,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    /**
     * Method to turn on GPS
     **/
    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivity(intent);
            if (!provider.contains("gps")) {//if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {
        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }

    /**
     * Check the type of GPS Provider available at that instance and
     * collect the location informations
     *
     * @Output Latitude and Longitude
     */
    void getMyCurrentLocation() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new CurrentLocation.MyLocationListener();
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;
        try {

            if (gps_enabled) {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

            }


            if (gps_enabled) {
                location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            }


            if (network_enabled && location == null) {
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

            }


            if (network_enabled && location == null) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }

            if (location != null) {

                MyLat = location.getLatitude();
                MyLong = location.getLongitude();


            } else {
                Location loc = getLastKnownLocation(this);
                if (loc != null) {

                    MyLat = loc.getLatitude();
                    MyLong = loc.getLongitude();


                }
            }
            locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        try {
// Getting address from found locations.
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            // StateName= addresses.get(0).getAdminArea();
            knownName = addresses.get(0).getSubLocality();
            Area = addresses.get(0).getAddressLine(0);
            CityName = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            CountryName = addresses.get(0).getCountryName();
            //address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            postalCode = addresses.get(0).getPostalCode();
            // you can get more details other than this . like country code, state code, etc.
            System.out.println(" Areaname " + Area);
            System.out.println(" CityName " + CityName);
            System.out.println(" CountryName " + CountryName);
            System.out.println(" StateName " + state);
            System.out.println(" address " + address);
            System.out.println(" postal code " + postalCode);
            System.out.println(" know name " + knownName);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* textView2.setText(""+MyLat);
        textView3.setText(""+MyLong);
        textView1.setText( " "+knownName+","+Area +"\n," + CityName  +","+postalCode);*/
        full_address.setText(knownName + "," + Area + "," + CityName + "," + postalCode);
        locality_address.setText("" + knownName + "," + CityName);
        locality_pincode.setText(postalCode);
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String Area = "";
    String CountryName = "";

    String state;
    String address; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String postalCode;
    String knownName;

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context) {
        Location location = null;
        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        try {
            do {
                //System.out.println("---------------------------------------------------------------------");
                if (!iterator.hasNext())
                    break;
                String s = (String) iterator.next();
                //if(i != 0 && !locationmanager.isProviderEnabled(s))
                if (i != false && !locationmanager.isProviderEnabled(s))
                    continue;
                // System.out.println("provider ===> "+s);
                Location location1 = locationmanager.getLastKnownLocation(s);
                if (location1 == null)
                    continue;
                if (location != null) {
                    //System.out.println("location ===> "+location);
                    //System.out.println("location1 ===> "+location);
                    float f = location.getAccuracy();
                    float f1 = location1.getAccuracy();
                    if (f >= f1) {
                        long l = location1.getTime();
                        long l1 = location.getTime();
                        if (l - l1 <= 600000L)
                            continue;
                    }
                }
                location = location1;
                // System.out.println("location  out ===> "+location);
                //System.out.println("location1 out===> "+location);
                i = locationmanager.isProviderEnabled(s);
                // System.out.println("---------------------------------------------------------------------");
            } while (true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CurrentLocation.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is Working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CurrentLocation.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        et_amc_purchase_date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpGet = new HttpPost(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        //   Toast.makeText(HomeRegistration.this,""+content,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            stringResponce = Html.fromHtml(result).toString().trim();
            if (pDialog.isShowing())
                pDialog.dismiss();
            // Toast.makeText(NewRegistration.this,""+result,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder2 = new AlertDialog.Builder(CurrentLocation.this);
            builder2.setTitle("New Appointment");
            builder2.setMessage(String.valueOf(stringResponce).toString().trim());
            // Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_LONG).show();
            builder2.setCancelable(false);
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (stringResponce.equals("Successful")) {
                        dialog.cancel();
                        finish();
                        Intent intent = new Intent(CurrentLocation.this, frontview.class);
                        startActivity(intent);
                    } else {
                        dialog.cancel();
                    }
                }
            });
            builder2.show();
        }
    }
    public class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpGet = new HttpPost(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        //   Toast.makeText(HomeRegistration.this,""+content,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            stringResponce2 = Html.fromHtml(result).toString().trim();
            if (pDialog.isShowing())
                pDialog.dismiss();
         //   purchasePoint.setText("Purchase Point\n"+stringResponce2);
          /*  // Toast.makeText(NewRegistration.this,""+result,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder2 = new AlertDialog.Builder(CurrentLocation.this);
            builder2.setTitle("New Appointment");
            builder2.setMessage(String.valueOf(stringResponce2).toString().trim());
            // Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_LONG).show();
            builder2.setCancelable(false);
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (stringResponce2.equals("Successful")) {
                        dialog.cancel();
                        finish();
                        Intent intent = new Intent(CurrentLocation.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        dialog.cancel();
                    }
                }
            });
            builder2.show();
        }*/
        }
    }
    public class DownloadWebPageTask3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost httpGet = new HttpPost(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                        //   Toast.makeText(HomeRegistration.this,""+content,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            stringResponce3 = Html.fromHtml(result).toString().trim();
            if (pDialog.isShowing())
                pDialog.dismiss();
         //   redeemPoint.setText("Redeem Point\n"+stringResponce3);
          /*  // Toast.makeText(NewRegistration.this,""+result,Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder2 = new AlertDialog.Builder(CurrentLocation.this);
            builder2.setTitle("New Appointment");
            builder2.setMessage(String.valueOf(stringResponce2).toString().trim());
            // Toast.makeText(getApplicationContext(), ""+result, Toast.LENGTH_LONG).show();
            builder2.setCancelable(false);
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (stringResponce2.equals("Successful")) {
                        dialog.cancel();
                        finish();
                        Intent intent = new Intent(CurrentLocation.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        dialog.cancel();
                    }
                }
            });
            builder2.show();
        }*/
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure want to Exit?");
        alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
