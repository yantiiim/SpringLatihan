/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yanti.latihan.service;

import com.yanti.latihan.impl.KoneksiJdbc;
import com.yanti.latihan.model.DataTablesRequest;
import com.yanti.latihan.model.DataTablesResponse;
import com.yanti.latihan.model.Kabupaten;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yanti
 */
@Service
public class KabupatenService {
    @Autowired
        private KoneksiJdbc koneksiJdbc;
    
        @Transactional(readOnly = false)
        public DataTablesResponse<Kabupaten> listKabupatenDataTable(DataTablesRequest req) {
            DataTablesResponse dataTableRespon  = new DataTablesResponse();
            dataTableRespon.setData(koneksiJdbc.getListKabupaten(req));
            Integer total = koneksiJdbc.getBanyakKabupaten(req);
            dataTableRespon.setRecordsFiltered(total);
            dataTableRespon.setRecordsTotal(total);
            dataTableRespon.setDraw(req.getDraw());
            return dataTableRespon;
        }
}
