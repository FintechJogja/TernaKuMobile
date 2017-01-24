package com.fintech.ternaku.Setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.TernakPerah.Main.NavBar.Ternak.InsertTernak;
import com.fintech.ternaku.UrlList;
import com.orhanobut.dialogplus.DialogPlus;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditTernakActivity extends AppCompatActivity {

    UrlList url = new UrlList();
    private TextView txtNama, txtRFID, txtTgl;
    private EditText txtBrt;
    private Button btnTambah, btnPerbaharui;
    float counter = 200;
    private int flag_error_nama = 0, flag_error_berat = 0;
    private int year;
    private int month;
    private int day;
    private DatePicker dpResult;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private RadioGroup radioKelaminGroup;
    private RadioButton radioKelamin;
    private DialogPlus dialog_picker_berat;
    String id_ternak;

    String old_rfid;

    boolean isPerbaruiRFid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Ternak");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id_ternak = getIntent().getStringExtra("id_ternak");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        //JSONRequest();

        Log.d("URLEDIT", url.getUrl_GetTernakByID());

        txtRFID = (TextView) findViewById(R.id.txtRFID);
        txtRFID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtRFID.length() == 10) {
                    txtRFID.setEnabled(false);
                }
            }
        });
        btnPerbaharui = (Button) findViewById(R.id.btnPerbaharui);
        btnPerbaharui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRFID.setEnabled(true);
                txtRFID.setText("");
                txtRFID.setHint("Dekatkan Tag RFID");
                if(!isPerbaruiRFid) {
                    isPerbaruiRFid = true;
                }else{
                    isPerbaruiRFid = false;
                }
            }
        });

        txtNama = (TextView) findViewById(R.id.nama_ternak);
        txtBrt = (EditText) findViewById(R.id.berat_ternak);
        txtTgl = (TextView) findViewById(R.id.txtTgl);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        //txtTgl.setText(dateFormatter.format(Calendar.getInstance().getTime()));

        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showinputnama();
            }
        });


        radioKelaminGroup = (RadioGroup) findViewById(R.id.radioSex);
        btnTambah = (Button) findViewById(R.id.btnInsertTernak);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check input box---
                if (checkform()) {
                    String urlParameters2 = "rfid=" + txtRFID.getText().toString() +
                            "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);

                    if(isPerbaruiRFid) {
                        new CheckRFID().execute(url.getUrlGet_RFIDCek(), urlParameters2);
                    }{
                        JSONRequest(url.getUrl_EditTernak(),false,true);
                    }


                } else {
                    new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }

            }
        });
        setDateTimeField();
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);


        txtTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        JSONRequest(url.getUrl_GetTernakByID(),true,false);

    }

    protected void showinputnama() {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(EditTernakActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTernakActivity.this);
        alertDialogBuilder.setView(promptView);
        int a = 0;
        final EditText editText = (EditText) promptView.findViewById(R.id.edtnamaternak);
        editText.setText(txtNama.getText());

        final InputMethodManager imgr = (InputMethodManager) EditTernakActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        txtNama.setText(editText.getText());
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //imgr.toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private class CheckRFID extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Menyimpan Data");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES_Insert", result);
            pDialog.dismiss();
            if (result.trim().equals("0")) {
                JSONRequest(url.getUrl_EditTernak(),false,true);
            } else {
                new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai ")
                        .show();
            }
        }
    }

    private void makeJsonArrayRequest(String url) {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("EditTernakActivity", response.toString());

                        try {

                            JSONObject jObj = response.getJSONObject(0);

                            // Parsing json array response
                            // loop through each json object
                            //jsonResponse = "";
                            /*
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                                jsonResponse += "Mobile: " + mobile + "\n\n\n";

                            }

                            txtResponse.setText(jsonResponse);
                            */

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("EditTernak", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(req, "Edit");
    }

    public boolean checkform() {
        boolean value = true;
        if (txtNama.getText().toString().equalsIgnoreCase("Isikan nama ternak disini") ||
                txtNama.getText().toString().equalsIgnoreCase("")) {
            value = false;
            txtNama.setError("Isikan Nama Ternak");
        }
        if (TextUtils.isEmpty(txtRFID.getText().toString())) {
            value = false;
            txtRFID.setError("Isikan No RFID");
        }
        if (txtBrt.getText().toString().equalsIgnoreCase("Isikan berat ternak disini")) {
            value = false;
            txtBrt.setError("Isikan Berat");
        }
        if (TextUtils.isEmpty(txtTgl.getText().toString())) {
            value = false;
            txtTgl.setError("Isikan Umur");
        }
        if (TextUtils.isEmpty(txtBrt.getText().toString())) {
            value = false;
            txtBrt.setError("Isikan Berat");
        }

        return value;
    }


    public void JSONRequest(String url, final boolean isGetDetail, final boolean isUpdate) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
        pDialog.setTitleText("Merubah data..");
        pDialog.setCancelable(false);
        pDialog.show();
        Log.d("URL", url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Response", response);
                            if(isGetDetail) {
                                JSONArray ternakArr = new JSONArray(response);
                                JSONObject jObj = ternakArr.getJSONObject(0);
                                txtRFID.setText(jObj.getString("rfid_code"));
                                old_rfid = jObj.getString("rfid_code");

                                txtNama.setText(jObj.getString("nama_ternak"));
                                txtBrt.setText(jObj.getString("berat_lahir"));
                                txtTgl.setText(getFormattedDate(jObj.getString("tgl_lahir")));
                                pDialog.dismissWithAnimation();
                            }
                            else if(isUpdate){
                                if(response.equals("1")){
                                    pDialog.dismissWithAnimation();

                                    new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Sukses!")
                                            .setContentText("Data Ternak Berhasil Dirubah")

                                            .show();
                                }else{
                                    new SweetAlertDialog(EditTernakActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Gagal!")
                                            .setContentText("Gagal Merubah Data Ternak")
                                            .show();
                                    pDialog.dismissWithAnimation();
                                }
                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if(isGetDetail) {
                    params.put("uid", getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null));
                    params.put("idternak", id_ternak);
                }else if(isUpdate){
                    int selectedId=radioKelaminGroup.getCheckedRadioButtonId();
                    radioKelamin=(RadioButton) findViewById(selectedId);


                    params.put("uid", getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null));
                    params.put("idternak", id_ternak);
                    params.put("namaternak", txtNama.getText().toString());
                    params.put("jeniskelaminternak", radioKelamin.getText().toString());
                    params.put("tanggallahirternak", txtTgl.getText().toString());
                    params.put("beratbadan", txtBrt.getText().toString());
                    if(isPerbaruiRFid) {
                        params.put("rfid", txtBrt.getText().toString());
                    }else{
                        params.put("rfid", old_rfid);
                    }

                }

                Log.d("ParamEdit",params.toString());
                return params;
            }

        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, "SearchFLightActivity");
    }

    private void refreshData(){
        JSONRequest(url.getUrl_GetTernakByID(),true,false);
    }
    private String getFormattedDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date myDate = sdf.parse(date);

            return dateFormatter.format(myDate.getTime());

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getAge(String birthdate){
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss");
        final LocalDate dt = dtf.parseLocalDate(birthdate);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(dt, now);

        return String.valueOf(age.getYears());
    }

    private void setDateTimeField() {
        //toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtTgl.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
