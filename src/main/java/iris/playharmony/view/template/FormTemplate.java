package iris.playharmony.view.template;

import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class FormTemplate extends VBox {

    private static int SPACING = 15;

    public FormTemplate(String title) {
        super(SPACING);
        add(TextFactory.label(title, DefaultStyle.title()));
        initElements();
        add(addPaddingTo(bottomButtonPanel()));
        setPadding(new Insets(SPACING));
    }

    private HBox addPaddingTo(Node[] nodes) {
        Node[] nodesWithPadding = new Node[2 * nodes.length - 1];
        for (int i = 0; i < nodesWithPadding.length; i++) {
            nodesWithPadding[i] = i%2 == 0 ? nodes[i/2] : getNewPadding();
        }
        return new HBox(nodesWithPadding);
    }

    private Region getNewPadding() {
        Region padding = new Region();
        padding.setPrefHeight(5);
        return padding;
    }

    protected abstract void initElements();

    protected abstract Node[] bottomButtonPanel();

    protected void add(Node node) {
        getChildren().add(node);
    }

    public abstract void refresh();
}
