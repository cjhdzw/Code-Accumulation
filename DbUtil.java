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
    public static void close(ResultSet rs, Statement stmt, PreparedStatement pstmt, Connection con) throws SQLException {
        if(rs != null)rs.close();
        if(stmt != null)stmt.close();
        if(pstmt != null)pstmt.close();
        if(con != null)con.close();
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
        ResultSet rs = null;

        try{
            PreparedStatement pst = DbUtil.getConnection().prepareStatement(sql);
            if(sqlParams != null && sqlParams.size()>0){
                DbUtil.bindParams(pst,sqlParams);
            }
            rs = pst.executeQuery();
        }catch (Exception e){}

        return rs;
    }

    //增删改操作
    public static int executeUpdate(String sql,List<Object> sqlParams){
        int result = 0;

        try{
            Connection con = DbUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            if(sqlParams != null && sqlParams.size()>0){
                DbUtil.bindParams(pst,sqlParams);
            }
            result = pst.executeUpdate();
        } catch (Exception e) {}

        return result;
    }

}
