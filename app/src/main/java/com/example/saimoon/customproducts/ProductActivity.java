package com.example.saimoon.customproducts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle bundle = getIntent().getExtras();
        String id =bundle.getString("Collection_ID");
        final String productURL = "https://shopicruit.myshopify.com/admin/collects.json?collection_id=" + id + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
        Log.i("URL",productURL);
        final ArrayList<String> pID = new ArrayList<String>();

        final ListView lstView = (ListView) findViewById(R.id.ProductListView);


        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ArrayList<String> productTitles = new ArrayList<String>();
        final ArrayList<String> variantNames = new ArrayList<>();
        final ArrayList<String> available = new ArrayList<String>();
        final HashMap<String, Integer> variants = new HashMap<>();




        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, productURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jArr = response.getJSONArray("collects");
                            for (int k = 0; k < jArr.length(); k++) {
                                JSONObject param = jArr.getJSONObject(k);
                                pID.add(param.getString("product_id"));
                            }

                            String pinfoURL1 = "https://shopicruit.myshopify.com/admin/products.json?ids=";
                            String pinfoURL2 = "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
                            String ids = pID.get(0);
                            if(pID.size() > 1 ){
                                for(int l = 1; l<pID.size(); l++){
                                    ids = ids + "," + pID.get(l);
                                }
                            }
                            String pinfoURL = pinfoURL1 + ids + pinfoURL2;
                            Log.i("PINFO",pinfoURL);

                            //////////////////////////////////////////////////////////////////////////
                            //        FINAL CALL FOR THE API CONTAINING PRODUCT INFORMATION         //
                            /////////////////////////////////////////////////////////////////////////
                            JsonObjectRequest productIfoRequest = new JsonObjectRequest(
                                    Request.Method.GET, pinfoURL, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response3) {
                                            Log.i("FOR THE PRODUCT", "HERE");

                                            try {
                                                ArrayList<String> nameList = new ArrayList<>();
                                                ArrayList<Integer> left = new ArrayList<>();
                                                int total = 0;
                                                JSONArray titleArr = response3.getJSONArray("products");
                                                for(int m = 0; m<titleArr.length();m++){
                                                    JSONObject param = titleArr.getJSONObject(m);
                                                    nameList.add(param.getString("title"));
                                                    JSONArray vArr = param.getJSONArray("variants");
                                                    total = 0;
                                                    int quantity = 0;
                                                    for(int j = 0; j<vArr.length(); j++){
                                                        Log.i("CREAM","In Loop");
                                                        JSONObject tempV = vArr.getJSONObject(j);
                                                        total = total + tempV.getInt("inventory_quantity");
                                                    }
                                                    left.add(total);
                                                }

                                                Log.i("TOTLA",Integer.toString(left.size()));
                                                Log.i("CHOCOLATE","Almost");
                                                Log.i("KAKE",nameList.get(3));
                                                lstView.setAdapter(new ProductAdapter(ProductActivity.this,nameList,left));



                                            }catch (JSONException e){
                                                Log.e("Title error", e.toString());
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Product error occured", error.toString());
                                        }
                                    }
                            );
                            requestQueue.add(productIfoRequest);

                        } catch (JSONException e) {
                            Log.e("Product error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error ", error.toString());
            }
        });
        requestQueue.add(objectRequest);


    }
}