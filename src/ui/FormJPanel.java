package ui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.User;
import utility.DatabaseConnector;

/**
 * Create view: collects a new student's details (name, gender, date of birth,
 * age, phone, email, college, experience, hobbies and a photo) and inserts them
 * into the database via {@link utility.DatabaseConnector#addUser(model.User)}.
 */
public class FormJPanel extends javax.swing.JPanel {

    static final String[] GENDERS = {"Male", "Female", "Other"};
    static final String[] COLLEGES = {
        "College of Engineering",
        "Khoury College of Computer Sciences",
        "D'Amore-McKim School of Business"
    };

    /** Set by MainJFrame so this form can navigate to (and refresh) the view. */
    private ViewJPanel viewPanel;
    private java.awt.Container cardContainer;

    // ----- input components -----
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
    private javax.swing.JButton uploadButton;
    private javax.swing.JButton submitButton;

    /** Raw bytes of the chosen photo (stored in the DB as a BLOB). */
    private byte[] selectedPhoto;

    public FormJPanel() {
        initComponents();

        // Auto-fill Age whenever a Date of Birth is picked on the calendar.
        dobChooser.addChangeListener(evt -> updateAgeFromDob());

        uploadButton.addActionListener(evt -> uploadPhoto());
        submitButton.addActionListener(evt -> submitButtonActionPerformed());
    }

    public void setViewPanel(ViewJPanel viewPanel) {
        this.viewPanel = viewPanel;
    }

    public void setCardContainer(java.awt.Container cardContainer) {
        this.cardContainer = cardContainer;
    }

    /** Builds the whole form with a two-column GridBagLayout inside a scroll pane. */
    private void initComponents() {
        javax.swing.JPanel form = new javax.swing.JPanel(new GridBagLayout());

        javax.swing.JLabel titleLabel = new javax.swing.JLabel("Student Registration Form");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        firstNameField = new javax.swing.JTextField(18);
        lastNameField = new javax.swing.JTextField(18);
        genderCombo = new javax.swing.JComboBox<>(GENDERS);
        dobChooser = new de.wannawork.jcalendar.JCalendarComboBox();
        dobChooser.setDate(null);   // start empty so the user must pick a date

        ageSpinner = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(18, 0, 120, 1));

        phoneField = new javax.swing.JFormattedTextField(createPhoneFormatter());
        phoneField.setColumns(18);
        phoneField.setToolTipText("Format: 123-456-7890");

        emailField = new javax.swing.JTextField(18);
        collegeCombo = new javax.swing.JComboBox<>(COLLEGES);
        experienceField = new javax.swing.JTextField(18);

        hobbyArea = new javax.swing.JTextArea(3, 18);
        hobbyArea.setLineWrap(true);
        hobbyArea.setWrapStyleWord(true);
        javax.swing.JScrollPane hobbyScroll = new javax.swing.JScrollPane(hobbyArea);

        // Lock each input's minimum width to its preferred width, so the fields
        // never collapse to a sliver if GridBagLayout is ever short on space.
        for (javax.swing.JComponent c : new javax.swing.JComponent[]{
                firstNameField, lastNameField, genderCombo, ageSpinner, phoneField,
                emailField, collegeCombo, experienceField, hobbyScroll}) {
            c.setMinimumSize(c.getPreferredSize());
        }

        photoPreview = new javax.swing.JLabel("No photo", javax.swing.SwingConstants.CENTER);
        photoPreview.setPreferredSize(new java.awt.Dimension(120, 120));
        photoPreview.setMinimumSize(new java.awt.Dimension(120, 120));
        photoPreview.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        uploadButton = new javax.swing.JButton("Upload Photo");

        submitButton = new javax.swing.JButton("Submit");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Title spanning both columns.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        int y = 1;
        y = addRow(form, gbc, y, "First Name:", firstNameField);
        y = addRow(form, gbc, y, "Last Name:", lastNameField);
        y = addRow(form, gbc, y, "Gender:", genderCombo);
        y = addRow(form, gbc, y, "Date of Birth:", dobChooser);
        y = addRow(form, gbc, y, "Age:", ageSpinner);
        y = addRow(form, gbc, y, "Phone:", phoneField);
        y = addRow(form, gbc, y, "Email:", emailField);
        y = addRow(form, gbc, y, "College:", collegeCombo);
        y = addRow(form, gbc, y, "Experience:", experienceField);
        y = addRow(form, gbc, y, "Hobbies:", hobbyScroll);

        // Photo row: label, then preview + upload button side by side.
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new javax.swing.JLabel("Photo:"), gbc);
        javax.swing.JPanel photoPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        photoPanel.add(photoPreview);
        photoPanel.add(uploadButton);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        form.add(photoPanel, gbc);
        y++;

        // Submit button.
        gbc.gridx = 1;
        gbc.gridy = y;
        form.add(submitButton, gbc);

        // Wrap in a scroll pane so the form keeps its proper size (and scrolls
        // instead of collapsing) when the window is short.
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        setLayout(new java.awt.BorderLayout());
        add(scroll, java.awt.BorderLayout.CENTER);
    }

    /** Adds a "label: field" row to the given panel and returns the next row index. */
    private int addRow(javax.swing.JPanel panel, GridBagConstraints gbc, int y,
                       String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new javax.swing.JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return y + 1;
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

    /** Opens a file chooser, loads the image bytes and shows a preview. */
    private void uploadPhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                selectedPhoto = Files.readAllBytes(file.toPath());
                showPhotoPreview(selectedPhoto);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not read the image file.",
                        "Oops!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void showPhotoPreview(byte[] bytes) {
        if (bytes == null) {
            photoPreview.setIcon(null);
            photoPreview.setText("No photo");
            return;
        }
        ImageIcon icon = new ImageIcon(bytes);
        Image scaled = icon.getImage().getScaledInstance(118, 118, Image.SCALE_SMOOTH);
        photoPreview.setIcon(new ImageIcon(scaled));
        photoPreview.setText("");
    }

    /** Computes age from the chosen date of birth and fills the Age spinner. */
    private void updateAgeFromDob() {
        java.util.Date dob = dobChooser.getDate();
        if (dob != null) {
            int age = calculateAge(dob);
            if (age < 0) {
                age = 0;
            }
            ageSpinner.setValue(age);
        }
    }

    static int calculateAge(java.util.Date dob) {
        java.util.Calendar dobCal = java.util.Calendar.getInstance();
        dobCal.setTime(dob);
        java.util.Calendar today = java.util.Calendar.getInstance();
        int years = today.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
        if (today.get(java.util.Calendar.MONTH) < dobCal.get(java.util.Calendar.MONTH)
                || (today.get(java.util.Calendar.MONTH) == dobCal.get(java.util.Calendar.MONTH)
                    && today.get(java.util.Calendar.DAY_OF_MONTH) < dobCal.get(java.util.Calendar.DAY_OF_MONTH))) {
            years--;
        }
        return years;
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        genderCombo.setSelectedIndex(0);
        dobChooser.setDate(null);
        ageSpinner.setValue(18);
        phoneField.setValue(null);
        emailField.setText("");
        collegeCombo.setSelectedIndex(0);
        experienceField.setText("");
        hobbyArea.setText("");
        selectedPhoto = null;
        showPhotoPreview(null);
    }

    private void submitButtonActionPerformed() {
        String errorTitle = "Oops!";
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            java.util.Date dob = dobChooser.getDate();
            int age = (int) ageSpinner.getValue();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String college = (String) collegeCombo.getSelectedItem();
            String experience = experienceField.getText().trim();
            String hobby = hobbyArea.getText().trim();

            // ----- validation -----
            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your first name.",
                        errorTitle, JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your last name.",
                        errorTitle, JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dob == null) {
                JOptionPane.showMessageDialog(this, "Please pick your date of birth.",
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

            User user = new User(firstName, lastName, gender, dob, age,
                    phone, email, college, experience, hobby, selectedPhoto);

            DatabaseConnector.addUser(user);
            System.out.println("Added user into the database");

            // Success dialog with the photo (if any) next to the summary.
            ImageIcon dialogIcon = null;
            if (selectedPhoto != null) {
                Image scaled = new ImageIcon(selectedPhoto).getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                dialogIcon = new ImageIcon(scaled);
            }
            JOptionPane.showMessageDialog(this, user, "Success!",
                    JOptionPane.INFORMATION_MESSAGE, dialogIcon);

            // Navigate to the View panel and refresh its table.
            if (viewPanel != null) {
                viewPanel.populateTable();
            }
            if (cardContainer != null) {
                CardLayout layout = (CardLayout) cardContainer.getLayout();
                layout.show(cardContainer, "view");
            }
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Oops, something went wrong!", "Failed!",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
}
