package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.User;
import utility.DatabaseConnector;

/**
 * View / Read view: shows every student in a JTable (including a photo
 * thumbnail) and supports Delete and Edit + Submit (update) on the selected row.
 *
 * The JTable is the mandatory component for this lab.
 */
public class ViewJPanel extends javax.swing.JPanel {

    private static final String[] COLUMNS = {
        "Id", "Photo", "First Name", "Last Name", "Gender", "DOB", "Age",
        "Phone", "Email", "College", "Experience", "Hobbies"
    };

    private final java.text.SimpleDateFormat dateFmt =
            new java.text.SimpleDateFormat("MM/dd/yyyy");

    private User userObject;              // the row currently being edited
    private ArrayList<User> users = new ArrayList<>();
    private byte[] editPhoto;             // photo bytes for the row being edited

    // ----- table -----
    private javax.swing.JTable userJTable;

    // ----- edit controls -----
    private javax.swing.JTextField firstNameField;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JComboBox<String> genderCombo;
    private de.wannawork.jcalendar.JCalendarComboBox dobChooser;
    private javax.swing.JSpinner ageSpinner;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JTextField emailField;
    private javax.swing.JComboBox<String> collegeCombo;
    private javax.swing.JTextField experienceField;
    private javax.swing.JTextArea hobbyArea;
    private javax.swing.JLabel photoPreview;
    private javax.swing.JButton changePhotoButton;

    private javax.swing.JButton editButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton submitEditButton;

    public ViewJPanel() {
        initComponents();

        dobChooser.addChangeListener(evt -> updateAgeFromDob());
        changePhotoButton.addActionListener(evt -> changePhoto());
        editButton.addActionListener(evt -> editButtonActionPerformed());
        deleteButton.addActionListener(evt -> deleteButtonActionPerformed());
        submitEditButton.addActionListener(evt -> submitEditButtonActionPerformed());

        populateTable();
        disableAll();   // edit fields stay locked until "Edit" is pressed
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- table -----
        userJTable = new javax.swing.JTable(new DefaultTableModel(new Object[][]{}, COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 1) ? ImageIcon.class : Object.class;
            }
        });
        userJTable.setRowHeight(44);
        userJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        javax.swing.JScrollPane tableScroll = new javax.swing.JScrollPane(userJTable);
        tableScroll.setPreferredSize(new java.awt.Dimension(640, 220));
        add(tableScroll, BorderLayout.CENTER);

        // ----- south: buttons + edit fields -----
        javax.swing.JPanel south = new javax.swing.JPanel();
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.JPanel buttons = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        editButton = new javax.swing.JButton("Edit");
        deleteButton = new javax.swing.JButton("Delete");
        submitEditButton = new javax.swing.JButton("Submit Edit");
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(submitEditButton);
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        south.add(buttons);

        south.add(buildEditPanel());
        add(south, BorderLayout.SOUTH);
    }

    private javax.swing.JPanel buildEditPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel(new GridBagLayout());
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected student"));
        panel.setAlignmentX(LEFT_ALIGNMENT);

        firstNameField = new javax.swing.JTextField(16);
        lastNameField = new javax.swing.JTextField(16);
        genderCombo = new javax.swing.JComboBox<>(FormJPanel.GENDERS);
        dobChooser = new de.wannawork.jcalendar.JCalendarComboBox();
        dobChooser.setDate(null);
        ageSpinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(18, 0, 120, 1));
        phoneField = new javax.swing.JFormattedTextField(createPhoneFormatter());
        phoneField.setColumns(16);
        emailField = new javax.swing.JTextField(16);
        collegeCombo = new javax.swing.JComboBox<>(FormJPanel.COLLEGES);
        experienceField = new javax.swing.JTextField(16);
        hobbyArea = new javax.swing.JTextArea(2, 16);
        hobbyArea.setLineWrap(true);
        hobbyArea.setWrapStyleWord(true);
        javax.swing.JScrollPane hobbyScroll = new javax.swing.JScrollPane(hobbyArea);

        // Keep fields from collapsing to a sliver if space ever gets tight.
        for (javax.swing.JComponent c : new javax.swing.JComponent[]{
                firstNameField, lastNameField, genderCombo, ageSpinner, phoneField,
                emailField, collegeCombo, experienceField, hobbyScroll}) {
            c.setMinimumSize(c.getPreferredSize());
        }

        photoPreview = new javax.swing.JLabel("No photo", javax.swing.SwingConstants.CENTER);
        photoPreview.setPreferredSize(new java.awt.Dimension(90, 90));
        photoPreview.setMinimumSize(new java.awt.Dimension(90, 90));
        photoPreview.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        changePhotoButton = new javax.swing.JButton("Change Photo");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Left column of fields (grid columns 0-1), right column (2-3).
        addField(panel, gbc, 0, 0, "First Name:", firstNameField);
        addField(panel, gbc, 0, 1, "Last Name:", lastNameField);
        addField(panel, gbc, 0, 2, "Gender:", genderCombo);
        addField(panel, gbc, 0, 3, "Date of Birth:", dobChooser);
        addField(panel, gbc, 0, 4, "Age:", ageSpinner);

        addField(panel, gbc, 2, 0, "Phone:", phoneField);
        addField(panel, gbc, 2, 1, "Email:", emailField);
        addField(panel, gbc, 2, 2, "College:", collegeCombo);
        addField(panel, gbc, 2, 3, "Experience:", experienceField);
        addField(panel, gbc, 2, 4, "Hobbies:", hobbyScroll);

        // Photo cell.
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(photoPreview, gbc);
        gbc.gridy = 3;
        gbc.gridheight = 1;
        panel.add(changePhotoButton, gbc);
        gbc.anchor = GridBagConstraints.WEST;

        return panel;
    }

    private void addField(javax.swing.JPanel panel, GridBagConstraints gbc,
                          int col, int row, String label, java.awt.Component field) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(new javax.swing.JLabel(label), gbc);
        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

    private javax.swing.text.MaskFormatter createPhoneFormatter() {
        try {
            javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter("###-###-####");
            mf.setPlaceholderCharacter('_');
            return mf;
        } catch (java.text.ParseException ex) {
            return null;
        }
    }

    /** Builds a scaled ImageIcon from raw bytes, or null. */
    private ImageIcon thumbnail(byte[] bytes, int w, int h) {
        if (bytes == null) {
            return null;
        }
        Image img = new ImageIcon(bytes).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /** Reads every row from the database and (re)fills the JTable. */
    public void populateTable() {
        this.users = DatabaseConnector.getUsers();

        DefaultTableModel model = (DefaultTableModel) this.userJTable.getModel();
        model.setRowCount(0);
        for (User u : users) {
            String dobStr = (u.getDob() != null) ? dateFmt.format(u.getDob()) : "";
            model.addRow(new Object[]{
                u.getUserId(),
                thumbnail(u.getPhoto(), 40, 40),
                u.getFirstName(),
                u.getLastName(),
                u.getGender(),
                dobStr,
                u.getAge(),
                u.getPhone(),
                u.getEmail(),
                u.getCollege(),
                u.getExperience(),
                u.getHobby()
            });
        }
    }

    /** Copies the selected user's values into the edit fields. */
    private void setAllFields() {
        firstNameField.setText(userObject.getFirstName());
        lastNameField.setText(userObject.getLastName());
        genderCombo.setSelectedItem(userObject.getGender());
        dobChooser.setDate(userObject.getDob());
        ageSpinner.setValue(userObject.getAge());
        phoneField.setText(userObject.getPhone());
        emailField.setText(userObject.getEmail());
        collegeCombo.setSelectedItem(userObject.getCollege());
        experienceField.setText(userObject.getExperience());
        hobbyArea.setText(userObject.getHobby());
        editPhoto = userObject.getPhoto();
        showPhotoPreview(editPhoto);
    }

    private void showPhotoPreview(byte[] bytes) {
        ImageIcon icon = thumbnail(bytes, 88, 88);
        if (icon == null) {
            photoPreview.setIcon(null);
            photoPreview.setText("No photo");
        } else {
            photoPreview.setIcon(icon);
            photoPreview.setText("");
        }
    }

    public void enableAll() {
        setFieldsEnabled(true);
    }

    public void disableAll() {
        setFieldsEnabled(false);
    }

    private void setFieldsEnabled(boolean on) {
        firstNameField.setEnabled(on);
        lastNameField.setEnabled(on);
        genderCombo.setEnabled(on);
        dobChooser.setEnabled(on);
        ageSpinner.setEnabled(on);
        phoneField.setEnabled(on);
        emailField.setEnabled(on);
        collegeCombo.setEnabled(on);
        experienceField.setEnabled(on);
        hobbyArea.setEnabled(on);
        changePhotoButton.setEnabled(on);
    }

    private void updateAgeFromDob() {
        java.util.Date dob = dobChooser.getDate();
        if (dob != null) {
            int age = FormJPanel.calculateAge(dob);
            if (age < 0) {
                age = 0;
            }
            ageSpinner.setValue(age);
        }
    }

    private void changePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                editPhoto = Files.readAllBytes(file.toPath());
                showPhotoPreview(editPhoto);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not read the image file.",
                        "Oops!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteButtonActionPerformed() {
        int selectedRowIndex = userJTable.getSelectedRow();
        if (selectedRowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.",
                    "Oops!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            userObject = users.get(selectedRowIndex);
            DatabaseConnector.deleteUser(userObject);
            System.out.println("User deleted successfully.");
            populateTable();
            disableAll();
        } catch (Exception e) {
            System.out.println("Unable to delete");
            System.out.println(e);
        }
    }

    private void editButtonActionPerformed() {
        int selectedRowIndex = userJTable.getSelectedRow();
        if (selectedRowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.",
                    "Oops!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            userObject = users.get(selectedRowIndex);
            setAllFields();
            enableAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void submitEditButtonActionPerformed() {
        if (userObject == null) {
            JOptionPane.showMessageDialog(this, "Please click Edit on a row first.",
                    "Oops!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String errorTitle = "Oops!";
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First and last name are required.",
                        errorTitle, JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!phone.matches("\\d{3}-\\d{3}-\\d{4}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid phone number (123-456-7890).",
                        errorTitle, JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w.\\-]+@([\\w\\-]+\\.)+[\\w\\-]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                        errorTitle, JOptionPane.WARNING_MESSAGE);
                return;
            }

            User updatedUser = new User(
                    firstName, lastName,
                    (String) genderCombo.getSelectedItem(),
                    dobChooser.getDate(),
                    (int) ageSpinner.getValue(),
                    phone, email,
                    (String) collegeCombo.getSelectedItem(),
                    experienceField.getText().trim(),
                    hobbyArea.getText().trim(),
                    editPhoto);

            DatabaseConnector.updateUser(userObject, updatedUser);
            System.out.println("The record has been updated.");

            JOptionPane.showMessageDialog(this, "Record updated!", "Success!",
                    JOptionPane.INFORMATION_MESSAGE);

            populateTable();
            disableAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
