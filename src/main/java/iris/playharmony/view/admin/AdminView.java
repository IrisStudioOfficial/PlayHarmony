package iris.playharmony.view.admin;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.admin.song.AdminSongListView;
import iris.playharmony.view.admin.user.UserListView;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.ButtonFactory;
import javafx.scene.Node;

public class AdminView extends FormTemplate {

    public AdminView() {
        super("Admin");
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Users", event -> NavController.get().pushView(new UserListView())),
                ButtonFactory.button("Songs", event -> NavController.get().pushView(new AdminSongListView()))
        };
    }
}
