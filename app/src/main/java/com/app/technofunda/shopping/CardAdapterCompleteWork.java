package com.app.technofunda.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.app.technofunda.R;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class CardAdapterCompleteWork extends RecyclerView.Adapter<CardAdapterCompleteWork.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private ProgressDialog pDialog;
    //List of superHeroes
    private List<PackageListPojo> superHeroes;
    String accepteCoustomer,accepteCoustomernURL,resultVerifiy;
    String user_id,user_mobile,password,str_remove_to_card,str_remove_to_cardurl,pakid;
    SharedPreferences pref;
    String mbl="9371113850";


    public CardAdapterCompleteWork(List<PackageListPojo> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_completed_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PackageListPojo m   =  superHeroes.get(position);
        /*imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(m.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.imageView.setImageUrl(m.getImageUrl(), imageLoader);*/
        holder.packageid.setText(m.getPackageid());
        holder.packagename.setText(m.getPackagename());
        holder.feature1.setText("Rs. "+m.getFeature1());
        holder.feature2.setText("Rs. "+m.getFeature2());
        pakid=m.getPackageid();
     /*   holder.feature3.setText("Package :  "+m.getFeature3());
        holder.packageAmount.setText("Time : "+m.getPackageAmount());
        holder.discount.setText("Package Amount : "+m.getDiscount());*/
      /*  holder.finalAmount.setText("RAM :  "+m.getFinalAmount());
        holder.offer.setText("Display Size  : "+m.getOffer());
        holder.membershipBenifit.setText("Location :   "+m.getMembershipBenifit());*/

     /*   Glide.with(context).load(m.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);*/
     /*   pref =context.getSharedPreferences(Config.PREF_SETPIN, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_Pin)) {
            user_id_mobile = pref.getString(Config.KEY_Pin, "");
        }*/
        // getid=m.getPackageid();
        /*holder.btn_add_tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepteCoustomer = "CompleteRequest";
                accepteCoustomernURL = Config.URL + "/api/Partner_Regi.aspx?msg=" + String.valueOf(accepteCoustomer) + "%20" + String.valueOf(mbl)+ "%20" + String.valueOf(getid);
                DownloadWebPageTask task1 = new DownloadWebPageTask();
                task1.execute(new String[]{String.valueOf(accepteCoustomernURL)});
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                pDialog.show();


            }
        });*/

        pref =context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            user_id = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            user_mobile =  pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            password = pref.getString(Config.KEY_PASSWORD, "");
        }
        holder.btn_add_tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    str_remove_to_card="RemoveFromCart";
                    str_remove_to_cardurl = Config.URL+"/API/Apiurl.aspx?Mobile="+ String.valueOf(user_mobile)+"&pass="+String.valueOf(password)+"&msg=" + String.valueOf(str_remove_to_card)+"%20"+String.valueOf(user_mobile)+"%20"+String.valueOf(pakid);
                    DownloadWebPageTask2 task2 = new DownloadWebPageTask2();
                    task2.execute(new String[]{String.valueOf(str_remove_to_cardurl)});

                    // Toast.makeText(context, ""+str_add_to_cardurl, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }


            }
        });
        holder.liner_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context,BookingActivity.class);
                intent.putExtra("ID",m.getPackageid());
                context.startActivity(intent);*/
            }
        });
        animate(holder);
    }


    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView packageid;
        public TextView packagename;
        public TextView feature1;
        public TextView feature2;
        public TextView feature3;
        public TextView packageAmount;
        public TextView discount;
        public TextView finalAmount;
        public TextView offer;
        public TextView membershipBenifit;
        public Button btn_add_tocart;
        public LinearLayout liner_book;

        public ViewHolder(View itemView) {
            super(itemView);
            // image = (ImageView)itemView.findViewById(R.id.Image);
            packageid = (TextView) itemView.findViewById(R.id.Packageid);
            packagename = (TextView) itemView.findViewById(R.id.Packagename);
            feature1 = (TextView) itemView.findViewById(R.id.Feature1);
            feature2 = (TextView) itemView.findViewById(R.id.Feature2);
           /* feature3 = (TextView) itemView.findViewById(R.id.Feature3);
            packageAmount = (TextView) itemView.findViewById(R.id.PackageAmount);
            discount = (TextView) itemView.findViewById(R.id.Discount);*/
           /* finalAmount = (TextView) itemView.findViewById(R.id.FinalAmount);
            offer = (TextView) itemView.findViewById(R.id.Offer);
            membershipBenifit = (TextView) itemView.findViewById(R.id.MembershipBenifit);*/
            btn_add_tocart = (Button) itemView.findViewById(R.id.btn_add_tocart3);
            liner_book = (LinearLayout) itemView.findViewById(R.id.liner_book);
        }
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
       /* final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);*/
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
            resultVerifiy = Html.fromHtml(result).toString();
            if (pDialog.isShowing())
                pDialog.dismiss();
            notifyDataSetChanged();
        }
    }
    private class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
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
            String sd = Html.fromHtml(result).toString().trim();
            Toast.makeText(context, "" + sd, Toast.LENGTH_LONG).show();
            Intent i=new Intent(context, ShowCart.class);
            context.startActivity(i);
            ((ShowCart)context).finish();
            // txtewamt.setText("Current Wallet Amount :" + sd);
        }
    }
    private class DownloadWebPageTask3 extends AsyncTask<String, Void, String> {
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
            String sd = Html.fromHtml(result).toString().trim();
            Toast.makeText(context, "" + sd, Toast.LENGTH_LONG).show();

            // txtewamt.setText("Current Wallet Amount :" + sd);
        }
    }
}
