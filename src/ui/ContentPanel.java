package ui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;
import util.Util;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    private final JSplitPane querySplitPane;
    private JPanel newQueryPanel;
    private JPanel existingQueriesPanel;
    private JMapViewer mapViewer;
    private Application application;

    public ContentPanel(Application application) {
        init(application);

        JPanel layerPanelContainer = buildExistingQueriesPanel();
        querySplitPane = buildQuerySplitPane(layerPanelContainer);

        add(buildTopLevelSplitPane(), "Center");
        revalidate();
        repaint();
    }

    private void init(Application application) {
        this.application = application;

        mapViewer = new JMapViewer();
        mapViewer.setMinimumSize(new Dimension(100, 50));
        setLayout(new BorderLayout());
        newQueryPanel = new NewQueryPanel(application);
    }

    private JPanel buildExistingQueriesPanel() {
        JPanel layerPanelContainer = new JPanel();
        existingQueriesPanel = new JPanel();
        existingQueriesPanel.setLayout(new BoxLayout(existingQueriesPanel, BoxLayout.Y_AXIS));

        layerPanelContainer.setLayout(new BorderLayout());
        Util.addTittledBorderToPanel(layerPanelContainer, "Current Queries");
        layerPanelContainer.add(existingQueriesPanel, BorderLayout.NORTH);

        return layerPanelContainer;
    }

    private JSplitPane buildQuerySplitPane(JPanel layerPanelContainer) {
        JSplitPane querySplitPane = new JSplitPane(0);
        querySplitPane.setDividerLocation(150);
        querySplitPane.setTopComponent(newQueryPanel);
        querySplitPane.setBottomComponent(layerPanelContainer);

        return querySplitPane;
    }

    private JSplitPane buildTopLevelSplitPane() {
        JSplitPane topLevelSplitPane = new JSplitPane(1);
        topLevelSplitPane.setDividerLocation(150);
        topLevelSplitPane.setLeftComponent(querySplitPane);
        topLevelSplitPane.setRightComponent(mapViewer);

        return topLevelSplitPane;
    }

    public void addQueryToUI(Query query) {
        ActiveQueryPanel newQueryPanel = new ActiveQueryPanel(query, application, existingQueriesPanel);
        existingQueriesPanel.add(newQueryPanel);
        validate();
    }

    public JMapViewer getMapViewer() {
        return mapViewer;
    }
}
