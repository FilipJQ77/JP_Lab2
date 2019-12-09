/*
Autor: Filip Przygoński
*/

import java.util.Vector;

/**
 * Klasa która "łączy" klasy plakatu oraz prostą bibliotekę I/O konsoli w jedną aplikację
 */
public class PosterConsoleApp implements IPosterDatabaseHandling {

    static private Poster currentPoster = null; //obecny plakat
    static private Vector<Poster> currentlySavedPosters = new Vector<>(); //wszystkie plakaty
    static private ConsoleUserDialog io = new ConsoleUserDialog(); //instancja klasy pomocniczej

    final static private String AUTHORINFO = "| Konsolowy program o plakatach |\n| Autor: Filip Przygonski, 248892 |";
    final static private String MENU = "\nMENU \n1 - aby dodac nowy plakat \n2 - aby wybrac jeden z plakatow z bazy danych (wpisz nazwe) \n3 - aby edytowac aktualnie wybrany plakat \n4 - aby usunac aktualnie wybrany plakat \n5 - aby wypisac wszystkie dostepne plakaty \n6 - aby wczytac dane o plakatach z pliku \n7 - aby zapisac aktualne dane o plakatach do pliku \n8 - informacje o autorze \n0 - aby zamknac program\n";
    final static private String CHANGE_MENU = "\nCo chcesz zmienic? \n1 - szerokosc \n2 - wysokosc \n3 - nazwe \n4 - temat \n0 - powrot\n";

    /**
     * metoda zajmująca się tworzeniem plakatu
     */
    public static void createNewPoster() {
        Poster newPoster;
        try {
            newPoster = new Poster();
        } catch (PosterException e) {
            io.printErrorMessage(e.getMessage());
            return;
        }
        setPosterWidth(newPoster);
        setPosterHeight(newPoster);
        setPosterName(newPoster);
        setPosterPosterTheme(newPoster);
        currentlySavedPosters.add(newPoster);
        currentPoster = newPoster;
        io.printMessage("Nowy plakat zostal utworzony");
    }

    /**
     * wybiera aktualny plakat
     */
    public static void consoleChooseAPoster() {
        String searchedPosterName = PosterConsoleApp.io.enterString("Podaj nazwe szukanego plakatu: ");
        try {
            currentPoster = IPosterDatabaseHandling.chooseAPoster(currentlySavedPosters, searchedPosterName);
        } catch (Exception e) {
            io.printErrorMessage(e.getMessage());
        }
    }

    /**
     * metoda zajmująca się edycją jednego z parametrów aktualnego plakatu
     */
    public static void editCurrentPoster() {
        if (currentPoster == null) {
            io.printErrorMessage("Nie wybrano zadnego plakatu");
        } else {
            boolean isError = false;
            do {
                io.printMessage(CHANGE_MENU);
                int chosenOption = io.enterInt("Wpisz liczbe: ");
                switch (chosenOption) {
                    case 1:
                        setPosterWidth(currentPoster);
                        break;
                    case 2:
                        setPosterHeight(currentPoster);
                        break;
                    case 3:
                        setPosterName(currentPoster);
                        break;
                    case 4:
                        setPosterPosterTheme(currentPoster);
                        break;
                    case 0:
                        break;
                    default:
                        io.printErrorMessage("Niepoprawna opcja, sprobuj jeszcze raz");
                        isError = true;
                        break;
                }
            } while (isError);
            io.printMessage("Plakat zostal zedytowany");
        }
    }

    /**
     * usuwa referencje do aktualnego plakatu i wzywa garbage collector
     */
    public static void removeCurrentPoster() {
        currentlySavedPosters.remove(currentPoster);
        currentPoster = null;
        System.gc();
        io.printMessage("Plakat zostal usuniety");
    }

    /**
     * wyświetla wszystkie zapisane plakaty
     */
    public static void showAllPosterAvailable() {
        io.printMessage("Wszystkie plakaty:");
        for (Poster i : currentlySavedPosters) {
            io.printMessage(i.toString());
        }
    }

    /**
     * zajmuje się ogólną obsługą odczytu/zapisu oraz wyborem pliku w aplikacji konsolowej
     *
     * @param mode tryb - "r" - read - wczytaj, "w" - write - zapisz
     */
    public static void consoleReadOrWritePosters(String mode) {
        String filename = PosterConsoleApp.io.enterString("Podaj sciezke do pliku: ");
        try {
            IPosterDatabaseHandling.readOrWritePosters(currentlySavedPosters, mode, filename);
        } catch (Exception e) {
            io.printErrorMessage(e.getMessage());
        }
    }

    /**
     * ustawia szerokość plakatu
     *
     * @param poster plakat któremu ustawia się szerokość
     */
    public static void setPosterWidth(Poster poster) {
        boolean exceptionHappened;
        do {
            exceptionHappened = false;
            int width = io.enterInt("Podaj szerokosc plakatu: ");
            try {
                poster.setWidth(width);
            } catch (PosterException e) {
                io.printErrorMessage(e.getMessage());
                exceptionHappened = true;
            }
        } while (exceptionHappened);
    }

    /**
     * ustawia wysokość plakatu
     *
     * @param poster plakat któremu ustawia się wysokość
     */
    public static void setPosterHeight(Poster poster) {
        boolean exceptionHappened;
        do {
            exceptionHappened = false;
            int height = io.enterInt("Podaj wysokosc plakatu: ");
            try {
                poster.setHeight(height);
            } catch (PosterException e) {
                io.printErrorMessage(e.getMessage());
                exceptionHappened = true;
            }
        } while (exceptionHappened);
    }

    /**
     * ustawia nazwę plakatu
     *
     * @param poster plakat któremu ustawia się nazwę
     */
    public static void setPosterName(Poster poster) {
        boolean exceptionHappened;
        do {
            exceptionHappened = false;
            String name = io.enterString("Podaj nazwe plakatu: ");
            try {
                name = setANameForThePoster(currentlySavedPosters, name);
                poster.setName(name);
            } catch (Exception e) {
                io.printErrorMessage(e.getMessage());
                exceptionHappened = true;
            }
        } while (exceptionHappened);
    }

    /**
     * ustawia nazwę plakatu biorąc pod uwagę fakt że daną nazwę może mieć tylko jeden plakat
     *
     * @param name nazwa którą powinien posiadać plakat
     * @return ta sama nazwa, jeśli żaden inny zapisany plakat nie ma takiej nazwy
     * @throws Exception jeśli plakat o danej nazwie już istnieje
     */
    public static String setANameForThePoster(Vector<Poster> currentlySavedPosters, String name) throws Exception {
        if (IPosterDatabaseHandling.returnIndexOfAPosterIfItExists(currentlySavedPosters, name) != -1) {
            throw new Exception("Plakat o tej nazwie już istnieje");
        } else return name;
    }

    /**
     * ustawia temat plakatu
     *
     * @param poster plakat któremu ustawia się temat
     */
    public static void setPosterPosterTheme(Poster poster) {
        boolean exceptionHappened;
        do {
            exceptionHappened = false;
            String theme = io.enterString("Podaj temat plakatu: ");
            try {
                poster.setTheme(theme);
            } catch (PosterException e) {
                io.printErrorMessage(e.getMessage());
                exceptionHappened = true;
            }
        } while (exceptionHappened);
    }

    /**
     * uruchomienie aplikacji
     */
    public static void run() {
        int chosenOption;

        io.printMessage(AUTHORINFO);

        boolean exit = false;
        while (!exit) {
            io.printMessage("\nObecnie wybrany plakat: " + currentPoster + MENU);
            chosenOption = io.enterInt("Wpisz liczbe odpowiadajaca opcji ktora chcesz wykonac: ");
            switch (chosenOption) {
                case 1:
                    createNewPoster();
                    break;
                case 2:
                    consoleChooseAPoster();
                    break;
                case 3:
                    editCurrentPoster();
                    break;
                case 4:
                    removeCurrentPoster();
                    break;
                case 5:
                    showAllPosterAvailable();
                    break;
                case 6:
                    consoleReadOrWritePosters("r");
                    break;
                case 7:
                    consoleReadOrWritePosters("w");
                    break;
                case 8:
                    io.printMessage(AUTHORINFO);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    io.printErrorMessage("Nieprawidlowa opcja! Wpisz opcje jeszcze raz.");
                    break;
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        PosterConsoleApp.run();
    }


}