package app.ui;

import app.query.Query;

import javax.swing.*;
import java.awt.*;

public class ActiveQueryPanel extends JPanel {
    private final Query query;
    private final ApplicationMainPanel applicationMainPanel;
    private final JPanel owner;

    public ActiveQueryPanel(Query query, ApplicationMainPanel applicationMainPanel, JPanel owner) {
        this.query = query;
        this.applicationMainPanel = applicationMainPanel;
        this.owner = owner;
        buildGUI();
    }

    private void buildGUI() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        add(buildQueryColorPanel(), c);

        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(buildVisibilityCheckBox(), c);

        add(buildRemoveButton());
    }

    private JPanel buildQueryColorPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        return colorPanel;
    }

    private JCheckBox buildVisibilityCheckBox() {
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        checkbox.addActionListener(e -> applicationMainPanel.updateVisibility());
        query.setCheckBox(checkbox);
        return checkbox;
    }

    private JButton buildRemoveButton() {
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(40, 20));
        removeButton.addActionListener(e -> {
            applicationMainPanel.terminateQuery(query);
            owner.remove(this);
            owner.revalidate();
        });

        return removeButton;
    }
}
