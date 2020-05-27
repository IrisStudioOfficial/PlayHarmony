package iris.playharmony.view.template;

import iris.playharmony.model.ObservableSong;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public abstract class ListTemplate<T> extends VBox {

    private Comparator<T> comparator;
    private ObservableList<T> data;

    private TextField searchField;
    private TableView table;
    private Pagination pagination;

    private static final int SPACING = 15;

    public ListTemplate(String title) {
        super(SPACING);
        init(title);
        setPadding(new Insets(SPACING));
    }

    public ListTemplate(String title, Object baseElement) {
        super(SPACING);
        initBaseElement(baseElement);
        init(title);
        setPadding(new Insets(SPACING));
    }

    protected void initBaseElement(Object baseElement) {}

    private void init(String title) {
        initData();
        add(TextFactory.label(title, DefaultStyle.title()));
        beforeTable();
        table();
        afterTable();
    }

    private void initData() {
        comparator = initComparator();
        data = getObservableData();
    }

    private void table() {
        add(TextFactory.searchField(searchField = new TextField(), event -> searchCommand()));
        add(table = TableFactory.table(data, initTable()));
        add(pagination = TableFactory.pagination(data, table));
        add(TemplateHelper.addPaddingTo(bottomButtonPanel()));
    }

    protected abstract Comparator<T> initComparator();

    protected abstract ObservableList<T> getData();

    protected void beforeTable() {}

    protected abstract String fieldToFilter(T fieldData);

    protected abstract TableColumn[] initTable();

    protected abstract Node[] bottomButtonPanel();

    protected void afterTable() {}

    protected void beforeRefresh() {}

    @OnRefresh
    public void refresh() {
        beforeRefresh();
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    protected void add(Node node) {
        getChildren().add(node);
    }

    protected T getSelectedItem() {
        return (T) table.getSelectionModel().getSelectedItem();
    }

    private ObservableList<T> getObservableData() {
        data = FXCollections.observableArrayList();
        ObservableList<T> dataToSort = getData();
        dataToSort.stream().sorted(comparator).forEach(data::add);
        return data;
    }

    private void searchCommand() {
        data = getData();
        if(!searchField.getText().isEmpty()) {
            data = data.filtered(fieldData -> fieldToFilter(fieldData).toLowerCase().contains(searchField.getText().toLowerCase()));
        }
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }
}