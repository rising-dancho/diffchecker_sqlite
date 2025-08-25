package com.diffchecker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.diffchecker.components.ClosableTabTitleComponent;
import com.diffchecker.components.ComponentResizer;
import com.diffchecker.components.CustomTitleBar;
// IMPORT COMPONENTS
import com.diffchecker.components.RoundedTabbedPaneUI;
import com.diffchecker.components.SplitTextTabPanel;
import com.diffchecker.components.Database.DB;
import com.diffchecker.components.Database.DiffData;
import com.diffchecker.components.Database.DiffRepository;

import java.util.List;

public class Main extends JFrame {

    // ─── Static Resources ──────────────────────────────────────────────────────
    private static final String PACKAGE_NAME = "diffchecker";
    private static final ImageIcon LOGO = new ImageIcon(
            Main.class.getResource("/" + PACKAGE_NAME + "/images/logo/logo.png"));

    // ─── Instance Fields ───────────────────────────────────────────────────────
    private final JPanel container = new JPanel();
    private final Color FONT_COLOR = new Color(0xd6d6d6);

    // FOR CLOSING TABS WITH CTRL+W
    JTabbedPane tabbedPane;
    private final Runnable onTabEmptyFallback = () -> addNewTab(tabbedPane);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        initFrame(); // 1. Frame setup
        JPanel wrapper = initWrapper(); // 2. Background wrapper
        JPanel titleBar = buildTitleBar(); // 3. Custom title bar
        // JPanel menuPanel = buildMenuPanel(); // 4. Menu bar
        JPanel content = buildMainContent(); // 5. Main tabbed pane area

        // FOR DEBUGGING PURPOSES ONLY
        // menuPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        // ── Compose Layout ─────────────────────────────────────────────────────
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(new Color(0x242526));
        // centerContent.add(menuPanel);
        centerContent.add(content);

        wrapper.add(titleBar, BorderLayout.NORTH);
        wrapper.add(centerContent, BorderLayout.CENTER);
        setContentPane(wrapper);

        // ── Final Steps ────────────────────────────────────────────────────────
        centerWindow();
        enableResizing();
        setVisible(true);
    }

    // ─── 1. Initialize Frame ───────────────────────────────────────────────────
    private void initFrame() {
        setTitle("Diffchecker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(LOGO.getImage());
        setUndecorated(true);
        setBackground(new Color(0x1F1F1F));
        setSize(1080, 720);

        // Rounded corners
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(),
                getHeight(), 20, 20));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(),
                        getHeight(), 20, 20));
            }
        });
    }

    // ─── 2. Wrapper Panel ──────────────────────────────────────────────────────
    private JPanel initWrapper() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(0x242526));
        wrapper.setOpaque(true);
        // ALLOWING THE CORNERS TO HAVE ENOUGH SPACE TO DETECT RESIZING
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return wrapper;
    }

    // ─── 3. Title Bar Panel ────────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        CustomTitleBar titleBar = new CustomTitleBar(
                this,
                "",
                PACKAGE_NAME,
                "/" + PACKAGE_NAME + "/images/logo/logo_24x24.png",
                new Color(0x242526),
                33);

        // FOR DEBUGGING PURPOSES ONLY
        // titleBar.setBorder(BorderFactory.createLineBorder(Color.RED));

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setOpaque(false);
        // titleWrapper.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        titleWrapper.add(titleBar, BorderLayout.NORTH);

        return titleWrapper;
    }

    // ─── 4. Main Content Panel with Tabs ───────────────────────────────────────
    private JPanel buildMainContent() {
        container.setBackground(new Color(0x242526));
        container.setLayout(new BorderLayout());
        container.setBorder(null);

        // FOR DEBUGGING PURPOSES ONLY
        // container.setBorder(
        // BorderFactory.createCompoundBorder(
        // BorderFactory.createLineBorder(Color.BLUE), // outer border
        // BorderFactory.createEmptyBorder(0, 5, 5, 5) // inner padding
        // ));

        // container.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        // container.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        tabbedPane = new JTabbedPane();
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        // HOTKEY FOR CLOSING TABS (CTRL + W)
        // Ctrl+W → Close tab
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "closeTab");
        actionMap.put("closeTab", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeTabAt();
            }
        });

        // HOTKEY FOR OPENING TABS (CTRL + T)
        // Ctrl+T → New tab
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "newTab");
        actionMap.put("newTab", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab(tabbedPane);
            }
        });

        RoundedTabbedPaneUI ui = new RoundedTabbedPaneUI();
        tabbedPane.setUI(ui); // Set only once

        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabbedPane.setFocusable(false);

        // Hover behavior
        ui.setHoverListener(index -> {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component c = tabbedPane.getTabComponentAt(i);
                if (c instanceof ClosableTabTitleComponent) {
                    ((ClosableTabTitleComponent) c).setHovered(i == index);
                }
            }
        });

        // ADD TABS
        JButton addButton = new JButton("➕");
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setPreferredSize(new Dimension(26, 26));
        addButton.addActionListener(e -> addNewTab(tabbedPane));
        addButton.setForeground(FONT_COLOR);
        addButton.setFont(addButton.getFont().deriveFont(13.8f));
        addButton.setToolTipText("<html><strong>New Tab</strong> <br> ( Ctrl + T )</html>");
        // ---- Restore last session (load all diffs from DB) ----
        DB db = new DB();
        DiffRepository repo = new DiffRepository(db);

        List<DiffData> diffs = repo.getAllDiffs();
        for (DiffData data : diffs) {
            SplitTextTabPanel panel = new SplitTextTabPanel();
            panel.loadFromDatabase(data); // populate the text areas
            int index = tabbedPane.getTabCount();
            tabbedPane.insertTab(data.title, null, panel, null, index);
            tabbedPane.setTabComponentAt(index,
                    new ClosableTabTitleComponent(
                            tabbedPane, data.title, onTabEmptyFallback, tabIndex -> closeTabAt(tabIndex)));
        }

        // Only add an empty "Untitled" tab if nothing was loaded from DB
        if (tabbedPane.getTabCount() == 0) {
            addNewTab(tabbedPane);
        }

        // Add the + button as the *last* tab
        tabbedPane.addTab("", null);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, addButton);

        container.add(tabbedPane, BorderLayout.CENTER);

        return container;
    }

    public void closeTabAt() {
        int index = tabbedPane.getSelectedIndex();
        closeTabAt(index);
    }

    public void closeTabAt(int index) {
        if (index == -1)
            return;

        Component comp = tabbedPane.getComponentAt(index);

        // don’t close the "+" tab
        if (comp instanceof JButton)
            return;

        // Ask to save unsaved changes
        if (comp instanceof SplitTextTabPanel panel && panel.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "You have unsaved changes. Save before closing?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                // Do nothing if cancelled or dialog closed
                return;
            }
            if (option == JOptionPane.YES_OPTION) {
                panel.saveToDatabase();
            }
        }

        tabbedPane.remove(index);

        // if only "+" tab is left, run fallback
        if (tabbedPane.getTabCount() == 1) {
            onTabEmptyFallback.run();
            return;
        }

        // Move selection to previous real tab, skipping "+"
        int newIndex = Math.max(0, index - 1);
        Component newComp = tabbedPane.getTabComponentAt(newIndex);
        if (newComp instanceof JButton) {
            newIndex = Math.max(0, newIndex - 1);
        }
        tabbedPane.setSelectedIndex(newIndex);
    }

    private int untitledCounter = 1;

    private void addNewTab(JTabbedPane tabbedPane) {
        // Decide where to insert:
        // - If the last tab is the "+" button, insert before it
        // - Otherwise, append at the end (e.g., on first run before "+" exists)
        int insertIndex = tabbedPane.getTabCount();
        if (insertIndex > 0) {
            Component lastTabComponent = tabbedPane.getTabComponentAt(insertIndex - 1);
            if (lastTabComponent instanceof JButton) {
                insertIndex--; // keep new tab before the "+" tab
            }
        }

        String title = "Untitled-" + untitledCounter++;
        SplitTextTabPanel splitArea = new SplitTextTabPanel();

        tabbedPane.insertTab(title, null, splitArea, null, insertIndex);
        tabbedPane.setTabComponentAt(
                insertIndex,
                new ClosableTabTitleComponent(tabbedPane, title, onTabEmptyFallback, tabIndex -> closeTabAt(tabIndex)));
        tabbedPane.setSelectedIndex(insertIndex);
    }

    // ─── 5. Window Positioning ─────────────────────────────────────────────────
    private void centerWindow() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    // ─── 6. Enable Edge Resizing ───────────────────────────────────────────────
    private void enableResizing() {
        new ComponentResizer(
                new Insets(8, 8, 8, 8),
                new Dimension(1, 1),
                new Dimension(100, 100),
                this);
    }
}
