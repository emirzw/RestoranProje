package com.restoranprojesi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RaporYonetim extends JPanel {
    private JButton raporOlusturButton;
    private JTextArea raporTextArea;
    private JComboBox<String> tarihAraligiComboBox;

    // Rapor için GUI oluşturulur.
    public RaporYonetim() {
        setLayout(new BorderLayout());

        raporOlusturButton = new JButton("Rapor Oluştur");
        raporTextArea = new JTextArea(20, 50);
        tarihAraligiComboBox = new JComboBox<>(new String[]{"Son 24 Saat", "Son 7 Gün", "Son 30 Gün"});

        JPanel topPanel = new JPanel();
        topPanel.add(tarihAraligiComboBox);
        topPanel.add(raporOlusturButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(raporTextArea), BorderLayout.CENTER);

        raporOlusturButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    // Rapor oluşturulmasını sağlayan fonksiyon.
    private void generateReport() {
        String selectedPeriod = (String) tarihAraligiComboBox.getSelectedItem();
        String query = "SELECT * FROM siparisler WHERE created_at >= NOW() - INTERVAL 1 DAY";

        if (selectedPeriod.equals("Son 7 Gün")) {
            query = "SELECT * FROM siparisler WHERE created_at >= NOW() - INTERVAL 7 DAY";
        } else if (selectedPeriod.equals("Son 30 Gün")) {
            query = "SELECT * FROM siparisler WHERE created_at >= NOW() - INTERVAL 30 DAY";
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "Gardascim35");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            StringBuilder report = new StringBuilder();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int masaId = resultSet.getInt("masa_id");
                int menuItemId = resultSet.getInt("menu_urun_id");
                int quantity = resultSet.getInt("miktar");
                String status = resultSet.getString("durum");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                report.append("ID: ").append(id)
                        .append(", Masa ID: ").append(masaId)
                        .append(", Menü Ürünü ID: ").append(menuItemId)
                        .append(", Miktar: ").append(quantity)
                        .append(", Durum: ").append(status)
                        .append(", Oluşturulma Tarihi: ").append(createdAt)
                        .append("\n");
            }

            raporTextArea.setText(report.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Rapor oluşturma hatası: " + ex.getMessage());
        }
    }
}
