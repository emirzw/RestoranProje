package com.restoranprojesi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MasaYonetim extends JPanel {
    private JTextField masaIdField;
    private JComboBox<String> masaDurumuField;
    private JButton addButton;
    private JButton deleteButton;
    private JTable masaTable;
    private DefaultTableModel tableModel;

    // Panel, GUI'ler oluşturulur.
    public MasaYonetim() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        masaIdField = new JTextField(5);
        masaDurumuField = new JComboBox<>(new String[]{"Boş", "Dolu", "Rezerve"});
        addButton = new JButton("Masa Ekle");
        deleteButton = new JButton("Masa Sil");

        inputPanel.add(new JLabel("Masa ID:"));
        inputPanel.add(masaIdField);
        inputPanel.add(new JLabel("Masa Durumu:"));
        inputPanel.add(masaDurumuField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Masa ID", "Masa Durumu"}, 0);
        masaTable = new JTable(tableModel);
        masaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = masaTable.getSelectedRow();
                if (selectedRow != -1) {
                    masaIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    masaDurumuField.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
                }
            }
        });
        add(new JScrollPane(masaTable), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMasa();
                loadMasa();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMasa();
                loadMasa();
            }
        });

        loadMasa();
    }

    // Masa ekleme işlemini gerçekleştiren fonksiyon.
    private void addMasa() {
        int masaId = Integer.parseInt(masaIdField.getText());
        String masaDurumu = (String) masaDurumuField.getSelectedItem();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO masa (id, masa_durumu) VALUES (?, ?)")) {
            statement.setInt(1, masaId);
            statement.setString(2, masaDurumu);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Masa başarıyla eklendi.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Masa ekleme hatası: " + ex.getMessage());
        }
    }

    // Masa silme işlemini gerçekleştiren fonksiyon.
    private void deleteMasa() {
        int masaId = Integer.parseInt(masaIdField.getText());

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             PreparedStatement statement = connection.prepareStatement("DELETE FROM masa WHERE id = ?")) {
            statement.setInt(1, masaId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Masa başarıyla silindi.");
            } else {
                JOptionPane.showMessageDialog(null, "Masa bulunamadı: " + masaId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Masa silme hatası: " + ex.getMessage());
        }
    }

    // Veritabanında bulunan masalar bu fonskiyonla geri uygulamaya yüklenir.
    private void loadMasa() {
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM masa")) {
            while (resultSet.next()) {
                int masaId = resultSet.getInt("id");
                String masaDurumu = resultSet.getString("masa_durumu");
                tableModel.addRow(new Object[]{masaId, masaDurumu});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Masaları yükleme hatası: " + ex.getMessage());
        }
    }
}
