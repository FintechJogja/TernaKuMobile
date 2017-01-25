package com.fintech.ternaku.TernakPotong.AddPakan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.TernakPotong.KompisisiPakan.KomposisiPakanActivity;
import com.fintech.ternaku.TernakPotong.SapiPotongAddKomposisiPakan;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.fintech.ternaku.R.id.textView;
import static com.fintech.ternaku.R.id.time;

public class AddPakanPedaging extends AppCompatActivity {

    //Initiate Data-------------------------------------
    private ListView list_addpakanpedaging_activity;
    private AutoCompleteTextView input_addpakanpedaging_activity_idternak;
    ArrayList<String> list_addpakanpedaging_activity_idternak = new ArrayList<String>();
    ArrayAdapter<String> adp;
    TextView input_addpakanpedaging_activity_konsentrat;
    private LinearLayout linearlayout_addpakanpedaging_activity_loading,linearlayout_addpakanpedaging_activity_main;
    private String idpeternakan;
    List<ModelAddPakanPedaging> listdata_addpakanpedaging_activity_temp = new ArrayList<ModelAddPakanPedaging>();
    List<ModelAddPakanPedaging> listdata_addpakanpedaging_activity_main = new ArrayList<ModelAddPakanPedaging>();
    AdapterAddPakanPedaging adapterAddPakanPedaging;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    //Get Current Time----------------------
    public static final String inputFormat = "hh:mm a";

    private Date date;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    private String compareStringOne = "00:01";
    private String compareStringTwo = "11:59";
    String time;
    Calendar calender;
    SimpleDateFormat simpleDateFormat;

    //Set Data Komposisi----------------------
    int choosenindexkomposisi;
    Dialog dialog_list_komposisi;
    AdapterAddPakanPedagingListKomposisi adapter_list_komposisi;
    ArrayList<ModelAddPakanPedagingListKomposisi> KomposisiList  = new ArrayList<ModelAddPakanPedagingListKomposisi>();

    //Set Up RFID-----------------------------
    private Bluetooth bt;
    public final String TAG = "AddPakan";

    //Insert Pakan----------------------------
    private int status_insert=1;
    private int status_complete=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pakan_pedaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Insert Pakan");
        }
        hideSoftKeyboard();

        //Initiate Input Komposisi-----------------------------------------------------------
        input_addpakanpedaging_activity_konsentrat = (TextView) findViewById(R.id.input_addpakanpedaging_activity_konsentrat);
        input_addpakanpedaging_activity_konsentrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
                new GetKomposisi().execute(url.getUrl_GetKomposisi(), urlParameters);
            }
        });
        input_addpakanpedaging_activity_konsentrat.addTextChangedListener(CheckKonsentrat);

        //Initiate TextWatcher-------------------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addpakanpedaging_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addpakanpedaging_activity_idternak);
        input_addpakanpedaging_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addpakanpedaging_activity_idternak);
        input_addpakanpedaging_activity_idternak.setAdapter(adp);
        input_addpakanpedaging_activity_idternak.addTextChangedListener(CheckId);

        //Initiate ListView----------------------------------------------------------
        list_addpakanpedaging_activity = (ListView)findViewById(R.id.list_addpakanpedaging_activity);

        //Initial----------------------------------------------------
        linearlayout_addpakanpedaging_activity_loading = (LinearLayout)findViewById(R.id.linearlayout_addpakanpedaging_activity_loading);
        linearlayout_addpakanpedaging_activity_main = (LinearLayout) findViewById(R.id.linearlayout_addpakanpedaging_activity_main);
        idpeternakan =  getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        choosenindexkomposisi = -1;
        if(input_addpakanpedaging_activity_konsentrat.getText().toString().equalsIgnoreCase("")){
            InitiateUI();
        }

        //Set RFID Bluetooth----------------------------------------------------------
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
                    final String rfid = msg.obj.toString();
                    input_addpakanpedaging_activity_idternak.setText(rfid);
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
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

    private void InitiateUI(){
        linearlayout_addpakanpedaging_activity_loading.setVisibility(View.VISIBLE);
        linearlayout_addpakanpedaging_activity_main.setVisibility(View.GONE);
    }

    private void RefreshUI(){
        linearlayout_addpakanpedaging_activity_loading.setVisibility(View.GONE);
        linearlayout_addpakanpedaging_activity_main.setVisibility(View.VISIBLE);
    }

    //Set AutoComplete-----------------------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Memuat Data");
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
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!Silahkan Pilih Ternak Lagi")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else{
                AddTernakToList(result);
                input_addpakanpedaging_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_addpakanpedaging_activity_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addpakanpedaging_activity_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Kandang--------------------------------------------------------
    private class GetKomposisi extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Memuat Data");
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
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Komposisi Masih Kosong" + "\nApakah Ingin Memasukkan Data Komposisi?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddPakanPedaging.this, KomposisiPakanActivity.class));
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
            else{
                AddKomposisiToList(result);
            }
        }
    }

    private void AddKomposisiToList(String result) {
        KomposisiList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelAddPakanPedagingListKomposisi k = new ModelAddPakanPedagingListKomposisi();
                k.setId_komposisi(jObj.getString("id_komposisi"));
                k.setJenis_sapi(jObj.getString("jenis_sapi"));
                k.setUmur_bawah(jObj.getString("usia_bawah"));
                k.setUmur_atas(jObj.getString("usia_atas"));
                k.setKg_konsentrat(jObj.getString("kg_konsentrat"));
                k.setKg_hijauan(jObj.getString("kg_hijauan"));
                k.setTotal_harga(jObj.getString("total_harga"));
                KomposisiList.add(k);
            }
            ShowDialogKomposisi(KomposisiList);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void ShowDialogKomposisi(final ArrayList<ModelAddPakanPedagingListKomposisi> Komposisi) {
        dialog_list_komposisi = new Dialog(AddPakanPedaging.this);
        dialog_list_komposisi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_list_komposisi.setContentView(R.layout.list_komposisi_addpakanpedaing);

        ListView list = (ListView) dialog_list_komposisi.findViewById(R.id.list_komposisi_dial);
        EditText edtsearch = (EditText) dialog_list_komposisi.findViewById(R.id.edtSearchDialogKomposisi);
        Button btnbtl = (Button) dialog_list_komposisi.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_list_komposisi.dismiss();
            }
        });

        adapter_list_komposisi = new AdapterAddPakanPedagingListKomposisi(getApplicationContext(), Komposisi);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkomposisi = i;
                input_addpakanpedaging_activity_konsentrat.setText(Komposisi.get(choosenindexkomposisi).getId_komposisi());
                //hideSoftKeyboard_event(AddPakanPedaging.this);
                dialog_list_komposisi.dismiss();
                input_addpakanpedaging_activity_idternak.setText("");
                input_addpakanpedaging_activity_idternak.setVisibility(View.VISIBLE);
            }
        });
        list.setAdapter(adapter_list_komposisi);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //InsTernakToKandangActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter_list_komposisi.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog_list_komposisi.show();
    }

    private String compareDates(){
        /*Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);
        int status = now.get(Calendar.AM_PM);

        date = parseDate(hour + ":" + minute + " " + status);*/
        calender = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calender.getTime());
        date = parseDate(time);
        Log.d("Time:",String.valueOf(time));
        dateCompareOne = parseDate(compareStringOne);
        dateCompareTwo = parseDate(compareStringTwo);

        if ( dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
            return "Pagi";
        }else{
            return "Sore";
        }
    }

    private String getCurrentDate(){
        calender = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("d MMM yyyy hh:mm a");
        time = simpleDateFormat.format(calender.getTime());
        return time;
    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private final TextWatcher CheckKonsentrat = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if(listdata_addpakanpedaging_activity_temp.size()==0&&listdata_addpakanpedaging_activity_main.size()==0){
                    RefreshUI();
                }else{
                    SetList();
                    RefreshUI();
                }
                //Toast.makeText(getApplicationContext(),"Isi Huruf : "+ String.valueOf(s.length()),Toast.LENGTH_LONG).show();
            }else{
                InitiateUI();
            }
        }
    };

    private final TextWatcher CheckId = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 10||(input_addpakanpedaging_activity_idternak.getText().toString().contains("TRK")&&s.length()>=13)) {
                String urlParameters2;
                urlParameters2 = "id=" + input_addpakanpedaging_activity_idternak.getText().toString().trim() +
                        "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                new CheckRFID().execute(url.getUrlGet_RFIDanIdCek(), urlParameters2);

                //Toast.makeText(getApplicationContext(),"Isi Huruf : "+ String.valueOf(s.length()),Toast.LENGTH_LONG).show();
            }
        }
    };

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.PROGRESS_TYPE);

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
            if(result.trim().equals("1")) {
                SetData();
                input_addpakanpedaging_activity_idternak.setText("");
            }else{
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID atau Id Ternak Ditemukan, Silahkan Masukkan Dengan Benar")
                        .show();
            }
        }
    }

    private void SaveData(){

        for(int i=listdata_addpakanpedaging_activity_temp.size();i>0;i--){
            int count = i-1;
            String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                    + "&idternak=" + listdata_addpakanpedaging_activity_temp.get(count).getId_ternak().toString().trim()
                    + "&idkomposisi=" + listdata_addpakanpedaging_activity_temp.get(count).getJenis_konsentrat().toString().trim()
                    + "&tglmakan=" + listdata_addpakanpedaging_activity_temp.get(count).getTgl_makan().toString().trim()
                    + "&sesimakan=" + listdata_addpakanpedaging_activity_temp.get(count).getSesi_makan().toString().trim();
            Log.d("CEK_INSERT",urlParameters);
            new InsertPakan().execute("http://developer.ternaku.com/C_HistoryMakan/InsertPemakaianPakan2", urlParameters);

            //Cek Koneksi Data dan Hilangkan Dari List-----------------------------------------------------
            if(status_insert==1&&count>0){
                ClearListData(i);
            }else if(status_insert==1&&count==0) {
                ClearListData(i);
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }else{
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Koneksi Terputus")
                        .setContentText("Data Gagal Dimasukkan, Silahkan Simpan Data Kembali")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private void ClearListData(int i){
        listdata_addpakanpedaging_activity_temp.remove(i-1);
        Log.d("Data ke ", String.valueOf(i-1) + String.valueOf(listdata_addpakanpedaging_activity_temp.size()));
        adapterAddPakanPedaging.notifyDataSetChanged();
    }

    //Insert To Database--------------------------------------------------------
    private class InsertPakan extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            return;
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
            if(result.equalsIgnoreCase("1")){
                status_insert=1;
            }else if(result.equalsIgnoreCase("kosong")){
                status_insert=0;
            }

        }
    }

    private void SetList(){
        listdata_addpakanpedaging_activity_temp.clear();
        Log.d("IsiList",String.valueOf(listdata_addpakanpedaging_activity_main.size()));
        for(int i=0;i<listdata_addpakanpedaging_activity_main.size();i++) {
            if(listdata_addpakanpedaging_activity_main.get(i).getJenis_konsentrat().trim().equalsIgnoreCase(input_addpakanpedaging_activity_konsentrat.getText().toString().trim())){
                listdata_addpakanpedaging_activity_temp.add(listdata_addpakanpedaging_activity_main.get(i));
            }
        }

        Log.d("IsiList",String.valueOf(listdata_addpakanpedaging_activity_temp.size()));
        adapterAddPakanPedaging = new AdapterAddPakanPedaging(AddPakanPedaging.this, R.layout.holder_list_addpakan_pedaging, listdata_addpakanpedaging_activity_temp);
        list_addpakanpedaging_activity.setAdapter(adapterAddPakanPedaging);
    }

    private void SetData(){
        ModelAddPakanPedaging mAddPakan = new ModelAddPakanPedaging();
        mAddPakan.setId_ternak(input_addpakanpedaging_activity_idternak.getText().toString().trim());
        mAddPakan.setSesi_makan(String.valueOf(compareDates()));
        mAddPakan.setTgl_makan(String.valueOf(getCurrentDate()));
        mAddPakan.setJenis_konsentrat(input_addpakanpedaging_activity_konsentrat.getText().toString().trim());
        listdata_addpakanpedaging_activity_temp.add(mAddPakan);
        listdata_addpakanpedaging_activity_main.add(mAddPakan);

        adapterAddPakanPedaging = new AdapterAddPakanPedaging(AddPakanPedaging.this, R.layout.holder_list_addpakan_pedaging, listdata_addpakanpedaging_activity_temp);
        list_addpakanpedaging_activity.setAdapter(adapterAddPakanPedaging);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_pakan, menu);
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
            return true;
        }else if(id == R.id.action_save){
            SaveData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard_event (Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
