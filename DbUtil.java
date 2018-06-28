package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {
    private static String drive          = "com.mysql.jdbc.Driver";
    private static String url            = "jdbc:mysql://localhost:3306/student?characterEncoding=utf8&useSSL=true";
    private static String user           = "root";
    private static String password       = "root";

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
    private static void bindsqlParams(PreparedStatement pst, List<?> sqlParams) throws Exception{
        for(int i = 0;i<sqlParams.size();i++){
            pst.setObject(i+1,sqlParams.get(i));
        }
    }

    //查询操作
    public static List<Map<String, Object>> executeQuery(String sql, List<?> sqlParams) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Connection con = DbUtil.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        if (sqlParams != null && !sqlParams.isEmpty()) {
            DbUtil.bindsqlParams(pst,sqlParams);
        }
        ResultSet rst = pst.executeQuery();
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
        close(rst,null,pst,con);
        return list;
    }

    //增删改操作
    public static int executeUpdate(String sql,List<?> sqlParams) throws Exception{
        Connection con = DbUtil.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        if(sqlParams != null && !sqlParams.isEmpty()) {
            DbUtil.bindsqlParams(pst, sqlParams);
        }
        int result = pst.executeUpdate();
        close(null,null,pst,con);
        return result;
    }

}
