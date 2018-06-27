package util;

import java.sql.*;
import java.util.List;

public class DbUtil {
    private static String drive    = "com.mysql.jdbc.Driver";
    private static String url      = "jdbc:mysql://localhost:3306/student?characterEncoding=utf8&useSSL=true";
    private static String user     = "root";
    private static String password = "root";

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
    private static void bindParams(PreparedStatement pst, List<Object> sqlParams){
        for(int i = 0;i<sqlParams.size();i++){
            try{
                pst.setObject(i+1,sqlParams.get(i));
            }catch (Exception e){}
        }
    }

    //查询操作
    public static ResultSet executeQuery(String sql,List<Object> sqlParams){
        ResultSet rst = null;
        Connection con = null;
        PreparedStatement pst = null;
        try{
            con = DbUtil.getConnection();
            pst = con.prepareStatement(sql);
            if(sqlParams != null && sqlParams.size()>0){
                DbUtil.bindParams(pst,sqlParams);
            }
            rst = pst.executeQuery();
        }catch (Exception e){}
        
        close(rst,null,pst,con);
        
        return rst;
    }

    //增删改操作
    public static int executeUpdate(String sql,List<Object> sqlParams){
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try{
            con = DbUtil.getConnection();
            pst = con.prepareStatement(sql);
            if(sqlParams != null && sqlParams.size()>0){
                DbUtil.bindParams(pst,sqlParams);
            }
            result = pst.executeUpdate();
        } catch (Exception e) {}

        close(null,null,pst,con);
        
        return result;
    }

}
