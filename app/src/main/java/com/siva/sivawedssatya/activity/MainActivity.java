package com.siva.sivawedssatya.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.WeddingApplication;
import com.siva.sivawedssatya.fragments.ContactFragment;
import com.siva.sivawedssatya.fragments.HomeFragment;
import com.siva.sivawedssatya.fragments.InvitationFragment;
import com.siva.sivawedssatya.fragments.VenueFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Siva on 20/03/16.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    EditText mEmailView;
    EditText mNameView;
    Spinner mSpinner;
    private ProgressDialog pDialog;
    AppCompatDialog dialog;
//    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
            drawerFragment.setDrawerListener(this);
            displayView(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        showDialog();
    }

    void showDialog() {
        try {
            dialog = new AppCompatDialog(this);
//        dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.setTitle("You are invited !!");

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);

            mEmailView = (EditText) dialog.findViewById(R.id.email);
            mNameView = (EditText) dialog.findViewById(R.id.name);
            mSpinner = (Spinner) dialog.findViewById(R.id.team);
            final RadioButton maybe = (RadioButton) dialog.findViewById(R.id.maybe);
            final RadioButton going = (RadioButton) dialog.findViewById(R.id.going);
            final RadioButton contgo = (RadioButton) dialog.findViewById(R.id.contgo);

            String team[] = {"Friends", "Colleagues", "Relatives"};

            // Application of the Array to the Spinner
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, team);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            mSpinner.setAdapter(spinnerArrayAdapter);
            mSpinner.setSelection(0);
            //adding button click event
            Button dismissButton = (Button) dialog.findViewById(R.id.button);
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store values at the time of the login attempt.
                    final String email = mEmailView.getText().toString();
                    final String name = mNameView.getText().toString();
                    if (validate(name, email)) {

                        String status = "";
                        if (maybe.isChecked()) {
                            status = "maybe";
                        } else if (going.isChecked()) {
                            status = "yes";
                        } else if (contgo.isChecked()) {
                            status = "no";
                        }
                        int team = mSpinner.getSelectedItemPosition();
                        invitation(name, email, status, team);
                    } else {
                        return;
                    }


                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invitation(final String fullName, final String email, final String status, final int team) {

        try {
            showpDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST,
                    "http://sivawedding.2fh.co/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("accessToken:", response);
                    WeddingApplication.showLongToast(getApplicationContext(), response);
                    System.out.println("invitation:" + response);
                    hidepDialog();
                    dialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("error:", volleyError.toString());
                    hidepDialog();
                    dialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", "sivasankar");
                    params.put("password", "s1v@5@nk@r@312");
                    params.put("email", email);
                    params.put("name", fullName);
                    params.put("team", team + "");
                    params.put("status", status);

                    return params;
    //                status,email,name,team,username,password - post
                }
            };
            WeddingApplication.getInstance().addToRequestQueue(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showpDialog() {
        try {
            if (!pDialog.isShowing())
                pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hidepDialog() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate(String fullName, String email) {

        try {
            if (fullName == null || fullName.equals("")) {
                mNameView.requestFocus();
                mNameView.setError("Field Required");
                return false;
            } else if (email == null || email.equals("")) {
                mEmailView.requestFocus();
                mEmailView.setError("Field Required");
                return false;
            } else if (!isEmailValid(email)) {
                mEmailView.requestFocus();
                mEmailView.setError("Please enter correct email !!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:

                fragment = new InvitationFragment();
//                showDialog();
                break;
            case 2:
                fragment = new VenueFragment();
                title = getString(R.string.title_venue);

                break;
            case 3:
                Intent in2 = new Intent(this, TabsList.class);
                startActivityForResult(in2, 100);
                break;
            case 4:
                fragment = new ContactFragment();
                title = getString(R.string.title_contact);
                break;
            default:
                break;
        }

        try {
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}