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

public class AmountWithdraw extends Fragment {

    EditText withdrawamt;
    Button subwithdrawamt;
    String withdraw,withdamount,s;
    String userloginid,userloginmobile,Url,withd;
    SharedPreferences pref;
    private ProgressDialog pDialog;
    ImageView homef,updatef,logoutf,profilef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewm = inflater.inflate(R.layout.amount_withdraw_fragment, container, false);

        withdrawamt = (EditText)viewm.findViewById(R.id.withdraw_amt_edit);
        subwithdrawamt = (Button)viewm.findViewById(R.id.btnSubmit_witd);

        pref =  getContext().getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            userloginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            userloginmobile=pref.getString(Config.KEY_MOBILE, "");
        }
        ConnectivityManager cn = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {

        subwithdrawamt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw = withdrawamt.getText().toString().trim();
                boolean check = true;
                Validate validate = new Validate();
                if (validate.isNotEmpty(withdraw)) {
                    withdraw = withdrawamt.getText().toString();
                    withdamount = withdraw.replaceAll(" ", "");
                } else {
                    withdraw = "";
                    check = false;
                    withdrawamt.setError("Please enter withdraw amount");
                }
                if (check) {
                    withd="WithdrawAmt";
                    Url = Config.URL+"/api/Apiurl.aspx?Mobile=" + String.valueOf(userloginmobile) + "&msg=" + String.valueOf(withd) + "%20" + String.valueOf(userloginid)+ "%20" + String.valueOf(withdamount);
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
        return viewm;
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
                    withdrawamt.setText("");
                }
            });
            builder.show();
        }
    }
}
