package iris.playharmony.view.playlist;

import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.File;

public class AddSongToPlaylistView extends VBox implements View {

    private static int SPACING = 15;

    private Playlist playlist;
    private TableView songsTable;


    public AddSongToPlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        title("Add Song to Playlist");

        songsTable = table(getSongs(),
                tableColumnPhoto("Photo", "photo"),
                tableColumn("Title", "title"),
                tableColumn("Author", "author"),
                tableColumn("Date", "date")
        );

        button("Add Songs", event -> addSongs());
    }

    private ObservableList<ObservableSong> getSongs() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        mockSongs()
                .stream()
                .forEach(data::add);
        return data;
    }

    private void addSongs() {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            playlist.addSong(null);
            updateTable(getSongs(), songsTable);
        }
    }
    
    private ObservableList<ObservableSong> mockSongs() {
        ObservableList<ObservableSong> users = FXCollections.observableArrayList();
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        return users;
    }
}
