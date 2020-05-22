package iris.playharmony.view.main;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.user.UserView;
import iris.playharmony.view.util.ButtonFactory;
import javafx.scene.Node;

public class LobbyView extends FormTemplate {

    public LobbyView() {
        super("Lobby");
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Admin", event -> NavController.get().pushView(new AdminView())),
                ButtonFactory.button("User", event -> NavController.get().pushView(new UserView()))
        };
    }

    @Override
    public void refresh() {

    }
}
