import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
        frame.setLayout(new BorderLayout());    // BorderLayout for top & center separation

        JPanel panel = new JPanel();

        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(600, 30));
        input.setForeground(Color.GRAY);
        input.setText("Enter new Task");

        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(input.getText().equals("Enter new Task")) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(input.getText().isEmpty()) {
                    input.setForeground(Color.GRAY);
                    input.setText("Enter new Task");
                }
            }
        });

        JButton button = new JButton("Add");
        panel.add(input);
        panel.add(button);

        // Main panel for tasks (scrollable)
        JPanel middleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel innerBox = new JPanel();

        innerBox.setPreferredSize(new Dimension(750, 400));
        innerBox.setLayout(new BorderLayout());

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        innerBox.add(scrollPane, BorderLayout.CENTER);
        middleContainer.add(innerBox);

        button.addActionListener(e -> {
            String task = input.getText().trim();
            if (!task.isEmpty() && !task.equals("Enter new Task")) {
                Combine c = new Combine(task, new JButton("Delete"), new JButton("↓"), new JButton("↑"));

                JPanel taskRow = new JPanel();
                taskRow.setLayout(new BoxLayout(taskRow, BoxLayout.X_AXIS));
                taskRow.setMaximumSize(new Dimension(550, 40));

                // JLabel for task text
                JLabel label = new JLabel(task);

                // Wrap label inside scroll pane
                JScrollPane textScroll = new JScrollPane(label);
                textScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                textScroll.setPreferredSize(new Dimension(300, 30)); // width limit before scroll appears
                textScroll.setBorder(null); // cleaner look

                // Add everything to task row
                taskRow.add(textScroll);
                taskRow.add(Box.createHorizontalStrut(10)); // space
                taskRow.add(c.del);
                taskRow.add(c.morePrioritize);
                taskRow.add(c.lessPrioritize);

                taskPanel.add(taskRow);
//                taskPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                taskPanel.revalidate();
                taskPanel.repaint();

                // delete button action
                c.del.addActionListener(event -> {
                    taskPanel.remove(taskRow);
                    taskPanel.revalidate();
                    taskPanel.repaint();
                });

                System.out.println("Task added: " + task);
                input.setText("Enter new Task");
                input.setForeground(Color.GRAY);
            }
        });


        // Add both sections to frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(middleContainer, BorderLayout.CENTER);

        frame.setVisible(true);
        // Remove focus from text field at startup
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }
}