package com.bloodFinder.mybloodbank.covid19Report.affectedCountries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.MySingletonRequestQueue;
import com.bloodFinder.mybloodbank.common.NodeNamesForJasonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountries extends AppCompatActivity {

    private List<AffectedCountriesModel> affectedCountriesModelList;
    private AffectedCountriesAdapter affectedCountriesAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);

        getSupportActionBar().setTitle("All Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv_affectedCountry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        searchEditText = findViewById(R.id.searchEditText_AffectedCountries);

        affectedCountriesModelList = new ArrayList<>();
        affectedCountriesAdapter = new AffectedCountriesAdapter(AffectedCountries.this,affectedCountriesModelList);
        recyclerView.setAdapter(affectedCountriesAdapter);

        //filtering results
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                affectedCountriesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Api request
        String url = "https://corona.lmao.ninja/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                for(int i = 0 ; i<response.length() ; i++){
                    AffectedCountriesModel affectedCountriesModelObject = new AffectedCountriesModel();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String countryName = jsonObject.getString(NodeNamesForJasonData.COUNTRY_NAME);
                        JSONObject jsonObjectCountryInfo = jsonObject.getJSONObject(NodeNamesForJasonData.COUNTRY_INFO);
                        String flagImage = jsonObjectCountryInfo.getString(NodeNamesForJasonData.FLAG);
                        affectedCountriesModelObject.setCountryName(countryName);
                        affectedCountriesModelObject.setFlag(flagImage);
                        //Log.d("tag","country : "+countryName);
                        affectedCountriesModelList.add(affectedCountriesModelObject);
                        affectedCountriesAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AffectedCountries.this, "Failed To Retrive Data", Toast.LENGTH_SHORT).show();
            }
        });

        MySingletonRequestQueue.getInstance(AffectedCountries.this).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}