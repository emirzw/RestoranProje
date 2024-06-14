package com.restoranprojesi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SiparisYonetim extends JPanel {
    private JTextField masaIdField;
    private JTextField menuUrunIdField;
    private JTextField miktarField;
    private JTextField durumField;
    private JButton addButton;
    private JButton deleteButton;
    private JTable siparisTable;
    private DefaultTableModel tableModel;

    // Sipariş ekranı için GUI oluşturulur.
    public SiparisYonetim() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        masaIdField = new JTextField(5);
        menuUrunIdField = new JTextField(10);
        miktarField = new JTextField(5);
        durumField = new JTextField(10);
        addButton = new JButton("Sipariş Ekle");
        deleteButton = new JButton("Sipariş Sil");

        inputPanel.add(new JLabel("Masa ID:"));
        inputPanel.add(masaIdField);
        inputPanel.add(new JLabel("Menu Ürün ID:"));
        inputPanel.add(menuUrunIdField);
        inputPanel.add(new JLabel("Miktar:"));
        inputPanel.add(miktarField);
        inputPanel.add(new JLabel("Durum:"));
        inputPanel.add(durumField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Sipariş ID", "Masa ID", "Menu Ürün ID", "Miktar", "Durum"}, 0);
        siparisTable = new JTable(tableModel);
        add(new JScrollPane(siparisTable), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSiparis();
                loadSiparisler();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSiparis();
                loadSiparisler();
            }
        });

        loadSiparisler();
    }

    // Panele sipariş ekleme işlemini gerçekleştiren fonksiyon.
    private void addSiparis() {
        int masaId = Integer.parseInt(masaIdField.getText());
        int menuUrunId = Integer.parseInt(menuUrunIdField.getText());
        int miktar = Integer.parseInt(miktarField.getText());
        String durum = durumField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO siparisler (masa_id, menu_urun_id, miktar, durum) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, masaId);
            statement.setInt(2, menuUrunId);
            statement.setInt(3, miktar);
            statement.setString(4, durum);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sipariş başarıyla eklendi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Sipariş ekleme hatası: " + ex.getMessage());
        }
    }

    // Panelden sipariş silme işlemini gerçekleştiren fonksiyon.
    private void deleteSiparis() {
        int selectedRow = siparisTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silmek için bir sipariş seçmelisiniz.");
            return;
        }

        int siparisId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM siparisler WHERE id = ?")) {
            statement.setInt(1, siparisId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sipariş başarıyla silindi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Sipariş silme hatası: " + ex.getMessage());
        }
    }

    // Veritabanında bulunan siparişler bu fonskiyonla geri uygulamaya yüklenir.
    private void loadSiparisler() {
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM siparisler")) {
            while (resultSet.next()) {
                int siparisId = resultSet.getInt("id");
                int masaId = resultSet.getInt("masa_id");
                int menuUrunId = resultSet.getInt("menu_urun_id");
                int miktar = resultSet.getInt("miktar");
                String durum = resultSet.getString("durum");
                tableModel.addRow(new Object[]{siparisId, masaId, menuUrunId, miktar, durum});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Siparişleri yükleme hatası: " + ex.getMessage());
        }
    }
}
