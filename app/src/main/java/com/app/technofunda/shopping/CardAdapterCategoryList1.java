package com.app.technofunda.shopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.technofunda.R;
import com.app.technofunda.addvertise.ImageLoader;
import com.app.technofunda.ethiqs.SessionManager;
import com.app.technofunda.util.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CardAdapterCategoryList1 extends RecyclerView.Adapter<CardAdapterCategoryList1.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    //List of superHeroes
    private List<CategoryList1> superHeroes;
    String id,name,price,str_add_to_card,str_add_to_cardurl,myresponce,loginid,mobilenumber,passwords;
    SessionManager session;
    SharedPreferences pref;


    public CardAdapterCategoryList1(List<CategoryList1> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final CategoryList1 m   =  superHeroes.get(position);

        final String encodedString = m.getImageUrl();
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);

        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


        holder.textViewId.setText(m.getId());
        holder.textViewName.setText(m.getName());
        holder.textPrice.setText(m.getPrice());
        holder.textDiscount.setText(m.getDisc());
        holder.imageView.setImageBitmap(decodedBitmap);


//        Glide.with(context).load(decodedBitmap)
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.imageView);
//        holder.liner_service.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, FullGridView.class);
//                intent.putExtra("ID",m.getId());
//                context.startActivity(intent);
//            }
//        });

        session = new SessionManager(context);
        pref = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid=pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber=pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords=pref.getString(Config.KEY_PASSWORD, "");
        }


        holder.addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = m.getId();
                name =m.getName();
                price = m.getPrice();
                holder.addtoCart.setVisibility(View.VISIBLE);

                Intent i=new Intent(context, ShowCart.class);
                context.startActivity(i);

                ConnectivityManager cn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    str_add_to_card="AddToCart";
                    str_add_to_cardurl = Config.URL+"/API/Apiurl.aspx?Mobile="+ String.valueOf(mobilenumber)+"&msg=" + String.valueOf(str_add_to_card)+"%20"+String.valueOf(id);

                    DownloadWebPageTask task1 = new DownloadWebPageTask();
                    task1.execute(new String[]{String.valueOf(str_add_to_cardurl)});


                    // Toast.makeText(context, ""+str_add_to_cardurl, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Network Connection Not Available", Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.liner_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullGridView.class);
                context.startActivity(intent);
            }
        });

        animate(holder);

    }


    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textViewId;
        public TextView textViewName;
        public TextView textPrice;
        public TextView textDiscount;
        public LinearLayout liner_service;
        public Button addtoCart;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageViewHero);
            textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textDiscount = (TextView) itemView.findViewById(R.id.discount);
            liner_service = (LinearLayout) itemView.findViewById(R.id.liner_service);
            addtoCart = (Button) itemView.findViewById(R.id.addtocart);
        }
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
       /* final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);*/
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
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
            myresponce = Html.fromHtml(result).toString().trim();
            Toast.makeText(context, myresponce+"  Item added to cart", Toast.LENGTH_LONG).show();
            // Toast.makeText(context, ""+myresponce, Toast.LENGTH_LONG).show();
        }
    }

}

