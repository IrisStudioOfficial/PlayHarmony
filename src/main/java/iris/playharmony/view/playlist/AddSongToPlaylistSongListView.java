package iris.playharmony.view.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.FooterView;
import iris.playharmony.view.HeaderView;
import iris.playharmony.view.NavigationView;
import iris.playharmony.view.View;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static iris.playharmony.util.TypeUtils.initSingleton;

public class AddSongToPlaylistSongListView extends BorderPane {

    private static int SPACING = 15;

    private HeaderView headerView;
    private NavigationView navigationView;
    protected NavController navController;
    private FooterView footerView;

    public AddSongToPlaylistSongListView() {
        headerView = new HeaderView();

        navigationView = new NavigationView();
        navigationView.setView(new AddSongToPlaylistSongListViewNavigation());
        navController = new NavController(navigationView);

        footerView = new FooterView();

        setTop(headerView);
        setCenter(navigationView);
        setBottom(footerView);

        initSingleton(AddSongToPlaylistSongListViewNavigation.class, navController);
    }

    public class AddSongToPlaylistSongListViewNavigation extends VBox implements View {

    }
}
