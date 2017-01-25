package com.fintech.ternaku.TernakPerah.Main.TambahData;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.fintech.ternaku.TernakPerah.Main.NavBar.Keuangan.AddKeuangan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.AddCekKesehatan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.AddCekPenyakit;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.AddKarantina;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.AddVaksinasi;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.ProtocolKesehatan.ShowProtokolKesehatan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddInseminasi;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddMasaSubur;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddMelahirkan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddMengandung;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddMenyusui;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddPemeriksaanReproduksi;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.InjeksiHormon.ShowInjeksiHormon;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKandang;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKawanan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.PindahTernak;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Produksi.UpdateMasaKering;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Produksi.UpdateWeightActivity;
import com.fintech.ternaku.TernakPotong.ADG.ViewADG;
import com.fintech.ternaku.TernakPotong.AddPakan.AddPakanPedaging;
import com.fintech.ternaku.TernakPotong.BeratBadanActivity;
import com.fintech.ternaku.TernakPotong.KompisisiPakan.KomposisiPakanActivity;
import com.fintech.ternaku.TernakPotong.SapiPotongAddKomposisiPakan;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddDataFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    View view;

    //Slider Image Initiate----------------------------------------------
    private SliderLayout mDemoSlider;

    //Navbar Pilih Pengelolaan-------------------------------------------
    private NavigationTabBar navbar_pilih_fragment_pengelolaan ;

    //Pengembangbiakan---------------------------------------------
    private LinearLayout container_pengembangbiakan_fragment_add;
    private LinearLayout btn_mengandung_pengembangbiakan,btn_inseminasi_pengembangbiakan,
            btn_periksareproduksi_pengembangbiakan,btn_masasubur_pengembangbiakan,
            btn_injeksihormon_pengembangbiakan,btn_melahirkan_pengembangbiakan,
            btn_menyusui_pengembangbiakan,
            btn_cekkesehatan_pengembangbiakan,btn_vaksinasi_pengembangbiakan,
            btn_cekpenyakit_pengembangbiakan,btn_karantina_pengembangbiakan,
            btn_protokol_pengembangbiakan,
            btn_jual_pengembangbiakan,btn_beli_pengembangbiakan,
            btn_tambahkeuangan_pengembangbiakan;

    //Penggemukan---------------------------------------------------
    private LinearLayout container_penggemukan_fragment_add;
    private LinearLayout btn_pakan_penggemukan,btn_adg_penggemukan,
            btn_komposisipakan_penggemukan,
            btn_cekkesehatan_penggemukan,btn_vaksinasi_penggemukan,
            btn_cekpenyakit_penggemukan,btn_karantina_penggemukan,
            btn_protokol_penggemukan,
            btn_jual_penggemukan,btn_beli_penggemukan,
            btn_tambahkeuangan_penggemukan;

    public AddDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_data, container, false);



        //Create Slider-----------------------------------------------------------------------
        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Farm 1",R.drawable.slider_4);
        file_maps.put("Farm 2",R.drawable.slider_3);
        file_maps.put("Farm 3",R.drawable.slider_2);
        file_maps.put("Farm 4", R.drawable.slider_1);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);

        //Initiate View----------------------------------------------------------------------------
        container_penggemukan_fragment_add = (LinearLayout)view.findViewById(R.id.container_penggemukan_fragment_add);
        container_pengembangbiakan_fragment_add = (LinearLayout)view.findViewById(R.id.container_pengembangbiakan_fragment_add);

        //Pengembangbiakan-------------------------------------------------------------------------
        btn_mengandung_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_mengandung_pengembangbiakan);
        btn_mengandung_pengembangbiakan.setOnClickListener(this);

        btn_inseminasi_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_inseminasi_pengembangbiakan);
        btn_inseminasi_pengembangbiakan.setOnClickListener(this);

        btn_periksareproduksi_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_periksareproduksi_pengembangbiakan);
        btn_periksareproduksi_pengembangbiakan.setOnClickListener(this);

        btn_masasubur_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_masasubur_pengembangbiakan);
        btn_masasubur_pengembangbiakan.setOnClickListener(this);

        btn_injeksihormon_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_injeksihormon_pengembangbiakan);
        btn_injeksihormon_pengembangbiakan.setOnClickListener(this);

        btn_melahirkan_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_melahirkan_pengembangbiakan);
        btn_melahirkan_pengembangbiakan.setOnClickListener(this);

        btn_menyusui_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_menyusui_pengembangbiakan);
        btn_menyusui_pengembangbiakan.setOnClickListener(this);

        btn_cekkesehatan_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_cekkesehatan_pengembangbiakan);
        btn_cekkesehatan_pengembangbiakan.setOnClickListener(this);

        btn_vaksinasi_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_vaksinasi_pengembangbiakan);
        btn_vaksinasi_pengembangbiakan.setOnClickListener(this);

        btn_cekpenyakit_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_cekpenyakit_pengembangbiakan);
        btn_cekpenyakit_pengembangbiakan.setOnClickListener(this);

        btn_karantina_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_karantina_pengembangbiakan);
        btn_karantina_pengembangbiakan.setOnClickListener(this);

        btn_protokol_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_protokol_pengembangbiakan);
        btn_protokol_pengembangbiakan.setOnClickListener(this);

        btn_jual_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_jual_pengembangbiakan);
        btn_jual_pengembangbiakan.setOnClickListener(this);

        btn_beli_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_beli_pengembangbiakan);
        btn_beli_pengembangbiakan.setOnClickListener(this);

        btn_tambahkeuangan_pengembangbiakan = (LinearLayout)view.findViewById(R.id.btn_tambahkeuangan_pengembangbiakan);
        btn_tambahkeuangan_pengembangbiakan.setOnClickListener(this);

        //Penggemukan-------------------------------------------------------------------------
        btn_pakan_penggemukan = (LinearLayout)view.findViewById(R.id.btn_pakan_penggemukan);
        btn_pakan_penggemukan.setOnClickListener(this);

        btn_adg_penggemukan = (LinearLayout)view.findViewById(R.id.btn_adg_penggemukan);
        btn_adg_penggemukan.setOnClickListener(this);

        btn_komposisipakan_penggemukan = (LinearLayout)view.findViewById(R.id.btn_komposisipakan_penggemukan);
        btn_komposisipakan_penggemukan.setOnClickListener(this);

        btn_cekkesehatan_penggemukan = (LinearLayout)view.findViewById(R.id.btn_cekkesehatan_penggemukan);
        btn_cekkesehatan_penggemukan.setOnClickListener(this);

        btn_vaksinasi_penggemukan = (LinearLayout)view.findViewById(R.id.btn_vaksinasi_penggemukan);
        btn_vaksinasi_penggemukan.setOnClickListener(this);

        btn_cekpenyakit_penggemukan = (LinearLayout)view.findViewById(R.id.btn_cekpenyakit_penggemukan);
        btn_cekpenyakit_penggemukan.setOnClickListener(this);

        btn_karantina_penggemukan = (LinearLayout)view.findViewById(R.id.btn_karantina_penggemukan);
        btn_karantina_penggemukan.setOnClickListener(this);

        btn_protokol_penggemukan = (LinearLayout)view.findViewById(R.id.btn_protokol_penggemukan);
        btn_protokol_penggemukan.setOnClickListener(this);

        btn_jual_penggemukan = (LinearLayout)view.findViewById(R.id.btn_jual_penggemukan);
        btn_jual_penggemukan.setOnClickListener(this);

        btn_beli_penggemukan = (LinearLayout)view.findViewById(R.id.btn_beli_penggemukan);
        btn_beli_penggemukan.setOnClickListener(this);

        btn_tambahkeuangan_penggemukan = (LinearLayout)view.findViewById(R.id.btn_tambahkeuangan_penggemukan);
        btn_tambahkeuangan_penggemukan.setOnClickListener(this);


        //Initiate Navbar Pilih Pengelolaan--------------------------------------------------------
        navbar_pilih_fragment_pengelolaan = (NavigationTabBar) view.findViewById(R.id.navbar_pilih_fragment_pengelolaan);
        ArrayList<NavigationTabBar.Model> models5 = new ArrayList<>();
        models5.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_milk_production), Color.WHITE
                ).title("Pengembangbiakan")
                        .build()
        );
        models5.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_bargraph),  Color.WHITE
                ).title("Penggemukan")
                        .build()
        );
        navbar_pilih_fragment_pengelolaan.setModels(models5);
        navbar_pilih_fragment_pengelolaan.setModelIndex(0, true);
        navbar_pilih_fragment_pengelolaan.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                if(index==0){
                    container_pengembangbiakan_fragment_add.setVisibility(View.VISIBLE);
                    container_penggemukan_fragment_add.setVisibility(View.GONE);
                }else if(index==1){
                    container_pengembangbiakan_fragment_add.setVisibility(View.GONE);
                    container_penggemukan_fragment_add.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent i = new Intent();
        Log.d("ID",String.valueOf(id));
        switch (id){
            //Case For Expander Pindah Ternak-------------------------------------
            /*case R.id.button_adddata_fragment_produksi_pindahternak :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),PindahTernak.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_tambahkandang :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKandang.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_tambahkawanan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKawanan.class);
                startActivity(i);
                break;*/

            //Pengembangbiakan=====================================================
            //Case For Expander Kesuburan-----------------------------------------
            case R.id.btn_mengandung_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMengandung.class);
                startActivity(i);
                break;
            case R.id.btn_inseminasi_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddInseminasi.class);
                startActivity(i);
                break;
            case R.id.btn_periksareproduksi_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddPemeriksaanReproduksi.class);
                startActivity(i);
                break;
            case R.id.btn_masasubur_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMasaSubur.class);
                startActivity(i);
                break;
            case R.id.btn_injeksihormon_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ShowInjeksiHormon.class);
                startActivity(i);
                break;
            case R.id.btn_melahirkan_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMelahirkan.class);
                startActivity(i);
                break;
            case R.id.btn_menyusui_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMenyusui.class);
                startActivity(i);
                break;
            //Case For Expander Kesehatan-----------------------------------------
            case R.id.btn_cekkesehatan_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekKesehatan.class);
                startActivity(i);
                break;
            case R.id.btn_vaksinasi_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddVaksinasi.class);
                startActivity(i);
                break;
            case R.id.btn_cekpenyakit_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekPenyakit.class);
                startActivity(i);
                break;
            case R.id.btn_karantina_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKarantina.class);
                startActivity(i);
                break;
            case R.id.btn_protokol_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ShowProtokolKesehatan.class);
                startActivity(i);
                break;
            //Case For Expander Keuangan-----------------------------------------
            case R.id.btn_tambahkeuangan_pengembangbiakan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(), AddKeuangan.class);
                startActivity(i);
                break;

            //Penggemukan=====================================================
            //Case For Expander Pakan-----------------------------------------
            case R.id.btn_pakan_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddPakanPedaging.class);
                startActivity(i);
                break;
            case R.id.btn_adg_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ViewADG.class);
                startActivity(i);
                break;
            case R.id.btn_komposisipakan_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),KomposisiPakanActivity.class);
                startActivity(i);
                break;
            //Case For Expander Kesehatan-----------------------------------------
            case R.id.btn_cekkesehatan_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekKesehatan.class);
                startActivity(i);
                break;
            case R.id.btn_vaksinasi_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddVaksinasi.class);
                startActivity(i);
                break;
            case R.id.btn_cekpenyakit_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekPenyakit.class);
                startActivity(i);
                break;
            case R.id.btn_karantina_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKarantina.class);
                startActivity(i);
                break;
            case R.id.btn_protokol_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ShowProtokolKesehatan.class);
                startActivity(i);
                break;
            //Case For Expander Keuangan-----------------------------------------
            case R.id.btn_tambahkeuangan_penggemukan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(), AddKeuangan.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
