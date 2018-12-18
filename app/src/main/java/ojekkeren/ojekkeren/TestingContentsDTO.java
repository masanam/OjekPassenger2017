package ojekkeren.ojekkeren;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by andi on 3/26/2016.
 */
public class TestingContentsDTO {
    private String search;
    private String nama;
    private String alamat;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}
