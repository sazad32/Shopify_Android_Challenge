package com.example.saimoon.customproducts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String customCollectionURL = "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";



        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        //final ListView lv = (ListView) findViewById(R.id.lv);
        final ArrayList<String> collections = new ArrayList<String>();
        final ArrayList<String> collectionsID = new ArrayList<String>();
        final ArrayList<String> imageURLs = new ArrayList<String>();

        //final ImageView img = (ImageView) findViewById(R.id.image_view);
        String imgURL = "https://cdn.shopify.com/s/files/1/1000/7970/collections/Awesome_20Bronze_20Bag_grande_0fa20b0a-0663-44cf-81c4-2d53b83e1d65.png?v=1545072802";
        //Picasso.get().load(imgURL).into(img);

        //RecyclerView variables
        final RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);



        JsonObjectRequest customCollections = new JsonObjectRequest(
                Request.Method.GET, customCollectionURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray custom_collections = response.getJSONArray("custom_collections");
                            final Adapter mAdapter;
                            for(int a = 0; a<custom_collections.length(); a++){
                                JSONObject param = custom_collections.getJSONObject(a);
                                collections.add(param.getString("title"));
                                collectionsID.add(param.getString("id"));
                                JSONObject imageSite = param.getJSONObject("image");
                                imageURLs.add(imageSite.getString("src"));
                                Log.i("TIRED A",imageURLs.get(a));
                            }

                            //lv.setAdapter(arrayAdapter);
                            Log.i("Collection Names ", collections.toString());
                            mAdapter = new Adapter(collections,imageURLs);

                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);

                            ((Adapter) mAdapter).setOnItemClickListener(new Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    collectionsID.get(position);
                                    mAdapter.notifyItemChanged(position);
                                    Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                                    intent.putExtra("Collection_ID", collectionsID.get(position));
                                    startActivity(intent);

                                }
                            });


                        } catch (JSONException error) {
                            Log.e("CustomCollection_Error ", error.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Volley_Error ", error.toString());
                    }
                }
        );
        requestQueue.add(customCollections);


    }
}
