package com.rbs.ui;

import com.rbs.model.Train;
import com.rbs.service.TrainService;
import com.rbs.util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TrainSearchPanel extends JPanel {
    private final JTextField fromField = new JTextField();
    private final JTextField toField = new JTextField();
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JComboBox<String> classBox = new JComboBox<>(new String[]{"Sleeper (SL)","AC 3 Tier (3A)","AC 2 Tier (2A)","AC First (1A)"});
    private final JComboBox<String> categoryBox = new JComboBox<>(new String[]{"General","Tatkal"});
    private final JComboBox<String> sortBox = new JComboBox<>(new String[]{"Date","Time","Duration"});
    private final JPanel results = new JPanel();

    private final TrainService trainService = new TrainService((f,t,d,c,cat) -> java.util.List.of());

    public TrainSearchPanel() {
        setLayout(new BorderLayout(0,0));
        setBackground(Theme.PRIMARY_BG);
        add(buildSearchBar(), BorderLayout.NORTH);
        add(buildResults(), BorderLayout.CENTER);
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new GridLayout(2, 6, 8, 8));
        bar.setBorder(new EmptyBorder(12, 12, 12, 12));
        bar.setBackground(Theme.SURFACE_BG);
        bar.add(label("From")); bar.add(fromField);
        bar.add(label("To")); bar.add(toField);
        bar.add(label("Date")); bar.add(dateSpinner);
        bar.add(label("Class")); bar.add(classBox);
        bar.add(label("Category")); bar.add(categoryBox);
        bar.add(label("Sort By")); bar.add(sortBox);
        JButton search = new JButton("Search");
        search.addActionListener(e -> doSearch());
        bar.add(new JLabel()); bar.add(search);
        return bar;
    }

    private JScrollPane buildResults() {
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
        results.setBackground(Theme.PRIMARY_BG);
        JScrollPane sp = new JScrollPane(results);
        sp.setBorder(null);
        return sp;
    }

    private void doSearch() {
        String from = fromField.getText();
        String to = toField.getText();
        LocalDate date = LocalDate.now();
        String clazz = switch (classBox.getSelectedIndex()) {
            case 0 -> "SL"; case 1 -> "3A"; case 2 -> "2A"; default -> "1A";
        };
        String cat = (String) categoryBox.getSelectedItem();
        TrainService.SortBy sort = switch (sortBox.getSelectedIndex()) {
            case 1 -> TrainService.SortBy.TIME;
            case 2 -> TrainService.SortBy.DURATION;
            default -> TrainService.SortBy.DATE;
        };
        List<Train> list = trainService.search(from, to, date, clazz, cat, sort);
        renderResults(list);
    }

    private void renderResults(List<Train> list) {
        results.removeAll();
        if (list == null || list.isEmpty()) {
            results.add(emptyCard("No trains found."));
        } else {
            for (Train t : list) {
                results.add(trainCard(t));
                results.add(Box.createVerticalStrut(12));
            }
        }
        results.revalidate();
        results.repaint();
    }

    private JPanel trainCard(Train t) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.CARD_BG);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel(t.getName()+" ("+t.getNumber()+")");
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JLabel runs = new JLabel("Runs On: "+String.join("", t.getRunDays()));
        runs.setForeground(Theme.TEXT_SECONDARY);

        JLabel sched = new JLabel(String.format("%s | %s    →    %s | %s",
                t.getDeparture(), t.getFromStation(), t.getArrival(), t.getToStation()));
        sched.setForeground(Theme.TEXT_PRIMARY);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(runs);
        left.add(Box.createVerticalStrut(6));
        left.add(sched);

        JPanel right = new JPanel(new GridLayout(4,2,6,6));
        right.setOpaque(false);
        String[] classes = {"Sleeper (SL)", "AC 3 Tier (3A)", "AC 2 Tier (2A)", "AC First Class (1A)"};
        for (String cls : classes) {
            right.add(new JLabel(cls));
            JButton refresh = new JButton("Refresh");
            JButton book = new JButton("Book Now");
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            row.setOpaque(false);
            row.add(refresh); row.add(book);
            right.add(row);
        }

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        JLabel note = new JLabel("Please check NTES website or app for actual time before boarding.");
        note.setForeground(Theme.TEXT_SECONDARY);
        card.add(note, BorderLayout.SOUTH);
        return card;
    }

    private JPanel emptyCard(String msg) {
        JPanel p = new JPanel();
        p.setBackground(Theme.CARD_BG);
        p.setBorder(new EmptyBorder(24,24,24,24));
        JLabel l = new JLabel(msg);
        l.setForeground(Theme.TEXT_SECONDARY);
        p.add(l);
        return p;
    }

    private JLabel label(String t) { JLabel l = new JLabel(t); l.setForeground(Theme.TEXT_PRIMARY); return l; }
}



