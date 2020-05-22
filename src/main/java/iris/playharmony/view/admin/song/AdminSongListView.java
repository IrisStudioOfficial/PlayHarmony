package iris.playharmony.view.admin.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.event.Event;

import java.util.Comparator;

public class AdminSongListView extends ListTemplate<ObservableSong> {

    public AdminSongListView() {
        super("Songs");
        init();
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected void searchCommand() {
        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    @Override
    protected TableView initTable() {
        return TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date"),
                TableFactory.tableColumn("Path", "Path")
        );
    }

    @Override
    protected Pagination initPagination() {
        return TableFactory.pagination(data, table);
    }

    @Override
    protected Comparator<ObservableSong> getComparator() {
        return Comparator.comparing(o -> o.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getObservableData() {
        data = FXCollections.observableArrayList();
        new DatabaseController()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", e -> addSong()),
                ButtonFactory.button("Delete Song", this::removeSong)
        };
    }

    @OnRefresh
    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private static void addSong() {
        NavController.get().pushView(new NewSongView());
    }

    public void removeSong(Event event) {
        event.consume();
        ObservableSong selection = (ObservableSong) table.getSelectionModel().getSelectedItem();
        if (selection == null)
            return;
        if (!new DatabaseController().deleteSong(new Song().setTitle(selection.getTitle())))
            AlertFactory.errorAlert("ERROR! Couldn't remove song", "ERROR! Couldn't remove song");
        refresh();
    }
}