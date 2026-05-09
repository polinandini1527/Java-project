import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    int rollNo;
    String name;
    String branch;

    Student(int rollNo, String name, String branch) {
        this.rollNo = rollNo;
        this.name = name;
        this.branch = branch;
    }
}

public class StudentManagementSystem {

    JFrame frame;
    JTextField rollField, nameField, branchField;
    JTable table;
    DefaultTableModel model;
    ArrayList<Student> students = new ArrayList<>();

    public StudentManagementSystem() {

        frame = new JFrame("Smart Student Management System");
        frame.setSize(800, 550);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel rollLabel = new JLabel("Roll No:");
        rollLabel.setBounds(20, 20, 100, 25);
        frame.add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(120, 20, 150, 25);
        frame.add(rollField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 60, 100, 25);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 60, 150, 25);
        frame.add(nameField);

        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setBounds(20, 100, 100, 25);
        frame.add(branchLabel);

        branchField = new JTextField();
        branchField.setBounds(120, 100, 150, 25);
        frame.add(branchField);

        JButton addButton = new JButton("Add Student");
        addButton.setBounds(320, 20, 150, 30);
        frame.add(addButton);

        JButton updateButton = new JButton("Update Student");
        updateButton.setBounds(320, 60, 150, 30);
        frame.add(updateButton);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBounds(320, 100, 150, 30);
        frame.add(deleteButton);

        model = new DefaultTableModel();
        model.addColumn("Roll No");
        model.addColumn("Name");
        model.addColumn("Branch");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 160, 740, 300);
        frame.add(scrollPane);

        // ADD STUDENT
        addButton.addActionListener(e -> {
            if (rollField.getText().isEmpty() ||
                nameField.getText().isEmpty() ||
                branchField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(frame, "All fields are required!");
                return;
            }

            int roll = Integer.parseInt(rollField.getText());
            String name = nameField.getText();
            String branch = branchField.getText();

            students.add(new Student(roll, name, branch));
            model.addRow(new Object[]{roll, name, branch});

            clearFields();
        });

        // UPDATE STUDENT
        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a student to update");
                return;
            }

            int roll = Integer.parseInt(rollField.getText());
            String name = nameField.getText();
            String branch = branchField.getText();

            model.setValueAt(roll, row, 0);
            model.setValueAt(name, row, 1);
            model.setValueAt(branch, row, 2);

            clearFields();
        });

        // DELETE STUDENT
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a student to delete");
                return;
            }

            model.removeRow(row);
            clearFields();
        });

        // CLICK ROW → LOAD DATA
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                rollField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                branchField.setText(model.getValueAt(row, 2).toString());
            }
        });

        frame.setVisible(true);
    }

    void clearFields() {
        rollField.setText("");
        nameField.setText("");
        branchField.setText("");
    }

    public static void main(String[] args) {
        new StudentManagementSystem();
    }
}