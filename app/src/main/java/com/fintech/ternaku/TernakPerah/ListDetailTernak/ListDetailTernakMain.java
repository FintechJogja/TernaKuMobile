package com.fintech.ternaku.TernakPerah.ListDetailTernak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Setting.AppSingleton;
import com.fintech.ternaku.Setting.EditTernakActivity;
import com.fintech.ternaku.TernakPerah.DetailTernak.DetailTernakMain;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListDetailTernakMain extends AppCompatActivity {
    AdapterDetailTernakListDetailTernak adapter;
    List<ModelDetailTernalListDetailTernak> ternakList = new ArrayList<ModelDetailTernalListDetailTernak>();
    List<String> melahirkanList = new ArrayList<>();
    GridView list;
    SearchView searchView = null;
    private Button btnUrutkan,btnFilter, btnclearfilter;
    String[] listItems = {"Heat", "Menyusui", "Kering"};
    String[] listItemsurut = {"Berat - Paling Berat","Berat - Paling Ringan" , "Umur - Paling Tua", "Umur - Paling Muda"};
    private int lastLastitem;
    boolean isloading;
    int segment;
    DialogPlus dialog,dialogurut;
    boolean isFilter, isUrut;
    ArrayAdapter<String> adapterfilter,adapterurut;
    View mProgressBarFooter;
    boolean isdashboardperiksa,isdashboardbelumperiksa, isdashboardsubur, isdashboardtidaksubur,isdashboardkehamilanlainnya, isdashboardmenyusui, isdashboardmelahirkan, isdashboardmengandung,
            isdashboarddewasa,isdashboardmuda,isdashboardbayi, iskawananlainnya, isdashboardproduksi, isdashboardsakit,isdashboardsehat;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_ternak_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Data Ternak");
        }
        isdashboardsubur = false;
        isdashboardmenyusui = false;
        isdashboardmelahirkan = false;
        isdashboardmengandung = false;
        isdashboarddewasa = false;
        isdashboardmuda = false;
        isdashboardbayi = false;
        isdashboardbelumperiksa = false;
        isdashboardsakit = false;
        isdashboardsehat = false;
        isdashboardtidaksubur = false;
        isdashboardkehamilanlainnya = false;
        iskawananlainnya = false;
        isdashboardproduksi = false;

        list = (GridView) findViewById(R.id.list_listdetailternak_activity);
        segment = 1;

        animateList();
        adapter = new AdapterDetailTernakListDetailTernak(ListDetailTernakMain.this, R.layout.layout_listdetailternak_list, ternakList);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Click!!" + i, Toast.LENGTH_LONG).show();
                String idternak = ternakList.get(i).getId_ternak();
                Intent intent = new Intent(ListDetailTernakMain.this, DetailTernakMain.class);
                intent.putExtra("idternak",idternak);
                startActivity(intent);
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Click",ternakList.get(position).getId_ternak());
                showOption(ternakList.get(position).getId_ternak(), position);
                return true;
            }
        });
        isFilter = false;
        isUrut = false;
        isloading = false;
        mProgressBarFooter = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_listview, null, false);
        //list.addFooterView(mProgressBarFooter);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isloading) {
                    if (segment > 1) {
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if (lastItem == totalItemCount) {
                            isloading = true;
                            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                    + "&segment=" + segment;
                            if(isdashboarddewasa){
                                new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakDewasa(), urlParameters);
                            }
                            else if(isdashboardsubur){
                                new GetAllTernak().execute(url.getUrlGet_SemuaTernakHeat(), urlParameters);
                            }
                            else if(isdashboardtidaksubur){
                                new GetAllTernak().execute(url.getUrlGet_SemuaTernakTidakHeat(), urlParameters);
                            }
                            else if(isdashboardmuda){
                                new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakHeifers(), urlParameters);
                            }
                            else if(isdashboardbayi){
                                new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakBayi(), urlParameters);
                            }
                            else if(iskawananlainnya){
                                new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakLainnya(), urlParameters);
                            }
                            else if(isdashboardperiksa){
                                new GetAllTernak().execute(url.getUrlGet_PeriksaHariIni(), urlParameters);
                            }
                            else if(isdashboardbelumperiksa){
                                new GetAllTernak().execute(url.getUrlGet_BelumPeriksaHariIni(), urlParameters);
                            }
                            else if(isdashboardsehat){
                                new GetAllTernak().execute(url.getUrlGet_SehatHariIni(), urlParameters);
                            }
                            else if(isdashboardsakit){
                                new GetAllTernak().execute(url.getUrlGet_SakitHariIni(), urlParameters);
                            }
                            else if(isdashboardmengandung){
                                new GetAllTernak().execute(url.getUrlGet_KehamilanMengandung(), urlParameters);
                            }
                            else if(isdashboardmenyusui){
                                new GetAllTernak().execute(url.getUrlGet_KehamilanMenyusui(), urlParameters);
                            }
                            else if(isdashboardmelahirkan){
                                new GetAllTernak().execute(url.getUrlGet_KehamilanMelahirkan(), urlParameters);
                            }
                            else if(isdashboardkehamilanlainnya){
                                new GetAllTernak().execute(url.getUrlGet_KehamilanLainnya(), urlParameters);
                            }
                            else if(isdashboardproduksi){
                                new GetAllTernak().execute("http://service.ternaku.com/C_Ternak/GetSemuaProduksiSusuTernakByPeternakan", urlParameters);
                            }
                            else{
                                new GetAllTernak().execute(url.getUrlGet_SegmentList(), urlParameters);
                            }
                            Log.d("Scroll", "Bottom");
                        }
                    }
                }
            }

        });
        animateList();


        if(getIntent().hasExtra("periksa")) {
            isdashboardperiksa = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            new GetAllTernak().execute(url.getUrlGet_PeriksaHariIni(), urlParameters);
            getSupportActionBar().setTitle("Ternak sudah diperiksa");

        }
        else if(getIntent().hasExtra("belumperiksa")) {
            isdashboardbelumperiksa = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_BelumPeriksaHariIni(), urlParameters);
            getSupportActionBar().setTitle("Ternak belum periksa");

        }
        else if(getIntent().hasExtra("sehat")) {
            isdashboardsehat = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            new GetAllTernak().execute(url.getUrlGet_SehatHariIni(), urlParameters);
            getSupportActionBar().setTitle("Ternak sehat");

        }
        else if(getIntent().hasExtra("sakit")) {
            isdashboardsakit = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SakitHariIni(), urlParameters);
            getSupportActionBar().setTitle("Ternak sakit");

        }
        else if(getIntent().hasExtra("masasubur")) {
            isdashboardsubur = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SemuaTernakHeat(), urlParameters);
            getSupportActionBar().setTitle("Ternak sedang heat");

        }
        else if(getIntent().hasExtra("tidakmasasubur")){
            isdashboardtidaksubur = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            new GetAllTernak().execute(url.getUrlGet_SemuaTernakTidakHeat(), urlParameters);
            getSupportActionBar().setTitle("Ternak tidak heat");
        }
        else if(getIntent().hasExtra("menyusui")) {
            isdashboardmenyusui = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_KehamilanMenyusui(), urlParameters);
            getSupportActionBar().setTitle("Ternak menyusui");

        }
        else if(getIntent().hasExtra("melahirkan")) {
            isdashboardmelahirkan = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_KehamilanMelahirkan(), urlParameters);
            getSupportActionBar().setTitle("Ternak melahirkan");

        }
        else if(getIntent().hasExtra("mengandung")) {
            isdashboardmengandung = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_KehamilanMengandung(), urlParameters);
            getSupportActionBar().setTitle("Ternak mengandung");

        }
        else if(getIntent().hasExtra("kehamilanlainnya")) {
            isdashboardkehamilanlainnya = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_KehamilanLainnya(), urlParameters);
            getSupportActionBar().setTitle("Ternak Lainnya");
        }
        else if(getIntent().hasExtra("dewasa")) {
            isdashboarddewasa = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakDewasa(), urlParameters);
            getSupportActionBar().setTitle("Ternak Dewasa");

        }
        else if(getIntent().hasExtra("bayi")) {
            isdashboardbayi = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakBayi(), urlParameters);
            getSupportActionBar().setTitle("Ternak Bayi");

        }
        else if(getIntent().hasExtra("muda")) {
            isdashboardmuda = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;

            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakHeifers(), urlParameters);
            getSupportActionBar().setTitle("Ternak Muda");

        }
        else if(getIntent().hasExtra("kawananlainnya")) {
            iskawananlainnya = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;

            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SemuaKawananTernakLainnya(), urlParameters);
            getSupportActionBar().setTitle("Ternak Muda");

        }
        else if(getIntent().hasExtra("produksisusu")) {
            isdashboardproduksi = true;
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute("http://service.ternaku.com/C_Ternak/GetSemuaProduksiSusuTernakByPeternakan", urlParameters);
            getSupportActionBar().setTitle("Produksi Susu");
        }
        else {
            String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    + "&segment=" + segment;
            // new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetSemuaTernakByPeternakan",urlParameters);
            new GetAllTernak().execute(url.getUrlGet_SegmentList(), urlParameters);

        }
        initButton();
    }


    private class GetAllTernak extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("segment",String.valueOf(segment));
            Log.d("Ternak2",s);

            if(s.trim().equals("404"))
            {
                if(segment>1){
                    //list.removeFooterView(mProgressBarFooter);
                }else {
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan...", Toast.LENGTH_LONG).show();
                }
            }else{
                /*
                if(isdashboardsubur || isdashboardperiksa || isdashboardmelahirkan || isdashboardmenyusui || isdashboardmengandung){
                    showTernak(s);
                    list.removeFooterView(mProgressBarFooter);
                }
                else {*/
                if (segment == 1) {
                    showTernak(s);
                    Log.d("showternak", s);
                    isloading = false;
                } else if (segment > 1) {
                    showTernakMore(s);
                    Log.d("showternakmore", s);
                    isloading = false;
                }
                segment++;
                //}
            }
        }
    }

    private void initButton(){
        adapterfilter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, android.R.id.text1, listItems);

        adapterurut = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, android.R.id.text1, listItemsurut);

        dialog = DialogPlus.newDialog(ListDetailTernakMain.this)
                .setHeader(R.layout.filter_header)
                .setFooter(R.layout.filter_footer)
                .setAdapter(adapterfilter)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                        isFilter = true;
                        getSupportActionBar().setTitle("Filter : " + item.toString());

                        animateList();
                        adapter.getFilter().filter(item.toString().toLowerCase().trim(), new Filter.FilterListener() {
                            @Override
                            public void onFilterComplete(int i) {
                                if(i == 0 || isFilter){
                                    btnclearfilter.setVisibility(View.VISIBLE);
                                }else{
                                    btnclearfilter.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                })
                // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialogurut = DialogPlus.newDialog(ListDetailTernakMain.this)
                .setHeader(R.layout.sort_header)
                .setFooter(R.layout.sort_footer)
                .setAdapter(adapterurut)
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                        isUrut = true;
                        btnclearfilter.setVisibility(View.VISIBLE);
                        animateList();
                        getSupportActionBar().setTitle("Urut : " + item.toString());

                        if(item.toString().toLowerCase().equals("berat - paling berat")){
                            adapter.sort(new Comparator<ModelDetailTernalListDetailTernak>() {
                                @Override
                                public int compare(ModelDetailTernalListDetailTernak modelDetailTernalListDetailTernak, ModelDetailTernalListDetailTernak t1) {
                                    return modelDetailTernalListDetailTernak.getBerat()<t1.getBerat()?1:-1;
                                }
                            });
                        }
                        else if(item.toString().toLowerCase().equals("berat - paling ringan")) {
                            adapter.sort(new Comparator<ModelDetailTernalListDetailTernak>() {
                                @Override
                                public int compare(ModelDetailTernalListDetailTernak modelDetailTernalListDetailTernak, ModelDetailTernalListDetailTernak t1) {
                                    return modelDetailTernalListDetailTernak.getBerat()>t1.getBerat()?1:-1;
                                }
                            });
                        }
                        else if(item.toString().toLowerCase().equals("umur - paling tua")){
                            adapter.sort(new Comparator<ModelDetailTernalListDetailTernak>() {
                                @Override
                                public int compare(ModelDetailTernalListDetailTernak modelDetailTernalListDetailTernak, ModelDetailTernalListDetailTernak t1) {
                                    String dtStart1 = modelDetailTernalListDetailTernak.getTgl_lahir();
                                    String dtStart2 = t1.getTgl_lahir();
                                    Date date = new Date(),date2 = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                                    try {
                                        date = format.parse(dtStart1);
                                        date2 = format.parse(dtStart2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return date.compareTo(date2);                                }
                            });
                        }

                        else if(item.toString().toLowerCase().equals("umur - paling muda")){
                            adapter.sort(new Comparator<ModelDetailTernalListDetailTernak>() {
                                @Override
                                public int compare(ModelDetailTernalListDetailTernak modelDetailTernalListDetailTernak, ModelDetailTernalListDetailTernak t1) {
                                    String dtStart1 = modelDetailTernalListDetailTernak.getTgl_lahir();
                                    String dtStart2 = t1.getTgl_lahir();
                                    Date date = new Date(),date2 = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                                    try {
                                        date = format.parse(dtStart1);
                                        date2 = format.parse(dtStart2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return date2.compareTo(date);                                }
                            });
                        }
                    }
                })
                // This will enable the expand feature, (similar to android L share dialog)
                .create();


        btnFilter = (Button)findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Log.d("filt",String.valueOf(isFilter));

                //btnclearfilter.setVisibility(View.VISIBLE);
                findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        btnclearfilter.setVisibility(View.GONE);
                    }
                });
            }
        });
        if(isdashboardsubur){
            btnFilter.setVisibility(View.GONE);
        }

        btnUrutkan = (Button)findViewById(R.id.btnUrut);
        btnUrutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogurut.show();
                //btnclearfilter.setVisibility(View.VISIBLE);
                findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogurut.dismiss();
                        btnclearfilter.setVisibility(View.GONE);
                    }
                });
            }
        });

        btnclearfilter = (Button)findViewById(R.id.btnclearfilter);
        btnclearfilter.setVisibility(View.GONE);
        btnclearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateList();
                getSupportActionBar().setTitle("Data Ternak");

                if(isUrut){
                    adapter.sort(new Comparator<ModelDetailTernalListDetailTernak>() {
                        @Override
                        public int compare(ModelDetailTernalListDetailTernak modelDetailTernalListDetailTernak, ModelDetailTernalListDetailTernak t1) {
                            String d1 = modelDetailTernalListDetailTernak.getId_ternak(), d2 = t1.getId_ternak();
                            d1 = d1.replaceAll("TRK","");
                            d2 = d2.replaceAll("TRK","");
                            return Integer.parseInt(d1) < Integer.parseInt(d2)?1:-1;                        }
                    });
                    isUrut = false;
                }
                else {
                    adapter.getFilter().filter("", new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int i) {
                            if (i > 0) {
                                btnclearfilter.setVisibility(View.GONE);
                                isFilter = false;
                                if(isUrut){
                                    isUrut = false;
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    private void ShowMelahirkan(String result){
        try {
            JSONArray jArray = new JSONArray(result);
            Log.d("COUNT", String.valueOf(jArray.length()));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                String id = jObj.getString("id_ternak");
                melahirkanList.add(id);
            }

        } catch (JSONException e){e.printStackTrace();}
    }

    private void showTernak(String result){

        try {
            JSONArray jArray = new JSONArray(result);
            Log.d("COUNT", String.valueOf(jArray.length()));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelDetailTernalListDetailTernak ter = new ModelDetailTernalListDetailTernak();
                ter.setId_ternak(jObj.getString("id_ternak"));
                ter.setNama_ternak(jObj.getString("nama"));
                ter.setBody_condition_score(jObj.getString("bodycondition"));
                ter.setId_peternakan(jObj.getString("idpeternakan"));
                ter.setTgl_lahir(jObj.getString("tgl_lahir"));
                ter.setRfid(jObj.getString("rfid"));
                ter.setIs_dry(jObj.getInt("is_dry"));
                ter.setIs_heat(jObj.getInt("is_heat"));
                ter.setIs_menyusui(jObj.getInt("is_menyusui"));
                ter.setBerat(jObj.getDouble("beratbadan"));
                ter.setUmur(jObj.getString("usia"));
                if(isdashboardproduksi){
                    ter.setProduksisusu(jObj.getDouble("kapasitas_pagi")+jObj.getDouble("kapasitas_sore"));
                }else{
                    ter.setProduksisusu(0);
                }
                ternakList.add(ter);
            }

            adapter.notifyDataSetChanged();



        } catch (JSONException e){e.printStackTrace();}
    }

    private void showTernakMore(String result){
        Log.d("Ternak2",result);

        try {
            JSONArray jArray = new JSONArray(result);
            Log.d("COUNT", String.valueOf(jArray.length()));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelDetailTernalListDetailTernak ter = new ModelDetailTernalListDetailTernak();
                ter.setId_ternak(jObj.getString("id_ternak"));
                ter.setNama_ternak(jObj.getString("nama"));
                ter.setBody_condition_score(jObj.getString("bodycondition"));
                ter.setId_peternakan(jObj.getString("idpeternakan"));
                ter.setTgl_lahir(jObj.getString("tgl_lahir"));
                ter.setRfid(jObj.getString("rfid"));
                ter.setIs_dry(jObj.getInt("is_dry"));
                ter.setIs_heat(jObj.getInt("is_heat"));
                ter.setIs_menyusui(jObj.getInt("is_menyusui"));
                ter.setBerat(jObj.getDouble("beratbadan"));
                ter.setUmur(jObj.getString("usia"));

                ternakList.add(ter);
            }

            animateList();
            adapter.notifyDataSetChanged();

            /*
            if(isdashboardsubur)
            {
                Log.d("masuksubur","subur");
                adapter.getFilter().filter("heat", new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        //btnclearfilter.setVisibility(View.VISIBLE);
                        if(i==1) {
                        }
                        btnclearfilter.setVisibility(View.INVISIBLE);

                        getSupportActionBar().setTitle("Filter : heat");
                    }
                });
            }*/

        } catch (JSONException e){e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(ListDetailTernakMain.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_ternak, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setQueryHint("Ketik RFID atau ID Ternak");

        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.BLACK);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),"Submit",Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getApplicationContext(),"Change",Toast.LENGTH_LONG).show();
                adapter.getFilter().filter(newText);

                return false;
            }
        });


        return true;
    }

    private  void animateList(){
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);

        list.setLayoutAnimation(controller);
    }

    public void showOption(String id_ternak, final int post){
        final String idt = id_ternak;
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ListDetailTernakMain.this);
        builderSingle.setTitle("Pilih Aksi");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListDetailTernakMain.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Hapus Ternak");
        arrayAdapter.add("Ubah Data Ternak");

        builderSingle.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                Log.d("Selected",String.valueOf(which));
                if(which == 0){
                    deleteRequest(url.getUrl_DeleteTernak(),idt,post);
                }else if(which == 1){
                    Intent i = new Intent(ListDetailTernakMain.this, EditTernakActivity.class);
                    i.putExtra("id_ternak",idt);
                    startActivity(i);
                }
            }
        });
        builderSingle.show();
    }

    public void deleteRequest(String url, final String id_ter, final int pos) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(ListDetailTernakMain.this, SweetAlertDialog.PROGRESS_TYPE);
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
                        Log.d("response",response);
                        if (response.equals("1")) {
                            pDialog.dismissWithAnimation();

                            new SweetAlertDialog(ListDetailTernakMain.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Sukses!")
                                    .setContentText("Data Ternak Berhasil Dihapus")

                                    .show();

                            ternakList.remove(pos);
                            adapter.notifyDataSetChanged();
                        } else {
                            new SweetAlertDialog(ListDetailTernakMain.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Gagal!")
                                    .setContentText("Gagal Menghapus Data Ternak")
                                    .show();
                            pDialog.dismissWithAnimation();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        pDialog.dismissWithAnimation();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                    params.put("uid", getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null));
                    params.put("idternak", id_ter);

                Log.d("ParamEdit",params.toString());
                return params;
            }

        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, "SearchFLightActivity");
    }

}
