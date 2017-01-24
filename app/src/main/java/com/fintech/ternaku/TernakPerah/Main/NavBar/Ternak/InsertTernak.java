package com.fintech.ternaku.TernakPerah.Main.NavBar.Ternak;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertTernak extends AppCompatActivity {
    private TextView txtNama,txtRFID, txtTgl;
    private EditText txtBrt;
    private Button btnTambah,btnPerbaharui;
    float counter=200;
    private int flag_error_nama=0,flag_error_berat=0;
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


    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setTitle("Ternak Baru");
        }

        //Initiate All------------------------------------
        dialog_picker_berat();


        txtRFID = (TextView)findViewById(R.id.txtRFID);
        txtRFID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtRFID.length()==10){
                    txtRFID.setEnabled(false);
                }
            }
        });
        btnPerbaharui = (Button)findViewById(R.id.btnPerbaharui);
        btnPerbaharui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRFID.setEnabled(true);
                txtRFID.setText("");
                txtRFID.setHint("Dekatkan Tag RFID");
            }
        });

        txtNama = (TextView)findViewById(R.id.nama_ternak);
        txtBrt = (EditText) findViewById(R.id.berat_ternak);
        txtTgl = (TextView) findViewById(R.id.txtTgl);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        txtTgl.setText(dateFormatter.format(Calendar.getInstance().getTime()));

        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showinputnama();
            }
        });

        setDateTimeField();

        radioKelaminGroup = (RadioGroup) findViewById(R.id.radioSex);
        btnTambah = (Button)findViewById(R.id.btnInsertTernak);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check input box---
                if(checkform()){
                    insertDB();
                }else{
                    new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }

            }
        });

        txtTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

    }



    private void insertDB(){

        int selectedId=radioKelaminGroup.getCheckedRadioButtonId();
        radioKelamin=(RadioButton) findViewById(selectedId);

        //Insert Database--
        new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Simpan")
                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                .setConfirmText("Ya")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                        if(TextUtils.isEmpty(txtRFID.getText().toString())){
                            final Calendar c = Calendar.getInstance();
                            Log.d("TAG_INSERT",txtNama.getText().toString()+txtBrt.getText().toString()+
                                    getSubStractYear (dateFormatter.format(Calendar.getInstance().getTime()), txtTgl.getText().toString())
                                    +radioKelamin.getText().toString());
                            String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null).trim()
                                    +"&namaternak=" + txtNama.getText().toString()
                                    +"&jeniskelamin=" + radioKelamin.getText().toString()
                                    +"&tanggallahirternak=" + txtTgl.getText().toString()

                                    +"&beratbadan=" + txtBrt.getText().toString()
                                    +"&rfidcode=" + txtRFID.getText().toString();
                            new insertTernakTask().execute(url.getUrl_InsertTernak(), urlParameters);
                        }
                        else {
                            //Cek RFID---------------------------------
                            Connection c = new Connection();
                            String urlParameters2;
                            urlParameters2 = "rfid=" + txtRFID.getText().toString() +
                                    "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                            new CheckRFID().execute(url.getUrlGet_RFIDCek(), urlParameters2);
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

        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");


    }

    @Override
    protected void onResume(){
        super.onResume();
        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
                    txtRFID.setText(msg.obj.toString().trim());
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);

                    break;
            }
        }
    };

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES_Insert",result);
            pDialog.dismiss();
            if (result.trim().equals("0")) {
                final Calendar c = Calendar.getInstance();
                Log.d("TAG_INSERT",txtNama.getText().toString()+txtBrt.getText().toString()+
                        getSubStractYear (dateFormatter.format(Calendar.getInstance().getTime()), txtTgl.getText().toString())
                        +radioKelamin.getText().toString());
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null).trim()
                        +"&namaternak=" + txtNama.getText().toString()
                        +"&jeniskelamin=" + radioKelamin.getText().toString()
                        +"&tanggallahirternak=" + txtTgl.getText().toString()

                        +"&beratbadan=" + txtBrt.getText().toString()
                        +"&rfidcode=" + txtRFID.getText().toString();
                new insertTernakTask().execute(url.getUrl_InsertTernak(), urlParameters);
            }else{
                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai ")
                        .show();
            }
        }
    }


    private class insertTernakTask extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES_Insert",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Peternak")
                                        .setContentText("Apakah Ingin Menambah Data Ternak Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                txtRFID.setEnabled(true);
                                                txtRFID.setText("");
                                                txtRFID.setHint("Dekatkan Tag RFID");
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
            }
            else if(result.trim().equals("2")){

                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Penambahan Gagal, Silahkan Simpan Data Kembali")
                        .show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getSubStractYear ( String date, String time )
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMMM yyyy" );
            Date myDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.YEAR , Integer.valueOf("-"+time));

            return ( sdf.format( calendar.getTime() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return "";
        }
    }


    protected void showinputnama() {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(InsertTernak.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InsertTernak.this);
        alertDialogBuilder.setView(promptView);
        int a=0;
        final EditText editText = (EditText) promptView.findViewById(R.id.edtnamaternak);

        final InputMethodManager imgr = (InputMethodManager) InsertTernak.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

    //Dialog Box Set Berat------------------------------------
    private void dialog_picker_berat(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_numberpicker,null);

        //Set Picker Tahun------------------------------
        final NumberPicker numberPicker_all = (NumberPicker) view.findViewById(R.id.numberPicker_all);
        numberPicker_all.setMinValue(50);
        numberPicker_all.setMaxValue(500);
        numberPicker_all.setWrapSelectorWheel(true);
        setNumberPickerDividerColour(numberPicker_all,view);

        dialog_picker_berat = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(view))
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();

        dialog_picker_berat.findViewById(R.id.button_numberpicker_all_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_picker_berat.dismiss();
                txtBrt.setText(String.valueOf(numberPicker_all.getValue()).trim());
            }
        });
        dialog_picker_berat.findViewById(R.id.button_numberpicker_all_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_picker_berat.dismiss();
            }
        });
    }
    private void setNumberPickerDividerColour(NumberPicker number_picker,View mContext){
        final int count = number_picker.getChildCount();

        for(int i = 0; i < count; i++){

            try{
                Field dividerField = number_picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(mContext.getResources().getColor(R.color
                        .colorPrimary));
                dividerField.set(number_picker,colorDrawable);

                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTxtClr", e);
            }
        }
    }

    public boolean checkform(){
        boolean value = true;
        if(txtNama.getText().toString().equalsIgnoreCase("Isikan nama ternak disini")||
                txtNama.getText().toString().equalsIgnoreCase("")){
            value= false;
            txtNama.setError("Isikan Nama Ternak");
        }
        /*
        if(TextUtils.isEmpty(txtRFID.getText().toString())){
            value= false;
            txtRFID.setError("Isikan No RFID");
        }*/
        if(txtBrt.getText().toString().equalsIgnoreCase("Isikan berat ternak disini")){
            value= false;
            txtBrt.setError("Isikan Berat");
        }
        if(TextUtils.isEmpty(txtTgl.getText().toString())){
            value= false;
            txtTgl.setError("Isikan Umur");
        }
        if(TextUtils.isEmpty(txtBrt.getText().toString())){
            value= false;
            txtBrt.setError("Isikan Berat");
        }

        return  value;
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

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
            Intent i = new Intent(InsertTernak.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
