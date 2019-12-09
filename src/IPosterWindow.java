/*
Autor: Filip Przygoński
*/

import javax.swing.*;

public interface IPosterWindow {
    byte CREATE_MODE = 0;
    byte EDIT_MODE = 1;

    /**
     * pokazuje (aktualizuje) w oknie aktualny plakat
     */
    default void showCurrentPoster(Poster poster, JTextField widthTextField, JTextField heightTextField, JTextField nameTextField, JTextField themeTextField) {
        if (poster == null) {
            widthTextField.setText("");
            heightTextField.setText("");
            nameTextField.setText("");
            themeTextField.setText("");
        } else {
            widthTextField.setText(String.valueOf(poster.getWidth()));
            heightTextField.setText(String.valueOf(poster.getHeight()));
            nameTextField.setText(poster.getName());
            themeTextField.setText(String.valueOf(poster.getTheme()));
        }
    }
}
