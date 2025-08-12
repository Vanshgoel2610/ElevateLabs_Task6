import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TODO {
    static class Combine {
        String str;
        JButton del;
        JButton lessPrioritize, morePrioritize;
        Combine(String str, JButton del, JButton lessPrioritize, JButton morePrioritize) {
            this.str = str;
            this.del = del;
            this.lessPrioritize = lessPrioritize;
            this.morePrioritize = morePrioritize;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ToDo App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Define colors and font
        Color backgroundColor = new Color(45, 45, 45); // Dark background
        Color foregroundColor = new Color(240, 240, 240); // Light text color
        Color buttonColor = new Color(60, 60, 60);
        Color hoverColor = new Color(75, 75, 75);
        Color deleteButtonColor = new Color(200, 70, 70); // Red for delete button
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);

        // --- Top Panel for input ---
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(600, 40)); // Increased height
        input.setFont(mainFont);
        input.setBackground(buttonColor);
        input.setForeground(foregroundColor);
        input.setCaretColor(foregroundColor);
        input.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Inner padding
        input.setText("Enter new Task");
        input.setForeground(new Color(150, 150, 150));

        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (input.getText().equals("Enter new Task")) {
                    input.setText("");
                    input.setForeground(foregroundColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (input.getText().isEmpty()) {
                    input.setForeground(new Color(150, 150, 150));
                    input.setText("Enter new Task");
                }
            }
        });

        JButton button = new JButton("Add");
        button.setPreferredSize(new Dimension(100, 40));
        styleButton(button, buttonColor, hoverColor, mainFont, foregroundColor);

        panel.add(input);
        panel.add(button);

        // --- Main panel for tasks (scrollable) ---
        JPanel middleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        middleContainer.setBackground(backgroundColor);

        JPanel innerBox = new JPanel();
        innerBox.setPreferredSize(new Dimension(750, 500)); // Increased height
        innerBox.setLayout(new BorderLayout());
        innerBox.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60))); // Border for the box

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(backgroundColor);

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // No border on the scroll pane

        innerBox.add(scrollPane, BorderLayout.CENTER);
        middleContainer.add(innerBox);

        button.addActionListener(e -> {
            String task = input.getText().trim();
            if (!task.isEmpty() && !task.equals("Enter new Task")) {
                JButton delButton = new JButton("X");
                JButton lessPrioritizeButton = new JButton("↓");
                JButton morePrioritizeButton = new JButton("↑");

                Combine c = new Combine(task, delButton, lessPrioritizeButton, morePrioritizeButton);

                JPanel taskRow = new JPanel();
                taskRow.setLayout(new BoxLayout(taskRow, BoxLayout.X_AXIS));
                taskRow.setBackground(buttonColor);
                taskRow.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(backgroundColor, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                Dimension taskRowSize = new Dimension(700, 40); // Adjusted size
                taskRow.setMinimumSize(taskRowSize);
                taskRow.setPreferredSize(taskRowSize);
                taskRow.setMaximumSize(taskRowSize);

                // JLabel for task text
                JLabel label = new JLabel(task);
                label.setFont(mainFont);
                label.setForeground(foregroundColor);

                // Add everything to task row
                taskRow.add(label);
                taskRow.add(Box.createHorizontalGlue()); // Pushes buttons to the right
                taskRow.add(c.morePrioritize);
                taskRow.add(Box.createHorizontalStrut(5));
                taskRow.add(c.lessPrioritize);
                taskRow.add(Box.createHorizontalStrut(5));
                taskRow.add(c.del);

                // Style buttons for the task row
                styleButton(c.del, deleteButtonColor, deleteButtonColor.darker(), mainFont, Color.WHITE);
                styleButton(c.morePrioritize, buttonColor, hoverColor, mainFont, foregroundColor);
                styleButton(c.lessPrioritize, buttonColor, hoverColor, mainFont, foregroundColor);

                taskPanel.add(taskRow);
                taskPanel.revalidate();
                taskPanel.repaint();

                // delete button action
                c.del.addActionListener(event -> {
                    taskPanel.remove(taskRow);
                    taskPanel.revalidate();
                    taskPanel.repaint();
                });

                // ↑ Move Up
                c.morePrioritize.addActionListener(event -> {
                    int index = getIndexOfComponent(taskPanel, taskRow);
                    if (index > 0) {
                        taskPanel.remove(taskRow);
                        taskPanel.add(taskRow, index - 1);
                        taskPanel.revalidate();
                        taskPanel.repaint();
                    }
                });

                // ↓ Move Down
                c.lessPrioritize.addActionListener(event -> {
                    int index = getIndexOfComponent(taskPanel, taskRow);
                    if (index < taskPanel.getComponentCount() - 1) {
                        taskPanel.remove(taskRow);
                        taskPanel.add(taskRow, index + 1);
                        taskPanel.revalidate();
                        taskPanel.repaint();
                    }
                });

                input.setText("Enter new Task");
                input.setForeground(new Color(150, 150, 150));
            }
        });

        frame.add(panel, BorderLayout.NORTH);
        frame.add(middleContainer, BorderLayout.CENTER);
        frame.getContentPane().setBackground(backgroundColor);

        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    // Utility method to find component index
    private static int getIndexOfComponent(JPanel panel, Component comp) {
        Component[] comps = panel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == comp) return i;
        }
        return -1;
    }

    // New helper method for button styling
    private static void styleButton(JButton button, Color bgColor, Color hoverColor, Font font, Color fgColor) {
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
}