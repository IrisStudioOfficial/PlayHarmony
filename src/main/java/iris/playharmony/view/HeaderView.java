package iris.playharmony.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HeaderView extends HBox {
    private static String title = "PlayHarmony";
    private static String rol = "Administrator";

    public HeaderView() {
        Label leftTitle = new Label(title);
        leftTitle.setFont(new Font("Arial", 28));

        Image image = new Image("UserLogo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);

        Label rolUser = new Label(rol);
        rolUser.setFont(new Font("Arial", 15));

        Region region1 = new Region();
        Region region2 = new Region();

        this.setHgrow(region1, Priority.ALWAYS);
        this.setHgrow(region2, Priority.ALWAYS);

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(leftTitle, region1, region2, rolUser, imageView);
        this.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPadding(new Insets(20));
    }

}
