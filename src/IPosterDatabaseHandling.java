import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public interface IPosterDatabaseHandling {

    /**
     * sprawdza, czy plakat o danej nazwie istnieje, jeśli istnieje to zwraca indeks tego plakatu, w przeciwnym wypadku zwraca -1
     *
     * @param currentlySavedPosters obecnie zapisane plakaty w bazie danych aplikacji
     * @param name                  nazwa plakatu
     * @return indeks plakatu o podanej nazwie, -1 jeśli takiego plakatu nie ma
     */
    static int returnIndexOfAPosterIfItExists(Vector<Poster> currentlySavedPosters, String name) {
        for (Poster i : currentlySavedPosters) {
            if (name.equals(i.getName())) return currentlySavedPosters.indexOf(i);
        }
        return -1;
    }

    /**
     * metoda zajmująca się wyborem aktualnego plakatu
     *  @param currentlySavedPosters obecnie zapisane plakaty w bazie danych aplikacji
     *  @param searchedPosterName    nazwa szukanego plakatu
     * @return zwraca wybrany plakat
     */
    static Poster chooseAPoster(Vector<Poster> currentlySavedPosters, String searchedPosterName) throws Exception {
        int index = returnIndexOfAPosterIfItExists(currentlySavedPosters, searchedPosterName);
        if (index != -1) {
            return (currentlySavedPosters.get(index));
        } else {
            throw new Exception("Nie ma plakatu o takiej nazwie");
        }
    }

    /**
     * zajmuje się ogólną obsługą odczyta/zapisu
     *
     * @param currentlySavedPosters obecnie zapisane plakaty w bazie danych aplikacji
     * @param mode                  tryb - "r" - read - wczytaj, "w" - write - zapisz
     * @param filename              nazwa obsługiwanego pliku
     */
    static void readOrWritePosters(Vector<Poster> currentlySavedPosters, String mode, String filename) throws Exception {
        if (mode.equals("r")) { //wczytywanie
            readPostersFromFile(currentlySavedPosters, filename);
        } else if (mode.equals("w")) { //zapisywanie
            writePostersToFile(currentlySavedPosters, filename);
        } else throw new Exception("Niepoprawny typ pliku");
    }

    /**
     * wczytuje plakaty z podanego pliku
     *
     * @param currentlySavedPosters obecnie zapisane plakaty w bazie danych aplikacji
     * @param filename              nazwa pliku z którego zostaną wczytane plakaty
     */
    static void readPostersFromFile(Vector<Poster> currentlySavedPosters, String filename) throws Exception {
        if (filename.endsWith(".txt")) {
            Scanner scanner = new Scanner(new File(filename));
            boolean exceptionHappened = false;
            while (scanner.hasNextLine()) {
                Poster posterToAdd = Poster.readPosterFromFile(scanner);
                if (returnIndexOfAPosterIfItExists(currentlySavedPosters, posterToAdd.getName()) == -1) {
                    currentlySavedPosters.add(posterToAdd);
                } else {
                    exceptionHappened = true;
                }
            }
            scanner.close();
            if (exceptionHappened) {
                throw new Exception("Niektore plakaty mogly nie zostac dodane, poniewaz w bazie nie moga istniec dwa plakaty o tej samej nazwie!");
            }

        } else if (filename.endsWith(".bin")) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
            boolean exceptionHappened = false;
            try {
                while (true) {
                    Poster posterToAdd = (Poster) inputStream.readObject();
                    if (returnIndexOfAPosterIfItExists(currentlySavedPosters, posterToAdd.getName()) == -1) {
                        currentlySavedPosters.add(posterToAdd);
                    } else {
                        exceptionHappened = true;
                    }
                }
            } catch (EOFException e) {
                //rozwiązanie pozwalające wczytywać obiekty do końca pliku, niezbyt eleganckie, ale lepsze niż żadne
                inputStream.close();
                if (exceptionHappened) {
                    throw new Exception("Niektore plakaty mogly nie zostac dodane, poniewaz w bazie nie moga istniec dwa i wiecej plakatow o tej samej nazwie!");
                }
            }
        } else throw new Exception("Niepoprawny typ pliku");
    }

    /**
     * zapisuje aktualne plakaty do danego pliku
     *
     * @param currentlySavedPosters obecnie zapisane plakaty w bazie danych aplikacji
     * @param filename              nazwa pliku do którego zostaną zapisane plakaty
     */
    static void writePostersToFile(Vector<Poster> currentlySavedPosters, String filename) throws Exception {
        if (filename.endsWith(".txt")) {
            for (Poster i : currentlySavedPosters) {
                i.writePosterToFile(filename);
            }
        } else if (filename.endsWith(".bin")) {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
            for (Poster i : currentlySavedPosters) {
                outputStream.writeObject(i);
            }
            outputStream.close();
        } else throw new Exception("Niepoprawny typ pliku");

    }
}
