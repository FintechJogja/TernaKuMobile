package com.fintech.ternaku.TernakPerah.Main.Laporan;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import me.wangyuwei.loadingview.LoadingView;

public class LaporanFragment extends Fragment {
    private NavigationTabBar navbar_laporan_fragment_pilih ;
    private ArrayList<NavigationTabBar.Model> models5 = new ArrayList<>();
    ListView listview_laporan_keuangan,listview_laporan_kesehatan;
    ProgressDialog progDialog;
    String bln,thn,bln_angka,jenis_transaksi;
    String json;
    ArrayList<Float> list_value_grafik_laporan_keuangan = new ArrayList<Float>();
    ArrayList<Float> list_value_grafik_laporan_kesehatan = new ArrayList<Float>();

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    //Loading------------------------------------------------------------
    private LoadingView loading_view_laporan;

    public LaporanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_laporan, container, false);

        listview_laporan_keuangan = (ListView) view.findViewById(R.id.list_laporan_keuangan);
        listview_laporan_kesehatan = (ListView) view.findViewById(R.id.list_laporan_kesehatan);

        //Loading UI--------------------------------------------------------
        loading_view_laporan = (LoadingView) view.findViewById(R.id.loading_view_laporan);
        InitiateFragment();

        navbar_laporan_fragment_pilih = (NavigationTabBar) view.findViewById(R.id.navbar_laporan_fragment_pilih);
        ArrayList<NavigationTabBar.Model> models5 = new ArrayList<>();
        models5.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_calculator), Color.WHITE
                ).title("Keuangan")
                        .build()
        );
        models5.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_milk_production),  Color.WHITE
                ).title("Kesehatan")
                        .build()
        );
        models5.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),  Color.WHITE
                ).title("Perlengkapan")
                        .build()
        );
        navbar_laporan_fragment_pilih.setModels(models5);
        navbar_laporan_fragment_pilih.setModelIndex(0, true);
        navbar_laporan_fragment_pilih.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                if(index==0){
                    listview_laporan_keuangan.setVisibility(View.VISIBLE);
                    listview_laporan_kesehatan.setVisibility(View.GONE);
                }
                if(index==1){
                    listview_laporan_keuangan.setVisibility(View.GONE);
                    listview_laporan_kesehatan.setVisibility(View.VISIBLE);
                }
                if(index==2){
                    listview_laporan_keuangan.setVisibility(View.GONE);
                    listview_laporan_kesehatan.setVisibility(View.GONE);
                }
            }
        });

        //Get Bundle-------------------------------------------------------
        String[] mMonths = new String[]{
                "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November", "Desember"};
        bln = mMonths[Calendar.getInstance().get(Calendar.MONTH)];
        thn = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));;
        bln_angka = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));;;
        jenis_transaksi = "Pembelian Vaksin";

        //Get Data From Web Service------------------------------------------------
        String param = "idpeternakan="+getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null)
                +"&bulan=" + bln.trim()
                +"&tahun=" + thn.trim();
        new GetDataKeuanganMasuk().execute(url.getUrlGetlaporanAll(),param);



        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navbar_laporan_fragment_pilih.onPageSelected(0);

    }

    //Initiate Fragment----------------------------------------------------
    private void InitiateFragment(){
        loading_view_laporan.setVisibility(View.VISIBLE);
        listview_laporan_keuangan.setVisibility(View.GONE);
        listview_laporan_kesehatan.setVisibility(View.GONE);
    }
    private void RefreshFragment(){
        loading_view_laporan.setVisibility(View.GONE);
        listview_laporan_keuangan.setVisibility(View.VISIBLE);
    }

    //Get Data From Web Service---------------------------------------------------
    private class GetDataKeuanganMasuk extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            loading_view_laporan.start();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RESGrafikKeuangan",result);
            SetList(result);
            loading_view_laporan.stop();
            RefreshFragment();
        }
    }

    private void SetList(String JSON){
        String Jenis_Grafik="N/A";
        int total_data_grafik=0;
        LineData temp_data_grafik;
        ArrayList<ModelLaporanKeuanganGrafik> temp_list_set_keuangan = new ArrayList<ModelLaporanKeuanganGrafik>();
        ArrayList<ModelLaporanKeuanganGrafik> temp_list_set_kesehatan = new ArrayList<ModelLaporanKeuanganGrafik>();

        try{
            JSONArray jArray = new JSONArray(JSON);
            JSONObject jObj = jArray.getJSONObject(0);

            //Get JSON Laporan Keuangan+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            JSONArray getPembelianPakan_arr = jObj.getJSONArray("uang_keluar_pakan");
            JSONArray getPembelianObat_arr = jObj.getJSONArray("uang_keluar_obat");
            JSONArray getPembelianSemen_arr = jObj.getJSONArray("uang_keluar_semen");
            JSONArray getPembelianVaksin_arr = jObj.getJSONArray("uang_keluar_belivaksin");
            JSONArray getPemeriksaanKesehatanSapi_arr = jObj.getJSONArray("uang_keluar_periksaKesehatanSapi");
            JSONArray getPembelianPerlengkapan_arr = jObj.getJSONArray("uang_keluar_beliPerlengkapan");
            JSONArray getPembelianTernak_arr = jObj.getJSONArray("uang_keluar_beliTernak");
            JSONArray getPembayaranListrik_arr = jObj.getJSONArray("uang_keluar_bayarListrik");
            JSONArray getPembelianLainnya_arr = jObj.getJSONArray("uang_keluar_lainnya");
            JSONArray getPenjualanTernak_arr = jObj.getJSONArray("uang_masuk_jualternak");
            JSONArray getPenjualanSusu_arr = jObj.getJSONArray("uang_masuk_jualsusu");
            JSONArray getPenjualanKompos_arr = jObj.getJSONArray("uang_masuk_jualkompos");
            JSONArray getPenjulanLainnya_arr = jObj.getJSONArray("uang_masuk_lainnya");

            //Get JSON Laporan Kesehatan+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            JSONArray getTernakHeat_arr = jObj.getJSONArray("sapi_birahi");
            JSONArray getProduksiSusuTernak_arr = jObj.getJSONArray("produksisusu");
            JSONArray getPenggunaanPakan_arr = jObj.getJSONArray("penggunaanpakan");


            for(int i=0;i<17;i++){
                total_data_grafik = getPembelianPakan_arr.length();
                list_value_grafik_laporan_keuangan.clear();
                list_value_grafik_laporan_kesehatan.clear();
                for(int k=0;k<getPembelianPakan_arr.length();k++) {
                    //Set Grafik Pembelian Pakan++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 0) {
                        Jenis_Grafik=" Pembelian Pakan";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianPakan_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembelian Obat++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 1) {
                        Jenis_Grafik=" Pembelian Obat";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianObat_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembelian Semen++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 2) {
                        Jenis_Grafik=" Pembelian Semen";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianSemen_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                        Log.d("Tes", String.valueOf(i));
                    }
                    //Set Grafik Pembelian Vaksin++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 3) {
                        Jenis_Grafik=" Pembelian Vaksin";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianVaksin_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pemeriksaan Kesehatan++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 4) {
                        Jenis_Grafik=" Biaya Pemeriksaan Kesehatan Sapi";
                        JSONObject Obj_DataKeuanganGrafik = getPemeriksaanKesehatanSapi_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembelian Perlengkapan++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 5) {
                        Jenis_Grafik=" Pembelian Perlengkapan";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianPerlengkapan_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembelian Ternak++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 6) {
                        Jenis_Grafik=" Pembelian Ternak";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianTernak_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembayaran Listrik++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 7) {
                        Jenis_Grafik=" Pembayaran Listrik";
                        JSONObject Obj_DataKeuanganGrafik = getPembayaranListrik_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Pembelian Lainnya++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 8) {
                        Jenis_Grafik=" Pembelian Lainnya";
                        JSONObject Obj_DataKeuanganGrafik = getPembelianLainnya_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Penjualan Ternak++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 9) {
                        Jenis_Grafik=" Penjualan Ternak";
                        JSONObject Obj_DataKeuanganGrafik = getPenjualanTernak_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Penjualan Susu++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 10) {
                        Jenis_Grafik=" Penjualan Susu";
                        JSONObject Obj_DataKeuanganGrafik = getPenjualanSusu_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Penjualan Kompos++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 11) {
                        Jenis_Grafik=" Penjualan Kompos";
                        JSONObject Obj_DataKeuanganGrafik = getPenjualanKompos_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Penjualan Lainnya++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 12) {
                        Jenis_Grafik=" Penjualan Lainnya";
                        JSONObject Obj_DataKeuanganGrafik = getPenjulanLainnya_arr.getJSONObject(k);
                        list_value_grafik_laporan_keuangan.add((float) Obj_DataKeuanganGrafik.getDouble("JumlahUang"));
                    }
                    //Set Grafik Ternak Birahi++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 13) {
                        Jenis_Grafik=" Jumlah Ternak Birahi";
                        JSONObject Obj_DataKesehatanGrafik = getTernakHeat_arr.getJSONObject(k);
                        list_value_grafik_laporan_kesehatan.add((float) Obj_DataKesehatanGrafik.getDouble("JumlahSapiBirahi"));
                    }
                    //Set Grafik Peroduksi Susu+++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 14) {
                        Jenis_Grafik=" Produksi Susu Peternakan";
                        JSONObject Obj_DataKesehatanGrafik = getProduksiSusuTernak_arr.getJSONObject(k);
                        list_value_grafik_laporan_kesehatan.add((float) Obj_DataKesehatanGrafik.getDouble("JumlahProduksi"));
                    }
                    //Set Grafik Penggunaan Pakan+++++++++++++++++++++++++++++++++++++++++++++++++++++
                    if (i == 15) {
                        Jenis_Grafik=" Penggunaan Pakan";
                        JSONObject Obj_DataKesehatanGrafik = getPenggunaanPakan_arr.getJSONObject(k);
                        list_value_grafik_laporan_kesehatan.add((float) Obj_DataKesehatanGrafik.getDouble("JumlahPakan"));
                    }
                }

                //Insert To List Keuangan+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                if(i>=0&&i<13){
                    temp_data_grafik = setDataKeuangan(total_data_grafik);
                    ModelLaporanKeuanganGrafik mdl_grfk = new ModelLaporanKeuanganGrafik();

                    mdl_grfk.setJudul(Jenis_Grafik.toString().trim());
                    mdl_grfk.setBulan(bln_angka.toString());
                    mdl_grfk.setGrafik(temp_data_grafik);
                    temp_list_set_keuangan.add(mdl_grfk);
                }

                //Insert To List Kesehatan+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                if(i>=13&&i<16){
                    temp_data_grafik = setDataKesehatan(total_data_grafik,i);
                    ModelLaporanKeuanganGrafik mdl_grfk = new ModelLaporanKeuanganGrafik();

                    mdl_grfk.setJudul(Jenis_Grafik.toString().trim());
                    mdl_grfk.setBulan(bln_angka.toString());
                    mdl_grfk.setGrafik(temp_data_grafik);
                    temp_list_set_kesehatan.add(mdl_grfk);
                }
            }

            //Set To Adapter+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            AdapterLaporanKeuangan cda_keuangan = new AdapterLaporanKeuangan(getActivity(), temp_list_set_keuangan);
            listview_laporan_keuangan.setAdapter(cda_keuangan);
            AdapterLaporanKeuangan cda_kesehatan = new AdapterLaporanKeuangan(getActivity(), temp_list_set_kesehatan);
            listview_laporan_kesehatan.setAdapter(cda_kesehatan);

        }catch (JSONException e){e.printStackTrace();}
    }


    private LineData setDataKeuangan(int count) {
        float val = 0;
        LineDataSet set_uang;


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count + 1; i++) {
            if (i != 0) {
                val = list_value_grafik_laporan_keuangan.get(i - 1);
            }
            yVals1.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        set_uang = new LineDataSet(yVals1, "Jumlah Uang (Rupiah)");
        set_uang.setAxisDependency(YAxis.AxisDependency.LEFT);
        set_uang.setColor(ColorTemplate.getHoloBlue());
        //set_uang.setCircleColor(Color.BLACK);
        set_uang.setLineWidth(2f);
        //set_uang.setCircleRadius(2f);
        set_uang.setFillAlpha(65);
        set_uang.setFillColor(ColorTemplate.getHoloBlue());
        set_uang.setHighLightColor(Color.rgb(244, 117, 117));
        set_uang.setDrawCircleHole(false);
        set_uang.setDrawCircles(false);
        set_uang.setDrawValues(false);


        // create a data object with the datasets
        LineData data = new LineData(set_uang);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        return data;
    }

    private LineData setDataKesehatan(int count,int Keterangan) {
        float val = 0;
        LineDataSet set_uang;


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count + 1; i++) {
            if (i != 0) {
                val = list_value_grafik_laporan_kesehatan.get(i - 1);
            }
            yVals1.add(new Entry(i, val));
        }

        // create a dataset and give it a type
        set_uang = new LineDataSet(yVals1, "N/A");
        if(Keterangan==13){
            set_uang = new LineDataSet(yVals1, "Jumlah Ternak");
        }else
        if(Keterangan==14){
            set_uang = new LineDataSet(yVals1, "Jumlah Produksi Susu (Liter)");
        }else
        if(Keterangan==15){
            set_uang = new LineDataSet(yVals1, "Jumlah Pakan (Kilogram)");
        }
        set_uang.setAxisDependency(YAxis.AxisDependency.LEFT);
        set_uang.setColor(ColorTemplate.getHoloBlue());
        //set_uang.setCircleColor(Color.BLACK);
        set_uang.setLineWidth(2f);
        //set_uang.setCircleRadius(2f);
        set_uang.setFillAlpha(65);
        set_uang.setFillColor(ColorTemplate.getHoloBlue());
        set_uang.setHighLightColor(Color.rgb(244, 117, 117));
        set_uang.setDrawCircleHole(false);
        set_uang.setDrawCircles(false);
        set_uang.setDrawValues(false);


        // create a data object with the datasets
        LineData data = new LineData(set_uang);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        return data;
    }
}
