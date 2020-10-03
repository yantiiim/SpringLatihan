/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yanti.latihan.service;

import com.yanti.latihan.impl.KoneksiJdbc;
import com.yanti.latihan.model.DataTablesRequest;
import com.yanti.latihan.model.DataTablesResponse;
import com.yanti.latihan.model.Provinsi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yanti
 */
@Service
public class ProvinsiService {
    @Autowired
        private KoneksiJdbc koneksiJdbc;
    
        @Transactional(readOnly = false)
        public DataTablesResponse<Provinsi> listProvinsiDataTable(DataTablesRequest req) {
            DataTablesResponse dataTableRespon  = new DataTablesResponse();
            dataTableRespon.setData(koneksiJdbc.getListProvinsi(req));
            Integer total = koneksiJdbc.getBanyakProvinsi(req);
            dataTableRespon.setRecordsFiltered(total);
            dataTableRespon.setRecordsTotal(total);
            dataTableRespon.setDraw(req.getDraw());
            return dataTableRespon;
        }
}
