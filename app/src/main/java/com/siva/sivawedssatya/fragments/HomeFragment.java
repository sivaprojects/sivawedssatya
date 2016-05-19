package com.siva.sivawedssatya.fragments;

/**
 * Created by Siva on 20/03/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.WeddingApplication;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    //    TextView tv = null;
    ImageView image, image1;
    AppCompatDialog dialog;
    EditText mEmailView;
    EditText mNameView;
    Spinner mSpinner;
    private ProgressDialog pDialog;
    SharedPreferences prefs;
    View rootView;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (!prefs.getBoolean("firstTime", false)) {
                // <---- run your one time code here
                showDialog();
            }

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);


            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Arizonia-Regular.ttf");
            Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "Pacifico.ttf");
            Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "KaushanScript-Regular.otf");

            TextView siva_label1 = (TextView) rootView.findViewById(R.id.siva_label1);
            TextView satya_label1 = (TextView) rootView.findViewById(R.id.satya_label1);
            TextView siva_label = (TextView) rootView.findViewById(R.id.siva_label);
            TextView satya_label = (TextView) rootView.findViewById(R.id.satya_label);
            TextView label = (TextView) rootView.findViewById(R.id.label);
//        ImageView image = (ImageView) rootView.findViewById(R.id.image);
//        ImageView image1 = (ImageView) rootView.findViewById(R.id.image1);
//        image1.setOnClickListener(new View.OnClickListener() {
//            // Start new list activity
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(getActivity(),
//                        ImageViewActivity.class);
//                startActivity(mainIntent);
//            }
//        });
//
//        image.setOnClickListener(new View.OnClickListener() {
//            // Start new list activity
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(getActivity(),
//                        ImageViewActivity.class);
//                startActivity(mainIntent);
//            }
//        });

            siva_label1.setTypeface(typeface1);
            satya_label1.setTypeface(typeface1);
            siva_label.setTypeface(typeface);
            satya_label.setTypeface(typeface);
            label.setTypeface(typeface2);

            image = (ImageView) rootView.findViewById(R.id.image);

            Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
            bm = getCircularBitmapWithWhiteBorder(bm, 10);
            image.setImageBitmap(bm);

            image1 = (ImageView) rootView.findViewById(R.id.image1);

            Bitmap bm1 = ((BitmapDrawable) image1.getDrawable()).getBitmap();
            bm1 = getCircularBitmapWithWhiteBorder(bm1, 10);
            image1.setImageBitmap(bm1);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    void showDialog() {
        try {
            dialog = new AppCompatDialog(getActivity());

            dialog.setContentView(R.layout.dialog_layout);
            dialog.setTitle("You are invited !!");
            dialog.setCancelable(false);

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
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, team);
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

                        // mark first time has runned.
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.commit();
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


    public void invitation(final String fullName, final String email, final String status, final int team) {

        try {
            showpDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST,
                    "http://sivawedding.2fh.co/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("accessToken:", response);
                    if(response.equals("Successfully Saved")){
                        WeddingApplication.showLongToast(getActivity(), "Guest List has been updated");
                    } else {
                        WeddingApplication.showLongToast(getActivity(), response);
                    }

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
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth) {
        Bitmap canvasBitmap = null;
        try {
            if (bitmap == null || bitmap.isRecycled()) {
                return null;
            }

            final int width = bitmap.getWidth() + borderWidth;
            final int height = bitmap.getHeight() + borderWidth;

            canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canvasBitmap;
    }
}
