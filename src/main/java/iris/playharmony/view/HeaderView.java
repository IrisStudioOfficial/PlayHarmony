package iris.playharmony.view;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.util.ImageFactory;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.util.Resources;
import iris.playharmony.util.TypeUtils;
import iris.playharmony.view.main.LobbyView;
import iris.playharmony.view.util.ButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class HeaderView extends HBox {

    private static final String TITLE = "PlayHarmony";
    private static final String ROLE = "Administrator";
    private static final Font TITLE_FONT = new Font("Arial", 28);
    private static final Font ROLE_FONT = new Font("Arial", 28);

    public HeaderView() {

        add(ButtonFactory.button("<-", e -> {
            Parent view = NavController.get().getCurrentView();
            if(view != null){
                if(!Objects.equals(view.getClass(), LobbyView.class)) {
                    NavController.get().popView();
                    TypeUtils.callAnnotatedMethod(NavController.get().getCurrentView(), OnRefresh.class);
                }
            }
        }));

        add(setTitle());
        add(getRegion());
        add(getRegion());
        add(setRole());
        add(setImage());

        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.rgb(174, 214, 241), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(20));
    }

    private void add(Node node) {
        getChildren().add(node);
    }

    private Label setTitle() {
        Label leftTitle = new Label(TITLE);
        leftTitle.setFont(TITLE_FONT);

        return leftTitle;
    }

    private ImageView setImage() {
        Image image = ImageFactory.loadFromFile(PathHandler.DEFAULT_PHOTO_PATH);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);

        return imageView;
    }

    private Label setRole() {
        Label roleUser = new Label(ROLE);
        roleUser.setFont(ROLE_FONT);

        return roleUser;
    }

    private Region getRegion() {
        Region region = new Region();
        setHgrow(region, Priority.ALWAYS);

        return region;
    }

}
