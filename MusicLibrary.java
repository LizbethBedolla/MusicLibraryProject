package labs.lab9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Comparator;

public class MusicLibrary extends JFrame {
    private JTextField titleField, artistField, runningTimeField;
    private JTextArea notesTextArea;
    private JComboBox<String> categoryComboBox;
    private JCheckBox friendsCheckBox, familyCheckBox, coworkersCheckBox;
    private JButton saveButton, newSongButton, deleteButton;
    private JList<Song> songList;
    private DefaultListModel<Song> songListModel;
    private JLabel totalRunningTimeLabel;
    private double totalRunningTime = 0.0;
    private JSplitPane mainSplitPane;
    private Comparator<Song> songComparator;

    public MusicLibrary() {
        setTitle("Lizbeth Bedola - 65488580");
        setSize(1000, 500);
        initializeComponents();
        setupLayout();
        addListeners();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeComponents() {
        songListModel = new DefaultListModel<>();
        songList = new JList<>(songListModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        titleField = new JTextField();
        artistField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[]{"Work", "Exercise", "Party", "Sleep", "Driving", "Other"});
        friendsCheckBox = new JCheckBox("Friends");
        familyCheckBox = new JCheckBox("Family");
        coworkersCheckBox = new JCheckBox("Coworkers");
        
        runningTimeField = new JTextField("0.0");
        totalRunningTimeLabel = new JLabel("Total Running Time: 0.0 ");
        
        notesTextArea = new JTextArea(5, 20);
        notesTextArea.setLineWrap(true); 
        notesTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesTextArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
        
        saveButton = new JButton("Save Song");
        newSongButton = new JButton("New Song");
        deleteButton = new JButton("Delete Song");
        
        songComparator = Comparator.comparing(Song::getTitle);
    }

    private void setupLayout() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JScrollPane(songList), BorderLayout.CENTER);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        detailsPanel.add(new JLabel("Title:"), gbc);
        detailsPanel.add(titleField, gbc);
        detailsPanel.add(new JLabel("Artist:"), gbc);
        detailsPanel.add(artistField, gbc);
        detailsPanel.add(new JLabel("Share with:"), gbc);
        
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkBoxPanel.add(friendsCheckBox);
        checkBoxPanel.add(familyCheckBox);
        checkBoxPanel.add(coworkersCheckBox);
        detailsPanel.add(checkBoxPanel, gbc);
        
        detailsPanel.add(new JLabel("Category:"), gbc);
        detailsPanel.add(categoryComboBox, gbc);
        detailsPanel.add(new JLabel("Running time:"), gbc);
        detailsPanel.add(runningTimeField, gbc);
        detailsPanel.add(new JLabel("Notes:"), gbc);
        detailsPanel.add(new JScrollPane(notesTextArea), gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.add(saveButton);
        buttonsPanel.add(newSongButton);
        buttonsPanel.add(deleteButton);

        totalRunningTimeLabel.setHorizontalAlignment(JLabel.CENTER); 

        bottomPanel.add(buttonsPanel, BorderLayout.CENTER);
        bottomPanel.add(totalRunningTimeLabel, BorderLayout.SOUTH);

        mainSplitPane.setLeftComponent(listPanel);
        mainSplitPane.setRightComponent(detailsPanel);
        mainSplitPane.setResizeWeight(0.5); 

        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        saveButton.addActionListener(this::saveSong);
        newSongButton.addActionListener(e -> clearFieldsAndSelection());
        deleteButton.addActionListener(this::deleteSong);
        songList.addListSelectionListener(e -> showSelectedSongDetails());
    }

    private void clearFields() {
        titleField.setText("");
        artistField.setText("");
        categoryComboBox.setSelectedIndex(0); 
        runningTimeField.setText("0.0");
        notesTextArea.setText("");
        friendsCheckBox.setSelected(false);
        familyCheckBox.setSelected(false);
        coworkersCheckBox.setSelected(false);
    }

    private void saveSong(ActionEvent e) {
        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        double runningTime;
        try {
            runningTime = Double.parseDouble(runningTimeField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Running Time must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (title.isEmpty() || artist.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid input! Title and Artist cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedIndex = songList.getSelectedIndex();
        boolean isEditing = selectedIndex != -1;

        if (!isEditing && titleExists(title)) {
            JOptionPane.showMessageDialog(this, "Invalid input! Song title must be unique.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String notes = notesTextArea.getText().trim();
        boolean shareWithFriends = friendsCheckBox.isSelected();
        boolean shareWithFamily = familyCheckBox.isSelected();
        boolean shareWithCoworkers = coworkersCheckBox.isSelected();

        Song song = new Song(title, artist, category, runningTime, notes, shareWithFriends, shareWithFamily, shareWithCoworkers);
        addOrUpdateSong(song, isEditing);

        JOptionPane.showMessageDialog(this, "Song saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFieldsAndSelection();
        updateTotalRunningTime();
    }

    private boolean titleExists(String title) {
        for (int i = 0; i < songListModel.size(); i++) {
            if (songListModel.get(i).getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    private void clearFieldsAndSelection() {
        clearFields();
        songList.clearSelection();
    }

    private void deleteSong(ActionEvent e) {
        int selected = songList.getSelectedIndex();
        if (selected >= 0) {
            songListModel.remove(selected);
            clearFields();
            updateTotalRunningTime();
        }
    }

    private void updateTotalRunningTime() {
        totalRunningTime = 0.0;
        for (int i = 0; i < songListModel.getSize(); i++) {
            Song song = songListModel.getElementAt(i);
            totalRunningTime += song.getRunningTime();
        }
        totalRunningTimeLabel.setText(String.format("Total Running Time: %.2f minutes", totalRunningTime));
    }

    private void showSelectedSongDetails() {
        Song selectedSong = songList.getSelectedValue();
        if (selectedSong != null) {
            friendsCheckBox.setSelected(selectedSong.isSharedWithFriends());
            familyCheckBox.setSelected(selectedSong.isSharedWithFamily());
            coworkersCheckBox.setSelected(selectedSong.isSharedWithCoworkers());
            titleField.setText(selectedSong.getTitle());
            artistField.setText(selectedSong.getArtist());
            categoryComboBox.setSelectedItem(selectedSong.getCategory());
            runningTimeField.setText(String.format("%.2f", selectedSong.getRunningTime()));
            notesTextArea.setText(selectedSong.getNotes());
        }
    }

    private void addOrUpdateSong(Song song, boolean isUpdate) {
        if (isUpdate) {
            songListModel.remove(songList.getSelectedIndex());
        }

        int insertAt = 0;
        for (; insertAt < songListModel.getSize(); insertAt++) {
            if (songComparator.compare(songListModel.getElementAt(insertAt), song) > 0) {
                break;
            }
        }

        songListModel.add(insertAt, song);
        songList.setSelectedIndex(insertAt);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MusicLibrary frame = new MusicLibrary();
            frame.setVisible(true);
            frame.mainSplitPane.setDividerLocation(0.5); 
        });
    }
}
