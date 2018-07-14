package com.team_pi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {
    private static String drive          = "com.mysql.jdbc.Driver";
    private static String url            = "jdbc:mysql://localhost:3306/team_pi?characterEncoding=utf8&useSSL=true";
    private static String user           = "root";
    private static String password       = "root";

    private static Connection con        = null;
    private static PreparedStatement pst = null;
    private static Statement stmt        = null;
    private static ResultSet rst         = null;

    //建立连接
    public static Connection getConnection() throws Exception{
        Class.forName(drive);
        return DriverManager.getConnection(url,user,password);
    }

    //释放资源
    public static void close(ResultSet rst, Statement stmt, PreparedStatement pst, Connection con){
        if(rst != null) {
            try {
                rst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                rst = null;
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                stmt = null;
            }
        }
        if(pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                pst = null;
            }
        }
        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                con = null;
            }
        }
    }

    //执行预编译
    private static void bindParams(PreparedStatement pst, List<?> sqlParams) throws Exception{
        for(int i = 0;i<sqlParams.size();i++){
            pst.setObject(i+1,sqlParams.get(i));
        }
    }

    //查询操作
    public static List<Map<String, Object>> executeQuery(String sql, List<?> sqlParams) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        con = DbUtil.getConnection();
        pst = con.prepareStatement(sql);
        if (sqlParams != null && !sqlParams.isEmpty()) {
            DbUtil.bindParams(pst,sqlParams);
        }
        rst = pst.executeQuery();
        ResultSetMetaData metaData = rst.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (rst.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = rst.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }
        close(rst,stmt,pst,con);
        return list;
    }

    //增删改操作
    public static int executeUpdate(String sql,List<?> sqlParams) throws Exception{
        con = DbUtil.getConnection();
        pst = con.prepareStatement(sql);
        if(sqlParams != null && !sqlParams.isEmpty()) {
            DbUtil.bindParams(pst, sqlParams);
        }
        int result = pst.executeUpdate();
        close(rst,stmt,pst,con);
        return result;
    }
    //md5加密
    public static String md5(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}
