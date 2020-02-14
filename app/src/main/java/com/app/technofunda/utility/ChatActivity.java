package com.app.technofunda.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.technofunda.R;
import com.app.technofunda.adapter.ChatAdapter;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.model.ChatModel;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressDialog loading;
    private List<ChatModel> listSuperHeroes;
    private RecyclerView.Adapter adapter;
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    private TextView tvID, tvName, tvTiket;
    private EditText edSubject, edMsg;
    String name = "", tiket = "", subject = "", message = "";
    private Button btnNewMsg, btnHideMsg;
    LinearLayout layMsg;
    AlertDialog.Builder dialogBuilder;
    View dialogView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }
        getNameAndTiket();
        btnHideMsg = findViewById(R.id.btnHideMsg);
        btnNewMsg = findViewById(R.id.btnNewMsg);
        layMsg = findViewById(R.id.layMsg);
        tvID = findViewById(R.id.tvLogId);
        tvName = findViewById(R.id.tvName);
        tvTiket = findViewById(R.id.tvTiketId);
        edSubject = findViewById(R.id.edSub);
        edMsg = findViewById(R.id.edMsg);
        tvID.setText("Id-  " + loginid);

        btnNewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(ChatActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.alert_newmsg, null);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                TextView tvLogId = dialogView.findViewById(R.id.tvLogId);
                TextView tvName = dialogView.findViewById(R.id.tvName);
                TextView tvTiket = dialogView.findViewById(R.id.tvTiketId);
                final EditText edMsg = dialogView.findViewById(R.id.edMsg);
                final EditText edSub = dialogView.findViewById(R.id.edSub);
                tvLogId.setText("ID-  " + loginid);
                if (!name.equals("")) {
                    tvName.setText("Name-  " + name);
                }
                tvTiket.setText("Ticket ID-  " + tiket);

                Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
                Button btnClose = dialogView.findViewById(R.id.btnCancel);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strSubject = edSub.getText().toString();
                        String strMsg = edMsg.getText().toString();
                        if (strMsg.equals("")) {
                            Toast.makeText(ChatActivity.this, "Enter message", Toast.LENGTH_SHORT).show();
                        } else  if (strSubject.equals("")) {
                            Toast.makeText(ChatActivity.this, "Enter subject", Toast.LENGTH_SHORT).show();
                        } else {

                            String subb = strSubject.replaceAll("\\s+", "_");
                            String msg = strMsg.replaceAll("\\s+", "_");

                            // http://technofundaresearch.com/API/APINew.aspx?msg=InsertTicket%20TFR%201001%20samarth%20holiday%20fsfnogngogngiodngd
                            String url = Config.URL + "API/APINew.aspx?msg=InsertTicket%20" + loginid + "%20" + tiket + "%20" + name + "%20" + subb + "%20" + msg;
                            Log.d("urlChatdetails", url);
                            Log.d("testingurl", url);
                            DownloadWebPageTask task1 = new DownloadWebPageTask();
                            task1.execute(new String[]{String.valueOf(url)});
                            alertDialog.dismiss();
                        }

                    }
                });
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });
        btnHideMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject = edSubject.getText().toString();
                message = edMsg.getText().toString();
                if (subject.equals("")) {
                    Toast.makeText(ChatActivity.this, "Enter subject", Toast.LENGTH_SHORT).show();
                } else if (message.equals("")) {
                    Toast.makeText(ChatActivity.this, "Enter some message", Toast.LENGTH_SHORT).show();
                } else {
                    // http://technofundaresearch.com/API/APINew.aspx?msg=InsertTicket%20TFR%201001%20samarth%20holiday%20fsfnogngogngiodngd
                    String msg = message.replaceAll("\\s+", "_");
                    String subb = subject.replaceAll("\\s+", "_");
                    String url = Config.URL + "API/APINew.aspx?msg=InsertTicket%20" + loginid + "%20" + tiket + "%20" + name + "%20" + subb + "%20" + msg;

                    Log.d("testingurl", url);
                    DownloadWebPageTask task1 = new DownloadWebPageTask();
                    task1.execute(new String[]{String.valueOf(url)});
                    //   loading = ProgressDialog.show(ChatActivity.this, "Loading Data", "Please Wait...", false, false);

                }
            }
        });
        getData();
        listSuperHeroes = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
            String s = Html.fromHtml(result).toString();
            Log.d("res", s);
            if (loading.isShowing())
                loading.dismiss();

            AlertDialog.Builder builder2 = new AlertDialog.Builder(ChatActivity.this);
            builder2.setTitle("Information");
            builder2.setMessage(String.valueOf(s).toString().trim());
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                    finish();


                }
            });
            builder2.setCancelable(false);
            builder2.show();
        }
    }

    private void getNameAndTiket() {

        // http://technofundaresearch.com/API/APINew.aspx?msg=MemberName%20TFR

        String MY_URL = Config.URL + "API/APINew.aspx?msg=MemberName%20" + loginid;
        loading = ProgressDialog.show(ChatActivity.this, "Loading Data", "Please Wait...", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", String.valueOf(response));
                        loading.dismiss();
                        try {
                            //  JSONArray array = new JSONArray(aarray);
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject json = null;
                                try {

                                    json = response.getJSONObject(i);
                                    name = String.valueOf(json.getString("memberName"));
                                    tiket = String.valueOf(json.getString("ticketID"));
                                    tvName.setText("Name-  " + String.valueOf(json.getString("memberName")));
                                    tvTiket.setText("Ticket ID-  " + String.valueOf(json.getString("ticketID")));
                                    Log.d("name", String.valueOf(json.getString("memberName")) + String.valueOf(json.getString("ticketID")));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ChatActivity.this);
                        builder2.setTitle("Network Problem");

                        builder2.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                getNameAndTiket();
                            }
                        });
                        builder2.setCancelable(false);
                        builder2.show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void getData() {
        // http://technofundaresearch.com/API/APINew.aspx?msg=TotalTickets%20TFR
        String MY_URL = Config.URL + "API/APINew.aspx?msg=TotalTickets%20" + loginid;
        // loading = ProgressDialog.show(ChatActivity.this, "Loading Data", "Please Wait...", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("response", String.valueOf(response));
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array) {
        try {

            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                ChatModel superHero = new ChatModel();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    superHero.setTicketID(json.getString("TicketID"));
                    superHero.setSenderId(json.getString("SenderId"));
                    superHero.setCreatedDate(json.getString("CreatedDate"));
                    superHero.setSubject(json.getString("Subject"));
                    superHero.setSendTo(json.getString("SendTo"));
                    superHero.setStatus(json.getString("Status"));
                    superHero.setUpdateddate(json.getString("Updateddate"));
                    superHero.setMsgStatus(json.getString("MsgStatus"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listSuperHeroes.add(superHero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new ChatAdapter(listSuperHeroes, ChatActivity.this);
        if (adapter.getItemCount() <= 0) {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
        }
        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
        loading.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                getData();
                listSuperHeroes.clear();
                return true;


        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


