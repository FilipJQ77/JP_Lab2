/*
    Program: Aplikacje: konsolowa oraz okienkowa z GUI, umożliwiające testowanie obiektów klasy Poster i operacji na nich
    Plik: PosterWindow.java
    Autor: Filip Przygoński, 248892
    Ostatnia data modyfikacji: 30.10.19
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class PosterWindowSearchDialog extends JDialog implements ActionListener {

    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    JLabel nameLabel = new JLabel("Podaj szukana nazwe plakatu: ");
    JTextField nameTextField = new JTextField(25);

    JButton cancelButton = new JButton("Anuluj");
    JButton okButton = new JButton("OK");

    PosterWindowSearchDialog(PosterWindow owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 135);
        setTitle("Tworzenie/Edycja plakatu");
        setLocationRelativeTo(owner);

        nameLabel.setFont(font);

        cancelButton.addActionListener(this);
        okButton.addActionListener(this);

        JPanel panel = new JPanel();

        panel.add(nameLabel);
        panel.add(nameTextField);

        panel.add(cancelButton);
        panel.add(okButton);

        setContentPane(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object sourceOfEvent = e.getSource();

        if (sourceOfEvent == cancelButton) {
            if (JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz anulowac akcje?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
            }
        }

        if (sourceOfEvent == okButton) {
            String name = nameTextField.getText();
            try {
                PosterWindowApp.chooseAPoster(name);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class PosterWindowDialog extends JDialog implements ActionListener, IPosterWindow {

    private byte mode;

    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    JLabel widthLabel = new JLabel("Szerokosc plakatu: ");
    JLabel heightLabel = new JLabel("Wysokosc plakatu: ");
    JLabel nameLabel = new JLabel("Nazwa plakatu: ");
    JLabel themeLabel = new JLabel("Temat plakatu: ");

    JTextField widthTextField = new JTextField(10);
    JTextField heightTextField = new JTextField(10);
    JTextField nameTextField = new JTextField(10);
    JTextField themeTextField = new JTextField(10);

    JButton cancelButton = new JButton("Anuluj");
    JButton okButton = new JButton("OK");


    PosterWindowDialog(PosterWindow owner, byte mode) {

        super(owner, ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setTitle("Tworzenie/Edycja plakatu");
        setLocationRelativeTo(owner);
        this.mode = mode;

        widthLabel.setFont(font);
        heightLabel.setFont(font);
        nameLabel.setFont(font);
        themeLabel.setFont(font);

        cancelButton.addActionListener(this);
        okButton.addActionListener(this);

        JPanel panel = new JPanel();

        panel.add(widthLabel);
        panel.add(widthTextField);
        panel.add(heightLabel);
        panel.add(heightTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(themeLabel);
        panel.add(themeTextField);

        showCurrentPoster(PosterWindowApp.getCurrentPoster(), widthTextField, heightTextField, nameTextField, themeTextField);

        panel.add(cancelButton);
        panel.add(okButton);

        setContentPane(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object sourceOfEvent = e.getSource();

        if (sourceOfEvent == cancelButton) {
            if (JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz anulowac akcje?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
            }
        }

        if (sourceOfEvent == okButton) {
            boolean isError = false;
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(widthTextField.getText());
                height = Integer.parseInt(heightTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Podana szerokosc/wysokosc nie jest liczba", "Blad", JOptionPane.ERROR_MESSAGE);
                isError = true;
            }
            String name = nameTextField.getText();
            String theme = themeTextField.getText();

            if (mode == CREATE_MODE) {
                try {
                    PosterWindowApp.createNewPoster(width, height, name, theme);
                } catch (PosterException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
                    isError = true;
                }
            } else if (mode == EDIT_MODE) {
                try {
                    PosterWindowApp.editCurrentPoster(width, height, name, theme);
                } catch (PosterException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
                    isError = true;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Niepoprawny tryb okna", "Blad", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
            if (!isError) {
                dispose();
            }
        }
    }
}

public class PosterWindow extends JFrame implements ActionListener, IPosterWindow {

    private static final String INFO = "Program okienkowy testujacy dzialanie klasy o plakatach\nAutor: Filip Przygonski, 248892\nOstatnia data modyfikacji: 30.10.19";

    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    // tablica/arraylist/wektor przycisków/pól/opcji(?)

    JMenuBar menuBar = new JMenuBar();
    JMenu menuOptions = new JMenu("Opcje");
    JMenu menuHelp = new JMenu("Pomoc");
    JMenuItem menuNew = new JMenuItem("Stworz");
    JMenuItem menuChange = new JMenuItem("Zmien");
    JMenuItem menuEdit = new JMenuItem("Edytuj");
    JMenuItem menuSave = new JMenuItem("Zapisz");
    JMenuItem menuLoad = new JMenuItem("Wczytaj");
    JMenuItem menuDelete = new JMenuItem("Usun");
    JMenuItem menuInfo = new JMenuItem("Info");
    JMenuItem menuExit = new JMenuItem("Wyjdz");

    JLabel widthLabel = new JLabel("Szerokosc plakatu: ");
    JLabel heightLabel = new JLabel(" Wysokosc plakatu: ");
    JLabel nameLabel = new JLabel("    Nazwa plakatu: ");
    JLabel themeLabel = new JLabel("    Temat plakatu: ");

    JTextField widthTextField = new JTextField(10);
    JTextField heightTextField = new JTextField(10);
    JTextField nameTextField = new JTextField(10);
    JTextField themeTextField = new JTextField(10);

    JButton previousButton = new JButton("Poprzedni plakat");
    JButton nextButton = new JButton("Nastepny plakat");
    JButton newButton = new JButton("Nowy plakat");
    JButton changeButton = new JButton("Wybierz inny plakat");
    JButton editButton = new JButton("Zmien dane plakatu");
    JButton saveButton = new JButton("Zapisz plakaty do pliku");
    JButton loadButton = new JButton("Wczytaj plakaty z pliku");
    JButton deleteButton = new JButton("Usun aktualny plakat");
    JButton infoButton = new JButton("O programie");
    JButton exitButton = new JButton("Zakoncz aplikacje");

    public PosterWindow() {
        super();
        setSize(300, 400);
        setTitle("Aplikacja do plakatow");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
//        setLayout(null);
        setLocationRelativeTo(null);

        //dodanie wszystkich elementów
        setJMenuBar(menuBar);
        menuBar.add(menuOptions);
        menuBar.add(menuHelp);

        // tablice + pętle zamiast tylu linijek(?)

        menuOptions.add(menuNew);
        menuOptions.add(menuChange);
        menuOptions.add(menuEdit);
        menuOptions.add(menuSave);
        menuOptions.add(menuLoad);
        menuOptions.add(menuDelete);
        menuHelp.add(menuInfo);
        menuHelp.add(menuExit);

        widthLabel.setFont(font);
        heightLabel.setFont(font);
        nameLabel.setFont(font);
        themeLabel.setFont(font);

        widthTextField.setEditable(false);
        heightTextField.setEditable(false);
        nameTextField.setEditable(false);
        themeTextField.setEditable(false);

        //dodanie action listenerów
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);
        newButton.addActionListener(this);
        changeButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        deleteButton.addActionListener(this);
        infoButton.addActionListener(this);
        exitButton.addActionListener(this);

        menuNew.addActionListener(this);
        menuChange.addActionListener(this);
        menuEdit.addActionListener(this);
        menuSave.addActionListener(this);
        menuLoad.addActionListener(this);
        menuDelete.addActionListener(this);
        menuInfo.addActionListener(this);
        menuExit.addActionListener(this);

        JPanel panel = new JPanel();

        //dodanie pól tekstowych
        panel.add(widthLabel);
        panel.add(widthTextField);
        panel.add(heightLabel);
        panel.add(heightTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(themeLabel);
        panel.add(themeTextField);

        //dodanie przycisków
        panel.add(previousButton);
        panel.add(nextButton);
        panel.add(newButton);
        panel.add(changeButton);
        panel.add(editButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(deleteButton);
        panel.add(infoButton);
        panel.add(exitButton);

        setContentPane(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object sourceOfEvent = e.getSource();
        if (sourceOfEvent == previousButton) {
            moveThroughPosters((byte) -1); // -1 oznacza wybór poprzedniego plakatu
        }
        if (sourceOfEvent == nextButton) {
            moveThroughPosters((byte) 1); // 1 oznacza wybór następnego plakatu
        }
        if (sourceOfEvent == newButton || sourceOfEvent == menuNew) {
            createNewPoster();
        }
        if (sourceOfEvent == changeButton|| sourceOfEvent == menuChange) {
            searchThroughPosters();
        }
        if (sourceOfEvent == editButton|| sourceOfEvent == menuEdit) {
            editCurrentPoster();
        }
        if (sourceOfEvent == saveButton|| sourceOfEvent == menuSave) {
            saveCurrentPosters();
        }
        if (sourceOfEvent == loadButton|| sourceOfEvent == menuLoad) {
            loadPosters();
        }
        if (sourceOfEvent == deleteButton|| sourceOfEvent == menuDelete) {
            deleteCurrentPoster();
        }
        if (sourceOfEvent == infoButton|| sourceOfEvent == menuInfo) {
            info();
        }
        if (sourceOfEvent == exitButton|| sourceOfEvent == menuExit) {
            exit();
        }
        showCurrentPoster(PosterWindowApp.getCurrentPoster(), widthTextField, heightTextField, nameTextField, themeTextField);
    }

    /**
     * ustawia aktualny plakat przesuwając się po wektorze wszystkich plakatów w bazie
     *
     * @param movement o ile przesuwa, np. (-1) - poprzedni plakat, 1 - następny plakat
     */
    public void moveThroughPosters(byte movement) {
        PosterWindowApp.chooseAPoster(movement);
    }

    /**
     * tworzy nowy plakat
     */
    public void createNewPoster() {
        new PosterWindowDialog(this, CREATE_MODE);
    }

    /**
     * wybiera inny plakat niż aktualny, po podanej nazwie
     */
    public void searchThroughPosters() {
        new PosterWindowSearchDialog(this);
    }

    /**
     * edytuje aktualny plakat
     */
    public void editCurrentPoster() {
        new PosterWindowDialog(this, EDIT_MODE);

    }

    /**
     * wybiera plik z którego wczytamy, lub do którego zapiszemy
     *
     * @return wybrany plik
     */
    public File chosenFile() {
        JFileChooser fileChooser = new JFileChooser();
        File file = null;
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        return file;
    }

    /**
     * zapisuje aktualne plakaty do pliku
     */
    public void saveCurrentPosters() {
        File file = chosenFile();
        try {
            PosterWindowApp.writePostersToFile(file);
            JOptionPane.showMessageDialog(this, "Zapisano pomyslnie plakaty", "", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * wczytuje plakaty z pliku
     */
    public void loadPosters() {
        File file = chosenFile();
        try {
            PosterWindowApp.readPostersFromFile(file);
            JOptionPane.showMessageDialog(this, "Wczytano pomyslnie plakaty", "", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * usuwa aktualnie wybrany plakat
     */
    public void deleteCurrentPoster() {
        PosterWindowApp.deleteCurrentPoster();
    }

    /**
     * wyświetla informacje o programie i o autorze
     */
    public void info() {
        JOptionPane.showMessageDialog(this, INFO, "Informacje", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * pyta czy chcesz zakończyć program, jeśli odpowiesz tak, to kończy program
     */
    public void exit() {
        if (JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyjsc z programu?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}