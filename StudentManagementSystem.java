import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {
    private final JTextField rollField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField branchField = new JTextField();
    private final JTextField searchField = new JTextField();
    private final JLabel statusLabel = new JLabel("Ready", SwingConstants.LEFT);
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    private final List<Student> students = new ArrayList<>();

    public StudentManagementSystem() {
        configureLookAndFeel();

        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(245, 247, 250));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createContent(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Management Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(22, 33, 62));

        JLabel subtitle = new JLabel("Add, update, search, and manage student records from one screen.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(90, 98, 118));

        JPanel text = new JPanel(new GridBagLayout());
        text.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        text.add(title, constraints);
        constraints.gridy = 1;
        constraints.insets = new Insets(4, 0, 0, 0);
        text.add(subtitle, constraints);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);

        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();

        content.add(formPanel, BorderLayout.WEST);
        content.add(tablePanel, BorderLayout.CENTER);
        return content;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        panel.setPreferredSize(new Dimension(330, 0));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 0, 8, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.weightx = 1.0;

        panel.add(createFieldLabel("Roll No"), constraints);
        constraints.gridy = 1;
        panel.add(rollField, constraints);

        constraints.gridy = 2;
        panel.add(createFieldLabel("Name"), constraints);
        constraints.gridy = 3;
        panel.add(nameField, constraints);

        constraints.gridy = 4;
        panel.add(createFieldLabel("Branch"), constraints);
        constraints.gridy = 5;
        panel.add(branchField, constraints);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);

        JButton addButton = createActionButton("Add Student", new Color(18, 140, 126));
        JButton updateButton = createActionButton("Update Student", new Color(44, 88, 188));
        JButton deleteButton = createActionButton("Delete Student", new Color(208, 66, 66));
        JButton clearButton = createActionButton("Clear Form", new Color(105, 113, 132));

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.weightx = 1.0;
        buttonConstraints.insets = new Insets(0, 0, 10, 0);
        buttons.add(addButton, buttonConstraints);
        buttonConstraints.gridy = 1;
        buttons.add(updateButton, buttonConstraints);
        buttonConstraints.gridy = 2;
        buttons.add(deleteButton, buttonConstraints);
        buttonConstraints.gridy = 3;
        buttons.add(clearButton, buttonConstraints);

        constraints.gridy = 6;
        constraints.insets = new Insets(18, 0, 0, 0);
        panel.add(buttons, constraints);

        styleTextField(rollField);
        styleTextField(nameField);
        styleTextField(branchField);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = createFieldLabel("Search");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        styleTextField(searchField);
        searchField.setToolTipText("Search by roll no, name, or branch");

        model = new DefaultTableModel(new Object[]{"Roll No", "Name", "Branch"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setSelectionBackground(new Color(214, 228, 255));
        table.setSelectionForeground(new Color(22, 33, 62));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearchFilter();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedRowIntoForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(90, 98, 118));
        footer.add(statusLabel, BorderLayout.WEST);
        return footer;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(22, 33, 62));
        return label;
    }

    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(207, 214, 226)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void addStudent() {
        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        if (findStudentIndex(student.getRollNo()) != -1) {
            showMessage("A student with this roll number already exists.");
            return;
        }

        students.add(student);
        model.addRow(new Object[]{student.getRollNo(), student.getName(), student.getBranch()});
        clearForm();
        showStatus("Student added successfully.");
    }

    private void updateStudent() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow == -1) {
            showMessage("Select a student in the table first.");
            return;
        }

        Student student = readStudentFromForm();
        if (student == null) {
            return;
        }

        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
        int existingIndex = findStudentIndex(student.getRollNo());
        if (existingIndex != -1 && existingIndex != selectedModelRow) {
            showMessage("That roll number is already assigned to another student.");
            return;
        }

        students.set(selectedModelRow, student);
        model.setValueAt(student.getRollNo(), selectedModelRow, 0);
        model.setValueAt(student.getName(), selectedModelRow, 1);
        model.setValueAt(student.getBranch(), selectedModelRow, 2);
        clearForm();
        showStatus("Student record updated.");
    }

    private void deleteStudent() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow == -1) {
            showMessage("Select a student in the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete the selected student record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
        students.remove(selectedModelRow);
        model.removeRow(selectedModelRow);
        clearForm();
        showStatus("Student record deleted.");
    }

    private Student readStudentFromForm() {
        String rollText = rollField.getText().trim();
        String name = nameField.getText().trim();
        String branch = branchField.getText().trim();

        if (rollText.isEmpty() || name.isEmpty() || branch.isEmpty()) {
            showMessage("All fields are required.");
            return null;
        }

        try {
            int rollNo = Integer.parseInt(rollText);
            if (rollNo <= 0) {
                showMessage("Roll number must be greater than zero.");
                return null;
            }
            return new Student(rollNo, name, branch);
        } catch (NumberFormatException ex) {
            showMessage("Roll number must be a valid integer.");
            return null;
        }
    }

    private int findStudentIndex(int rollNo) {
        for (int index = 0; index < students.size(); index++) {
            if (students.get(index).getRollNo() == rollNo) {
                return index;
            }
        }
        return -1;
    }

    private void loadSelectedRowIntoForm() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow == -1) {
            return;
        }

        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
        rollField.setText(model.getValueAt(selectedModelRow, 0).toString());
        nameField.setText(model.getValueAt(selectedModelRow, 1).toString());
        branchField.setText(model.getValueAt(selectedModelRow, 2).toString());
        showStatus("Editing selected student.");
    }

    private void applySearchFilter() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int column = 0; column < entry.getValueCount(); column++) {
                    String cell = entry.getValue(column).toString().toLowerCase();
                    if (cell.contains(query)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void clearForm() {
        rollField.setText("");
        nameField.setText("");
        branchField.setText("");
        table.clearSelection();
        showStatus("Ready");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
    }

    private void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}