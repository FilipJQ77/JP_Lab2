/*
    Program: Aplikacje: konsolowa oraz okienkowa z GUI, umożliwiające testowanie obiektów klasy Poster i operacji na nich
    Plik: IPosterWindow.java
    Autor: Filip Przygoński, 248892
    Ostatnia data modyfikacji: 30.10.19
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
