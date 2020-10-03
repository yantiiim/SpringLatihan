/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yanti.latihan.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author yanti
 */
public class Provinsi {
    
    @NotNull(message = "Tidak boleh Null")
    @Min(value = 3000, message = "Tidak Boleh Kurang Dari 3000")
    private int idProvinsi;
    
    @NotEmpty(message = "Harus diisi")
    @Size(min = 3)
    private String namaProvinsi;

    public int getIdProvinsi() {
        return idProvinsi;
    }

    public void setIdProvinsi(int idProvinsi) {
        this.idProvinsi = idProvinsi;
    }

    public String getNamaProvinsi() {
        return namaProvinsi;
    }

    public void setNamaProvinsi(String namaProvinsi) {
        this.namaProvinsi = namaProvinsi;
    }
    
    
}
