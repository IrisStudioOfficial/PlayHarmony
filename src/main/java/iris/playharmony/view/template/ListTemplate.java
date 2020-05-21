package iris.playharmony.view.template;

import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class ListTemplate extends VBox {

    protected TableView table;
    protected Pagination pagination;

    private static int SPACING = 15;

    public ListTemplate(String title) {
        super(SPACING);
        add(TextFactory.label(title, DefaultStyle.title()));
        initElements();
        initSearchForm();
        initTable();
        initPagination();
        add(addPaddingTo(bottomButtonPanel()));
        setPadding(new Insets(SPACING));
    }

    protected abstract void initElements();

    protected abstract void initSearchForm();

    protected abstract void initTable();

    protected abstract void initPagination();

    protected abstract ObservableList<?> getObservableData();

    private HBox addPaddingTo(Node[] nodes) {
        HBox hBox = new HBox();
        for (int i = 0; i < 2 * nodes.length - 1; i++) {
            hBox.getChildren().add(i%2 == 0 ? nodes[i/2] : getNewPadding());
        }
        return hBox;
    }

    private Region getNewPadding() {
        Region padding = new Region();
        padding.setPrefWidth(5);
        return padding;
    }

    protected abstract Node[] bottomButtonPanel();

    protected void add(Node node) {
        getChildren().add(node);
    }

    public abstract void refresh();
}
