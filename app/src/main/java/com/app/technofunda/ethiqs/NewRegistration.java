package com.app.technofunda.ethiqs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NewRegistration extends AppCompatActivity {

    String s, id, userid, mob, nam, fname,posi,states,districts,getstate,getdistricts,planid,position,pinno,pnum,pinumb,yes,no,current_city,email;
    String url = "";
    Button register,displaypin;
    EditText refid, name, mobile,pin,planshowid,emailid,citys;
    Spinner district,state,positions;
    private ArrayList<String> district_options;
    private ArrayList<String> statelist;
    private ArrayList<String> selectcity;
    private ProgressDialog pDialog;
    RadioButton btnyes,btnno;
    TextView termscondi;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newregister_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refid = (EditText) findViewById(R.id.editrefid);
        name = (EditText) findViewById(R.id.editname);
        state = (Spinner) findViewById(R.id.state_groups);
        district = (Spinner) findViewById(R.id.district_groups);
        positions = (Spinner) findViewById(R.id.Groups);
        mobile = (EditText) findViewById(R.id.editmobile);
        pin = (EditText) findViewById(R.id.edit_pin);
        citys=(EditText)findViewById(R.id.edtcity);
        planshowid = (EditText) findViewById(R.id.edit_planshow_id);
        register = (Button) findViewById(R.id.btnSignUp);
        displaypin=(Button)findViewById(R.id.button_pin);
        emailid=(EditText)findViewById(R.id.edtmail);
        btnyes=(RadioButton)findViewById(R.id.panyes);
        btnno=(RadioButton) findViewById(R.id.panno);
        termscondi=(TextView) findViewById(R.id.term_cond_id);

        List<String> list = new ArrayList<String>();
        list.add("Select Your Position");
        list.add("leftside");
        list.add("rightside");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positions.setAdapter(dataAdapter);

        positions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        district_options=new ArrayList<String>();

        statelist=new ArrayList<String>();
        statelist.add("Select Your State");
        statelist.add("Andaman and Nicobar Islands");
        statelist.add("Andhra Pradesh");statelist.add("Arunachal Pradesh");statelist.add("Assam");statelist.add("Bihar");statelist.add("Chandigarh");statelist.add("Chhattisgarh");
        statelist.add("Dadra and Nagar Haveli");statelist.add("Daman and Diu");statelist.add("Delhi");statelist.add("Goa");statelist.add("Gujarat");statelist.add("Haryana");statelist.add("Himachal Pradesh");
        statelist.add("Jammu and Kashmir");statelist.add("Jharkhand");statelist.add("Karnataka");statelist.add("Kerala");statelist.add("Lakshadweep");statelist.add("Madhya Pradesh");statelist.add("Maharashtra");statelist.add("Manipur");statelist.add("Meghalaya");
        statelist.add("Mizoram");statelist.add("Nagaland");statelist.add("Odisha");statelist.add("Puducherry");statelist.add("Punjab");statelist.add("Rajasthan");statelist.add("Sikkim");statelist.add("Tamil Nadu");statelist.add("Telangana");
        statelist.add("Tripura");statelist.add("Uttarakhand");statelist.add("Uttar Pradesh");statelist.add("West Bengal");


        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,statelist);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateAdapter);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                String stateName = statelist.get(position).toString();
                resetCity(stateName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                pinno = null;
            } else {
                pinno = extras.getString("STRING_I_NEED");
                Log.e("PIN No: ", pinno);
            }
        } else {
            pinno = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        pin.setText(pinno);

        displaypin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PinRegistrationActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userid = refid.getText().toString().trim();
                nam = name.getText().toString().trim();
                mob = mobile.getText().toString().trim();
                posi = positions.getSelectedItem().toString().trim();
                states = state.getSelectedItem().toString().trim();
                districts = district.getSelectedItem().toString().trim();
                pnum=pin.getText().toString().trim();
                planid = planshowid.getText().toString().trim();
                email=emailid.getText().toString().trim();


                boolean check = true;
                Validate validate = new Validate();
                if (posi.equals("Select Your Position")) {
                    check = false;
                    Toast.makeText(getApplicationContext(), "Please Select your position", Toast.LENGTH_SHORT).show();
                }
                if (states.equals("Select Your State")) {
                    check = false;
                    Toast.makeText(getApplicationContext(), "Please Select your state", Toast.LENGTH_SHORT).show();

                }if (districts.equals("Select Your District")) {
                    check = false;
                    Toast.makeText(getApplicationContext(), "Please Select your district", Toast.LENGTH_SHORT).show();
                }if (validate.isNotEmpty(userid)) {
                    userid = refid.getText().toString();
                    id = userid.replaceAll(" ", "%20");
                } else {
                    userid = "";
                    check = false;
                    refid.setError("Please enter valid reference ID");
                }if (validate.isNotEmpty(nam)) {
                    nam = name.getText().toString();
                    fname = nam.replaceAll(" ", "-");
                } else {
                    nam = "";
                    check = false;
                    name.setError("Please enter your name");
                }
                if (validate.isValidPhone(mob))
                    mob = mobile.getText().toString();
                else {
                    mob = "";
                    check = false;
                    mobile.setError("Please enter mobile number");
                }
                if (validate.isNotEmpty(pnum)) {
                    pnum = pin.getText().toString();
                    pinumb = pnum.replaceAll(" ", "");
                } else {
                    pnum = "";
                    check = false;
                    pin.setError("Click above button to choose pin");
                }

                if (check) {
                    getstate=states.replaceAll(" ","-");
                    getdistricts=districts.replaceAll(" ","-");

                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        position="leftside";
                        if(planid.isEmpty() || planid.length() == 0 || planid.equals("") || planid == null) {
                            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + "Swaliya" + "&msg=" + String.valueOf(id) + "%20" + String.valueOf(position) + "%20" + String.valueOf(fname) + "%20" + String.valueOf(mob) + "%20" + String.valueOf(getstate) + "%20" + String.valueOf(getdistricts) + "%20" + String.valueOf(pinumb);
                        } else {
                            url = Config.URL+"/api/Apiurl.aspx?Mobile=" + "Swaliya" + "&msg=" + String.valueOf(id) + "%20" + String.valueOf(position) + "%20" + String.valueOf(fname) + "%20" + String.valueOf(mob) + "%20" + String.valueOf(getstate) + "%20" + String.valueOf(getdistricts) + "%20" + String.valueOf(pinumb) + "%20" + String.valueOf(planid);
                        }

                        DownloadWebPageTask task1 = new DownloadWebPageTask();
                        task1.execute(new String[]{String.valueOf(url)});
                        pDialog = new ProgressDialog(NewRegistration.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        termscondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TermsCondition.class);
                startActivity(intent);

            }
        });

    }



    public void resetCity(String stateName)
    {
        district_options.removeAll(district_options);
        if(stateName.equals("Andaman and Nicobar Islands"))
        {
            district_options.add("Select Your District");
            district_options.add("North and Middle Andaman");
            district_options.add("South Andaman");
            district_options.add("Nicobar");
        }
        else if (stateName.equals("Andhra Pradesh"))
        {
            district_options.add("Select Your District");
            district_options.add("Anantapur");district_options.add("Chittoor");district_options.add("East Godavari");district_options.add("Guntur");
            district_options.add("Kadapa");district_options.add("Krishna");district_options.add("Kurnool");district_options.add("Nellore");district_options.add("Prakasam");district_options.add("Srikakulam");
            district_options.add("Visakhapatnam");district_options.add("Vizianagaram");district_options.add("West Godavari");
        }
        else if (stateName.equals("Arunachal Pradesh"))
        {
            district_options.add("Select Your District");
            district_options.add("Anjaw");district_options.add("Changlang");district_options.add("East Kameng");district_options.add("East Siang");
            district_options.add("Kurung Kumey");district_options.add("Kra Daadi");
            district_options.add("Lohit");district_options.add("Longding");district_options.add("Lower Subansiri");district_options.add("Lower Dibang Valley");district_options.add("Namsai");district_options.add("Papum Pare");district_options.add("Tawang");district_options.add("Tirap");
            district_options.add("Upper Subansiri");district_options.add("Upper Dibang Valley");district_options.add("Upper Siang");district_options.add("West Kameng");district_options.add("West Siang");

        }
        else if (stateName.equals("Assam"))
        {
            district_options.add("Select Your District");
            district_options.add("Barpeta");district_options.add("Bongaigaon");district_options.add("Baksa");district_options.add("Biswanath");district_options.add("Cachar");district_options.add("Charaideo");
            district_options.add("Chirang");district_options.add("Darrang");district_options.add("Dhemaji");district_options.add("Dhubri");district_options.add("Dibrugarh");
            district_options.add("Dima Hasao");district_options.add("East Kamrup");district_options.add("Goalpara");district_options.add("Golaghat");
            district_options.add("Hailakandi");district_options.add("Hojai");district_options.add("Jorhat");district_options.add("Karbi Anglong");district_options.add("Karimganj");
            district_options.add("Kokrajhar");district_options.add("Kamrup");district_options.add("Kamrup Metropolitan");district_options.add("Lakhimpur");district_options.add("Morigaon");
            district_options.add("Nagaon");district_options.add("Nalbari");district_options.add("Sivasagar");district_options.add("Sonitpur");district_options.add("South Salmara Mankachar");
            district_options.add("South Kamrup");district_options.add("Tinsukia");district_options.add("Udalguri");district_options.add("West Karbi Anglong");

        }
        else if (stateName.equals("Bihar"))
        {
            district_options.add("Select Your District");
            district_options.add("Araria");district_options.add("Arwal");district_options.add("Aurangabad");district_options.add("Banka");district_options.add("Begusarai");district_options.add("Bhagalpur");district_options.add("Bhojpur");
            district_options.add("Buxar");district_options.add("Darbhanga");district_options.add("East Champaran");district_options.add("Gaya");district_options.add("Gopalganj");district_options.add("Jamui");district_options.add("Jehanabad");district_options.add("Khagaria");
            district_options.add("Kishanganj");district_options.add("Kaimur");district_options.add("Katihar");district_options.add("Lakhisarai");district_options.add("Madhubani");district_options.add("Munger");district_options.add("Madhepura");district_options.add("Muzaffarpur");
            district_options.add("Nalanda");district_options.add("Nawada");district_options.add("Patna");district_options.add("Purnia");district_options.add("Rohtas");district_options.add("Saharsa");district_options.add("Samastipur");district_options.add("Sheohar");
            district_options.add("Sheikhpura");district_options.add("Saran");district_options.add("Sitamarhi");district_options.add("Supaul");district_options.add("Siwan");district_options.add("Vaishali");district_options.add("West Champaran");

        }
        else if (stateName.equals("Chandigarh"))
        {
            district_options.add("Select Your District");
            district_options.add("Chandigarh");
        }
        else if (stateName.equals("Chhattisgarh"))
        {
            district_options.add("Select Your District");
            district_options.add("Balod");district_options.add("Baloda Bazar");district_options.add("Balrampur");district_options.add("Bastar");district_options.add("Bemetara");district_options.add("Bijapur");district_options.add("Bilaspur");
            district_options.add("Dantewada");district_options.add("Dhamtari");district_options.add("Durg");district_options.add("Gariaband");district_options.add("Janjgir-Champa");district_options.add("Jashpur");district_options.add("Kabirdham");
            district_options.add("Kanker");district_options.add("Kondagaon");district_options.add("Korba");district_options.add("Koriya");district_options.add("Mahasamund");district_options.add("Mungeli");district_options.add("Narayanpur");
            district_options.add("Raigarh");district_options.add("Raipur");district_options.add("Rajnandgaon");
            district_options.add("Sukma");district_options.add("Surajpur");district_options.add("Surguja");

        }
        else if (stateName.equals("Dadra and Nagar Haveli"))
        {
            district_options.add("Select Your District");
            district_options.add("Dadra and Nagar Haveli");
        }
        else if (stateName.equals("Daman and Diu"))
        {
            district_options.add("Select Your District");
            district_options.add("Daman");district_options.add("Diu");
        }
        else if (stateName.equals("Delhi"))
        {
            district_options.add("Select Your District");
            district_options.add("Daryaganj");district_options.add("East Delhi");district_options.add("New Delhi");district_options.add("North Delhi");district_options.add("North East Delhi");
            district_options.add("North West Delhi");district_options.add("Shahdara");district_options.add("South Delhi");district_options.add("South East Delhi");district_options.add("South West Delhi");district_options.add("West Delhi");
        }
        else if (stateName.equals("Goa"))
        {
            district_options.add("Select Your District");
            district_options.add("North Goa");district_options.add("South Goa");
        }
        else if (stateName.equals("Gujarat"))
        {
            district_options.add("Select Your District");
            district_options.add("Ahmedabad");district_options.add("Amreli");district_options.add("Anand");district_options.add("Aravalli");district_options.add("Banaskantha");district_options.add("Bharuch");district_options.add("Bhavnagar");district_options.add("Botad");district_options.add("Chhota Udaipur");
            district_options.add("Dahod");district_options.add("Dang");district_options.add("Devbhoomi Dwarka");district_options.add("Gandhinagar");district_options.add("Gir Somnath");district_options.add("Jamnagar");district_options.add("Junagadh");
            district_options.add("Kheda");district_options.add("Kutch");district_options.add("Mahisagar");district_options.add("Mehsana");district_options.add("Morbi");district_options.add("Narmada");district_options.add("Navsari");
            district_options.add("Panchmahal");district_options.add("Patan");district_options.add("Porbandar");district_options.add("Rajkot");district_options.add("Sabarkantha");district_options.add("Surat");district_options.add("Surendranagar");
            district_options.add("Tapi");district_options.add("Vadodara");district_options.add("Valsad");

        }
        else if (stateName.equals("Haryana"))
        {
            district_options.add("Select Your District");
            district_options.add("Ambala");district_options.add("Bhiwani");district_options.add("Faridabad");district_options.add("Fatehabad");district_options.add("Gurgaon");district_options.add("Hissar");district_options.add("Jhajjar");district_options.add("Jind");district_options.add("Kaithal");district_options.add("Karnal");
            district_options.add("Kurukshetra");district_options.add("Mahendragarh");district_options.add("Mewat");district_options.add("Palwal");district_options.add("Panchkula");district_options.add("Panipat");district_options.add("Rewari");district_options.add("Rohtak");
            district_options.add("Sirsa");district_options.add("Sonipat");district_options.add("Yamuna Nagar");

        }
        else if (stateName.equals("Himachal Pradesh"))
        {
            district_options.add("Select Your District");
            district_options.add("Bilaspur");district_options.add("Chamba");district_options.add("Hamirpur");district_options.add("Kangra");district_options.add("Kinnaur");district_options.add("Kullu");district_options.add("Lahaul and Spiti");district_options.add("Mandi");district_options.add("Shimla");
            district_options.add("Sirmaur");district_options.add("Solan");district_options.add("Una");
        }
        else if (stateName.equals("Jammu and Kashmir"))
        {
            district_options.add("Select Your District");
            district_options.add("Anantnag");district_options.add("Bandipora");district_options.add("Baramulla");district_options.add("Badgam");district_options.add("Doda");district_options.add("Ganderbal");district_options.add("Jammu");district_options.add("Kargil");district_options.add("Kathua");
            district_options.add("Kishtwar");district_options.add("Kulgam");district_options.add("Kupwara");district_options.add("Leh");district_options.add("Poonch");district_options.add("Pulwama");district_options.add("Rajouri");district_options.add("Ramban");district_options.add("Reasi");
            district_options.add("Samba");district_options.add("Shopian");district_options.add("Srinagar");district_options.add("Udhampur");

        }
        else if (stateName.equals("Jharkhand"))
        {
            district_options.add("Select Your District");
            district_options.add("Bokaro");district_options.add("Chatra");district_options.add("Deoghar");district_options.add("Dhanbad");district_options.add("Dumka");district_options.add("East Singhbhum");district_options.add("Garhwa");district_options.add("Giridih");district_options.add("Godda");district_options.add("Gumla");district_options.add("Hazaribag");
            district_options.add("Jamtara");district_options.add("Khunti");district_options.add("Koderma");district_options.add("Latehar");district_options.add("Lohardaga");district_options.add("Pakur");district_options.add("Palamu");district_options.add("Ramgarh");district_options.add("Ranchi");district_options.add("Sahibganj");district_options.add("Seraikela Kharsawan");
            district_options.add("Simdega");district_options.add("West Singhbhum");

        }
        else if (stateName.equals("Karnataka"))
        {
            district_options.add("Select Your District");
            district_options.add("Bagalkot");district_options.add("Bellary");district_options.add("Belgaum");district_options.add("Bangalore Rural");district_options.add("Bangalore Urban");district_options.add("Bidar");district_options.add("Chamarajnagar");district_options.add("Chikkaballapur");district_options.add("Chikkamagaluru");
            district_options.add("Chitradurga");district_options.add("Dakshina Kannada");district_options.add("Davanagere");district_options.add("Dharwad");district_options.add("Gadag");district_options.add("Hassan");
            district_options.add("Haveri");district_options.add("Gulbarga");district_options.add("Kodagu");district_options.add("Kolar");district_options.add("Koppal");district_options.add("Mandya");district_options.add("Mysore");district_options.add("Raichur");district_options.add("Ramanagara");
            district_options.add("Shimoga");district_options.add("Tumkur");district_options.add("Udupi");district_options.add("Uttara Kannada");district_options.add("Vijayapura");district_options.add("Yadgir");

        }
        else if (stateName.equals("Kerala"))
        {
            district_options.add("Select Your District");
            district_options.add("Alappuzha");district_options.add("Ernakulam");district_options.add("Idukki");district_options.add("Kannur");district_options.add("Kasaragod");district_options.add("Kollam");district_options.add("Kottayam");
            district_options.add("Kozhikode");district_options.add("Malappuram");district_options.add("Palakkad");district_options.add("Pathanamthitta");district_options.add("Thrissur");district_options.add("Thiruvananthapuram");district_options.add("Wayanad");
        }
        else if (stateName.equals("Lakshadweep"))
        {
            district_options.add("Select Your District");
            district_options.add("Lakshadweep");
        }
        else if (stateName.equals("Madhya Pradesh"))
        {
            district_options.add("Select Your District");
            district_options.add("Agar");district_options.add("Alirajpur");district_options.add("Anuppur");district_options.add("Ashok Nagar");district_options.add("Balaghat");district_options.add("Barwani");
            district_options.add("Betul");district_options.add("Bhind");district_options.add("Bhopal");district_options.add("Burhanpur");district_options.add("Chhatarpur");district_options.add("Chhindwara");district_options.add("Damoh");
            district_options.add("Datia");district_options.add("Dewas");district_options.add("Dhar");district_options.add("Dindori");district_options.add("Guna");district_options.add("Gwalior");district_options.add("Harda");district_options.add("Hoshangabad");
            district_options.add("Indore");district_options.add("Jabalpur");district_options.add("Jhabua");district_options.add("Katni");district_options.add("Khandwa");district_options.add("Khargone");district_options.add("Mandla");district_options.add("Mandsaur");
            district_options.add("Morena");district_options.add("Narsinghpur");district_options.add("Neemuch");district_options.add("Panna");district_options.add("Raisen");district_options.add("Rajgarh");district_options.add("Ratlam");district_options.add("Rewa");
            district_options.add("Sagar");district_options.add("Satna");district_options.add("Sehore");district_options.add("Seoni");district_options.add("Shahdol");district_options.add("Shajapur");district_options.add("Sheopur");district_options.add("Shivpuri");
            district_options.add("Sidhi");district_options.add("Singrauli");district_options.add("Tikamgarh");district_options.add("Ujjain");district_options.add("Umaria");district_options.add("Vidisha");
        }
        else if (stateName.equals("Maharashtra"))
        {
            district_options.add("Select Your District");
            district_options.add("Aurangabad");district_options.add("Beed");district_options.add("Bhandara");district_options.add("Buldhana");district_options.add("Chandrapur");district_options.add("Dhule");district_options.add("Gadchiroli");district_options.add("Gondia");
            district_options.add("Hingoli");district_options.add("Jalgaon");district_options.add("Jalna");district_options.add("Kolhapur");district_options.add("Latur");district_options.add("Mumbai City");district_options.add("Mumbai suburban");district_options.add("Nanded");
            district_options.add("Nandurbar");district_options.add("Nagpur");district_options.add("Nashik");district_options.add("Osmanabad");district_options.add("Palghar");district_options.add("Parbhani");district_options.add("Pune");district_options.add("Raigad");
            district_options.add("Ratnagiri");district_options.add("Sangli");district_options.add("Satara");district_options.add("Sindhudurg");district_options.add("Solapur");district_options.add("Thane");district_options.add("Wardha");district_options.add("Washim");district_options.add("Yavatmal");
        }
        else if (stateName.equals("Manipur"))
        {
            district_options.add("Select Your District");
            district_options.add("Bishnupur");district_options.add("Churachandpur");district_options.add("Chandel");district_options.add("Imphal East");district_options.add("Senapati");district_options.add("Tamenglong");district_options.add("Thoubal");district_options.add("Ukhrul");district_options.add("Imphal West");
        }
        else if (stateName.equals("Meghalaya"))
        {
            district_options.add("Select Your District");
            district_options.add("Williamnagar");district_options.add("East Khasi Hills");district_options.add("East Jaintia Hills");district_options.add("North Garo Hills");district_options.add("Ri Bhoi");district_options.add("South Garo Hills");district_options.add("South West Garo Hills");district_options.add("South West Khasi Hills");
            district_options.add("West Jaintia Hills");district_options.add("West Garo Hills");district_options.add("West Khasi Hills");
        }
        else if (stateName.equals("Mizoram"))
        {
            district_options.add("Select Your District");
            district_options.add("Aizawl");district_options.add("Champhai");district_options.add("Kolasib");district_options.add("Lawngtlai");district_options.add("Lunglei");district_options.add("Mamit");district_options.add("Saiha");district_options.add("Serchhip");
        }
        else if (stateName.equals("Nagaland"))
        {
            district_options.add("Select Your District");
            district_options.add("Dimapur");district_options.add("Kiphire");district_options.add("Kohima");district_options.add("Longleng");district_options.add("Mokokchung");district_options.add("Mon");district_options.add("Peren");district_options.add("Phek");
            district_options.add("Tuensang");district_options.add("Wokha");district_options.add("Zunheboto");
        }
        else if (stateName.equals("Odisha"))
        {
            district_options.add("Select Your District");
            district_options.add("Angul");district_options.add("Boudh");district_options.add("Bhadrak");district_options.add("Balangir");district_options.add("Bargarh");district_options.add("Balasore");district_options.add("Cuttack");district_options.add("Debagarh");
            district_options.add("Dhenkanal");district_options.add("Ganjam");district_options.add("Gajapati");district_options.add("Jharsuguda");district_options.add("Jajpur");district_options.add("Jagatsinghpur");district_options.add("Khordha");district_options.add("Kendujhar");
            district_options.add("Kalahandi");district_options.add("Kandhamal");district_options.add("Koraput");district_options.add("Kendrapara");district_options.add("Malkangiri");district_options.add("Mayurbhanj");district_options.add("Nabarangpur");district_options.add("Nuapada");
            district_options.add("Nayagarh");district_options.add("Puri");district_options.add("Rayagada");district_options.add("Sambalpur");district_options.add("Subarnapur");district_options.add("Sundargarh");
        }
        else if (stateName.equals("Puducherry"))
        {
            district_options.add("Select Your District");
            district_options.add("Karaikal");district_options.add("Mahe");district_options.add("Pondicherry");district_options.add("Yanam");
        }
        else if (stateName.equals("Punjab"))
        {
            district_options.add("Select Your District");
            district_options.add("Amritsar");district_options.add("Barnala");district_options.add("Bathinda");district_options.add("Firozpur");district_options.add("Faridkot");district_options.add("Fatehgarh Sahib");district_options.add("Fazilka");district_options.add("Gurdaspur");
            district_options.add("Hoshiarpur");district_options.add("Jalandhar");district_options.add("Kapurthala");district_options.add("Ludhiana");district_options.add("Mansa");district_options.add("Moga");district_options.add("Sri Muktsar Sahib");district_options.add("Pathankot");
            district_options.add("Patiala");district_options.add("Rupnagar");district_options.add("Sahibzada Ajit Singh Nagar");district_options.add("Sangrur");district_options.add("Shahid Bhagat Singh Nagar");district_options.add("Tarn Taran");
        }
        else if (stateName.equals("Rajasthan"))
        {
            district_options.add("Select Your District");
            district_options.add("Barmer");district_options.add("Banswara");district_options.add("Bharatpur");district_options.add("Baran");district_options.add("Bundi");district_options.add("Bhilwara");district_options.add("Churu");district_options.add("Chittorgarh");
            district_options.add("Dausa");district_options.add("Dholpur");district_options.add("Dungapur");district_options.add("Ganganagar");district_options.add("Hanumangarh");district_options.add("Jhunjhunu");district_options.add("Jalore");district_options.add("Jodhpur");
            district_options.add("Jaipur");district_options.add("Jaisalmer");district_options.add("Jhalawar");district_options.add("Karauli");district_options.add("Kota");district_options.add("Nagaur");district_options.add("Pali");district_options.add("Pratapgarh");
            district_options.add("Rajsamand");district_options.add("Sikar");district_options.add("Sawai Madhopur");district_options.add("Sirohi");district_options.add("Tonk");district_options.add("Udaipur");
        }
        else if (stateName.equals("Sikkim"))
        {
            district_options.add("Select Your District");
            district_options.add("East Sikkim");district_options.add("North Sikkim");district_options.add("South Sikkim");district_options.add("West Sikkim");
        }
        else if (stateName.equals("Tamil Nadu"))
        {
            district_options.add("Select Your District");
            district_options.add("Ariyalur");district_options.add("Chennai");district_options.add("Coimbatore");district_options.add("Cuddalore");district_options.add("Dharmapuri");district_options.add("Dindigul");district_options.add("Erode");district_options.add("Kanchipuram");
            district_options.add("Kanyakumari");district_options.add("Karur");district_options.add("Krishnagiri");district_options.add("Madurai");district_options.add("Nagapattinam");district_options.add("Nilgiris");district_options.add("Namakkal");district_options.add("Perambalur");
            district_options.add("Pudukkottai");district_options.add("Ramanathapuram");district_options.add("Salem");district_options.add("Sivaganga");district_options.add("Tirupur");district_options.add("Tiruchirappalli");district_options.add("Theni");district_options.add("Tirunelveli");
            district_options.add("Thanjavur");district_options.add("Thoothukudi");district_options.add("Tiruvallur");district_options.add("Tiruvarur");district_options.add("Tiruvannamalai");district_options.add("Vellore");district_options.add("Viluppuram");district_options.add("Virudhunagar");

        }
        else if (stateName.equals("Telangana"))
        {
            district_options.add("Select Your District");
            district_options.add("Adilabad");district_options.add("Hyderabad");district_options.add("Karimnagar");district_options.add("Khammam");district_options.add("Mahbubnagar");district_options.add("Medak");district_options.add("Nalgonda");district_options.add("Nizamabad");
        }
        else if (stateName.equals("Tripura"))
        {
            district_options.add("Select Your District");
            district_options.add("Dhalai");district_options.add("Gomati");district_options.add("Khowai");district_options.add("North Tripura");district_options.add("Sepahijala");district_options.add("South Tripura");district_options.add("Unokoti");district_options.add("West Tripura");
        }
        else if (stateName.equals("Uttarakhand"))
        {
            district_options.add("Select Your District");
            district_options.add("Almora");district_options.add("Bageshwar");district_options.add("Chamoli");district_options.add("Champawat");district_options.add("Dehradun");district_options.add("Haridwar");district_options.add("Nainital");district_options.add("Pauri Garhwal");district_options.add("Pithoragarh");
            district_options.add("Rudraprayag");district_options.add("Tehri Garhwal");district_options.add("Udham Singh Nagar");district_options.add("Uttarkashi");
        }
        else if (stateName.equals("Uttar Pradesh"))
        {
            district_options.add("Select Your District");
            district_options.add("Agra");district_options.add("Aligarh");district_options.add("Allahabad");district_options.add("Ambedkar Nagar");district_options.add("Amethi");district_options.add("Amroha");district_options.add("Auraiya");district_options.add("Azamgarh");district_options.add("Bagpat");
            district_options.add("Bahraich");district_options.add("Ballia");district_options.add("Balrampur");district_options.add("Banda");district_options.add("Barabanki");district_options.add("Bareilly");district_options.add("Basti");district_options.add("Bijnor");district_options.add("Budaun");
            district_options.add("Bulandshahr");district_options.add("Chandauli");district_options.add("Chitrakoot");district_options.add("Deoria");district_options.add("Etah");district_options.add("Etawah");district_options.add("Faizabad");district_options.add("Farrukhabad");district_options.add("Fatehpur");district_options.add("Firozabad");
            district_options.add("Gautam Buddh Nagar");district_options.add("Ghaziabad");district_options.add("Ghazipur");district_options.add("Gonda");district_options.add("Gorakhpur");district_options.add("Hamirpur");district_options.add("Hapur");district_options.add("Hardoi");district_options.add("Hathras");
            district_options.add("Jalaun");district_options.add("Jaunpur");district_options.add("Jhansi");district_options.add("Kannauj");district_options.add("Kanpur Dehat");district_options.add("Kanpur Nagar");district_options.add("Kasganj");district_options.add("Kaushambi");district_options.add("Kushinagar");
            district_options.add("Lakhimpur Kheri");district_options.add("Lalitpur");district_options.add("Lucknow");district_options.add("Maharajganj");district_options.add("Mahoba");district_options.add("Mainpuri");district_options.add("Mathura");district_options.add("Mau");district_options.add("Meerut");
            district_options.add("Mirzapur");district_options.add("Moradabad");district_options.add("Muzaffarnagar");district_options.add("Pilibhit");district_options.add("Pratapgarh");district_options.add("Raebareli");district_options.add("Rampur");district_options.add("Saharanpur");district_options.add("Sambhal");
            district_options.add("Sant Kabir Nagar");district_options.add("Sant Ravidas Nagar");district_options.add("Shahjahanpur");district_options.add("Shamli");district_options.add("Shravasti");district_options.add("Siddharthnagar");district_options.add("Sitapur");district_options.add("Sonbhadra");district_options.add("Sultanpur");
            district_options.add("Unnao");district_options.add("Varanasi");

        }
        else if (stateName.equals("West Bengal"))
        {
            district_options.add("Select Your District");
            district_options.add("Alipurduar");district_options.add("Bankura");district_options.add("Bardhaman");district_options.add("Birbhum");district_options.add("Cooch Behar");district_options.add("Dakshin Dinajpur");district_options.add("Darjeeling");district_options.add("Hooghly");district_options.add("Howrah");district_options.add("Jalpaiguri");
            district_options.add("Kolkata");district_options.add("Maldah");district_options.add("Murshidabad");district_options.add("Nadia");district_options.add("North 24 Parganas");district_options.add("Paschim Medinipur");district_options.add("Purba Medinipur");district_options.add("Purulia");district_options.add("South 24 Parganas");district_options.add("Uttar Dinajpur");
        }
        else
        {
            district_options.add("Select Your District");
        }

        ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,district_options);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(distAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            s = Html.fromHtml(result).toString();

            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(NewRegistration.this);
            builder2.setTitle("Registration");
            builder2.setMessage(String.valueOf(s).toString().trim());
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder2.show();
        }
    }


}
