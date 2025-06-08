package finals;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.io.*;

public class activity extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchfield;
    private JTextField nameField;
    private JTextField passwordField;
    private JTextField studentIdField;
    private JTextField yearField;
    private JTextField courseField;
    private JTextField scholarshipField;
    private JTable table;

    private mayo dbOps = new mayo();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                activity frame = new activity();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public activity() {
        setBackground(new Color(255, 128, 192));
        setTitle("Scholarship Evaluation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 650);
        contentPane = new JPanel();
        contentPane.setBackground(Color.PINK);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(38, 155, 42, 20);
        contentPane.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(90, 150, 225, 30);
        contentPane.add(nameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(32, 207, 56, 20);
        contentPane.add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setBounds(98, 202, 217, 30);
        contentPane.add(passwordField);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(347, 155, 68, 20);
        contentPane.add(studentIdLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(416, 150, 107, 30);
        contentPane.add(studentIdField);

        JLabel yearLabel = new JLabel("Year Level:");
        yearLabel.setBounds(347, 259, 68, 20);
        contentPane.add(yearLabel);

        yearField = new JTextField();
        yearField.setBounds(416, 254, 113, 30);
        contentPane.add(yearField);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setBounds(359, 207, 56, 20);
        contentPane.add(courseLabel);

        courseField = new JTextField();
        courseField.setBounds(416, 202, 107, 30);
        contentPane.add(courseField);

        JLabel scholarshipLabel = new JLabel("Scholarship:");
        scholarshipLabel.setBounds(30, 259, 77, 20);
        contentPane.add(scholarshipLabel);

        scholarshipField = new JTextField();
        scholarshipField.setBounds(117, 254, 222, 30);
        contentPane.add(scholarshipField);

        searchfield = new JTextField();
        searchfield.setBounds(531, 60, 200, 30);
        contentPane.add(searchfield);

        JButton addButton = new JButton("ADD");
        addButton.setBounds(90, 302, 100, 30);
        contentPane.add(addButton);

        JButton updateButton = new JButton("UPDATE");
        updateButton.setBounds(200, 302, 100, 30);
        contentPane.add(updateButton);

        JButton deleteButton = new JButton("DELETE");
        deleteButton.setBounds(310, 302, 100, 30);
        contentPane.add(deleteButton);

        JButton printButton = new JButton("PRINT");
        printButton.setBounds(420, 302, 100, 30);
        contentPane.add(printButton);

        JButton searchButton = new JButton("SEARCH");
        searchButton.setBounds(632, 109, 100, 30);
        contentPane.add(searchButton);

        table = new JTable(new DefaultTableModel(
            new Object[]{"Student ID", "Name", "Password", "Year Level", "Course", "Scholarship"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(43, 362, 688, 240);
        contentPane.add(scrollPane);

        dbOps.loadStudentsIntoTable(table);

        addButton.addActionListener(e -> {
            String id = studentIdField.getText().trim();
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            String year = yearField.getText().trim();
            String course = courseField.getText().trim();
            String scholarship = scholarshipField.getText().trim();

            if (!id.isEmpty() && !name.isEmpty() && !password.isEmpty() 
                && !year.isEmpty() && !course.isEmpty() && !scholarship.isEmpty()) {
                dbOps.addStudent(id, name, password, year, course, scholarship);
                JOptionPane.showMessageDialog(null, "Added successfully!");
                clearFields();
                dbOps.loadStudentsIntoTable(table);
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = studentIdField.getText().trim();
                String name = nameField.getText().trim();
                String password = passwordField.getText().trim();
                String year = yearField.getText().trim();
                String course = courseField.getText().trim();
                String scholarship = scholarshipField.getText().trim();

                if (!id.isEmpty() && !name.isEmpty() && !password.isEmpty()
                    && !year.isEmpty() && !course.isEmpty() && !scholarship.isEmpty()) {

                    dbOps.updateStudent(id, name, password, year, course, scholarship);
                    JOptionPane.showMessageDialog(null, "Updated successfully!");
                    clearFields();
                    dbOps.loadStudentsIntoTable(table);
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No student selected to update.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) table.getValueAt(selectedRow, 0);
                dbOps.deleteStudent(id);
                JOptionPane.showMessageDialog(null, "Deleted successfully!");
                clearFields();
                dbOps.loadStudentsIntoTable(table);
            } else {
                JOptionPane.showMessageDialog(null, "No student selected to delete.");
            }
        });

        searchButton.addActionListener(e -> {
            String id = searchfield.getText().trim();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            if (!id.isEmpty()) {
                dbOps.searchStudent(id, table, nameField, passwordField, yearField, courseField, scholarshipField);
            } else {
                dbOps.loadStudentsIntoTable(table);
            }
        });

        printButton.addActionListener(e -> {
            try {
                File folder = new File("Student_Records");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                File file = new File(folder, "All_Student_Records.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int rowCount = model.getRowCount();
                int colCount = model.getColumnCount();

                writer.write("All Student Records\n");
                writer.write("=====================\n\n");

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < colCount; j++) {
                        writer.write(model.getColumnName(j) + ": " + model.getValueAt(i, j) + "\n");
                    }
                    writer.write("------------------------\n");
                }

                writer.close();

                JTextArea textArea = new JTextArea();
                textArea.read(new FileReader(file), null);
                boolean done = textArea.print();
                if (done) {
                    JOptionPane.showMessageDialog(null, "Student records printed successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Printing was cancelled.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while printing.");
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                studentIdField.setText((String) table.getValueAt(selectedRow, 0));
                nameField.setText((String) table.getValueAt(selectedRow, 1));
                passwordField.setText((String) table.getValueAt(selectedRow, 2));
                yearField.setText((String) table.getValueAt(selectedRow, 3));
                courseField.setText((String) table.getValueAt(selectedRow, 4));
                scholarshipField.setText((String) table.getValueAt(selectedRow, 5));
            }
        });
    }

    private void clearFields() {
        nameField.setText("");
        passwordField.setText("");
        studentIdField.setText("");
        yearField.setText("");
        courseField.setText("");
        scholarshipField.setText("");
    }
}
