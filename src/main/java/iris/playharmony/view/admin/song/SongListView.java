package iris.playharmony.view.admin.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public abstract class SongListView extends VBox {

    ObservableList<ObservableSong> data;

    protected TableView songsTable;
    protected Pagination pagination;
    protected TextField searchField = new TextField();

    protected static final int SPACING = 15;

    public SongListView() {
        super(SPACING);
        this.data = getSongs(Comparator.comparing(o -> o.title().get()));
        initElements();
        setPadding(new Insets(SPACING));
    }

    protected abstract void initElements();

    protected ObservableList<ObservableSong> getSongs(Comparator<ObservableSong> comparator) {
        data = FXCollections.observableArrayList();
        new DatabaseController()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    protected Node searchForm() {

        HBox searchRow = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        searchField.setOnAction(event -> searchCommand());
        searchRow.getChildren().add(region);
        searchRow.getChildren().add(searchField);
        searchRow.getChildren().add(ButtonFactory.button("Search", event -> searchCommand()));

        return searchRow;
    }

    protected void searchCommand() {
        refresh();

        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, songsTable);
        TableFactory.updatePagination(data, songsTable, pagination);
    }

    protected Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    @OnRefresh
    public void refresh() {
        data = getSongs(Comparator.comparing(o -> o.title().get()));
        TableFactory.updateTable(data, songsTable);
        TableFactory.updatePagination(data, songsTable, pagination);
    }

    protected void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        Song song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }
}
