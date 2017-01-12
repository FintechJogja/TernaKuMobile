package com.fintech.ternaku.TernakPerah.Main.NavBar.ProduksiSusu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProduksiSusu extends AppCompatActivity {
    private AutoCompleteTextView input_addproduksisusu_activity_idternak;
    private TextView input_addproduksisusu_activity_tglpemeriksaan;
    private Spinner spinner_addproduksisusu_activity_sesiperah;
    private EditText input_addproduksisusu_activity_durasiperah;
    private TextView input_addproduksisusu_activity_kapasitas;
    private SeekBar seekbar_addproduksisusu_activity_kapasitas;
    private Button button_addproduksisusu_activity_simpan;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TimePickerDialog mTimePicker;
    String datetime;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produksi_susu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setTitle("Tambah Produksi Susu");
        }

        //Set Auto Complete-----------------------------
        input_addproduksisusu_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addproduksisusu_activity_idternak);

        //Set Tanggal----------------------------------
        setDateTimeField();
        setTime();
        input_addproduksisusu_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addproduksisusu_activity_tglpemeriksaan);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addproduksisusu_activity_tglpemeriksaan.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        input_addproduksisusu_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddProduksiSusu.this);
                fromDatePickerDialog.show();
            }
        });

        //Set Durasi Perah dan Sesi------------------------------
        spinner_addproduksisusu_activity_sesiperah = (Spinner)findViewById(R.id.spinner_addproduksisusu_activity_sesiperah);
        String dataSesi[] = {"Silahkan Pilih Sesi Pemerahan","Pagi","Sore"};
        ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataSesi);
        spinner_addproduksisusu_activity_sesiperah.setAdapter(myAdapter);
        input_addproduksisusu_activity_durasiperah = (EditText)findViewById(R.id.input_addproduksisusu_activity_durasiperah);

        //Set Seekbar Kapasitas---------------------------------
        input_addproduksisusu_activity_kapasitas = (TextView)findViewById(R.id.input_addproduksisusu_activity_kapasitas);
        seekbar_addproduksisusu_activity_kapasitas = (SeekBar)findViewById(R.id.seekbar_addproduksisusu_activity_kapasitas);
        seekbar_addproduksisusu_activity_kapasitas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //float value = ((float)i / 10);
                input_addproduksisusu_activity_kapasitas.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Insert To Database------------------------------------
        button_addproduksisusu_activity_simpan = (Button)findViewById(R.id.button_addproduksisusu_activity_simpan);

        button_addproduksisusu_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    if(spinner_addproduksisusu_activity_sesiperah.getSelectedItemId()==0){
                        new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Sesi Perah")
                                .show();
                    }else{
                        new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Simpan")
                                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();

                                        //Cek RFID---------------------------------
                                        Connection c = new Connection();
                                        String urlParameters2;
                                        urlParameters2 = "id=" + input_addproduksisusu_activity_idternak.getText().toString().trim() +
                                                "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                        String json = c.GetJSONfromURL(url.getUrlGet_RFIDanIdCek(), urlParameters2);
                                        if(json.trim().equals("1")) {
                                            String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                                    + "&idternak=" + input_addproduksisusu_activity_idternak.getText().toString()
                                                    + "&tglperah=" + input_addproduksisusu_activity_tglpemeriksaan.getText().toString()
                                                    + "&sesiperah=" + spinner_addproduksisusu_activity_sesiperah.getSelectedItem().toString().trim()
                                                    + "&kapasitas=" + input_addproduksisusu_activity_kapasitas.getText().toString()
                                                    + "&durasi="+input_addproduksisusu_activity_durasiperah.getText().toString();
                                            new AddProduksiSusuInsertToDatabase().execute(url.getUtl_InsertProduksiSusu(), param);
                                            Log.d("Param",param);
                                        }else{
                                            new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Peringatan!")
                                                    .setContentText("Tidak Ada RFID Ditemukan")
                                                    .show();
                                        }
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                    }
                }
                else {
                    new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }

        });


    }

    //Insert To Database-----------------------------------------------
    private class AddProduksiSusuInsertToDatabase extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.PROGRESS_TYPE);
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
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Produksi Susu")
                                        .setContentText("Apakah Ingin Menambah Data Produksi Susu Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                cleartext();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }else if(result.trim().equals("0")){
                new SweetAlertDialog(AddProduksiSusu.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addproduksisusu_activity_tglpemeriksaan.setText(dateFormatter.format(newDate.getTime()));
                datetime = dateFormatter.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {

                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addproduksisusu_activity_tglpemeriksaan.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            Intent i = new Intent(AddProduksiSusu.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cleartext(){
        input_addproduksisusu_activity_idternak.setText("");
        input_addproduksisusu_activity_tglpemeriksaan.setText("01 Januari 1970");
        input_addproduksisusu_activity_durasiperah.setText("");
        input_addproduksisusu_activity_idternak.setHint("Ketikkan ID Sapi atau Scan RFID");
        input_addproduksisusu_activity_durasiperah.setHint("Masukkan Durasi Perah");
        seekbar_addproduksisusu_activity_kapasitas.setProgress(0);
    }

    public boolean cekForm(){
        boolean cek = true;
        if(input_addproduksisusu_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_addproduksisusu_activity_idternak.setError("Data belum diisi");
        }
        if(input_addproduksisusu_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addproduksisusu_activity_tglpemeriksaan.setError("Data belum diisi");
        }

        if(input_addproduksisusu_activity_durasiperah.getText().toString().matches("")){
            cek=false;
            input_addproduksisusu_activity_durasiperah.setError("Data belum diisi");
        }
        return cek;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
