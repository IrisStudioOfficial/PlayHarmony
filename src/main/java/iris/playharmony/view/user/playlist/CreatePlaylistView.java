package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.user.UserView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class CreatePlaylistView extends FormTemplate {

    private TextField namePlayList;

    public CreatePlaylistView() {
        super("Create Playlist");
    }

    @Override
    protected void initElements() {
        add(namePlayList = TextFactory.textField("Name Of PlayList"));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Create", e -> createPlayList())
        };
    }

    private void createPlayList() {
        Playlist playlist = new Playlist(namePlayList.getText());
        User user = Session.getSession().currentUser();

        if(new DatabaseController().addPlayList(playlist, user)) {
            NavController.get().popView();
            UserView userView = NavController.get().getCurrentView();
            userView.refresh();
        } else {
            AlertFactory.errorAlert("ERROR! PlayList is already registered", "Please introduce other name.");
        }
    }
}
