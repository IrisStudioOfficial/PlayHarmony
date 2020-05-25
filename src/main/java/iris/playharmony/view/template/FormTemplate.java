package iris.playharmony.view.template;

import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public abstract class FormTemplate extends VBox {

    private static int SPACING = 15;

    public FormTemplate(String title) {
        super(SPACING);
        add(TextFactory.label(title, DefaultStyle.title()));
        initElements();
        add(TemplateHelper.addPaddingTo(bottomButtonPanel()));
        setPadding(new Insets(SPACING));
    }

    protected abstract void initElements();

    protected abstract Node[] bottomButtonPanel();

    protected void add(Node node) {
        getChildren().add(node);
    }

    @OnRefresh
    public void refresh() {

    }
}
