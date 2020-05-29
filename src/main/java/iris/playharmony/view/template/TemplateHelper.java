package iris.playharmony.view.template;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class TemplateHelper {

    static HBox addPaddingTo(Node[] nodes) {
        HBox hBox = new HBox();
        if(nodes != null) {
            for (int i = 0; i < 2 * nodes.length - 1; i++) {
                hBox.getChildren().add(i % 2 == 0 ? nodes[i / 2] : getNewPadding());
            }
        }
        return hBox;
    }

    private static Region getNewPadding() {
        Region padding = new Region();
        padding.setPrefWidth(5);
        return padding;
    }
}
