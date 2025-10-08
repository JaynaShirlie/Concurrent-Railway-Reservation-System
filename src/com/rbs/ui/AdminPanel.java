package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel() {
        setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Trains", crudPanel("Train"));
        tabs.addTab("Users", crudPanel("User"));
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crudPanel(String entity) {
        JPanel p = new JPanel(new BorderLayout()); p.setBackground(Theme.PRIMARY_BG);
        JPanel form = new JPanel(new GridLayout(0,2,8,8)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        form.add(label(entity+" ID")); form.add(new JTextField());
        form.add(label("Name")); form.add(new JTextField());
        form.add(label("Meta")); form.add(new JTextField());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
        actions.add(new JButton("Add")); actions.add(new JButton("Update")); actions.add(new JButton("Remove"));
        p.add(form, BorderLayout.CENTER); p.add(actions, BorderLayout.SOUTH);
        return p;
    }

    private JLabel label(String t) { JLabel l = new JLabel(t); l.setForeground(Theme.TEXT_PRIMARY); return l; }
}



