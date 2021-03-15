package com.bloodFinder.mybloodbank.covid19Report.countryWiseReport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.MySingletonRequestQueue;
import com.bloodFinder.mybloodbank.common.NodeNamesForJasonData;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class CountryWiseReport extends AppCompatActivity {
    public String countryName,flagImage;
    private TextView tv_cases,tv_casesToday,tv_deaths,tv_deathsToday,tv_recovered,tv_recoveredToday,tv_active,tv_critical,tv_tests,tv_affectedCountries;
    private ImageView iv_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_wise_report2);

        countryName = getIntent().getStringExtra(Extras.COUNTRY_NAME);
        flagImage = getIntent().getStringExtra(Extras.FLAG);

        getSupportActionBar().setTitle(countryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        tv_cases = findViewById(R.id.tv_CasesValue_GlobalStateDetails_countryWiseReportActivity);
        tv_casesToday = findViewById(R.id.tv_CasesTodayValue_GlobalStateDetails_countryWiseReportActivity);
        tv_deaths = findViewById(R.id.tv_CasesDeathValue_GlobalStateDetails_countryWiseReportActivity);
        tv_deathsToday = findViewById(R.id.tv_CasesDeathsTodayValue_GlobalStateDetails_countryWiseReportActivity);
        tv_recovered = findViewById(R.id.tv_CasesRecovered_Value_GlobalStateDetails_countryWiseReportActivity);
        tv_recoveredToday = findViewById(R.id.tv_CasesRecoveredTodayValue_GlobalStateDetails_countryWiseReportActivity);
        tv_active = findViewById(R.id.tv_CasesActiveValue_GlobalStateDetails_countryWiseReportActivity);
        tv_critical = findViewById(R.id.tv_CasesCriticalValue_GlobalStateDetails_countryWiseReportActivity);
        tv_tests = findViewById(R.id.tv_CasesTestsValue_GlobalStateDetails_countryWiseReportActivity);
        tv_affectedCountries = findViewById(R.id.tv_CasesAffectedCountriesValue_GlobalStateDetails_countryWiseReportActivity);
        iv_flag = findViewById(R.id.iv_flag_countryWiseReportActivity);

        Glide.with(CountryWiseReport.this).load(flagImage).into(iv_flag);

        String url ="https://corona.lmao.ninja/v2/countries"+"/"+countryName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tv_cases.setText(response.getString(NodeNamesForJasonData.CASES));
                    tv_casesToday.setText(response.getString(NodeNamesForJasonData.CASES_TODAY));
                    tv_deaths.setText(response.getString(NodeNamesForJasonData.DEATHS));
                    tv_deathsToday.setText(response.getString(NodeNamesForJasonData.DEATHS_TODAY));
                    tv_recovered.setText(response.getString(NodeNamesForJasonData.RECOVERED));
                    tv_recoveredToday.setText(response.getString(NodeNamesForJasonData.RECOVERED_TODAY));
                    tv_active.setText(response.getString(NodeNamesForJasonData.ACTIVE));
                    tv_critical.setText(response.getString(NodeNamesForJasonData.CRITICAL));
                    tv_tests.setText(response.getString(NodeNamesForJasonData.TESTS));
                    tv_affectedCountries.setText(response.getString(NodeNamesForJasonData.AFFECTED_COUNTRIES));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CountryWiseReport.this, "Error to fetch Data"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingletonRequestQueue.getInstance(CountryWiseReport.this).addToRequestQueue(jsonObjectRequest);
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