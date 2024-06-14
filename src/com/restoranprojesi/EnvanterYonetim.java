package com.restoranprojesi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EnvanterYonetim extends JPanel {
    private JTextField nameField;
    private JTextField quantityField;
    private JButton addButton;
    private JButton deleteButton;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    // Envanter yönetim ekranı için GUI oluşturulur.
    public EnvanterYonetim() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        nameField = new JTextField(10);
        quantityField = new JTextField(10);
        addButton = new JButton("Envanter Ekle");
        deleteButton = new JButton("Envanter Sil");

        inputPanel.add(new JLabel("Ürün Adı:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Miktar:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Ürün Adı", "Miktar"}, 0);
        inventoryTable = new JTable(tableModel);
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInventoryItem();
                loadInventoryItems();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteInventoryItem();
                loadInventoryItems();
            }
        });

        loadInventoryItems();
    }

    // Envantere eşya ekleme işlemini yapabilen fonksiyon.
    private void addInventoryItem() {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO envanter (isim, miktar) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Envanter öğesi başarıyla eklendi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Envanter ekleme hatası: " + ex.getMessage());
        }
    }

    // Envanterde bulunan eşyaları silme işlemini yapabilen fonksiyon.
    private void deleteInventoryItem() {
        String name = nameField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM envanter WHERE isim = ?")) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Envanter öğesi başarıyla silindi.");
            } else {
                JOptionPane.showMessageDialog(null, "Envanter öğesi bulunamadı.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Envanter silme hatası: " + ex.getMessage());
        }
    }

    // Veritabanında bulunan envanter bu fonskiyonla geri uygulamaya yüklenir.
    private void loadInventoryItems() {
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM envanter")) {
            while (resultSet.next()) {
                String name = resultSet.getString("isim");
                int quantity = resultSet.getInt("miktar");
                tableModel.addRow(new Object[]{name, quantity});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Envanter öğelerini yükleme hatası: " + ex.getMessage());
        }
    }
}
