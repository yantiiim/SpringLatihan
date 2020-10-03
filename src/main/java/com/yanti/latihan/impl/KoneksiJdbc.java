/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yanti.latihan.impl;

import com.yanti.latihan.model.DataTablesRequest;
import com.yanti.latihan.model.Kabupaten;
import com.yanti.latihan.model.Kecamatan;
import com.yanti.latihan.model.Provinsi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author yanti
 */
@Repository
public class KoneksiJdbc {
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void getNamaAnggota() {
        Connection conn = null;
        try{
            conn = dataSource.getConnection();
//                DriverManager.getConnection("jdbc:mysql://192.168.100.250/bmt_v1?" +
//                                            "user=root&password=passwordnyaRoot&serverTimezone=UTC");
            
            
            String sql = "SELECT namaProvinsi, kodeBPS FROM provinsi";
            PreparedStatement preparedStatement =
                    conn.prepareStatement(sql);
            
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                System.out.println(result.getInt("kodeBPS")+" : "+result.getString("namaProvinsi"));
            }
        } catch (SQLException ex){
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: ");
            ex.printStackTrace();
        }
    }
    
    public List<Provinsi> getProvinsi(){
        
        String SQL = "SELECT namaProvinsi,kodeBPS as idProvinsi FROM provinsi";
//        List <Provinsi> prop = jdbcTemplate.query(SQL, (rs, rowNum) -> {
//            Provinsi pr = new Provinsi();
//            pr.setIdProvinsi(rs.getInt("kodeBPS"));
//            pr.setNamaProvinsi(rs.getString("namaProvinsi"));
//            return pr;
//        });
        List<Provinsi> prop = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Provinsi.class));
        return prop;
    }
    
    public Optional<Provinsi> getProvinsiById(int id){
        String SQL = "SELECT namaProvinsi, kodeBPS as idProvinsi FROM provinsi where kodeBPS = ?";
        Object param[] = {id};
        try {
            return Optional.of (jdbcTemplate.queryForObject(SQL, param, BeanPropertyRowMapper.newInstance(Provinsi.class)));
        }catch (Exception e) {
            return Optional.empty();
        }
    }
    

    
    public void insertProvinsi(Provinsi provinsi){
        String sql = "insert into provinsi (kodeBPS,namaProvinsi) values (?,?)";
        Object param[] = {provinsi.getIdProvinsi(),provinsi.getNamaProvinsi()};
        jdbcTemplate.update(sql, param);
    }
    
    public void updateProvinsi(Provinsi provinsi){
        String sql = "UPDATE provinsi SET namaProvinsi=? WHERE kodeBPS=?";
        Object param[] = {provinsi.getNamaProvinsi(),provinsi.getIdProvinsi()};
        jdbcTemplate.update(sql, param);
    }
    
    public void insertOrUpdateProvinsi (Provinsi provinsi){
        Optional<Provinsi> data=getProvinsiById(provinsi.getIdProvinsi());
        if(data.isPresent()){
            updateProvinsi(provinsi);
        }else{
            insertProvinsi(provinsi);
        }
    }
    
//    public void deleteProvinsi(Provinsi provinsi){
//        String sql = "DELETE FROM provinsi WHERE kodeBPS=?";
//        Object param[] = {provinsi.getNamaProvinsi(),provinsi.getIdProvinsi()};
//        jdbcTemplate.update(sql, param);
//    }
    
    //Kabupaten
    public List<Kabupaten> getKabupaten(){
        
        String SQL = "SELECT a.kodeBPS as idKabupaten, namaKabupaten as namaKabupaten, \n "
                + "kodeProvinsi as idProvinsi , p.namaProvinsi "
                + "FROM kabupaten a INNER JOIN provinsi p on p.kodeBPS = a.kodeProvinsi "
                + "WHERE 1=1";
        
        
        return jdbcTemplate.query(SQL, (rs, rowNum) -> {
            Kabupaten kab = new Kabupaten();
            kab.setIdKabupaten(rs.getInt("idKabupaten"));
            kab.setNamaKabupaten(rs.getString("namaKabupaten"));
            Provinsi prov = new Provinsi();
            prov.setNamaProvinsi(rs.getString("namaProvinsi"));
            prov.setIdProvinsi(rs.getInt("idProvinsi"));
            kab.setProvinsi(prov);
            return kab;
        });
//        List<Kabupaten> kab = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class));
//        return kab;
    }
    
        public List<Kabupaten> getListKabupaten(int idProv){
        
        String SQL = "SELECT a.kodeBPS as idKabupaten, namaKabupaten as namaKabupaten, \n "
                + "kodeProvinsi as idProvinsi , p.namaProvinsi "
                + "FROM kabupaten a INNER JOIN provinsi p on p.kodeBPS = a.kodeProvinsi "
                + "WHERE kodeProvinsi = ?";
        
        Object[] param = {idProv};
        return jdbcTemplate.query(SQL, param, (rs, rowNum) -> {
            Kabupaten kab = new Kabupaten();
            kab.setIdKabupaten(rs.getInt("idKabupaten"));
            kab.setNamaKabupaten(rs.getString("namaKabupaten"));
            Provinsi prov = new Provinsi();
            prov.setNamaProvinsi(rs.getString("namaProvinsi"));
            prov.setIdProvinsi(rs.getInt("idProvinsi"));
            kab.setProvinsi(prov);
            return kab;
        });
//        List<Kabupaten> kab = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class));
//        return prop;
    }
    
    public List<Kabupaten> getKabupatenSearch(Optional<String>nama){
        
        String SQL = "SELECT a.kodeBPS as idKabupaten, namaKabupaten as namaKabupaten, \n kodeProvinsi as idProvinsi , p.namaProvinsi FROM kabupaten a INNER JOIN provinsi p on p.kodeBPS = a.kodeProvinsi WHERE 1=1";
        
        Object[] param=new Object[1];
        if(nama.isPresent()){
            SQL += " and namaKabupaten like CONCAT('%', ? , '%')";
            param[0] =nama.get();
        }
        
        return jdbcTemplate.query(SQL, param, (rs, rowNum) -> {
            Kabupaten kab = new Kabupaten();
            kab.setIdKabupaten(rs.getInt("idKabupaten"));
            kab.setNamaKabupaten(rs.getString("namaKabupaten"));
            Provinsi prov = new Provinsi();
            prov.setNamaProvinsi(rs.getString("namaProvinsi"));
            prov.setIdProvinsi(rs.getInt("idProvinsi"));
            kab.setProvinsi(prov);
            return kab;
        });
//        List<Kabupaten> kab = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class));
//        return kab;
    }
    
    public Optional<Kabupaten> getKabupatenById(int id){
        String SQL = "SELECT namaKabupaten, kodeBPS as idKabupaten, kodeProvinsi FROM kabupaten where kodeBPS = ?";
        Object param[] = {id};
        try {
            return Optional.of (jdbcTemplate.queryForObject(SQL, param, BeanPropertyRowMapper.newInstance(Kabupaten.class)));
        }catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public void insertKabupaten(Kabupaten kabupaten){
        String sql = "insert into kabupaten (kodeBPS,namaKabupaten,kodeProvinsi) values (?,?,?)";
        Object param[] = {kabupaten.getIdKabupaten(),kabupaten.getNamaKabupaten(),kabupaten.getKodeProvinsi()};
        jdbcTemplate.update(sql, param);
    }
    
    public void updateKabupaten(Kabupaten kabupaten){
        String sql = "UPDATE kabupaten SET namaKabupaten=?, kodeProvinsi=? WHERE kodeBPS=?";
        Object param[] = {kabupaten.getNamaKabupaten(),kabupaten.getKodeProvinsi(),kabupaten.getIdKabupaten()};
        jdbcTemplate.update(sql, param);
    }
    
    public void insertOrUpdateKabupaten (Kabupaten kabupaten){
        Optional<Kabupaten> data=getKabupatenById(kabupaten.getIdKabupaten());
        if(data.isPresent()){
            updateKabupaten(kabupaten);
        }else{
            insertKabupaten(kabupaten);
        }
    }
    
    public void deleteKabupaten(Kabupaten kabupaten){
        String sql = "delete from kabupaten where kodeBPS = ?";

        Object param[] = {kabupaten.getNamaKabupaten(),kabupaten.getKodeProvinsi(),kabupaten.getIdKabupaten()};

        jdbcTemplate.update(sql,param);
    }
    
    //Kecamatan
    
    public List<Kecamatan> getKecamatan(){
        
        String SQL = "SELECT a.kodeBPS as idKecamatan, namaKecamatan as namaKecamatan, \n kodeKabupaten as idKabupaten , p.namaKabupaten FROM kecamatan a INNER JOIN kabupaten p on p.kodeBPS = a.kodeKabupaten WHERE 1=1";
        
        
        return jdbcTemplate.query(SQL, (rs, rowNum) -> {
            Kecamatan kec = new Kecamatan();
            kec.setIdKecamatan(rs.getInt("idKecamatan"));
            kec.setNamaKecamatan(rs.getString("namaKecamatan"));
            Kabupaten kab = new Kabupaten();
            kab.setNamaKabupaten(rs.getString("namaKabupaten"));
            kab.setIdKabupaten(rs.getInt("idKabupaten"));
            kec.setKabupaten(kab);
            return kec;
        });
//        List<Kabupaten> kab = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class));
//        return kab;
    }
    
    public List<Kecamatan> getKecamatanSearch(Optional<String>nama){
        
        String SQL = "SELECT a.kodeBPS as idKecamatan, namaKecamatan as namaKecamatan, \n kodeKabupaten as idKabupaten , p.namaKabupaten FROM kecamatan a INNER JOIN kabupaten p on p.kodeBPS = a.kodeKabupaten WHERE 1=1";
        
        Object[] param=new Object[1];
        if(nama.isPresent()){
            SQL += " and namaKecamatan like CONCAT('%', ? , '%')";
            param[0] =nama.get();
        }
        
        return jdbcTemplate.query(SQL, param, (rs, rowNum) -> {
            Kecamatan kec = new Kecamatan();
            kec.setIdKecamatan(rs.getInt("idKecamatan"));
            kec.setNamaKecamatan(rs.getString("namaKecamatan"));
            Kabupaten kab = new Kabupaten();
            kab.setNamaKabupaten(rs.getString("namaKabupaten"));
            kab.setIdKabupaten(rs.getInt("idKabupaten"));
            kec.setKabupaten(kab);
            return kec;
        });
//        List<Kabupaten> kab = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class));
//        return kab;
    }
    
    public Optional<Kecamatan> getKecamatanById(int id){
        String SQL = "SELECT k.kodeBPS as idKecamatan, k.namaKecamatan, k.kodeKabupaten, kb.kodeProvinsi as kodeProvinsi from kecamatan k join kabupaten kb on k.kodeKabupaten = kb.kodeBps where k.kodeBps = ?";
        Object param[] = {id};
        try {
            return Optional.of (jdbcTemplate.queryForObject(SQL, param, BeanPropertyRowMapper.newInstance(Kecamatan.class)));
        }catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public void insertKecamatan(Kecamatan kecamatan){
        String sql = "insert into kecamatan (kodeBPS,namaKecamatan,kodeKabupaten) values (?,?,?)";
        Object param[] = {kecamatan.getIdKecamatan(),kecamatan.getNamaKecamatan(),kecamatan.getKodeKabupaten()};
        jdbcTemplate.update(sql, param);
    }
    
    public void updateKecamatan(Kecamatan kecamatan){
        String sql = "UPDATE kecamatan SET namaKecamatan=?, kodeKabupaten=? WHERE kodeBPS=?";
        Object param[] = {kecamatan.getNamaKecamatan(),kecamatan.getKodeKabupaten(),kecamatan.getIdKecamatan()};
        jdbcTemplate.update(sql, param);
    }
    
    public void insertOrUpdateKecamatan (Kecamatan kecamatan){
        Optional<Kecamatan> data=getKecamatanById(kecamatan.getIdKecamatan());
        if(data.isPresent()){
            updateKecamatan(kecamatan);
        }else{
            insertKecamatan(kecamatan);
        }
    }
    
    public Integer getBanyakProvinsi(DataTablesRequest req) {
        String query = "SELECT count(kodeBPS) as banyak FROM provinsi";
        if(!req.getExtraParam().isEmpty()){
            String namaProvinsi = (String) req.getExtraParam().get("namaProvinsi");
            query = " SELECT count(kodeBPS) as banyak FROM provinsi where namaProvinsi like concat('%',?,'%')";
            return jdbcTemplate.queryForObject(query, Integer.class, namaProvinsi);
        }else{
            return this.jdbcTemplate.queryForObject(query, null, Integer.class);
        }
        
    }
    
    public List<Provinsi> getListProvinsi(DataTablesRequest req) {
        String SQL = "SELECT kodeBPS as idProvinsi, namaProvinsi FROM provinsi "
                + "order by "+(req.getSortCol()+1)+"  "+req.getSortDir() +" limit ? offset ?";
        if(!req.getExtraParam().isEmpty()){
            String namaProvinsi = (String) req.getExtraParam().get("namaProvinsi");
            SQL = "SELECT kodeBPS as idProvinsi, namaProvinsi FROM provinsi where namaProvinsi like concat('%',?,'%')"
                + " order by "+(req.getSortCol()+1)+"  "+req.getSortDir() +" limit ? offset ?";
            return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Provinsi.class), namaProvinsi, req.getLength(), req.getStart());
        }else{
            return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Provinsi.class), req.getLength(), req.getStart());
        }
        
    }
    
    public Integer getBanyakKabupaten(DataTablesRequest req) {
        String query = "SELECT count(kodeBPS) as banyak FROM kabupaten";
        if(!req.getExtraParam().isEmpty()){
            String namaKabupaten = (String) req.getExtraParam().get("namaKabupaten");
            query = " SELECT count(kodeBPS) as banyak FROM kabupaten where namaKabupaten like concat('%',?,'%')";
            return jdbcTemplate.queryForObject(query, Integer.class, namaKabupaten);
        }else{
            return this.jdbcTemplate.queryForObject(query, null, Integer.class);
        }
    }
    
    public List<Kabupaten> getListKabupaten(DataTablesRequest req) {
        String SQL = "SELECT a.kodeBPS as idKabupaten, namaKabupaten as namaKabupaten, \n kodeProvinsi as idProvinsi , p.namaProvinsi FROM kabupaten a INNER JOIN provinsi p on p.kodeBPS = a.kodeProvinsi WHERE 1=1 "
                + "order by "+(req.getSortCol()+1)+"  "+req.getSortDir() +" limit ? offset ?";
        if(!req.getExtraParam().isEmpty()){
            String namaKabupaten = (String) req.getExtraParam().get("namaKabupaten");
            SQL = "SELECT a.kodeBPS as idKabupaten, namaKabupaten as namaKabupaten, "
                    + "\n kodeProvinsi as idProvinsi , p.namaProvinsi "
                    + "FROM kabupaten a INNER JOIN provinsi p on p.kodeBPS = a.kodeProvinsi WHERE 1=1 "
                    + "AND namaKabupaten like concat('%',?,'%')"
                + " order by "+(req.getSortCol()+1)+"  "+req.getSortDir() +" limit ? offset ?";
            return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class), namaKabupaten, req.getLength(), req.getStart());
        }else{
            return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kabupaten.class), req.getLength(), req.getStart());
        }
    }
    
    public Integer getBanyakKecamatan() {
        String query = "SELECT count(kodeBPS) as banyak FROM kecamatan";
        return this.jdbcTemplate.queryForObject(query, null, Integer.class);
    }
    
    public List<Kecamatan> getListKecamatan(DataTablesRequest req) {
        String SQL = "SELECT a.kodeBPS as idKecamatan, namaKecamatan as namaKecamatan, "
                + "\n kodeKabupaten as idKabupaten, k.namaKabupaten, "
                + "\n kodeProvinsi as idProvinsi, p.namaProvinsi FROM kecamatan a "
                + "INNER JOIN kabupaten k on k.kodeBPS = a.kodeKabupaten "
                + "INNER JOIN provinsi p on p.kodeBPS = k.kodeProvinsi WHERE 1=1 "
                + "order by "+(req.getSortCol()+1)+"  "+req.getSortDir() +" limit ? offset ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Kecamatan.class), req.getLength(), req.getStart());
    }
}
