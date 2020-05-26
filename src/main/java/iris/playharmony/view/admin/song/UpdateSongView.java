package iris.playharmony.view.admin.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Song;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.util.TypeUtils;
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
    private TextField dateDay;
    private TextField dateMonth;
    private TextField dateYear;
    private TextField pathPhoto;
    private TextField pathFile;

    private File photoFile;
    private File songFile;

    public UpdateSongView(Object baseElement) {
        super("Update Song", baseElement);
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
        add(author = TextFactory.textField(song.getAuthor()));
        String[] date = song.getDate().split("-");
        add(TextFactory.label("Day", DefaultStyle.label()));
        add(dateDay = TextFactory.textField(date[0]));
        add(TextFactory.label("Month", DefaultStyle.label()));
        add(dateMonth = TextFactory.textField(date[1]));
        add(TextFactory.label("Year", DefaultStyle.label()));
        add(dateYear = TextFactory.textField(date[2]));
        pathPhoto = new TextField(song.getPhoto());
        pathFile = new TextField(song.getPathFile());
        add(TextFactory.label("Photo", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(pathPhoto, "Upload Image", event -> uploadImage(pathPhoto)));
        add(TextFactory.label("Song File", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(pathFile, "Upload Song", event -> uploadSong(pathFile)));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
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
        if(allFieldsAreSet()) {
            Song song = new Song(title.getText(), author.getText(), pathPhoto.getText(), dateDay.getText() + "-" + dateMonth.getText() + "-" + dateYear.getText(), pathFile.getText());
            if(DatabaseController.get().updateSong(song, this.song.getTitle())) {
                NavController.get().popView();
                TypeUtils.callAnnotatedMethod(NavController.get().getCurrentView(), OnRefresh.class);
            } else {
                AlertFactory.errorAlert("ERROR! Song is already registered", "ERROR! Song is already registered");
            }
        } else {
            AlertFactory.errorAlert("ERROR! Song is incorrect", "ERROR! All required fields must be filled");
        }
    }

    private boolean allFieldsAreSet() {
        return !isEmpty(title) && !isEmpty(author) && isNumber(dateDay) && isNumber(dateMonth) && isNumber(dateYear) && !isEmpty(pathPhoto) && !isEmpty(pathFile);
    }

    private boolean isEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }

    private boolean isNumber(TextField textField) {
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}