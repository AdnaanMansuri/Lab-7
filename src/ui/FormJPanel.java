package ui;

import java.awt.CardLayout;
import javax.swing.JOptionPane;
import model.User;
import utility.DatabaseConnector;

/**
 * Create view: collects a new student's details and inserts them into the
 * database via {@link utility.DatabaseConnector#addUser(model.User)}.
 */
public class FormJPanel extends javax.swing.JPanel {

    /** Set by MainJFrame so this form can navigate to (and refresh) the view. */
    private ViewJPanel viewPanel;
    private java.awt.Container cardContainer;

    /** Groups the college radio buttons so only one can be selected. */
    private final javax.swing.ButtonGroup collegeButtonGroup = new javax.swing.ButtonGroup();

    /**
     * Creates new form FormJPanel
     */
    public FormJPanel() {
        initComponents();

        // Group the college radio buttons (getSelection() stays null until one is picked).
        collegeButtonGroup.add(collegeRadio1);
        collegeButtonGroup.add(collegeRadio2);
        collegeButtonGroup.add(collegeRadio3);

        submitButton.addActionListener(evt -> submitButtonActionPerformed(evt));
    }

    public void setViewPanel(ViewJPanel viewPanel) {
        this.viewPanel = viewPanel;
    }

    public void setCardContainer(java.awt.Container cardContainer) {
        this.cardContainer = cardContainer;
    }

    /** @return the text of the selected college radio button. */
    private String getSelectedCollege() {
        // getSelection() returns null when nothing is selected -> NullPointerException,
        // which is caught below and reported as "Please select your college."
        return collegeButtonGroup.getSelection().getActionCommand();
    }

    private void clearForm() {
        nameTextField.setText("");
        ageTextField.setText("");
        hobbyTextArea.setText("");
        collegeButtonGroup.clearSelection();
    }

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            User userObject = new User();
            userObject.setName(nameTextField.getText().trim());
            userObject.setAge(Integer.parseInt(ageTextField.getText().trim()));
            userObject.setCollege(getSelectedCollege());
            userObject.setHobby(hobbyTextArea.getText().trim());

            if (userObject.getName().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Oops!",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                // All validations passed: add the userObject into the database.
                DatabaseConnector.addUser(userObject);
                System.out.println("Added user into the database");

                JOptionPane.showMessageDialog(this, userObject, "Success!",
                        JOptionPane.INFORMATION_MESSAGE);

                // Navigate to the View panel and refresh its table.
                if (viewPanel != null) {
                    viewPanel.populateTable();
                }
                if (cardContainer != null) {
                    CardLayout layout = (CardLayout) cardContainer.getLayout();
                    layout.show(cardContainer, "view");
                }
                clearForm();
            }
        } catch (NullPointerException ne) {
            // No college radio button was selected.
            JOptionPane.showMessageDialog(this, "Please select your college.", "Oops!",
                    JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter your age correctly.", "Oops!",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Oops, something went wrong!", "Failed!",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        ageLabel = new javax.swing.JLabel();
        ageTextField = new javax.swing.JTextField();
        collegeLabel = new javax.swing.JLabel();
        collegeRadio1 = new javax.swing.JRadioButton();
        collegeRadio2 = new javax.swing.JRadioButton();
        collegeRadio3 = new javax.swing.JRadioButton();
        hobbyLabel = new javax.swing.JLabel();
        hobbyScrollPane = new javax.swing.JScrollPane();
        hobbyTextArea = new javax.swing.JTextArea();
        submitButton = new javax.swing.JButton();

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setText("Student Registration Form");

        nameLabel.setText("Name:");

        ageLabel.setText("Age:");

        collegeLabel.setText("College:");

        collegeRadio1.setText("College of Engineering");

        collegeRadio2.setText("Khoury College of Computer Sciences");

        collegeRadio3.setText("D'Amore-McKim School of Business");

        hobbyLabel.setText("Hobbies:");

        hobbyTextArea.setColumns(20);
        hobbyTextArea.setRows(3);
        hobbyScrollPane.setViewportView(hobbyTextArea);

        submitButton.setText("Submit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(ageLabel)
                            .addComponent(collegeLabel)
                            .addComponent(hobbyLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(collegeRadio1)
                            .addComponent(collegeRadio2)
                            .addComponent(collegeRadio3)
                            .addComponent(hobbyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(submitButton))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ageLabel)
                    .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collegeLabel)
                    .addComponent(collegeRadio1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collegeRadio2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collegeRadio3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hobbyLabel)
                    .addComponent(hobbyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(submitButton)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ageLabel;
    private javax.swing.JTextField ageTextField;
    private javax.swing.JLabel collegeLabel;
    private javax.swing.JRadioButton collegeRadio1;
    private javax.swing.JRadioButton collegeRadio2;
    private javax.swing.JRadioButton collegeRadio3;
    private javax.swing.JLabel hobbyLabel;
    private javax.swing.JScrollPane hobbyScrollPane;
    private javax.swing.JTextArea hobbyTextArea;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
