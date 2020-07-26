package app.ui;

import app.query.Query;
import app.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * A UI panel for entering new queries.
 */
public class NewQueryPanel extends JPanel {
    private final JTextField newQuery = new JTextField(10);
    private final JPanel colorSetter;
    private final ApplicationMainPanel app;
    private final Random random;

    public NewQueryPanel(ApplicationMainPanel app) {
        this.app = app;
        this.colorSetter = new JPanel();
        this.random = new Random();
        buildGUI();
    }

    private void buildGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel enterSearchLabel = new JLabel("Enter Search: ");
        enterSearchLabel.setLabelFor(newQuery);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.gridy = 0;
        c.gridx = 0;
        add(enterSearchLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        newQuery.setMaximumSize(new Dimension(200, 20));
        c.gridx = 1;
        add(newQuery, c);

        createRigidArea();

        JLabel colorLabel = new JLabel("Select Color: ");
        colorSetter.setBackground(getRandomColor());

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.gridy = 1;
        c.gridx = 0;
        add(colorLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        colorSetter.setMaximumSize(new Dimension(200, 20));
        add(colorSetter, c);

        createRigidArea();

        JButton addQueryButton = new JButton("Add New Search");
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridwidth = 2;
        c.gridy = GridBagConstraints.RELATIVE;
        add(addQueryButton, c);

        Util.addTittledBorderToPanel(this, "New Search");
        addQueryButtonActionListener(addQueryButton);
        app.getRootPane().setDefaultButton(addQueryButton);
        addColorSetterMouseListeners(app);
    }

    private void addQueryButtonActionListener(JButton addQueryButton) {
        addQueryButton.addActionListener(e -> {
            if (!newQuery.getText().equals("")) {
                action_AddNewQuery(newQuery.getText().toLowerCase());
                newQuery.setText("");
            }
        });
    }

    private void action_AddNewQuery(String newQuery) {
        Query query = new Query(newQuery, colorSetter.getBackground(), app.getMap());
        app.addQuery(query);
        colorSetter.setBackground(getRandomColor());
    }

    public Color getRandomColor() {
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    private void addColorSetterMouseListeners(ApplicationMainPanel app) {
        colorSetter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color newColor = JColorChooser.showDialog(app, "Choose Background Color", colorSetter.getBackground());
                    if (newColor != null) {
                        colorSetter.setBackground(newColor);
                    }
                }
            }
        });
    }

    private void createRigidArea() {
        add(Box.createRigidArea(new Dimension(5, 5)));
    }
}
