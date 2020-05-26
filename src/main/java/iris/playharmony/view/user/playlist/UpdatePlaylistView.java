package iris.playharmony.view.user.playlist;

import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class UpdatePlaylistView extends FormTemplate {

    private TextField namePlayList;

    public UpdatePlaylistView() {
        super("Update Playlist");
    }

    @Override
    protected void initElements() {
        add(namePlayList = TextFactory.textField("Name Of PlayList"));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Save", e -> updatePlayList())
        };
    }

    private void updatePlayList() {
        User user = Session.getSession().currentUser();
    }
}
