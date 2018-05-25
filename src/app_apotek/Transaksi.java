/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app_apotek;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Adriansyah Ramadhan
 */
public class Transaksi extends javax.swing.JFrame {
    Connection con = koneksi.koneksiDB.Conn();
    public Statement st;
    ResultSet rs = null;
    ResultSet rs2;
    public DefaultTableModel tabModel;
    public DefaultTableModel tabModel1;

public Transaksi() {
        initComponents();
        tanggal();
        judul();
        tampilData();
        tema();
        Show();
        AutoKodePre();
        AutoKode();
        kd_obat.setEnabled(false);
        nama_obat.setEnabled(false);
        tgl_beli.setEnabled(false);
        tgl_exp.setEnabled(false);
        harga_obt.setEnabled(false);
        kd_transaksi.setEnabled(false);
        kd_pretransaksi.setEnabled(false);
        kembali_bel.setEnabled(false);
    }
public void tanggal(){
    Date ys = new Date();
    SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
    tgl_beli.setText(s.format(ys));

}
public void judul(){
       Object[] judul ={"Kode Obat","NamaObat","TgllExp","TglMasuk","HargaObat","JumlahObat","JenisObat"};
       tabModel = new DefaultTableModel(null,judul);
       tbl_data.setModel(tabModel);
} 

public void tampilData(){
    try{
        st = con.createStatement();
        tabModel.getDataVector().removeAllElements();
        tabModel.fireTableDataChanged();
        rs = st.executeQuery("SELECT * FROM tbl_input WHERE jumlah_obat > 0 ORDER BY kd_obat asc");
        while(rs.next()){
            Object[] data = {
             rs.getString("kd_obat"),
             rs.getString("nama_obat"),
             rs.getString("tgl_exp"),
             rs.getString("tgl_msk"),
             rs.getString("harga_obt"),
             rs.getString("jumlah_obat"),
             rs.getString("jenis_obt")             
            };
            tabModel.addRow(data);
        }
    }catch(Exception e){
            e.printStackTrace();
    }    
}

public void tema(){
       Object[] judul ={"Kode Pretransaksi","Kode Transaksi","Kode","NamaObat","TgllExp","TglBeli","HargaObat","JumlahObat","JenisObat"};
       tabModel1 = new DefaultTableModel(null,judul);
       tbl_daftar.setModel(tabModel1);
}

public void Show(){
        try{
        st = con.createStatement();
        tabModel1.getDataVector().removeAllElements();
        tabModel1.fireTableDataChanged();
        rs = st.executeQuery("SELECT * FROM tbl_keranjang ORDER BY kd_obat asc");
        while(rs.next()){
            Object[] data = {
             rs.getString("kd_pretransaksi"),
             rs.getString("kd_transaksi"),
             rs.getString("kd_obat"),
             rs.getString("nama_obt"),
             rs.getString("tgl_exp"),
             rs.getString("tgl_beli"),
             rs.getString("harga_obat"),
             rs.getString("jumlah_obt"),
             rs.getString("jenis_obt")             
            };
            tabModel1.addRow(data);
        }
    }catch(Exception e){
            e.printStackTrace();
    } 
}
public Boolean validasi(){
     return !(nama_obat.getText().equals("") || tgl_beli.getText().equals("") || tgl_exp.getText().equals("") || harga_obt.getText().equals("")
             || jumlah_obat.getText().equals(""));
    }

public void reset(){
        nama_obat.setText("");
        tgl_exp.setText("");
        harga_obt.setText("");
        jumlah_obat.setText("");
        jenis_obt.getSelectedItem().toString();
}
public void clear(){
        bayar_bel.setText("");
        total_bel.setText("");
        nama_pelanggan.setText("");
}
public void bayar(){
  try
  {
        int hasil,a,b;
        String sql;
 
        a = Integer.parseInt(total_bel.getText());
        b = Integer.parseInt(bayar_bel.getText());
        hasil = b - a;
        sql = "truncate table tbl_keranjang";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.execute();
        Show();
        
        kembali_bel.setText(String.valueOf(hasil));
  }
  catch(Exception e)
  {
      e.printStackTrace();
  }
}
public void AutoKode(){
        try{
            String sql;
            sql = "SELECT COUNT(kd_transaksi) as jumlah FROM tbl_keranjang";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                String jumlah = rs.getString("jumlah");
                int jumint = Integer.parseInt(String.valueOf(jumlah));
                if (jumint > 0) {
                    st = con.createStatement();
                        sql = "SELECT MAX(kd_transaksi) AS kode FROM tbl_keranjang";
                        rs = st.executeQuery(sql);
                        if(rs.next()){
                            String id = rs.getString("kode").substring(2);
                            String kode = String.valueOf(Integer.parseInt(id) + 1);
                            if(kode.length() == 1){
                                kd_transaksi.setText("TR00"+kode);
                            }else if(kode.length() == 2){
                                kd_transaksi.setText("TR0"+kode);
                            }else
                                kd_transaksi.setText("TR"+kode);
                        }
                }else{
                    kd_transaksi.setText("TR001");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
}

public void AutoKodePre(){
        try{
            String sql;
            sql = "SELECT COUNT(kd_pretransaksi) as jumlah FROM tbl_keranjang";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                String jumlah = rs.getString("jumlah");
                int jumint = Integer.parseInt(String.valueOf(jumlah));
                if (jumint > 0) {
                    st = con.createStatement();
                        sql = "SELECT MAX(kd_pretransaksi) AS kode FROM tbl_keranjang";
                        rs = st.executeQuery(sql);
                        if(rs.next()){
                            String id = rs.getString("kode").substring(2);
                            String kode = String.valueOf(Integer.parseInt(id) + 1);
                            if(kode.length() == 1){
                                kd_pretransaksi.setText("PR00"+kode);
                            }else if(kode.length() == 2){
                                kd_pretransaksi.setText("PR0"+kode);
                            }else
                                kd_pretransaksi.setText("PR"+kode);
                        }
                }else{
                    kd_pretransaksi.setText("PR001");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        stock = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_data = new javax.swing.JTable();
        nama_obat = new javax.swing.JTextField();
        harga_obt = new javax.swing.JTextField();
        jumlah_obat = new javax.swing.JTextField();
        jenis_obt = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        kd_obat = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_daftar = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tambah = new javax.swing.JButton();
        tgl_beli = new javax.swing.JTextField();
        tgl_exp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        total_bel = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        bayar_bel = new javax.swing.JTextField();
        kembali_bel = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        kd_transaksi = new javax.swing.JTextField();
        kd_pretransaksi = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        nama_pelanggan = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(44, 62, 80));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(247, 202, 24));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Adriansyah Ramadhan\\Downloads\\round-account-button-with-user-inside.png")); // NOI18N

        back.setBackground(new java.awt.Color(217, 30, 24));
        back.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        back.setForeground(new java.awt.Color(255, 255, 255));
        back.setText("< Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        logout.setBackground(new java.awt.Color(217, 30, 24));
        logout.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        stock.setForeground(new java.awt.Color(247, 202, 24));
        stock.setText("jLabel16");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logout, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(back, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stock)
                .addGap(54, 54, 54))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stock)
                .addGap(56, 56, 56))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 665, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CARI");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 11, 65, 39));

        cari.setEditable(false);
        cari.setBackground(new java.awt.Color(44, 62, 80));
        cari.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cari.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(762, 11, 438, 39));

        tbl_data.setBackground(new java.awt.Color(44, 62, 80));
        tbl_data.setForeground(new java.awt.Color(255, 255, 255));
        tbl_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tbl_data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_dataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_data);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(693, 63, 614, 291));

        nama_obat.setBackground(new java.awt.Color(44, 62, 80));
        nama_obat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nama_obat.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(nama_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 419, 150, 40));

        harga_obt.setBackground(new java.awt.Color(44, 62, 80));
        harga_obt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        harga_obt.setForeground(new java.awt.Color(255, 255, 255));
        harga_obt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_obtActionPerformed(evt);
            }
        });
        jPanel1.add(harga_obt, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 506, 150, 40));

        jumlah_obat.setBackground(new java.awt.Color(44, 62, 80));
        jumlah_obat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jumlah_obat.setForeground(new java.awt.Color(255, 255, 255));
        jumlah_obat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlah_obatActionPerformed(evt);
            }
        });
        jPanel1.add(jumlah_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(911, 506, 170, 40));

        jenis_obt.setBackground(new java.awt.Color(44, 62, 80));
        jenis_obt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jenis_obt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Pilih -", "Cair", "Bubuk", "Kapsul", " " }));
        jPanel1.add(jenis_obt, new org.netbeans.lib.awtextra.AbsoluteConstraints(1125, 505, 150, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Obat");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 378, 110, 35));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tanggal Exp");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(935, 387, 122, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tanggal Pembelian");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1134, 381, 141, 28));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Harga Obat");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(749, 477, 98, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Jumlah Obat");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(917, 477, 102, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Jenis");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1122, 477, 90, -1));

        kd_obat.setBackground(new java.awt.Color(44, 62, 80));
        kd_obat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        kd_obat.setForeground(new java.awt.Color(255, 255, 255));
        kd_obat.setText("\n");
        jPanel1.add(kd_obat, new org.netbeans.lib.awtextra.AbsoluteConstraints(1206, 11, 97, 39));

        jButton3.setBackground(new java.awt.Color(0, 230, 64));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("BAYAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 552, 168, 43));

        tbl_daftar.setBackground(new java.awt.Color(44, 62, 80));
        tbl_daftar.setForeground(new java.awt.Color(255, 255, 255));
        tbl_daftar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_daftar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_daftarMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_daftar);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 176, 665, 243));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DAFTAR BELI");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 144, -1, 27));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(667, 0, -1, 657));

        tambah.setBackground(new java.awt.Color(0, 230, 64));
        tambah.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tambah.setText("TAMBAH");
        tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahActionPerformed(evt);
            }
        });
        jPanel1.add(tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(733, 578, 150, 40));

        tgl_beli.setBackground(new java.awt.Color(44, 62, 80));
        jPanel1.add(tgl_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(1125, 419, 150, 40));

        tgl_exp.setBackground(new java.awt.Color(44, 62, 80));
        tgl_exp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tgl_exp.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(tgl_exp, new org.netbeans.lib.awtextra.AbsoluteConstraints(889, 419, 210, 40));

        jLabel11.setBackground(new java.awt.Color(44, 62, 80));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 32)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(240, 240, 240));
        jLabel11.setText("TOTAL");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 437, -1, 60));

        total_bel.setEditable(false);
        total_bel.setBackground(new java.awt.Color(44, 62, 80));
        total_bel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        total_bel.setForeground(new java.awt.Color(240, 240, 240));
        total_bel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        total_bel.setText("-");
        total_bel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_belActionPerformed(evt);
            }
        });
        jPanel1.add(total_bel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 200, 40));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator1.setAlignmentX(2.0F);
        jSeparator1.setAlignmentY(2.0F);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 200, 30));

        bayar_bel.setBackground(new java.awt.Color(44, 62, 80));
        bayar_bel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bayar_bel.setForeground(new java.awt.Color(240, 240, 240));
        bayar_bel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bayar_belKeyTyped(evt);
            }
        });
        jPanel1.add(bayar_bel, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 552, 233, 43));

        kembali_bel.setBackground(new java.awt.Color(44, 62, 80));
        kembali_bel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        kembali_bel.setForeground(new java.awt.Color(240, 240, 240));
        kembali_bel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kembali_belKeyReleased(evt);
            }
        });
        jPanel1.add(kembali_bel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 550, 150, 40));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Rp.");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 33, 50));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Rp.");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 554, 43, 37));

        jLabel14.setForeground(new java.awt.Color(240, 240, 240));
        jLabel14.setIcon(new javax.swing.ImageIcon("D:\\Wallpaper\\Icon\\delete.png")); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 468, -1, -1));

        kd_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_transaksiActionPerformed(evt);
            }
        });
        jPanel1.add(kd_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(517, 144, 135, 27));

        kd_pretransaksi.setBackground(new java.awt.Color(44, 62, 80));
        kd_pretransaksi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        kd_pretransaksi.setForeground(new java.awt.Color(240, 240, 240));
        kd_pretransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_pretransaksiActionPerformed(evt);
            }
        });
        jPanel1.add(kd_pretransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 147, 142, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Nama Pelanggan");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 430, 120, 35));

        nama_pelanggan.setBackground(new java.awt.Color(44, 62, 80));
        nama_pelanggan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nama_pelanggan.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(nama_pelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 470, 180, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        new Admin_Apotek().show();
        this.dispose();
    }//GEN-LAST:event_backActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        if(JOptionPane.showConfirmDialog(null,"Anda yakin ingin keluar","Exit",JOptionPane.YES_NO_OPTION)== 0 ){
        new Login_Apotek().show();
        this.dispose();
    }//GEN-LAST:event_logoutActionPerformed
  }
    private void tbl_dataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_dataMouseClicked
       kd_obat.setText((String) tabModel.getValueAt(tbl_data.getSelectedRow(),0));
       nama_obat.setText((String) tabModel.getValueAt(tbl_data.getSelectedRow(),1));
       tgl_exp.setText((String) tabModel.getValueAt(tbl_data.getSelectedRow(),2));
       stock.setText((String) tabModel.getValueAt(tbl_data.getSelectedRow(),5));
       harga_obt.setText((String) tabModel.getValueAt(tbl_data.getSelectedRow(),4));
       jenis_obt.setSelectedItem(tabModel.getValueAt(tbl_data.getSelectedRow(),6));
       kembali_bel.setText("");
    }//GEN-LAST:event_tbl_dataMouseClicked

    private void tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahActionPerformed
        if (validasi()) {
        try{
         int hasil,a,b,stk;
         
         stk = Integer.parseInt(String.valueOf(stock.getText()));
         a = Integer.parseInt(String.valueOf(harga_obt.getText()));
         b = Integer.parseInt(String.valueOf(jumlah_obat.getText()));
         if(b > stk){
             JOptionPane.showMessageDialog(null,"Stock Hanya Ada" + stock.getText());
         }
         else{
            hasil = a * b;
         
         
         st = con.createStatement();
         String sql = "INSERT INTO tbl_keranjang VALUES('"+ kd_pretransaksi.getText() +"','"+ kd_transaksi.getText() +"','"+ kd_obat.getText() + "','" + nama_obat.getText() + "','" + tgl_exp.getText() + "','" + tgl_beli.getText() +"','"
                 + hasil +"','"+  jumlah_obat.getText() +"','"+ jenis_obt.getSelectedItem() +"')";
         st.executeUpdate(sql);
         JOptionPane.showMessageDialog(null, "Data Berhasil Di Tambah");
         Show();
         AutoKode();
         }
         try{
             st = con.createStatement();
             String sqls = "SELECT SUM(harga_obat) FROM tbl_keranjang";
             rs2 = st.executeQuery(sqls);
             if(rs2.next()){
                 total_bel.setText(rs2.getString(1));
             }
         }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage());
         }
         
         reset();
         tampilData();
         
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
        else{
            JOptionPane.showMessageDialog(null, "Harap lengkapi data!");
        }
    }//GEN-LAST:event_tambahActionPerformed

    private void tbl_daftarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_daftarMouseClicked
//       kd_obat.setText(tabModel1.getValueAt(tbl_daftar.getSelectedRow(), 0)+ "");
//       tgl_exp.setText((String) tabModel1.getValueAt(tbl_daftar.getSelectedRow(), 2)+"");
//       tgl_beli.setText(tabModel1.getValueAt(tbl_daftar.getSelectedRow(), 3)+"");
//       harga_obt.setText(tabModel1.getValueAt(tbl_daftar.getSelectedRow(),4)+"");
//       jumlah_obat.setText(tabModel1.getValueAt(tbl_daftar.getSelectedRow(),5)+"");
//       jenis_obt.setSelectedItem(tabModel1.getValueAt(tbl_daftar.getSelectedRow(),6)+"");
    }//GEN-LAST:event_tbl_daftarMouseClicked

    private void total_belActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_belActionPerformed

    }//GEN-LAST:event_total_belActionPerformed

    private void harga_obtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_obtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_harga_obtActionPerformed

     public void printstruk(){
        String reportSource = null;
        String reportDest = null;
        try{
        reportSource = "C:\\Users\\Adriansyah Ramadhan\\Documents\\NetBeansProjects\\App_Apotek\\src\\app_apotek\\struk.jrxml";
        reportDest = "C:\\Users\\Adriansyah Ramadhan\\Documents\\NetBeansProjects\\App_Apotek\\src\\app_apotek\\struk.jasper";
        HashMap parameter = new HashMap();
        
        parameter.put("parameter1", kd_pretransaksi.getText());
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameter,con);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, reportDest);
        JasperViewer.viewReport(jasperPrint,false);
             
        AutoKodePre();
       }catch(JRException e){
           e.printStackTrace();
       }
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int a,b;
             a = Integer.parseInt(total_bel.getText());
             b = Integer.parseInt(bayar_bel.getText());
        if (bayar_bel.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Harap masukkan jumlah uang!");
            bayar_bel.requestFocus();
        }else if( b < a ){
            JOptionPane.showMessageDialog(null, "Uang Anda Kurang!!");
        }else{
            try{
            st = con.createStatement();
            String sql = "INSERT INTO tbl_transaksi VALUES('"+ kd_pretransaksi.getText() +"','"+ nama_pelanggan.getText() +"','"+ total_bel.getText() +"')";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Data Berhasil Di Tambah");
            Show();
            printstruk();
            AutoKode();
            AutoKodePre();

        }
        catch(Exception e){
            e.printStackTrace();
        }
            bayar();
            Show();
             
        }
     
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        try{
        st = con.createStatement();
        String sql = "DELETE FROM tbl_keranjang WHERE kd_pretransaksi='" + tabModel1.getValueAt(tbl_daftar.getSelectedRow(),0)+"'" ;
        st.executeUpdate(sql);
        JOptionPane.showMessageDialog(null, "Berhasil");
        Show();
        tampilData();
        AutoKode();
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jLabel14MouseClicked

    private void kd_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_transaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_transaksiActionPerformed

    private void kd_pretransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_pretransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_pretransaksiActionPerformed

    private void kembali_belKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembali_belKeyReleased
        
    }//GEN-LAST:event_kembali_belKeyReleased

    private void bayar_belKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayar_belKeyTyped
           if(!Character.isDigit(evt.getKeyChar())){
           evt.consume();
        }
    }//GEN-LAST:event_bayar_belKeyTyped

    private void jumlah_obatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlah_obatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jumlah_obatActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JTextField bayar_bel;
    private javax.swing.JTextField cari;
    private javax.swing.JTextField harga_obt;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox jenis_obt;
    private javax.swing.JTextField jumlah_obat;
    private javax.swing.JTextField kd_obat;
    private javax.swing.JTextField kd_pretransaksi;
    private javax.swing.JTextField kd_transaksi;
    private javax.swing.JTextField kembali_bel;
    private javax.swing.JButton logout;
    private javax.swing.JTextField nama_obat;
    private javax.swing.JTextField nama_pelanggan;
    private javax.swing.JLabel stock;
    private javax.swing.JButton tambah;
    private javax.swing.JTable tbl_daftar;
    private javax.swing.JTable tbl_data;
    private javax.swing.JTextField tgl_beli;
    private javax.swing.JTextField tgl_exp;
    private javax.swing.JTextField total_bel;
    // End of variables declaration//GEN-END:variables
}
