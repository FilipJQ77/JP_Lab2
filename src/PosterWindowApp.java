/*
Autor: Filip Przygoński
*/

import java.io.File;
import java.util.Vector;

public class PosterWindowApp implements IPosterDatabaseHandling {

    private static Vector<Poster> currentlySavedPosters = new Vector<>();
    private static Poster currentPoster = null;
    private static PosterWindow currentWindow;

    public static Poster getCurrentPoster() {
        return currentPoster;
    }

    /**
     * wybiera aktualny plakat
     *
     * @param movement o ile należy się przesunąć np. (-1) - poprzedni plakat, 1 - następny
     */
    public static void chooseAPoster(byte movement) {
        int size = currentlySavedPosters.size();
        if (size != 0) {
            int index = currentlySavedPosters.indexOf(currentPoster);
            index += movement;
            if (index < 0) {
                currentPoster = currentlySavedPosters.get(size + movement);
            } else if (index > (size - 1)) {
                currentPoster = currentlySavedPosters.get(-1 + movement);
            } else {
                currentPoster = currentlySavedPosters.get(index);
            }
        }
    }

    /**
     * tworzy nowy plakat
     */
    public static void createNewPoster(int width, int height, String name, String theme) throws PosterException {
        Poster newPoster = new Poster(width, height, name, theme);
        currentlySavedPosters.add(newPoster);
        currentPoster = newPoster;
    }

    /**
     * metoda zajmująca się wyborem aktualnego plakatu
     *
     * @param name nazwa szukanego plakatu
     */
    public static void chooseAPoster(String name) throws Exception {
        currentPoster = IPosterDatabaseHandling.chooseAPoster(currentlySavedPosters, name);
    }

    /**
     * edytuje aktualny plakat
     */
    public static void editCurrentPoster(int width, int height, String name, String theme) throws PosterException {
        currentPoster.setWidth(width);
        currentPoster.setHeight(height);
        currentPoster.setName(name);
        currentPoster.setTheme(theme);
    }

    /**
     * zapisuje aktualne plakaty do danego pliku
     *
     * @param file podany plik
     */
    public static void writePostersToFile(File file) throws Exception {
        IPosterDatabaseHandling.writePostersToFile(currentlySavedPosters, file.getName());
    }

    /**
     * wczytuje plakaty z podanego pliku
     *
     * @param file plik z którego zostaną wczytane plakaty
     */
    public static void readPostersFromFile(File file) throws Exception {
        IPosterDatabaseHandling.readPostersFromFile(currentlySavedPosters, file.getName());
        if (currentPoster == null) {
            currentPoster = currentlySavedPosters.get(0);
        }
    }

    /**
     * usuwa aktualnie wybrany plakat
     */
    public static void deleteCurrentPoster() {
        currentlySavedPosters.remove(currentPoster);
        currentPoster = null;
    }

    /**
     * uruchamia aplikację
     */
    public static void runWindowApp() {
        currentWindow = new PosterWindow();
    }

    public static void main(String[] args) {
        runWindowApp();
    }
}
