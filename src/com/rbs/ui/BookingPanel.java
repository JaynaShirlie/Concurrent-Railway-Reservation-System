package com.rbs.ui;

import com.rbs.util.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BookingPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JProgressBar progress = new JProgressBar(0, 3);

    private final PassengerDetailsStep step1 = new PassengerDetailsStep(this::nextFromStep1, this::cancelFlow);
    private final ReviewStep step2 = new ReviewStep(this::nextFromStep2, this::backToStep1);
    private final PaymentStep step3 = new PaymentStep(this::finishSuccess, this::backToStep2);

    public BookingPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_BG);

        progress.setValue(1);
        progress.setStringPainted(true);
        progress.setString("Passenger Details");

        cards.add(step1, "1");
        cards.add(step2, "2");
        cards.add(step3, "3");

        add(progress, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        showStep(1);
    }

    private void showStep(int i) {
        cardLayout.show(cards, String.valueOf(i));
        progress.setValue(i);
        switch (i) {
            case 1 -> progress.setString("Passenger Details");
            case 2 -> progress.setString("Review Journey");
            case 3 -> progress.setString("Payment");
        }
    }

    private void nextFromStep1(PassengerDetailsStep.Details d) {
        step2.setDetails(d);
        showStep(2);
    }

    private void backToStep1() {
        showStep(1);
    }

    private void nextFromStep2() {
        step3.setSummary(step2.getDetails());
        showStep(3);
    }

    private void backToStep2() {
        showStep(2);
    }

    private void finishSuccess() {
        JOptionPane.showMessageDialog(this, "Booking Confirmed");
        String ticket = "RBS E-Ticket\n"+
                "Train: "+step2.getDetails().trainInfo()+"\n"+
                "Boarding: "+step2.getDetails().boarding()+"\n"+
                "Passengers: "+step2.getDetails().passengers()+"\n"+
                "Contact: "+step2.getDetails().contactEmail()+" | "+step2.getDetails().contactPhone()+"\n";
        PrintUtil.printTicket(ticket);
        showStep(1);
    }

    private void cancelFlow() {
        showStep(1);
    }

    // Step 1: Passenger Details
    static class PassengerDetailsStep extends JPanel {
        record Details(String trainInfo, String boarding, List<String> passengers, String contactEmail, String contactPhone, String paymentMode) {}

        private final JTextField trainInfo = new JTextField("MS RMM EXPRESS (16751)");
        private final JComboBox<String> boarding = new JComboBox<>(new String[]{"TAMBARAM","KUMBAKONAM","CHENNAI EGMORE"});
        private final DefaultListModel<String> paxModel = new DefaultListModel<>();
        private final JList<String> paxList = new JList<>(paxModel);
        private final JTextField paxName = new JTextField();
        private final JSpinner paxAge = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        private final JComboBox<String> paxGender = new JComboBox<>(new String[]{"Male","Female","Other"});
        private final JTextField email = new JTextField();
        private final JTextField phone = new JTextField("+91 ");
        private final JRadioButton modeCards = new JRadioButton("Cards/NetBanking/Wallets (\u20B915 + GST)");
        private final JRadioButton modeUpi = new JRadioButton("BHIM/UPI (\u20B910 + GST)");

        public PassengerDetailsStep(java.util.function.Consumer<Details> onNext, Runnable onBack) {
            setLayout(new BorderLayout());
            setBackground(Theme.PRIMARY_BG);

            JPanel form = new JPanel(new GridBagLayout());
            form.setOpaque(false);
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6,6,6,6);
            c.fill = GridBagConstraints.HORIZONTAL;
            int y=0;
            c.gridx=0;c.gridy=y; form.add(label("Selected Train"), c); c.gridx=1; form.add(trainInfo, c); y++;
            c.gridx=0;c.gridy=y; form.add(label("Boarding Station"), c); c.gridx=1; form.add(boarding, c); y++;

            JPanel paxRow = new JPanel(new GridLayout(1, 0, 6, 0)); paxRow.setOpaque(false);
            paxRow.add(paxName); paxRow.add(paxAge); paxRow.add(paxGender);
            JButton addPax = new JButton("Add Passenger");
            addPax.addActionListener(e -> {
                String name = paxName.getText().trim();
                if (name.length() < 3) return;
                paxModel.addElement(name+" | "+paxAge.getValue()+" | "+paxGender.getSelectedItem());
                paxName.setText("");
            });
            c.gridx=0;c.gridy=y; form.add(label("Passenger"), c); c.gridx=1; form.add(paxRow, c); y++;
            c.gridx=1;c.gridy=y; form.add(addPax, c); y++;
            c.gridx=1;c.gridy=y; form.add(new JScrollPane(paxList), c); y++;

            c.gridx=0;c.gridy=y; form.add(label("Email"), c); c.gridx=1; form.add(email, c); y++;
            c.gridx=0;c.gridy=y; form.add(label("Phone"), c); c.gridx=1; form.add(phone, c); y++;

            ButtonGroup g = new ButtonGroup(); g.add(modeCards); g.add(modeUpi);
            modeUpi.setSelected(true);
            c.gridx=0;c.gridy=y; form.add(label("Payment Mode"), c); c.gridx=1; form.add(modeCards, c); y++;
            c.gridx=1;c.gridy=y; form.add(modeUpi, c); y++;

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
            JButton back = new JButton("Back"); back.addActionListener(e -> onBack.run());
            JButton next = new JButton("Next"); next.addActionListener(e -> onNext.accept(buildDetails()));
            actions.add(back); actions.add(next);

            add(form, BorderLayout.CENTER);
            add(actions, BorderLayout.SOUTH);
        }

        private Details buildDetails() {
            List<String> pax = new ArrayList<>();
            for (int i=0;i<paxModel.size();i++) pax.add(paxModel.get(i));
            String mode = modeUpi.isSelected()?"UPI":"CARDS";
            return new Details(trainInfo.getText(), (String) boarding.getSelectedItem(), pax, email.getText(), phone.getText(), mode);
        }

        private JLabel label(String t) { JLabel l = new JLabel(t); l.setForeground(Theme.TEXT_PRIMARY); return l; }
    }

    // Step 2: Review
    static class ReviewStep extends JPanel {
        private PassengerDetailsStep.Details details;
        private final JTextArea summary = new JTextArea();

        public ReviewStep(Runnable onNext, Runnable onBack) {
            setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
            summary.setEditable(false);
            add(new JScrollPane(summary), BorderLayout.CENTER);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
            JButton back = new JButton("Back"); back.addActionListener(e -> onBack.run());
            JButton next = new JButton("Proceed to Payment"); next.addActionListener(e -> onNext.run());
            actions.add(back); actions.add(next);
            add(actions, BorderLayout.SOUTH);
        }

        public void setDetails(PassengerDetailsStep.Details d) {
            this.details = d;
            StringBuilder sb = new StringBuilder();
            sb.append("Train: ").append(d.trainInfo()).append('\n');
            sb.append("Boarding: ").append(d.boarding()).append('\n');
            sb.append("Passengers:\n");
            for (String p : d.passengers()) sb.append("  • ").append(p).append('\n');
            sb.append("Contact: ").append(d.contactEmail()).append(" | ").append(d.contactPhone()).append('\n');
            sb.append("Payment Mode: ").append(d.paymentMode());
            summary.setText(sb.toString());
        }

        public PassengerDetailsStep.Details getDetails() { return details; }
    }

    // Step 3: Payment
    static class PaymentStep extends JPanel {
        private PassengerDetailsStep.Details details;

        public PaymentStep(Runnable onSuccess, Runnable onBack) {
            setLayout(new BorderLayout()); setBackground(Theme.PRIMARY_BG);
            JLabel msg = new JLabel("Dummy payment of \u20B91 will be requested.");
            msg.setForeground(Theme.TEXT_PRIMARY);
            add(msg, BorderLayout.CENTER);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
            JButton back = new JButton("Back"); back.addActionListener(e -> onBack.run());
            JButton pay = new JButton("Pay \u20B91"); pay.addActionListener(e -> onSuccess.run());
            actions.add(back); actions.add(pay);
            add(actions, BorderLayout.SOUTH);
        }

        public void setSummary(PassengerDetailsStep.Details d) { this.details = d; }
    }
}


