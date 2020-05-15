package iris.playharmony.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FooterView extends HBox {

    private static final String FOOTER_TEXT = "Copyright ©2020 PlayHarmony.";

    public FooterView(){
        Label footerLabel = new Label(FOOTER_TEXT);
        footerLabel.setFont(new Font("Arial", 15));
        this.getChildren().addAll(footerLabel);
        this.setAlignment(Pos.BASELINE_CENTER);
        this.setPadding(new Insets(15));
        this.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

    }

}
