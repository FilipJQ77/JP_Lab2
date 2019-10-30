/*
    Program: Aplikacje: konsolowa oraz okienkowa z GUI, umo¿liwiaj¹ce testowanie obiektów klasy Poster i operacji na nich
    Plik: Poster.java
    Autor: Filip Przygoñski, 248892
    Ostatnia data modyfikacji: 30.10.19
*/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Typ wyliczeniowy wyliczaj¹cy mo¿liwe tematy plakatu
 */
enum PosterTheme {

    NONE("Brak tematu"),
    SCHOOL("Szkola"),
    SCIENCE("Nauka"),
    MOVIE("Film"),
    MUSIC("Muzyka"),
    VIDEOGAME("Gra wideo");

    String posterTheme;

    PosterTheme(String theme) {
        posterTheme = theme;
    }

    @Override
    public String toString() {
        return posterTheme;
    }
}

/**
 * Klasa obs³uguj¹ca wyj¹tki dotycz¹ce plakatu
 */
class PosterException extends Exception {
    PosterException(String message) {
        super(message);
    }
}

/**
 * Klasa reprezentuj¹ca plakat
 */
public class Poster implements Serializable {

    private static long serialVersionUID;
    private int width; // w centymetrach
    private int height; // w centymetrach
    private String name;
    private PosterTheme theme;

    public Poster() throws PosterException {
        setWidth(1);
        setHeight(1);
        setName(" ");
        setTheme(PosterTheme.NONE);
    }

    public Poster(int width, int height) throws PosterException {
        setWidth(width);
        setHeight(height);
        setName("Pusty plakat");
        setTheme(PosterTheme.NONE);
    }

    public Poster(int width, int height, String name, String theme) throws PosterException {
        setWidth(width);
        setHeight(height);
        setName(name);
        setTheme(theme);
    }

    @Override
    public String toString() {
        return name + " " + "Temat: " + theme.posterTheme + " " + width + "cm x " + height + "cm";
    }

    /**
     * zapisuje plakat do podanego pliku
     *
     * @param filename nazwa/œcie¿ka pliku
     * @throws IOException gdy nie znaleziono pliku o podanej nazwie
     */
    public void writePosterToFile(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename, true));
        writer.println(width + "###" + height + "###" + name + "###" + theme.posterTheme);
        writer.close();
    }

    /**
     * wczytuje dane z pliku i tworzy z nich plakat
     *
     * @param scanner scanner aplikacji
     * @return utworzony plakat
     * @throws PosterException gdy z danych nie da siê utworzyæ plakatu
     */
    public static Poster readPosterFromFile(Scanner scanner) throws PosterException {
        String readData = scanner.nextLine();
        String[] data = readData.split("###");
        return new Poster(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3]);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) throws PosterException {
        if (width <= 0) {
            throw new PosterException("Szerokoœæ plakatu musi byæ nieujemna.");
        } else this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) throws PosterException {
        if (height <= 0) {
            throw new PosterException("Wysokoœæ plakatu musi byæ nieujemna.");
        } else this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws PosterException {
        if (name == null || name.equals("")) {
            throw new PosterException("Nazwa plakatu niepoprawna.");
        } else this.name = name;
    }

    public PosterTheme getTheme() {
        return theme;
    }

    public void setTheme(PosterTheme theme) {
        this.theme = theme;
    }

    public void setTheme(String theme) throws PosterException {
        if (theme == null || theme.equals("") || theme.toUpperCase().equals("BRAK TEMATU")) {
            setTheme(PosterTheme.NONE);
            return;
        } else {
            for (PosterTheme i : PosterTheme.values()) {
                if (theme.toUpperCase().equals(i.posterTheme.toUpperCase())) {
                    setTheme(i);
                    return;
                }
            }
        }
        throw new PosterException("Niepoprawny temat plakatu.");
    }

    /*
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.write(width);
        stream.write(height);
        stream.writeObject(name);
        stream.writeObject(theme);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException, PosterException {
        int width = (stream.readInt());
        int height = (stream.readInt());
        String name = (stream.readUTF());
        PosterTheme theme = ((PosterTheme) stream.readObject());
        setWidth(width);
        setHeight(height);
        setName(name);
        setTheme(theme);
    }*/

}
