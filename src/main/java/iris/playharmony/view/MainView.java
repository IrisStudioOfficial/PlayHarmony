package iris.playharmony.view;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.view.playlist.PlaylistView;
import iris.playharmony.view.user.UserListView;
import javafx.scene.layout.BorderPane;

import static iris.playharmony.util.TypeUtils.initSingleton;

public class MainView extends BorderPane {

    private HeaderView headerView;
    private NavigationView navigationView;
    private NavController navController;
    private FooterView footerView;

    public MainView() {

        headerView = new HeaderView();

        navigationView = new NavigationView();

        navController = new NavController(navigationView);
        navController.setView(new PlaylistView(new Playlist("Playlist de Prueba")));
        //navController.setView(new UserListView());
        footerView = new FooterView();

        setTop(headerView);

        setCenter(navigationView);

        setBottom(footerView);

        initSingleton(NavController.class, navController);
    }
}
