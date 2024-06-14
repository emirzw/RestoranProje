package com.restoranprojesi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MenuYonetim extends JPanel {
    private JTextField idField;
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JButton addButton;
    private JButton deleteButton;
    private JTable menuTable;
    private DefaultTableModel tableModel;

    // Panel, GUI'ler oluşturulur.
    public MenuYonetim() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        idField = new JTextField(5);
        nameField = new JTextField(10);
        categoryField = new JTextField(10);
        priceField = new JTextField(10);
        descriptionField = new JTextField(10);
        addButton = new JButton("Ürün Ekle");
        deleteButton = new JButton("Ürün Sil");

        inputPanel.add(new JLabel("Menü Ürün ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Ürün Adı:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Kategori:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Fiyat:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Açıklama:"));
        inputPanel.add(descriptionField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Menü Ürün ID", "Ürün Adı", "Kategori", "Fiyat", "Açıklama"}, 0);
        menuTable = new JTable(tableModel);
        menuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    descriptionField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });
        add(new JScrollPane(menuTable), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
                loadMenuItems();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
                loadMenuItems();
            }
        });

        loadMenuItems();
    }

    // Menüye eşya ekleme işlemini gerçekleştiren fonksiyon.
    private void addMenuItem() {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String category = categoryField.getText();
        double price = Double.parseDouble(priceField.getText());
        String description = descriptionField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO menu_urunleri (id, isim, kategori, fiyat, aciklama) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, category);
            statement.setDouble(4, price);
            statement.setString(5, description);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ürün başarıyla eklendi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ürün ekleme hatası: " + ex.getMessage());
        }
    }

    // Menüden eşya silme işlemini gerçekleştiren fonksiyon.
    private void deleteMenuItem() {
        int id = Integer.parseInt(idField.getText());

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM menu_urunleri WHERE id = ?")) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ürün başarıyla silindi.");
            } else {
                JOptionPane.showMessageDialog(null, "Ürün bulunamadı: " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ürün silme hatası: " + ex.getMessage());
        }
    }

    // Veritabanında bulunan ürünler bu fonskiyonla geri uygulamaya yüklenir.
    private void loadMenuItems() {
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu_urunleri")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("isim");
                String category = resultSet.getString("kategori");
                double price = resultSet.getDouble("fiyat");
                String description = resultSet.getString("aciklama");
                tableModel.addRow(new Object[]{id, name, category, price, description});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ürünleri yükleme hatası: " + ex.getMessage());
        }
    }
}
