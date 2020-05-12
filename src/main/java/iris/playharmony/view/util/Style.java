package iris.playharmony.view.util;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Style {

    public static final String CSS_BACK_COLOR = "-fx-background-color: #";
    public static final String CSS_FORE_COLOR = "-fx-text-fill: #";
    public static final String CSS_FONT_WEIGHT = "-fx-font-weight: ";
    public static final String CSS_BACK_IMAGE = "-fx-background-image: ";
    public static final String CSS_BACK_POS = "-fx-background-position: ";
    public static final String CSS_FONT = "-fx-font: ";
    public static final String CSS_BORDER_COLOR = "-fx-border-color: #";
    public static final String CSS_BORDER_WIDTH = "-fx-border-width: ";
    public static final String CSS_BORDER_STYLE = "-fx-border-style: ";
    public static final String CSS_GRAPHIC = "-fx-graphic: ";
    public static final String CSS_OPACITY = "-fx-opacity: ";

    public static String backgroundImage(String path) {
        return CSS_BACK_IMAGE+"url(\"" + path + "\");\n";
    }

    public static String backgroundColor(Color color) {
        return CSS_BACK_COLOR+color.toString().replace("0x", "")+";\n";
    }

    public static String foregroundColor(Color color) {
        return CSS_FORE_COLOR+color.toString().replace("0x", "")+";\n";
    }

    public static String graphic(String path) {
        return CSS_GRAPHIC + "url(\"" + path + "\");\n";
    }

    public static String opacity(double opacity) {
        return CSS_OPACITY + opacity + ";\n";
    }

    public static String font(String name, int size) {
        return CSS_FONT + size + "px " + "\""+name+"\";\n";
    }

    public static String fontWeight(String w) {
        return CSS_FONT_WEIGHT + w +";\n";
    }

    private final StringBuilder styleCSSBuilder;

    public Style() {
        styleCSSBuilder = new StringBuilder();
    }

    public Style backColor(Color color) {
        styleCSSBuilder.append(backgroundColor(color));
        return this;
    }

    public Style foreColor(Color color) {
        styleCSSBuilder.append(foregroundColor(color));
        return this;
    }

    public Style backImage(String name) {
        styleCSSBuilder.append(backgroundImage(name));
        return this;
    }

    public Style backPos(Pos position) {
        styleCSSBuilder.append(CSS_BACK_POS+position.name().toLowerCase()+";\n");
        return this;
    }

    public Style setOpacity(double opacity) {
        styleCSSBuilder.append(CSS_OPACITY + opacity);
        return this;
    }

    public Style borderColor(Color color) {
        styleCSSBuilder.append(CSS_BORDER_COLOR+color.toString().replace("0x", "")+";\n");
        return this;
    }

    public Style borderWidth(int width) {
        styleCSSBuilder.append(CSS_BORDER_WIDTH+width+";\n");
        return this;
    }

    public Style font(Font font) {
        styleCSSBuilder.append(font(font.getName(), (int) font.getSize()));
        return this;
    }

    public Style setFontWeight(String w) {
        styleCSSBuilder.append(fontWeight(w));
        return this;
    }

    public Style setGraphic(String path) {
        styleCSSBuilder.append(CSS_GRAPHIC + "url(\"" + path + "\");\n");
        backColor(new Color(0,0,0,0));
        return this;
    }

    public String toString() {
        return styleCSSBuilder.toString();
    }
}