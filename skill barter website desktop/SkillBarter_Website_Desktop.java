import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * SkillBarter_Website_Desktop.java
 *
 * Enhanced single-file Java Swing application that mimics a "complete website"
 * Additions include: Login/Register dialogs, Search, Theme toggle, FAQ accordion,
 * Testimonials carousel, Service detail modal, Contact form validation, Export to HTML,
 * keyboard shortcuts, and small accessibility improvements.
 *
 * Drop into an IDE and run. Requires Java 8+.
 */
public class SkillBarter_Website_Desktop {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}

class MainWindow extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Theme colors (toggleable)
    private Color bgDark = new Color(10, 10, 12);
    private Color cardDark = new Color(25, 25, 28);
    private Color textLight = new Color(230, 230, 230);
    private Color neonBlue = new Color(0, 200, 255);
    private Color neonPurple = new Color(170, 0, 255);

    private Color bgLight = new Color(245, 245, 248);
    private Color cardLight = new Color(255, 255, 255);
    private Color textDark = new Color(30, 30, 30);

    private boolean darkMode = true;

    private final Font headingFont = new Font("Segoe UI Semibold", Font.BOLD, 30);
    private final Font textFont = new Font("Segoe UI", Font.PLAIN, 16);

    // Testimonials carousel
    private final CardLayout testimonialLayout = new CardLayout();
    private final JPanel testimonialCards = new JPanel(testimonialLayout);
    private final Timer testimonialTimer;

    MainWindow() {
        setTitle("⚡ SkillBarter — Website Desktop Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgDark);

        add(createTopNav(), BorderLayout.NORTH);
        cards.setBackground(bgDark);

        cards.add(createHomePanel(), "HOME");
        cards.add(createAboutPanel(), "ABOUT");
        cards.add(createServicesPanel(), "SERVICES");
        cards.add(createModulesPanel(), "MODULES");
        cards.add(createContactPanel(), "CONTACT");
        cards.add(createLoginProfilePanel(), "PROFILE");

        add(cards, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        // Testimonials auto-advance every 4 seconds
        testimonialTimer = new Timer(4000, e -> testimonialLayout.next(testimonialCards));
        testimonialTimer.start();

        // Keyboard shortcuts
        setupShortcuts();
    }

    private void setupShortcuts() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), "goHome");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "openSearch");
        am.put("goHome", new AbstractAction() { public void actionPerformed(ActionEvent e) { cardLayout.show(cards, "HOME"); } });
        am.put("openSearch", new AbstractAction() { public void actionPerformed(ActionEvent e) { showSearchDialog(); } });
    }

    // ----- Top Navigation with Search, Login, Theme Toggle -----
    private JComponent createTopNav() {
        JPanel nav = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, neonPurple, getWidth(), 0, neonBlue);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        nav.setBorder(new EmptyBorder(12, 18, 12, 18));

        JLabel brand = new JLabel("⚡ SkillBarter");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brand.setForeground(Color.WHITE);
        nav.add(brand, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        // Search icon
        JButton searchBtn = new NeonIconButton("Search");
        searchBtn.addActionListener(e -> showSearchDialog());
        right.add(searchBtn);

        // Theme toggle
        JToggleButton themeToggle = new JToggleButton("Light");
        themeToggle.setFocusable(false);
        themeToggle.addActionListener(e -> toggleTheme(themeToggle.isSelected()));
        right.add(themeToggle);

        // Login/Register
        JButton login = new NeonIconButton("Login");
        login.addActionListener(e -> new LoginDialog(this).setVisible(true));
        right.add(login);

        // Quick nav buttons
        String[] names = {"Home", "About", "Services", "Modules", "Contact"};
        for (String n : names) {
            NeonButton btn = new NeonButton(n);
            btn.addActionListener(e -> cardLayout.show(cards, n.toUpperCase()));
            right.add(btn);
        }

        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    // ----- Theme toggle -----
    private void toggleTheme(boolean light) {
        darkMode = !light; // toggleState: true -> light, false -> dark
        if (light) {
            getContentPane().setBackground(bgLight);
            cards.setBackground(bgLight);
        } else {
            getContentPane().setBackground(bgDark);
            cards.setBackground(bgDark);
        }
        // In real app we'd recursively update components; for demo, repaint top-level
        SwingUtilities.updateComponentTreeUI(this);
    }

    // ----- Custom Buttons -----
    static class NeonButton extends JButton {
        private boolean hovered = false;
        private final Color neonBlue = new Color(0, 200, 255);
        private final Color neonPurple = new Color(170, 0, 255);

        NeonButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (hovered) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, neonPurple, getWidth(), getHeight(), neonBlue);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
            super.paintComponent(g);
            g2.dispose();
        }
    }

    static class NeonIconButton extends JButton {
        NeonIconButton(String label) {
            super(label);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }
    }

    // ----- Feature Card -----
    private JPanel makeFeatureCard(String icon, String title, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardDark);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 45), 1, true),
                new EmptyBorder(18, 18, 18, 18)));

        JLabel i = new JLabel(icon);
        i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(neonBlue);
        JLabel d = new JLabel("<html><body style='width:230px;color:#CCCCCC;font-size:13px;'>" + desc + "</body></html>");

        card.add(i);
        card.add(Box.createVerticalStrut(10));
        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(d);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(neonBlue, 2, true));
                card.setBackground(new Color(30, 30, 35));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 45), 1, true));
                card.setBackground(cardDark);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                showServiceDetail(title, desc);
            }
        });
        return card;
    }

    // ----- Home Panel -----
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel headline = new JLabel("Where Your Talent is the Currency");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headline.setForeground(neonBlue);

        JLabel sub = new JLabel("<html><body style='color:#EAEAEA;font-size:16px;'>Join a global network of learners and teachers who trade skills instead of money.</body></html>");
        sub.setBorder(new EmptyBorder(10, 0, 25, 0));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(bgDark);
        top.add(headline);
        top.add(sub);

        JPanel features = new JPanel(new GridLayout(1, 3, 20, 0));
        features.setOpaque(false);
        features.add(makeFeatureCard("💎", "Earn & Spend Points", "Teach to earn points, spend them to learn new skills."));
        features.add(makeFeatureCard("🤝", "AI Skill Matchmaking", "Find ideal skill exchange partners with AI."));
        features.add(makeFeatureCard("📅", "Smart Scheduling", "Plan and manage sessions effortlessly."));

        // Testimonials & CTA
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(24, 0, 0, 0));

        // Testimonials cards
        testimonialCards.setOpaque(false);
        testimonialCards.add(makeTestimonial("Asha —\nI learned UI design in 3 weeks!"), "t1");
        testimonialCards.add(makeTestimonial("Ravi —\nGreat teachers and fair points system."), "t2");
        testimonialCards.add(makeTestimonial("Leena —\nScheduling and chat made learning painless."), "t3");

        bottom.add(testimonialCards, BorderLayout.CENTER);

        JPanel cta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cta.setOpaque(false);
        JButton join = new JButton("Join SkillBarter — It's Free");
        join.setFont(new Font("Segoe UI", Font.BOLD, 16));
        join.addActionListener(e -> new RegisterDialog(this).setVisible(true));
        cta.add(join);

        bottom.add(cta, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(features, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel makeTestimonial(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel t = new JLabel("<html><div style='font-size:15px;color:#EAEAEA;padding:10px;'><i>\"" + text + "\"</i></div></html>");
        p.add(t, BorderLayout.CENTER);
        return p;
    }

    // ----- About Panel -----
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("About SkillBarter 🌐");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(neonBlue);
        panel.add(title, BorderLayout.NORTH);

        String aboutText = "<html><body style='font-size:16px;color:#E0E0E0;line-height:1.6;'>"
                + "<b>SkillBarter</b> is a futuristic platform where <b>knowledge becomes a shared currency</b>.<br><br>"
                + "Our mission is to connect passionate learners and teachers worldwide — enabling them to exchange skills, time, and creativity in a <b>cash-free ecosystem</b>."
                + "</body></html>";

        JLabel about = new JLabel(aboutText);
        about.setBorder(new EmptyBorder(18, 0, 0, 0));
        panel.add(about, BorderLayout.CENTER);

        // FAQ accordion on the right
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 20, 0, 0));
        right.add(makeAccordionItem("What is SkillBarter?","A peer-to-peer skill exchange platform."));
        right.add(makeAccordionItem("How do points work?","Earn points by teaching; spend them to learn."));
        right.add(makeAccordionItem("Is it free?","Yes — core features are free; premium features planned."));

        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JPanel makeAccordionItem(String q, String a) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        JButton header = new JButton(q);
        header.setFocusPainted(false);
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.addActionListener(new AbstractAction() {
            boolean open = false;
            JPanel body;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!open) {
                    body = new JPanel();
                    body.setOpaque(false);
                    body.add(new JLabel("<html><div style='width:260px;color:#E0E0E0;'>" + a + "</div></html>"));
                    container.add(body, BorderLayout.SOUTH);
                } else {
                    container.remove(1);
                }
                open = !open;
                container.revalidate();
                container.repaint();
            }
        });
        container.add(header, BorderLayout.NORTH);
        return container;
    }

    // ----- Services Panel -----
    private JPanel createServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel headline = new JLabel("Our Services ⚙️");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headline.setForeground(neonBlue);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);
        grid.add(makeFeatureCard("🤖", "AI Matchmaking", "Intelligent partner recommendations based on your goals."));
        grid.add(makeFeatureCard("🔐", "Secure Points", "Encrypted points ledger and dispute resolution."));
        grid.add(makeFeatureCard("📅", "Scheduler", "Take care of timezones and bookings."));
        grid.add(makeFeatureCard("💬", "Chat", "Real-time translation and messaging."));
        grid.add(makeFeatureCard("⭐", "Feedback", "Maintain high quality with ratings."));
        grid.add(makeFeatureCard("📊", "Analytics", "Progress reports and learning insights."));

        panel.add(headline, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private void showServiceDetail(String title, String desc) {
        JTextArea area = new JTextArea(title + "\n\n" + desc + "\n\nDetailed info, pricing tiers, FAQs, and sign-up CTA would go here.");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(cardDark);
        area.setForeground(textLight);
        area.setBorder(new EmptyBorder(12,12,12,12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Service — " + title, JOptionPane.INFORMATION_MESSAGE);
    }

    // ----- Modules Panel -----
    private JPanel createModulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel headline = new JLabel("Core Modules 🧩");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headline.setForeground(neonBlue);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);
        grid.add(makeFeatureCard("🏗️", "User Management", "Profiles, authentication, and roles."));
        grid.add(makeFeatureCard("🤖", "Recommender", "Suggests ideal matches using signals."));
        grid.add(makeFeatureCard("💰", "Points Engine", "Handles balances and transactions."));
        grid.add(makeFeatureCard("📅", "Scheduler", "Bookings, cancellations, and reminders."));
        grid.add(makeFeatureCard("💬", "Messaging", "Encrypted chat and notifications."));
        grid.add(makeFeatureCard("🛡️", "Admin Console", "Moderation and analytics."));

        panel.add(headline, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // ----- Contact Panel with validation and Export to HTML -----
    private JPanel createContactPanel() {
        JPanel contact = new JPanel(new BorderLayout());
        contact.setBackground(bgDark);
        contact.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel h = new JLabel("Contact Us ✉️");
        h.setFont(headingFont);
        h.setForeground(neonBlue);
        contact.add(h, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameL = new JLabel("Name:"); nameL.setForeground(textLight);
        JLabel emailL = new JLabel("Email:"); emailL.setForeground(textLight);
        JLabel msgL = new JLabel("Message:"); msgL.setForeground(textLight);

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextArea msg = new JTextArea(6, 20);
        msg.setLineWrap(true); msg.setWrapStyleWord(true);

        gbc.gridx=0; gbc.gridy=0; form.add(nameL, gbc);
        gbc.gridx=1; gbc.gridy=0; form.add(name, gbc);
        gbc.gridx=0; gbc.gridy=1; form.add(emailL, gbc);
        gbc.gridx=1; gbc.gridy=1; form.add(email, gbc);
        gbc.gridx=0; gbc.gridy=2; form.add(msgL, gbc);
        gbc.gridx=1; gbc.gridy=2; form.add(new JScrollPane(msg), gbc);

        JButton send = new JButton("Send Message");
        send.addActionListener(e -> {
            String nm = name.getText().trim();
            String em = email.getText().trim();
            String ms = msg.getText().trim();
            if (nm.isEmpty() || em.isEmpty() || ms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!em.contains("@") || !em.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Message Sent Successfully!");
        });

        JButton export = new JButton("Export Page to HTML");
        export.addActionListener(e -> exportToHTML());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.setOpaque(false);
        btns.add(send); btns.add(export);

        gbc.gridx=1; gbc.gridy=3; form.add(btns, gbc);

        contact.add(form, BorderLayout.CENTER);
        return contact;
    }

    private void exportToHTML() {
        String html = "<!doctype html><html><head><meta charset=\"utf-8\"><title>SkillBarter — Export</title></head><body>"
                + "<h1>SkillBarter — Exported Page</h1><p>This is a simple HTML export generated by the desktop app.</p>"
                + "<ul><li>AI Matchmaking</li><li>Points Engine</li><li>Scheduler</li></ul>"
                + "</body></html>";
        try (FileWriter fw = new FileWriter("skillbarter_export.html")) {
            fw.write(html);
            JOptionPane.showMessageDialog(this, "Exported to skillbarter_export.html in working directory.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to export: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----- Simple Profile / Login Panel -----
    private JPanel createLoginProfilePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bgDark);
        p.setBorder(new EmptyBorder(30,30,30,30));
        JLabel t = new JLabel("Profile"); t.setFont(headingFont); t.setForeground(neonBlue);
        p.add(t, BorderLayout.NORTH);
        JLabel info = new JLabel("<html><div style='color:#EAEAEA'>You are not logged in. Use Ctrl+S or the Login button to sign in.</div></html>");
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    // ----- Footer -----
    private JPanel createFooter() {
        JPanel f = new JPanel(new BorderLayout());
        f.setBackground(new Color(18, 18, 22));
        f.setBorder(new EmptyBorder(10, 20, 10, 20));
        JLabel left = new JLabel("©️ 2025 SkillBarter | Desktop Website Edition");
        left.setForeground(new Color(160, 160, 160));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        JButton terms = new JButton("Terms"); terms.setFocusPainted(false);
        terms.addActionListener(e -> JOptionPane.showMessageDialog(this, "Terms and Conditions placeholder."));
        JButton privacy = new JButton("Privacy"); privacy.setFocusPainted(false);
        privacy.addActionListener(e -> JOptionPane.showMessageDialog(this, "Privacy policy placeholder."));
        right.add(terms); right.add(privacy);

        f.add(left, BorderLayout.WEST);
        f.add(right, BorderLayout.EAST);
        return f;
    }

    // ----- Search Dialog -----
    private void showSearchDialog() {
        String q = JOptionPane.showInputDialog(this, "Search for skills, topics or users:");
        if (q != null) {
            // naive search simulation: show a small results dialog
            JOptionPane.showMessageDialog(this, "Search results for: " + q + "\n\n- UI Design\n- Java Swing\n- Photography");
        }
    }

}

// ----- Login Dialog -----
class LoginDialog extends JDialog {
    LoginDialog(JFrame parent) {
        super(parent, "Login to SkillBarter", true);
        setSize(420, 260);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.setBorder(new EmptyBorder(18,18,18,18));
        form.add(new JLabel("Email:"));
        JTextField email = new JTextField(); form.add(email);
        form.add(new JLabel("Password:"));
        JPasswordField pass = new JPasswordField(); form.add(pass);

        JCheckBox remember = new JCheckBox("Remember me");
        form.add(remember);

        JButton login = new JButton("Login");
        login.addActionListener(e -> {
            if (email.getText().contains("@") && pass.getPassword().length>0) {
                JOptionPane.showMessageDialog(this, "Login successful (demo). Welcome!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter valid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(login);

        add(form, BorderLayout.CENTER);
    }
}

// ----- Register Dialog -----
class RegisterDialog extends JDialog {
    RegisterDialog(JFrame parent) {
        super(parent, "Create an account", true);
        setSize(480, 360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12,12,12,12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Full name:"), gbc);
        gbc.gridx=1; gbc.gridy=0; JTextField name = new JTextField(); form.add(name, gbc);
        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Email:"), gbc);
        gbc.gridx=1; gbc.gridy=1; JTextField email = new JTextField(); form.add(email, gbc);
        gbc.gridx=0; gbc.gridy=2; form.add(new JLabel("Password:"), gbc);
        gbc.gridx=1; gbc.gridy=2; JPasswordField pass = new JPasswordField(); form.add(pass, gbc);

        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("Skills (comma-separated):"), gbc);
        gbc.gridx=1; gbc.gridy=3; JTextField skills = new JTextField(); form.add(skills, gbc);

        JButton create = new JButton("Create Account");
        create.addActionListener(e -> {
            if (name.getText().trim().isEmpty() || !email.getText().contains("@") || pass.getPassword().length<6) {
                JOptionPane.showMessageDialog(this, "Please provide valid details. Password must be 6+ chars.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // In a real app, we'd call backend; here we just show a brief success and close.
            JOptionPane.showMessageDialog(this, "Account created (demo). Welcome, " + name.getText());
            dispose();
        });

        gbc.gridx=1; gbc.gridy=4; form.add(create, gbc);

        add(form, BorderLayout.CENTER);
    }
}
