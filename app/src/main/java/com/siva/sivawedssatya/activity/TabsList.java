package com.siva.sivawedssatya.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.WeddingApplication;
import com.siva.sivawedssatya.fragments.GoingFragment;
import com.siva.sivawedssatya.fragments.MayBeFragment;
import com.siva.sivawedssatya.fragments.NotGoingFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabsList extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tab_list);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            getMembersList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == android.R.id.home) {
                //            setResult(100);
                finish();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMembersList() {

        try {
            showpDialog();
            StringRequest gettRequest = new StringRequest(Request.Method.GET,
                    "http://sivawedding.2fh.co/?username=sivasankar&password=s1v@5@nk@r@312", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("accessToken:", response);
                    hidepDialog();
                    try {
                        if (response.startsWith("{")) {

                            JSONObject resJson = new JSONObject(response);
                            JSONObject yes = resJson.getJSONObject("yes");
                            JSONObject no = resJson.getJSONObject("no");
                            JSONObject maybe = resJson.getJSONObject("maybe");

                            SharedPreferences preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("YES", yes.toString());
                            editor.putString("NO", no.toString());
                            editor.putString("MAYBE", maybe.toString());
                            editor.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("error:", volleyError.toString());
                    hidepDialog();
                }
            });
            WeddingApplication.getInstance().addToRequestQueue(gettRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new GoingFragment(), "GOING");
            adapter.addFragment(new MayBeFragment(), "MAY BE");
            adapter.addFragment(new NotGoingFragment(), "CAN'T GO");
            viewPager.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}