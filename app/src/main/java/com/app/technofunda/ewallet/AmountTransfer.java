package com.app.technofunda.ewallet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.app.technofunda.R;
import com.app.technofunda.ethiqs.Validate;
import com.app.technofunda.util.Config;

public class AmountTransfer extends Fragment {

    EditText transid,transamt;
    Button subtransamt;
    String trnsfrid,trid,trnsframt,tramt;
    String userloginid,userloginmobile,Url,tansfer,s;
    SharedPreferences pref;
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewt = inflater.inflate(R.layout.amount_transfer_fragment, container, false);
        transid = (EditText)viewt.findViewById(R.id.trans_id_edit);
        transamt = (EditText)viewt.findViewById(R.id.trans_amt_edit);
        subtransamt = (Button)viewt.findViewById(R.id.btnSubmit_trans);

        pref = getContext().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            userloginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            userloginmobile=pref.getString(Config.KEY_MOBILE, "");
        }
        ConnectivityManager cn = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

        subtransamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trnsfrid = transid.getText().toString().trim();
                trnsframt = transamt.getText().toString().trim();
                boolean check = true;
                Validate validate = new Validate();
                if (validate.isNotEmpty(trnsfrid)) {
                    trnsfrid = transid.getText().toString();
                    trid = trnsfrid.replaceAll(" ", "");
                } else {
                    trnsfrid = "";
                    check = false;
                    transid.setError("Please enter transfer id");
                }if (validate.isNotEmpty(trnsframt)) {
                    trnsframt = transamt.getText().toString();
                    tramt = trnsframt.replaceAll(" ", "");
                } else {
                    trnsframt = "";
                    check = false;
                    transamt.setError("Please enter transfer amount");
                }
                if (check) {
                    tansfer="TransferAmt";
                    Url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(tansfer) + "%20" + String.valueOf(userloginid)+ "%20" + String.valueOf(trid)+ "%20" + String.valueOf(tramt);
                    DownloadWebPageTask task1 = new DownloadWebPageTask();
                    task1.execute(new String[]{String.valueOf(Url)});
                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            }
        });
        }else {
            Toast.makeText(getContext(), "Network Connection Not Available", Toast.LENGTH_LONG).show();
        }
        return viewt;

        //footer
       /* homef=(ImageView)viewt.findViewById(R.id.home_footer);
        homef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AmountTransfer.this,frontview.class);
                startActivity(intent);
            }
        });
        updatef=(ImageView)viewt.findViewById(R.id.update_footer);
        updatef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(frontview.this,frontview.class);
                startActivity(intent);

            }
        });
        logoutf=(ImageView)findViewById(R.id.logout_footer);
        logoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(frontview.this);
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
        });
        profilef=(ImageView)findViewById(R.id.profile_footer);
        profilef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(frontview.this,ProfileActivity.class);
                startActivity(intent);

            }
        });*/


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

        protected void onPostExecute(String result) {
            s = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(String.valueOf(s).toString().trim());
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    transid.setText("");
                    transamt.setText("");
                }
            });
            builder.show();
        }
    }
}
