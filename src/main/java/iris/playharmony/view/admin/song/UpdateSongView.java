package iris.playharmony.view.admin.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Song;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UpdateSongView extends FormTemplate {

    private Song song;

    private TextField title;
    private TextField author;
    private TextField pathPhoto;
    private File photoFile;
    private TextField dateDay;
    private TextField dateMonth;
    private TextField dateYear;
    private TextField pathFile;
    private File songFile;

    public UpdateSongView(Song song) {
        super("Update Song", song);
    }

    @Override
    protected void initBaseElement(Object song) {
        this.song = (Song) song;
    }

    @Override
    protected void initElements() {
        add(TextFactory.label("Title", DefaultStyle.label()));
        add(title = TextFactory.textField(song.getTitle()));
        add(TextFactory.label("Author", DefaultStyle.label()));
        add(author = TextFactory.textField(""));
        add(TextFactory.label("Day", DefaultStyle.label()));
        add(dateDay = TextFactory.textField(""));
        add(TextFactory.label("Month", DefaultStyle.label()));
        add(dateMonth = TextFactory.textField(""));
        add(TextFactory.label("Year", DefaultStyle.label()));
        add(dateYear = TextFactory.textField(""));
        add(TextFactory.label("Photo", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(pathPhoto, "Upload Image", event -> uploadImage(pathPhoto)));
        add(TextFactory.label("Song File", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(pathFile, "Upload Song", event -> uploadSong(pathFile)));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[]{
                ButtonFactory.button("Update Song", event -> updateSong())
        };
    }

    private void uploadImage(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Search Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        photoFile = fileChooser.showOpenDialog(new Stage());
        textField.setText((photoFile == null) ? "" : photoFile.getAbsolutePath());
    }

    private void uploadSong(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Search Song");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        songFile = fileChooser.showOpenDialog(new Stage());
        textField.setText((songFile == null) ? "" : songFile.getAbsolutePath());
    }

    private void updateSong() {
        Song song = new Song(title.getText(), author.getText(), photoFile.toString(), dateDay.getText() + "-" + dateMonth.getText() + "-" + dateYear.getText(), pathFile.getText());
        if (new DatabaseController().addSong(song)) {
            NavController.get().popView();
        } else {
            AlertFactory.errorAlert("ERROR! Song is already registered", "ERROR! Song is already registered");
        }
    }
}