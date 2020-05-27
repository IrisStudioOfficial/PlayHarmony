package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
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

public class UpdatePlaylistView extends FormTemplate {

    private TextField namePlayList;
    private Playlist playlist;

    public UpdatePlaylistView(Playlist playlist) {
        super("Update Playlist");
        this.playlist = playlist;
    }

    @Override
    protected void initElements() {
        add(namePlayList = TextFactory.textField("New Name Of PlayList"));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Save", e -> updatePlayList())
        };
    }

    private void updatePlayList() {
        User user = Session.getSession().currentUser();

        if(DatabaseController.get().updatePlayList(namePlayList.getText(), playlist, user)){
            NavController.get().popView();
            UserView userView = NavController.get().getCurrentView();
            userView.refresh();
        }else{
            AlertFactory.errorAlert("ERROR! PlayList is already registered", "Please introduce other name.");
        }
    }
}
