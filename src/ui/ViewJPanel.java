package ui;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.User;
import utility.DatabaseConnector;

/**
 * View/read view: shows all students in a JTable and supports Delete and
 * Edit + Submit (update) on the selected row.
 */
public class ViewJPanel extends javax.swing.JPanel {

    private User userObject;
    private ArrayList<User> users;

    private static final String[] COLLEGES = {
        "College of Engineering",
        "Khoury College of Computer Sciences",
        "D'Amore-McKim School of Business"
    };

    /**
     * Creates new form ViewJPanel
     */
    public ViewJPanel() {
        initComponents();

        // Column headers for the table (index 2 "Last Name" is intentionally unused).
        userJTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Id", "Name", "Last Name", "College", "Age", "Hobbies"}
        ));

        // Fill the college drop-down used while editing.
        collegeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(COLLEGES));

        editButton.addActionListener(evt -> editButtonActionPerformed(evt));
        deleteButton.addActionListener(evt -> deleteButtonActionPerformed(evt));
        submitEditButton.addActionListener(evt -> submitEditButtonActionPerformed(evt));

        populateTable();
        disableAll();   // edit fields stay locked until "Edit" is pressed
    }

    /** Copies the currently selected user's values into the edit fields. */
    public void setAllFields() {
        this.nameTextField.setText(userObject.getName());
        this.collegeComboBox.setSelectedItem(userObject.getCollege());
        this.ageTextField.setText(String.valueOf(userObject.getAge()));
        this.hobbyTextArea.setText(userObject.getHobby());
    }

    /** Reads every row from the database and (re)fills the JTable. */
    public void populateTable() {
        // getting the data from the database
        this.users = DatabaseConnector.getUsers();

        // display the users in the JTable
        DefaultTableModel model = (DefaultTableModel) this.userJTable.getModel();
        model.setRowCount(0);
        for (User u1 : users) {
            Object[] row = new Object[6];
            row[0] = u1.getUserId();
            row[1] = u1.getName();
            // row[2] = u1.getLastName();   // last name not used in this lab
            row[3] = u1.getCollege();
            row[4] = u1.getAge();
            row[5] = u1.getHobby();
            model.addRow(row);
        }
    }

    public void enableAll() {
        this.nameTextField.setEnabled(true);
        this.collegeComboBox.setEnabled(true);
        this.ageTextField.setEnabled(true);
        this.hobbyTextArea.setEnabled(true);
    }

    public void disableAll() {
        this.nameTextField.setEnabled(false);
        this.collegeComboBox.setEnabled(false);
        this.ageTextField.setEnabled(false);
        this.hobbyTextArea.setEnabled(false);
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // 1. which user to delete? find the row the user clicked on
        int selectedRowIndex = this.userJTable.getSelectedRow();

        try {
            // find the user at that index
            this.userObject = this.users.get(selectedRowIndex);

            // call the delete method from the Database Connector
            DatabaseConnector.deleteUser(userObject);
            System.out.println("User deleted successfully.");

            // repopulate the table
            populateTable();
        } catch (Exception e) {
            System.out.println("Unable to delete");
            System.out.println(e);
        }
    }

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // 1. which user to edit? find the row the user clicked on
        int selectedRowIndex = this.userJTable.getSelectedRow();

        try {
            // find the user at that index
            this.userObject = this.users.get(selectedRowIndex);

            // set the values in the text boxes, comboboxes, ...
            setAllFields();

            // make them all enabled
            enableAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void submitEditButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // 1. read the updated values from the textboxes, ...
        User updatedUser = new User();
        updatedUser.setName(this.nameTextField.getText());
        updatedUser.setAge(Integer.parseInt(this.ageTextField.getText()));
        updatedUser.setCollege(this.collegeComboBox.getSelectedItem().toString());
        updatedUser.setHobby(this.hobbyTextArea.getText());

        // 2. call the update user method from the database connector
        try {
            DatabaseConnector.updateUser(userObject, updatedUser);
            System.out.println("The record has been updated.");

            JOptionPane.showMessageDialog(this, "Record updated!", "Success!",
                    JOptionPane.INFORMATION_MESSAGE);

            // repopulate the table
            populateTable();
            disableAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        userJTable = new javax.swing.JTable();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        submitEditButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        collegeLabel = new javax.swing.JLabel();
        collegeComboBox = new javax.swing.JComboBox<>();
        ageLabel = new javax.swing.JLabel();
        ageTextField = new javax.swing.JTextField();
        hobbyLabel = new javax.swing.JLabel();
        hobbyScrollPane = new javax.swing.JScrollPane();
        hobbyTextArea = new javax.swing.JTextArea();

        jScrollPane1.setViewportView(userJTable);

        editButton.setText("Edit");

        deleteButton.setText("Delete");

        submitEditButton.setText("Submit Edit");

        nameLabel.setText("Name:");

        collegeLabel.setText("College:");

        ageLabel.setText("Age:");

        hobbyLabel.setText("Hobbies:");

        hobbyTextArea.setColumns(20);
        hobbyTextArea.setRows(3);
        hobbyScrollPane.setViewportView(hobbyTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submitEditButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(collegeLabel)
                            .addComponent(ageLabel)
                            .addComponent(hobbyLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(collegeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hobbyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editButton)
                    .addComponent(deleteButton)
                    .addComponent(submitEditButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collegeLabel)
                    .addComponent(collegeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ageLabel)
                    .addComponent(ageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hobbyLabel)
                    .addComponent(hobbyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ageLabel;
    private javax.swing.JTextField ageTextField;
    private javax.swing.JComboBox<String> collegeComboBox;
    private javax.swing.JLabel collegeLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel hobbyLabel;
    private javax.swing.JScrollPane hobbyScrollPane;
    private javax.swing.JTextArea hobbyTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton submitEditButton;
    private javax.swing.JTable userJTable;
    // End of variables declaration//GEN-END:variables
}
