package com.fintech.ternaku.TernakPerah.DetailTernak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Setting.EditTernakActivity;
import com.fintech.ternaku.TernakPerah.DetailTernak.Event.AdapterDetailTernakEvent;
import com.fintech.ternaku.TernakPerah.DetailTernak.Event.ModelDetailTernakEvent;
import com.fintech.ternaku.TernakPerah.DetailTernak.Task.AdapterDetailTernakTask;
import com.fintech.ternaku.TernakPerah.DetailTernak.Task.ModelDetailTernakTask;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.UrlList;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DetailTernakMain extends AppCompatActivity {
    ExpandableRelativeLayout expander_profile_layout_fertility, expander_profile_layout_production, expander_profile_layout_health,
            expander_profile_layout_general;
    //General Profile Layout-------------------------------------------
    public TextView txt_detailternak_activity_umur_general;
    public TextView txt_detailternak_activity_tgllahir_general;
    public TextView txt_detailternak_activity_namakandang_general;
    public TextView txt_detailternak_activity_namakawanan_general;
    public TextView txt_detailternak_activity_tglpengelompokan_general;

    //Kesehatan Profile Layout-------------------------------------------
    public TextView txt_detailternak_activity_milkwithhold_kesehatan;
    public TextView txt_detailternak_activity_status_kesehatan;
    public TextView txt_detailternak_activity_diagnosisterakhir_kesehatan;
        //Cek Kesehatan--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekkesehatan_kesehatan;
    public TextView txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan;
        //Cek Mastitis--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekmastitis_kesehatan;
    public TextView txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan;
        //Cek Lameness--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahceklameness_kesehatan;
    public TextView txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan;
        //Cek Potong Kuku--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekpotongkuku_kesehatan;
    public TextView txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan;

    List<ModelDetailTernakEvent> temp_list = new ArrayList <ModelDetailTernakEvent>();
    AdapterDetailTernakEvent adapterEventTernak;
    private int finish_attempt_event=0;
    private String id_ternak="",id_peternakan="";
    ListView lvItems;

    //Task-----------------------------------------------------------------
    public ListView list_taskdetail_activity_tambahbaru;
    List<ModelDetailTernakTask> list_task = new ArrayList <ModelDetailTernakTask>();
    AdapterDetailTernakTask adapterTaskTernak;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ternak_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Detail Ternak");
        }

        //Get Data Id Ternak-------------------------------------
        id_ternak = getIntent().getExtras().getString("idternak");

        //GetProfileData--------------------------------------------
        TextView input_detailternak_activity_idternak = (TextView)findViewById(R.id.input_detailternak_activity_idternak);
        input_detailternak_activity_idternak.setText(id_ternak);

        //Get Data Profile--------------------------------------------
        String urlParameters_profile = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                + "&id_ternak=" + id_ternak.trim();
        new getDataProfile().execute(url.getUrlGet_TernakProfile(), urlParameters_profile);

        //Get Data Event Ternak---------------------------------------------------
        String urlParameters_events = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                + "&idternak=" + id_ternak.trim();
        new getDataEvent().execute(url.getUrlGet_TernakEvent(), urlParameters_events);

        //Get Data Task Ternak---------------------------------------------------
        String urlParameters_task = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null)
                + "&idternak=" + id_ternak.trim();
        new getDataTask().execute(url.getUrlGet_TernakTask(), urlParameters_task);

        initUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            Intent i = new Intent(DetailTernakMain.this, MainActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_edit) {
            Intent i = new Intent(DetailTernakMain.this, EditTernakActivity.class);
            i.putExtra("id_ternak",id_ternak);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_detailternak_activity);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                /*final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.layout_profile, null, false);
                container.addView(view);
                return view;*/
                /*LayoutInflater inflater = (LayoutInflater) container.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                int resId = 0;
                switch (position) {
                    case 0:
                        resId = R.layout.layout_profile;
                        break;
                    case 1:
                        resId = R.layout.layout_event;
                        break;
                    case 2:
                        resId = R.layout.layout_profile;
                        break;
                    case 3:
                        resId = R.layout.layout_event;
                        break;
                }

                View view = inflater.inflate(resId, null);
                container.addView(view, 0);
                return view;*/
                View parent_view = null;

                switch (position) {
                    case 0:
                        parent_view = getpageOne();
                        break;
                    case 1:
                        parent_view = getpageTwo();
                        break;
                    case 2:
                        parent_view = getpageThree();
                        break;
                    case 3:
                        parent_view = getpageFour();
                        break;
                }
                container.addView(parent_view, 0);
                return parent_view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation_detailternak_activity);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_cow),
                        Color.parseColor(colors[0]))
                        .title("Profile")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[0]))
                        .title("Event")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_seventh),
                        Color.parseColor(colors[0]))
                        .title("Susu")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[0]))
                        .title("Task")
                        .badgeTitle("*")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = navigationTabBar.getModels().size()-1; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    //Set Page Profile-------------------------------------------------------------
    public View getpageOne(){
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_profile, null, false);


        //Set General-----
        txt_detailternak_activity_umur_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_umur_general);
        txt_detailternak_activity_tgllahir_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_tgllahir_general);
        txt_detailternak_activity_namakandang_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_namakandang_general);
        txt_detailternak_activity_namakawanan_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_namakawanan_general);
        txt_detailternak_activity_tglpengelompokan_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglpengelompokan_general);
        //Set Kesehatan-----
        txt_detailternak_activity_milkwithhold_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_milkwithhold_kesehatan);
        txt_detailternak_activity_status_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_status_kesehatan);
        txt_detailternak_activity_diagnosisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_diagnosisterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekkesehatan_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekkesehatan_kesehatan);
        txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekmastitis_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekmastitis_kesehatan);
        txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan);

        txt_detailternak_activity_jumlahceklameness_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahceklameness_kesehatan);
        txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan);
        txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekpotongkuku_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekpotongkuku_kesehatan);
        txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan);

        Button btnFertility=(Button)view.findViewById(R.id.btnFertility);
        expander_profile_layout_fertility = (ExpandableRelativeLayout) view.findViewById(R.id.fertilityLayout);

        btnFertility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_fertility.toggle();
            }
        });

        Button btnProduction=(Button)view.findViewById(R.id.btnProduction);
        expander_profile_layout_production = (ExpandableRelativeLayout) view.findViewById(R.id.productionLayout);

        btnProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_production.toggle();
            }
        });

        Button btnHealth=(Button)view.findViewById(R.id.btnHealth);
        expander_profile_layout_health = (ExpandableRelativeLayout) view.findViewById(R.id.healthLayout);

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_health.toggle();
            }
        });

        Button btnGeneral=(Button)view.findViewById(R.id.btnGeneral);
        expander_profile_layout_general = (ExpandableRelativeLayout) view.findViewById(R.id.generalLayout);

        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_general.toggle();

            }
        });

        return view;
    }

    private class getDataProfile extends AsyncTask<String,Integer,String> {
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
        protected void onPostExecute(String res) {
            Log.d("Detail_Ternak_Json",res);
            SetTernakDetail(res);
        }
    }

    private void SetTernakDetail(String result){
        Log.d("Ternak2",result);

        try {
            JSONArray jArray = new JSONArray(result);
            JSONObject jObj = jArray.getJSONObject(0);

            JSONArray cekKehamilan_arr = jObj.getJSONArray("cekKehamilan");
            JSONArray getTernakUmum_arr = jObj.getJSONArray("getTernakUmum");
            JSONArray countTernakMastitis_arr = jObj.getJSONArray("countTernakMastitis");
            JSONArray countTernakLameness_arr = jObj.getJSONArray("countTernakLameness");
            JSONArray countTernakKuku_arr = jObj.getJSONArray("countTernakKuku");
            JSONArray countTernakSehat_arr = jObj.getJSONArray("countTernakSehat");

            JSONObject obj_cekKehamilan = cekKehamilan_arr.getJSONObject(0);
            JSONObject obj_getTernakUmum = getTernakUmum_arr.getJSONObject(0);
            JSONObject obj_countTernakMastitis = countTernakMastitis_arr.getJSONObject(0);
            JSONObject obj_countTernakLameness = countTernakLameness_arr.getJSONObject(0);
            JSONObject obj_countTernakKuku = countTernakKuku_arr.getJSONObject(0);
            JSONObject obj_countTernakSehat = countTernakSehat_arr.getJSONObject(0);

            //General Profile Layout-------------------------------------------
            if(nullHandle(obj_getTernakUmum, "year").equalsIgnoreCase("N/A")||
                    nullHandle(obj_getTernakUmum, "month").equalsIgnoreCase("N/A")||
                    nullHandle(obj_getTernakUmum, "day").equalsIgnoreCase("N/A")){
                txt_detailternak_activity_umur_general.setText("N/A");
            }else{
                txt_detailternak_activity_umur_general.setText(nullHandle(obj_getTernakUmum, "year") + " y" +
                        nullHandle(obj_getTernakUmum, "month") + " m" +
                        nullHandle(obj_getTernakUmum, "day") + " d");
            }
            txt_detailternak_activity_tgllahir_general.setText(nullHandle(obj_getTernakUmum, "tgl_lahir"));
            txt_detailternak_activity_namakandang_general.setText(nullHandle(obj_getTernakUmum, "kandang"));
            txt_detailternak_activity_namakawanan_general.setText(nullHandle(obj_getTernakUmum, "kawanan"));
            txt_detailternak_activity_tglpengelompokan_general.setText(nullHandle(obj_getTernakUmum, "Tglpengelompokan"));

            //Kesehatan Profile Layout-------------------------------------------
            txt_detailternak_activity_milkwithhold_kesehatan.setText("N/A");
            txt_detailternak_activity_status_kesehatan.setText("N/A");
            txt_detailternak_activity_diagnosisterakhir_kesehatan.setText("N/A");
            //Cek Kesehatan--------------------------------------------------
            txt_detailternak_activity_jumlahcekkesehatan_kesehatan.setText(nullHandle(obj_countTernakMastitis, "berapakali sakit mastitis"));
            txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan.setText(nullHandle(obj_countTernakMastitis, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan.setText(nullHandle(obj_countTernakMastitis, "lama menderita"));
            //Cek Mastitis--------------------------------------------------
            txt_detailternak_activity_jumlahcekmastitis_kesehatan.setText(nullHandle(obj_countTernakLameness, "berapakali sakit lamenes"));
            txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan.setText(nullHandle(obj_countTernakLameness, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan.setText(nullHandle(obj_countTernakLameness, "lama menderita"));
            //Cek Lameness--------------------------------------------------
            txt_detailternak_activity_jumlahceklameness_kesehatan.setText(nullHandle(obj_countTernakSehat, "berapakali dicek kesehatan"));
            txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan.setText(nullHandle(obj_countTernakSehat, "tgl_periksa_terakhir"));
            txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan.setText(nullHandle(obj_countTernakSehat, "lama pemeriksaan"));
            //Cek Potong Kuku--------------------------------------------------
            txt_detailternak_activity_jumlahcekpotongkuku_kesehatan.setText(nullHandle(obj_countTernakKuku, "berapakali sakit kuku"));
            txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan.setText(nullHandle(obj_countTernakKuku, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan.setText(nullHandle(obj_countTernakKuku, "lama menderita"));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static String nullHandle(JSONObject json, String key) throws JSONException {
        if (json.isNull(key))
            return "N/A";
        else{
            return json.getString(key);
        }
    }
    public static int nullHandleArray(JSONArray json) {
        if (json.isNull(0))
            return 0;
        else{
            return 1;
        }
    }
    //Set Page Profile-------------------------------------------------------------

    //Set Page Event-------------------------------------------------------------
    public View getpageTwo() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_event, null, false);
        lvItems = (ListView) view.findViewById(R.id.lv_items);

        return view;
    }

    private class getDataEvent extends AsyncTask<String,Integer,String> {
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
        protected void onPostExecute(String res) {
            Log.d("Detail_Event_Json",res);
            SetEventDetailTernak(res);
            finish_attempt_event=1;
        }
    }

    private void SetEventDetailTernak(String result){
        int count_data_total=0;

        try {
            JSONArray jArray = new JSONArray(result);
            JSONObject jObj = jArray.getJSONObject(0);

            JSONArray getPengelompokkan_arr = jObj.getJSONArray("EventPengelompokanKawanan");
            JSONArray getPemerahan_arr = jObj.getJSONArray("EventPemerahan");
            JSONArray getPemeriksaanKesehatan_arr = jObj.getJSONArray("EventKesehatan");
            JSONArray getPemeriksaanKuku_arr = jObj.getJSONArray("EventCekKuku");
            JSONArray getPemeriksaanLameness_arr = jObj.getJSONArray("EventCekLameness");
            JSONArray getPemeriksaanMastitis_arr = jObj.getJSONArray("EventCekMastitis");
            JSONArray getPemeriksaanReproduksi_arr = jObj.getJSONArray("EventCekReproduksi");
            JSONArray getPemeriksaanHeat_arr = jObj.getJSONArray("EventHeat");
            JSONArray getVaksinasi_arr = jObj.getJSONArray("EventVaksinasi");
            JSONArray getInseminasi_arr = jObj.getJSONArray("EventInseminasi");
            JSONArray getMelahirkan_arr = jObj.getJSONArray("EventMelahirkan");
            JSONArray getAborsi_arr = jObj.getJSONArray("EventAborsi");

            for (int i = 0; i < getPengelompokkan_arr.length(); i++) {
                JSONObject obj_getPengelompokkan = getPengelompokkan_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Pengelompokan";
                ter.tgl = obj_getPengelompokkan.getString("Tgl pengelompokan");
                ter.key_event = "1";

                setDefaultDataModel(ter);
                ter.pengelompokan_kandang = obj_getPengelompokkan.getString("kandang");
                ter.pengelompokan_kawanan = obj_getPengelompokkan.getString("kawanan");

                temp_list.add(ter);
            }

            for (int i = 0; i < getPemerahan_arr.length(); i++) {
                JSONObject obj_getPemerahan = getPemerahan_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Pemerahan";
                ter.tgl = obj_getPemerahan.getString("tgl_perah");
                ter.key_event = "2";

                setDefaultDataModel(ter);
                ter.pemerahan_sesi = obj_getPemerahan.getString("sesi_perah");
                ter.pemerahan_durasi = obj_getPemerahan.getString("durasi");
                ter.pemerahan_kapasitas = obj_getPemerahan.getString("kapasitas");

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanKesehatan_arr.length(); i++) {
                JSONObject obj_getPemeriksaanKesehatan = getPemeriksaanKesehatan_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Cek Kesehatan";
                ter.tgl = obj_getPemeriksaanKesehatan.getString("tgl_periksa");
                ter.key_event = "3";

                setDefaultDataModel(ter);
                ter.kesehatan_diagnosis = obj_getPemeriksaanKesehatan.getString("diagnosis");
                ter.kesehatan_berat = obj_getPemeriksaanKesehatan.getString("berat badan");
                ter.kesehatan_perawatan = obj_getPemeriksaanKesehatan.getString("perawatan");
                ter.kesehatan_statusfisik = obj_getPemeriksaanKesehatan.getString("statusfisik");
                ter.kesehatan_statusstress = obj_getPemeriksaanKesehatan.getString("statusstress");
                ter.kesehatan_suhu = obj_getPemeriksaanKesehatan.getString("suhubadan");

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanKuku_arr.length(); i++) {
                JSONObject obj_getPemeriksaanKuku = getPemeriksaanKuku_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Cek Kuku";
                ter.tgl = obj_getPemeriksaanKuku.getString("tgl_periksa");
                ter.key_event = "4";

                setDefaultDataModel(ter);
                ter.kuku_diagnosis = obj_getPemeriksaanKuku.getString("diagnosis");
                ter.kuku_perawatan = obj_getPemeriksaanKuku.getString("perawatan");
                ter.kuku_statusfisik = obj_getPemeriksaanKuku.getString("statusfisik");

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanMastitis_arr.length(); i++) {
                JSONObject obj_getPemeriksaanMastitis = getPemeriksaanMastitis_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Cek Mastitis";
                ter.tgl = obj_getPemeriksaanMastitis.getString("tgl_periksa");
                ter.key_event = "5";

                setDefaultDataModel(ter);
                ter.mastitis_diagnosis = obj_getPemeriksaanMastitis.getString("diagnosis");
                ter.mastitis_perawatan = obj_getPemeriksaanMastitis.getString("perawatan");
                ter.mastitis_statusfisik = obj_getPemeriksaanMastitis.getString("statusfisik");

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanLameness_arr.length(); i++) {
                JSONObject obj_getPemeriksaanLameness = getPemeriksaanLameness_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Cek Lameness";
                ter.tgl = obj_getPemeriksaanLameness.getString("tgl_periksa");
                ter.key_event = "6";

                setDefaultDataModel(ter);
                ter.lameness_diagnosis = obj_getPemeriksaanLameness.getString("diagnosis");
                ter.lameness_perawatan = obj_getPemeriksaanLameness.getString("perawatan");
                ter.lameness_statusfisik = obj_getPemeriksaanLameness.getString("statusfisik");

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanReproduksi_arr.length(); i++) {
                JSONObject obj_getPemeriksaanReproduksi = getPemeriksaanReproduksi_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Cek Reproduksi";
                ter.tgl = obj_getPemeriksaanReproduksi.getString("tgl_periksa");
                ter.key_event = "7";

                setDefaultDataModel(ter);
                if(obj_getPemeriksaanReproduksi.getString("Kondisi Reproduksi").equalsIgnoreCase("1")){
                    ter.reproduksi_kondisi = "BAIK";

                }else if(obj_getPemeriksaanReproduksi.getString("Kondisi Reproduksi").equalsIgnoreCase("0")){
                    ter.reproduksi_kondisi = "TIDAK BAIK";
                }

                temp_list.add(ter);

            }

            for (int i = 0; i < getPemeriksaanHeat_arr.length(); i++) {
                JSONObject obj_PemeriksaanHeat = getPemeriksaanHeat_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Birahi";
                ter.tgl = obj_PemeriksaanHeat.getString("tgl_periksa");
                ter.key_event = "8";

                setDefaultDataModel(ter);
                ter.heat_tglmulai = obj_PemeriksaanHeat.getString("tgl_mulai_heat");
                ter.heat_tglselesai = obj_PemeriksaanHeat.getString("tgl_heat_selesai");

                temp_list.add(ter);

            }

            for (int i = 0; i < getVaksinasi_arr.length(); i++) {
                JSONObject obj_getVaksinasi = getVaksinasi_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Vaksinasi";
                ter.tgl = obj_getVaksinasi.getString("tgl_vaksin_real");
                ter.key_event = "9";

                setDefaultDataModel(ter);
                ter.vaksinasi_nama = obj_getVaksinasi.getString("nama_vaksin");
                ter.vaksinasi_dosis = obj_getVaksinasi.getString("dosis");
                ter.vaksinasi_satuan = obj_getVaksinasi.getString("satuan_dosis");
                ter.vaksinasi_repetisi = obj_getVaksinasi.getString("repetisi_ke");

                temp_list.add(ter);

            }

            for (int i = 0; i < getInseminasi_arr.length(); i++) {
                JSONObject obj_getInseminasi = getInseminasi_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Inseminasi";
                ter.tgl = obj_getInseminasi.getString("tgl_inseminasi");
                ter.key_event = "10";

                setDefaultDataModel(ter);
                ter.inseminasi_metode = obj_getInseminasi.getString("metode_inseminasi");
                ter.inseminasi_status = obj_getInseminasi.getString("status_keberhasilan");
                ter.inseminasi_tgl_perkiraan_melahirkan = obj_getInseminasi.getString("tgl_perkiraan_melahirkan");

                temp_list.add(ter);

            }

            for (int i = 0; i < getMelahirkan_arr.length(); i++) {
                JSONObject obj_getMelahirkan = getMelahirkan_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Melahirkan";
                ter.tgl = obj_getMelahirkan.getString("tgl_melahirkan_real");
                ter.key_event = "11";

                setDefaultDataModel(ter);
                ter.melahirkan_jumlahanak = obj_getMelahirkan.getString("jumlah_anak");
                ter.melahirkan_kondisi = obj_getMelahirkan.getString("kondisi_melahirkan");

                temp_list.add(ter);

            }

            for (int i = 0; i < getAborsi_arr.length(); i++) {
                JSONObject obj_getAborsi = getAborsi_arr.getJSONObject(i);
                ModelDetailTernakEvent ter = new ModelDetailTernakEvent();
                ter.title = "Aborsi";
                ter.tgl = obj_getAborsi.getString("tgl_aborsi");
                ter.key_event = "12";

                setDefaultDataModel(ter);
                ter.aborsi_penyebab = obj_getAborsi.getString("penyebab_aborsi");

                temp_list.add(ter);

            }

            Collections.sort(temp_list, new Comparator<ModelDetailTernakEvent>() {
                @Override
                public int compare(ModelDetailTernakEvent o1, ModelDetailTernakEvent o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date_1 = null;
                    Date date_2 = null;
                    try {
                        date_1 = sdf.parse(o1.tgl);
                        date_2 = sdf.parse(o2.tgl);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    return date_2.compareTo(date_1);
                }
            });
            adapterEventTernak = new AdapterDetailTernakEvent(this, temp_list);
            lvItems.setAdapter(adapterEventTernak);
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AdapterDetailTernakEvent adapter = (AdapterDetailTernakEvent) parent.getAdapter();

                    ModelDetailTernakEvent item = (ModelDetailTernakEvent) adapter.getItem(position);
                    if (item != null) {
                        if (item.isExpanded) {
                            item.isExpanded = false;
                        } else {
                            item.isExpanded = true;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setDefaultDataModel(ModelDetailTernakEvent ter){
        ter.isExpanded = false;

        //Pengelompokan--------------------------------------
        ter.pengelompokan_kandang = "N/A";
        ter.pengelompokan_kawanan = "N/A";

        //Pemerahan------------------------------------------
        ter.pemerahan_sesi = "N/A";
        ter.pemerahan_durasi = "N/A";
        ter.pemerahan_kapasitas = "N/A";

        //Pemeriksaan Kesehatan------------------------------
        ter.kesehatan_diagnosis = "N/A";
        ter.kesehatan_perawatan = "N/A";
        ter.kesehatan_berat = "N/A";
        ter. kesehatan_statusfisik = "N/A";
        ter.kesehatan_statusstress = "N/A";
        ter.kesehatan_suhu = "N/A";

        //Kuku-----------------------------------------------
        ter.kuku_diagnosis = "N/A";
        ter.kuku_perawatan = "N/A";
        ter.kuku_statusfisik = "N/A";

        //Mastitis-------------------------------------------
        ter.mastitis_diagnosis = "N/A";
        ter.mastitis_perawatan = "N/A";
        ter.mastitis_statusfisik = "N/A";

        //Lameness-------------------------------------------
        ter.lameness_diagnosis = "N/A";
        ter.lameness_perawatan = "N/A";
        ter.lameness_statusfisik = "N/A";

        //Pemeriksaan Reproduksi-----------------------------
        ter.reproduksi_kondisi = "N/A";

        //Pemeriksaa Heat------------------------------------
        ter.heat_tglmulai = "N/A";
        ter.heat_tglselesai = "N/A";

        //Vaksinasi------------------------------------------
        ter.vaksinasi_nama = "N/A";
        ter.vaksinasi_satuan = "N/A";
        ter.vaksinasi_dosis = "N/A";
        ter.vaksinasi_repetisi = "N/A";
    }
    //Set Page Event-------------------------------------------------------------

    //Set Page Milk-------------------------------------------------------------
    public View getpageThree() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_susu, null, false);
        return view;
    }
    //Set Page Milk-------------------------------------------------------------

    //Set Page Task-------------------------------------------------------------
    public View getpageFour() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_task, null, false);

        list_taskdetail_activity_tambahbaru = (ListView)view.findViewById(R.id.list_taskdetail_activity_tambahbaru);


        return view;
    }

    //Get Data Task-------------------------------------------------------
    private class getDataTask extends AsyncTask<String,Integer,String> {
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
        protected void onPostExecute(String res) {
            Log.d("Detail_Ternak_Json",res);
            SetTernakTask(res);
        }
    }

    private void SetTernakTask(String result){
        list_task.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelDetailTernakTask mTask = new ModelDetailTernakTask();
                mTask.setTgl_task_schedule(jObj.getString("tgl_task"));
                mTask.setIsi_task(jObj.getString("isi"));

                list_task.add(mTask);
            }
        }
        catch (JSONException e){e.printStackTrace();}
        adapterTaskTernak = new AdapterDetailTernakTask(DetailTernakMain.this, R.layout.holder_list_task, list_task);
        list_taskdetail_activity_tambahbaru.setAdapter(adapterTaskTernak);
    }
    //Set Page Task-------------------------------------------------------------
}
