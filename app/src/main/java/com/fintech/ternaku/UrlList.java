package com.fintech.ternaku;

/**
 * Created by pandu on 10/21/16.
 */

public class UrlList {
    private String Url_Utama = "http://service.ternaku.com/";

    //Core Function================================
    private String Url_GetPeternakan = Url_Utama + "C_Ternak/getPeternakanByUserID";
    private String Url_GetKandang = Url_Utama + "C_Ternak/GetKandangByIdPeternakan";
    private String Url_GetKawanan =  Url_Utama + "C_Ternak/GetKawananByIdPeternakan";
    private String Url_GetTernakPengelompokkan = Url_Utama + "C_Ternak/GetTernakForPengelompokkan";
    private String Url_GetTernakInseminasi = Url_Utama + "C_HistoryInseminasi/GetTernakSudahInseminasi";
    private String Url_GetSemen = Url_Utama + "C_HistoryInseminasi/GetSemen";
    private String Url_GetHeat = Url_Utama + "C_HistoryKesehatan/getDataTernakHeatByPeternakan";
    private String Url_GetHamil = Url_Utama + "C_HistoryInseminasi/GetTernakSudahHamil";
    private String Url_GetMenyusui = Url_Utama + "C_HistoryKesehatan/getDataTernakMenyusuiByPeternakan";
    private String Url_GetVaksin = Url_Utama + "C_Vaksinasi/getVaksin";
    private String Url_GetKarantina = Url_Utama + "C_HistoryKesehatan/getDataTernakKarantinaByPeternakan";
    private String Url_GetKering = Url_Utama + "C_HistoryKesehatan/getDataTernakDryByPeternakan";
    private String Url_GetListJabatan = Url_Utama + "C_Pengguna/getListRole";
    private String Url_GetPakan = Url_Utama + "C_HistoryMakan/getDataPakan";
    private String Url_GetIdRFID = Url_Utama + "C_Ternak/getidTernakByRFID";

    //Login========================================
    private String Url_Login = Url_Utama + "C_Pengguna/cekLogin";

    //Setting======================================
    private String Url_GetPeternakanSetting = Url_Utama + "C_Peternakan/getPeternakan";
    private String Url_ChangePassword = Url_Utama + "C_Pengguna/updatePasswordPeternak";
    private String Url_UpdatePeternak = Url_Utama + "C_Pengguna/updatePeternak";
    private String Url_UpdatePeternakan = Url_Utama + "C_Peternakan/editPeternakan";


    //NavBar=======================================
    //Tambah Peternak++++++++++++++
    private String Url_InsertPeternak = Url_Utama + "C_Pengguna/insertPeternak";
    //Tambah Ternak++++++++++++++++
    private String Url_InsertTernak = Url_Utama + "C_Ternak/insertTernak";
    //Tambah Penggunaan Pakan++++++
    private String Url_InsertPenggunaanPakan = Url_Utama + "C_HistoryMakan/InsertPemakaianPakan";
    //Tambah Penggunaan Keuangan+++
    private String Url_InsertKeuangan = Url_Utama + "C_Keuangan/InsertKeuangan";
    //Tambah Batas Produksi++++++++
    private String Url_InsertBatasProduksi = Url_Utama + "C_BatasProduksi/InsertBatasProduksiSusu";
    //Tambah Produksi Susu+++++++++
    private String Utl_InsertProduksiSusu = Url_Utama + "C_HistoryProduksi/InsertProduksiSusu";

    //Dashboard====================================
    private String Url_GetDashboardInformation= Url_Utama + "C_Ternak/GetDashboardData";

    //Tambah Data==================================
    //Pindah Ternak++++++++++++++++
    //Pindah Ternak------------
    private String Url_InsertPindahTernak = Url_Utama + "C_Ternak/insertinsertHistoryPengelompokan";
    //Tambah Kawanan-----------
    private String Url_InsertKawanan = Url_Utama + "C_Ternak/insertKawanan";
    //Tambah Kandang-----------
    private String Url_InsertKandang = Url_Utama + "C_Ternak/insertKandang";
    //Kesuburan++++++++++++++++++++
    //Tambah Mengandung--------
    private String Url_InsertMengandung = Url_Utama + "C_HistoryInseminasi/UpdateStatusKehamilan";
    //Tambah Inseminasi--------
    private String Url_InsertInseminasi= Url_Utama + "C_HistoryInseminasi/insertInseminasi";
    //Tambah Pemeriksaan Reproduksi--------
    private String Url_InsertPemeriksaanReproduksi= Url_Utama + "C_HistoryKesehatan/InsertKesehatanReproduksi";
    //Tambah Masa Subur--------
    private String Url_InsertHeatMulai= Url_Utama + "C_HistoryKesehatan/HeatMulai";
    private String Url_InsertHeatSelesai= Url_Utama + "C_HistoryKesehatan/HeatSelesai";
    //Tambah Melahirkan--------
    private String Url_InsertMelahirkan = Url_Utama + "C_HistoryInseminasi/UpdateStatusKelahiran";
    //Tambah Menyusui----------
    private String Url_InsertMenyusui = Url_Utama + "C_HistoryKesehatan/StatusMenyusui";
    //Kesehatan++++++++++++++++++++
    //Tambah Cek Kesehatan-----
    private String Url_InsertCekKesehatan = Url_Utama + "C_HistoryKesehatan/InsertKesehatanUmum";
    //Tambah Insert Vaksinasi--
    private String Url_InsertVaksinasi = Url_Utama + "C_Vaksinasi/insertVaksinasi";
    //Tambah Penyakit----------
    private String Url_InsertPenyakit = Url_Utama + "C_HistoryKesehatan/InsertKesehatanKukuMastitisLameness";
    //Tambah Karantina---------
    private String Url_InsertKarantinaMulai = Url_Utama + "C_HistoryKesehatan/KarantinaMulai";
    private String Url_InsertKarantinaSelesai = Url_Utama + "C_HistoryKesehatan/KarantinaSelesai";
    //Produksi++++++++++++++++++++
    //Tambah Masa Kering-------
    private String UrlInsertMasaKeringMulai = Url_Utama + "C_HistoryKesehatan/MasaKeringMulai";
    private String UrlInsertMasaKeringSelesai = Url_Utama + "C_HistoryKesehatan/MasaKeringSelesai";
    //Tambah Culling-----------
    private String UrlInsertCulling = Url_Utama + "C_HistoryKesehatan/UpdateCulling";

    //Tambah Batas Berat
    private String UrlInsertBB = Url_Utama + "C_Pedaging/insertBatasBB";
    private String UrlUpdatetBB = Url_Utama + "C_Pedaging/updateBatasBB";

    //Pengingat====================================
    private String UrlInsert_Reminder = Url_Utama + "C_Fcm/send_message";

    //Laporan====================================
    private String UrlGetLaporanKeuanganList = Url_Utama + "C_Laporan/UangKeluarMasuk_PETERNAKAN_HARI";
    private String UrlGetLaporanKeuanganGrafik_Masuk = Url_Utama + "C_Laporan/UangMasuk_PETERNAKAN_BULAN_TERTENTU";
    private String UrlGetLaporanKeuanganGrafik_Keluar = Url_Utama + "C_Laporan/UangKeluar_PETERNAKAN_BULAN_TERTENTU";
    private String UrlGetlaporanAll = Url_Utama + "C_Laporan/GetDataLaporan";


    //ListDetailTernak=============================
    //Kesehatan+++++++++++++++++++
    private String UrlGet_SehatHariIni = Url_Utama + "C_Ternak/GetTernakSehatByPeternakan";
    private String UrlGet_SakitHariIni = Url_Utama + "C_Ternak/GetTernakTidakSehatByPeternakan";
    //Pemeriksaan+++++++++++++++++
    private String UrlGet_PeriksaHariIni = Url_Utama + "C_Ternak/GetPeriksaHariIni";
    private String UrlGet_BelumPeriksaHariIni = Url_Utama + "C_Ternak/GetBelumPeriksaHariIni";
    //Kesuburan+++++++++++++++++++
    private String UrlGet_SemuaTernakHeat = Url_Utama + "C_Ternak/GetSemuaTernakHeatByPeternakan";
    private String UrlGet_SemuaTernakTidakHeat = Url_Utama + "C_Ternak/GetSemuaTernakTidakHeatByPeternakan";
    //Kawanan+++++++++++++++++++++
    private String UrlGet_SemuaKawananTernakDewasa = Url_Utama + "C_Ternak/GetSemuaTernakDewasaByPeternakan";
    private String UrlGet_SemuaKawananTernakHeifers = Url_Utama + "C_Ternak/GetSemuaTernakHeifersByPeternakan";
    private String UrlGet_SemuaKawananTernakBayi = Url_Utama + "C_Ternak/GetSemuaTernakBayiByPeternakan";
    private String UrlGet_SemuaKawananTernakLainnya = Url_Utama + "C_Ternak/GetSemuaTernakLainnyaByPeternakan";
    //Data Kehamilan++++++++++++++
    private String UrlGet_KehamilanMenyusui = Url_Utama + "C_Ternak/GetSemuaTernakMenyusuiByPeternakan";
    private String UrlGet_KehamilanMelahirkan = Url_Utama + "C_Ternak/GetSemuaTernakMelahirkanByPeternakan";
    private String UrlGet_KehamilanMengandung = Url_Utama + "C_Ternak/GetSemuaTernakHamilByPeternakan";
    private String UrlGet_KehamilanLainnya = Url_Utama + "C_Ternak/GetSemuaTernakTidakHamilMelahirkanMenyusuiBulanIniByPeternakan";
    private String UrlGet_SegmentList = Url_Utama + "C_Ternak/GetSemuaTernakByPeternakanOnSegment";

    //DetailTernak=================================
    //Profile+++++++++++++++++++++
    private String UrlGet_TernakProfile = Url_Utama + "C_Ternak/GetTernakUmum";
    //Event+++++++++++++++++++++++
    private String UrlGet_TernakEvent = Url_Utama + "C_Ternak/GetTernakUmumEvent";
    //Task++++++++++++++++++++++++
    private String Url_InsertTask = Url_Utama + "C_Ternak/insertTask";
    private String UrlGet_TernakTask = Url_Utama + "C_Ternak/GetTask";

    //Handling=====================================
    private String UrlGet_RFIDanIdCek = Url_Utama + "C_Handling/cekIDTernakTerdaftar";
    private String UrlGet_RFIDCek = Url_Utama + "C_Handling/cekRFIdTerdaftar";


    //Pedaging||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    private String Url_GetBatasBBByID = Url_Utama + "C_Pedaging/getBatasBB";
    private String Url_GetKawananBB = Url_Utama + "C_Pedaging/GetKawananForBatasBB";
    private String Url_GetAdg = Url_Utama + "C_Pedaging/getADGTernak";
    //AddPakanPedaing--------------------------------------------------
    private String Url_GetKomposisi = Url_Utama + "C_Pedaging/GetKomposisiPakanForMakanTernak";
    private String Url_EditTernak = Url_Utama + "C_Ternak/editTernak";
    private String Url_GetTernakByID = Url_Utama + "C_Ternak/getTernakByID";

    private String Url_DeleteTernak = Url_Utama + "C_Ternak/deleteTernak";

    public UrlList(){

    }

    public String getUrl_GetKomposisi() {
        return Url_GetKomposisi;
    }

    public String getUrl_GetAdg() {
        return Url_GetAdg;
    }

    public String getUrlGetlaporanAll() {
        return UrlGetlaporanAll;
    }

    public String getUrlGet_SehatHariIni() {
        return UrlGet_SehatHariIni;
    }

    public String getUrlGet_SakitHariIni() {
        return UrlGet_SakitHariIni;
    }

    public String getUrl_GetIdRFID() {
        return Url_GetIdRFID;
    }

    public String getUrl_InsertTask() {
        return Url_InsertTask;
    }

    public String getUrlGet_TernakTask() {
        return UrlGet_TernakTask;
    }

    public String getUrl_UpdatePeternakan() {
        return Url_UpdatePeternakan;
    }

    public String getUrl_UpdatePeternak() {
        return Url_UpdatePeternak;
    }

    public String getUrl_ChangePassword() {
        return Url_ChangePassword;
    }

    public String getUrl_GetPeternakanSetting() {
        return Url_GetPeternakanSetting;
    }

    public String getUrlGet_RFIDanIdCek() {
        return UrlGet_RFIDanIdCek;
    }

    public String getUrlGet_RFIDCek() {
        return UrlGet_RFIDCek;
    }

    public String getUrlGetLaporanKeuanganList() {
        return UrlGetLaporanKeuanganList;
    }

    public String getUrlGetLaporanKeuanganGrafik_Masuk() {
        return UrlGetLaporanKeuanganGrafik_Masuk;
    }

    public String getUrlGetLaporanKeuanganGrafik_Keluar() {
        return UrlGetLaporanKeuanganGrafik_Keluar;
    }

    public String getUrlInsert_Reminder() {
        return UrlInsert_Reminder;
    }

    public String getUrlGet_TernakEvent() {
        return UrlGet_TernakEvent;
    }

    public String getUrlGet_TernakProfile() {
        return UrlGet_TernakProfile;
    }

    public String getUrlGet_SegmentList() {
        return UrlGet_SegmentList;
    }

    public String getUrlGet_SemuaKawananTernakDewasa() {
        return UrlGet_SemuaKawananTernakDewasa;
    }

    public String getUrlGet_SemuaKawananTernakBayi() {
        return UrlGet_SemuaKawananTernakBayi;
    }

    public String getUrlGet_SemuaKawananTernakHeifers() {
        return UrlGet_SemuaKawananTernakHeifers;
    }

    public String getUrlGet_SemuaKawananTernakLainnya() {
        return UrlGet_SemuaKawananTernakLainnya;
    }

    public String getUrlGet_SemuaTernakTidakHeat() {
        return UrlGet_SemuaTernakTidakHeat;
    }

    public String getUrlGet_SemuaTernakHeat() {
        return UrlGet_SemuaTernakHeat;
    }

    public String getUrlGet_KehamilanMenyusui() {
        return UrlGet_KehamilanMenyusui;
    }

    public String getUrlGet_KehamilanMengandung() {
        return UrlGet_KehamilanMengandung;
    }

    public String getUrlGet_KehamilanMelahirkan() {
        return UrlGet_KehamilanMelahirkan;
    }

    public String getUrlGet_KehamilanLainnya() {
        return UrlGet_KehamilanLainnya;
    }

    public String getUrlGet_PeriksaHariIni() {
        return UrlGet_PeriksaHariIni;
    }

    public String getUrlGet_BelumPeriksaHariIni() {
        return UrlGet_BelumPeriksaHariIni;
    }

    public String getUtl_InsertProduksiSusu() {
        return Utl_InsertProduksiSusu;
    }

    public String getUrl_InsertBatasProduksi() {
        return Url_InsertBatasProduksi;
    }

    public String getUrl_InsertKeuangan() {
        return Url_InsertKeuangan;
    }

    public String getUrl_InsertPenggunaanPakan() {
        return Url_InsertPenggunaanPakan;
    }

    public String getUrl_GetPakan() {
        return Url_GetPakan;
    }

    public String getUrl_InsertTernak() {
        return Url_InsertTernak;
    }

    public String getUrl_InsertPeternak() {
        return Url_InsertPeternak;
    }

    public String getUrl_GetListJabatan() {
        return Url_GetListJabatan;
    }

    public String getUrlInsertCulling() {
        return UrlInsertCulling;
    }

    public String getUrl_GetKering() {
        return Url_GetKering;
    }

    public String getUrlInsertMasaKeringSelesai() {
        return UrlInsertMasaKeringSelesai;
    }

    public String getUrlInsertMasaKeringMulai() {
        return UrlInsertMasaKeringMulai;
    }

    public String getUrl_InsertKarantinaMulai() {
        return Url_InsertKarantinaMulai;
    }

    public String getUrl_InsertKarantinaSelesai() {
        return Url_InsertKarantinaSelesai;
    }

    public String getUrl_GetKarantina() {
        return Url_GetKarantina;
    }

    public String getUrl_InsertPenyakit() {
        return Url_InsertPenyakit;
    }

    public String getUrl_InsertVaksinasi() {
        return Url_InsertVaksinasi;
    }

    public String getUrl_GetVaksin() {
        return Url_GetVaksin;
    }

    public String getUrl_InsertCekKesehatan() {
        return Url_InsertCekKesehatan;
    }

    public String getUrl_GetMenyusui() {
        return Url_GetMenyusui;
    }

    public String getUrl_InsertMenyusui() {
        return Url_InsertMenyusui;
    }

    public String getUrl_InsertMelahirkan() {
        return Url_InsertMelahirkan;
    }

    public String getUrl_GetHamil() {
        return Url_GetHamil;
    }

    public String getUrl_GetHeat() {
        return Url_GetHeat;
    }

    public String getUrl_InsertHeatSelesai() {
        return Url_InsertHeatSelesai;
    }

    public String getUrl_InsertHeatMulai() {
        return Url_InsertHeatMulai;
    }

    public String getUrl_InsertPemeriksaanReproduksi() {
        return Url_InsertPemeriksaanReproduksi;
    }

    public String getUrl_GetSemen() {
        return Url_GetSemen;
    }

    public String getUrl_InsertInseminasi() {
        return Url_InsertInseminasi;
    }

    public String getUrl_InsertMengandung() {
        return Url_InsertMengandung;
    }

    public String getUrl_GetTernakInseminasi() {
        return Url_GetTernakInseminasi;
    }

    public String getUrl_InsertKawanan() {
        return Url_InsertKawanan;
    }

    public String getUrl_InsertKandang() {
        return Url_InsertKandang;
    }

    public String getUrl_InsertPindahTernak() {
        return Url_InsertPindahTernak;
    }

    public String getUrl_GetTernakPengelompokkan() {
        return Url_GetTernakPengelompokkan;
    }

    public String getUrl_GetKawanan() {
        return Url_GetKawanan;
    }

    public String getUrl_GetKandang() {
        return Url_GetKandang;
    }

    public String getUrl_Login() {
        return Url_Login;
    }

    public String getUrl_GetPeternakan() {
        return Url_GetPeternakan;
    }

    public String getUrl_GetDashboardInformation() {
        return Url_GetDashboardInformation;
    }

    public String getUrlInsertBB() {
        return UrlInsertBB;
    }

    public void setUrlInsertBB(String urlInsertBB) {
        UrlInsertBB = urlInsertBB;
    }

    public String getUrlUpdatetBB() {
        return UrlUpdatetBB;
    }

    public void setUrlUpdatetBB(String urlUpdatetBB) {
        UrlUpdatetBB = urlUpdatetBB;
    }

    public String getUrl_GetBatasBBByID() {
        return Url_GetBatasBBByID;
    }

    public void setUrl_GetBatasBBByID(String url_GetBatasBBByID) {
        Url_GetBatasBBByID = url_GetBatasBBByID;
    }

    public String getUrl_GetKawananBB() {
        return Url_GetKawananBB;
    }

    public void setUrl_GetKawananBB(String url_GetKawananBB) {
        Url_GetKawananBB = url_GetKawananBB;
    }

    public String getUrl_EditTernak() {
        return Url_EditTernak;
    }

    public void setUrl_EditTernak(String url_EditTernak) {
        Url_EditTernak = url_EditTernak;
    }

    public String getUrl_GetTernakByID() {
        return Url_GetTernakByID;
    }

    public void setUrl_GetTernakByID(String url_GetTernakByID) {
        Url_GetTernakByID = url_GetTernakByID;
    }

    public String getUrl_DeleteTernak() {
        return Url_DeleteTernak;
    }

    public void setUrl_DeleteTernak(String url_DeleteTernak) {
        Url_DeleteTernak = url_DeleteTernak;
    }
}
