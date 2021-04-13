/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.test.pojo.VeXe;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VeXeService {
    public boolean kiemTraDat(int soGhe, int ChuyenID) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT VeDat FROM vexe WHERE ChuyenXeID = ? AND SoGhe = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, ChuyenID);
        stm.setInt(2, soGhe);
        ResultSet rs = stm.executeQuery();
        if (rs.next()){
            return rs.getByte("VeDat") == 0; // true nếu vé trống, false nếu vé đã được đặt
        }
       return false; // vé không tồn tại
    }
    
    public List<VeXe> getVeXe() throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "select * from vexe";
        PreparedStatement stm = conn.prepareStatement(sql);
        List<VeXe> dsVeXe = new ArrayList<>();
        ResultSet rs = stm.executeQuery();
        while(rs.next()){
            VeXe ve = new VeXe();
            ChuyenXeService cxService = new ChuyenXeService();
            KhachHangService khService = new KhachHangService();
            NhanVienService nvService = new NhanVienService();
            
            ve.setVeXeId(rs.getInt("VeXeID"));
            ve.setChuyenXe(cxService.getChuyenByID(rs.getInt("ChuyenXeID")));
            ve.setSoGhe(rs.getInt("SoGhe"));
            ve.setGioDat(rs.getTime("GioDat"));
            ve.setNgayDat(rs.getDate("NgayDat"));
            ve.setKhachHang(khService.getKhachHangById(rs.getInt("KhachHangID")));
            ve.setNhanVien(nvService.getNhanVienById(rs.getInt("NhanVienID")));
            ve.setVeDat(rs.getByte("VeDat"));
            
            dsVeXe.add(ve);
        }
        return dsVeXe;
    }
    
    public List<VeXe> getVeXeByChuyenXeId(int chuyenXeId) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "select * from vexe where ChuyenXeID = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, chuyenXeId);
        List<VeXe> dsVeXe = new ArrayList<>();
        ResultSet rs = stm.executeQuery();
        while(rs.next()){
            VeXe ve = new VeXe();
            ChuyenXeService cxService = new ChuyenXeService();
            KhachHangService khService = new KhachHangService();
            NhanVienService nvService = new NhanVienService();
            
            ve.setVeXeId(rs.getInt("VeXeID"));
            ve.setChuyenXe(cxService.getChuyenByID(rs.getInt("ChuyenXeID")));
            ve.setSoGhe(rs.getInt("SoGhe"));
            ve.setGioDat(rs.getTime("GioDat"));
            ve.setNgayDat(rs.getDate("NgayDat"));
            ve.setKhachHang(khService.getKhachHangById(rs.getInt("KhachHangID")));
            ve.setNhanVien(nvService.getNhanVienById(rs.getInt("NhanVienID")));
            ve.setVeDat(rs.getByte("VeDat"));
            
            dsVeXe.add(ve);
        }
        return dsVeXe;
    }
    
    public VeXe getVeXeBySoGhe(int soGhe, int chuyenXeID) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "SELECT * FROM vexe where SoGhe = ? AND ChuyenXeID = ?";
        //excute query
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, soGhe);
        stm.setInt(2, chuyenXeID);
        ResultSet rs = stm.executeQuery();
        if(rs.next()){
           VeXe ve = new VeXe();
           ChuyenXeService cxService = new ChuyenXeService();
           NhanVienService nvService = new NhanVienService();
           KhachHangService khService = new KhachHangService();
           
           ve.setVeXeId(rs.getInt("VeXeID"));
           ve.setChuyenXe(cxService.getChuyenByID(rs.getInt("ChuyenXeID")));
           ve.setSoGhe(rs.getInt("SoGhe"));
           ve.setGioDat(rs.getTime("GioDat"));
           ve.setNgayDat(rs.getDate("NgayDat"));
           ve.setKhachHang(khService.getKhachHangById(rs.getInt("KhachHangID")));
           ve.setNhanVien(nvService.getNhanVienById(rs.getInt("NhanVienID")));
           ve.setVeDat(rs.getByte("VeDat"));
           
           return ve;
        }
        return null;
    }
    
    public boolean addVeXe(VeXe v) {
        try {
            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO vexe(ChuyenXeID, SoGhe,VeDat)" + "Values(?,?,?)";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1,v.getChuyenXe().getChuyenXeId());
            stm.setInt(2,v.getSoGhe());
            stm.setInt(5, v.getVeDat());
            int executeUpdate = stm.executeUpdate();
            conn.commit();
            return executeUpdate > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean datVeXe(int soGhe,int chuyenXeId, int khachHangId, int nhanVienId){
        try {
            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);
            String sql = "Update vexe "
                    + "set GioDat = ?, NgayDat = ?, KhachHangID = ?, NhanVienID = ?,VeDat = ?"
                    + "where SoGhe = ? and ChuyenXeID = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setTime(1, java.sql.Time.valueOf(LocalTime.now()));
            stm.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stm.setInt(3, khachHangId);
            stm.setInt(4, nhanVienId);
            stm.setByte(5, (byte)1);
            stm.setInt(6, soGhe);
            stm.setInt(7, chuyenXeId);
            int executeUpdate = stm.executeUpdate();
            if (executeUpdate > 0){
                conn.commit();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
    public boolean huyVeXe(int soGhe, int chuyenXeId) {
        try {
            Connection conn = JdbcUtils.getConn();
            conn.setAutoCommit(false);
            String sql = "Update vexe "
                    + "set GioDat = ?, NgayDat = ?, KhachHangID = ?, NhanVienID = ?,VeDat = ?"
                    + "where SoGhe = ? and ChuyenXeID = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setObject(1, null);
            stm.setObject(2, null);
            stm.setObject(3, null);
            stm.setObject(4, null);
            stm.setByte(5, (byte)0);
            stm.setInt(6, soGhe);
            stm.setInt(7, chuyenXeId);
            int executeUpdate = stm.executeUpdate();
            if (executeUpdate > 0){
                conn.commit();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean doiVe(VeXe ve1, int gheMoi, int chuyenXeMoi) throws SQLException{
        boolean kt = kiemTraDat(gheMoi, chuyenXeMoi);
        if (kt == true){         
            if (datVeXe(gheMoi, chuyenXeMoi, ve1.getKhachHang().getKhachHangId(),ve1.getNhanVien().getNhanVienId()))
                if (huyVeXe(ve1.getSoGhe(), ve1.getChuyenXe().getChuyenXeId()))
                    return true;
        }
        return false;
    }
    
    public boolean CheckTimeDatVe(int chuyenXeId) throws SQLException{
        Connection conn = JdbcUtils.getConn();
        String sql = "Select DateDiff(NgayKhoiHanh,?) from chuyenxe where ChuyenXeID = ?";
        String sql2 = "Select TIME_TO_SEC(TimeDiff(GioKhoiHanh,?)) from chuyenxe where ChuyenXeID = ?";
        PreparedStatement stm1 = conn.prepareStatement(sql);
        stm1.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        stm1.setInt(2,chuyenXeId);
        ResultSet rs = stm1.executeQuery();
        if(rs.next())
            if(rs.getInt(1) > 0)
                return true;
            else if (rs.getInt(1) == 0)
            {
                PreparedStatement stm2 = conn.prepareStatement(sql2);
                stm2.setTime(1, java.sql.Time.valueOf(LocalTime.now()));
                stm2.setInt(2, chuyenXeId);
                ResultSet rs2 = stm2.executeQuery();
                rs.next();
                if (rs2.getFloat(1) >= 60)
                    return true;
            }
        return false;
    }
}
