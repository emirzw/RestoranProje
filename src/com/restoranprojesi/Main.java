package com.restoranprojesi;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Look and feel'i ayarlar.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ana çerçeveyi oluşturur.
        JFrame frame = new JFrame("Restoran Yönetim Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Sekmeli paneli oluşturarak ayrı ayrı sekmeleri ekler ve özelliklerini yükler.
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Masa Yönetimi", new MasaYonetim());
        tabbedPane.addTab("Menü Yönetimi", new MenuYonetim());
        tabbedPane.addTab("Sipariş Yönetimi", new SiparisYonetim());
        tabbedPane.addTab("Envanter Yönetimi", new EnvanterYonetim());
        tabbedPane.addTab("Raporlar", new RaporYonetim());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
