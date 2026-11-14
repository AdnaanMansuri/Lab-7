package ui;

import java.awt.CardLayout;

/**
 * Main application window. Hosts a top navigation bar (Form / View) and a
 * BottomPanel that swaps between the two views using a CardLayout.
 */
public class MainJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(MainJFrame.class.getName());

    private FormJPanel formPanel;
    private ViewJPanel viewPanel;

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        setTitle("Lab 7 - Student CRUD (JDBC + MySQL)");
        setLocationRelativeTo(null);

        formPanel = new FormJPanel();
        viewPanel = new ViewJPanel();

        BottomPanel.setLayout(new CardLayout());
        BottomPanel.add(formPanel, "form");
        BottomPanel.add(viewPanel, "view");

        // Let the form jump to (and refresh) the view after a successful insert.
        formPanel.setViewPanel(viewPanel);
        formPanel.setCardContainer(BottomPanel);

        // Top navigation buttons.
        RegistrationButton.addActionListener(evt -> showCard("form"));
        ViewButton.addActionListener(evt -> {
            viewPanel.populateTable();
            showCard("view");
        });

        // Start on the create-form card.
        showCard("form");
    }

    private void showCard(String name) {
        CardLayout layout = (CardLayout) BottomPanel.getLayout();
        layout.show(BottomPanel, name);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        TopPanel = new javax.swing.JPanel();
        RegistrationButton = new javax.swing.JButton();
        ViewButton = new javax.swing.JButton();
        BottomPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        TopPanel.setBackground(new java.awt.Color(0, 255, 255));

        RegistrationButton.setText("Form");

        ViewButton.setText("View");

        javax.swing.GroupLayout TopPanelLayout = new javax.swing.GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(RegistrationButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addComponent(ViewButton)
                .addGap(92, 92, 92))
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RegistrationButton)
                    .addComponent(ViewButton))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(TopPanel);

        BottomPanel.setLayout(new java.awt.CardLayout());
        jSplitPane1.setRightComponent(BottomPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new MainJFrame().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JButton RegistrationButton;
    private javax.swing.JPanel TopPanel;
    private javax.swing.JButton ViewButton;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
}
