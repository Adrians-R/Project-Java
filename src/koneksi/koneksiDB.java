/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class koneksiDB {
    public static Connection conn;
    
    public static Connection Conn(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/db_apotek", "root", "");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
}