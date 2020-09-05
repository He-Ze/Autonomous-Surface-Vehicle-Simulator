package util.file;

import javax.swing.*;
import java.io.File;

/**
 * This class holds a function that deals with file selection.
 * Note, that it might be a good idea to create my own file chooser,
 * this however will take time, so maybe in the future.
 * @author P3TE.
 */
public abstract class FileChooser implements Runnable {

    //The currently open file chooser. (IE, only 1 at a time)
    private static FileChooser openFileChooser = null;

    //Whether or not the current file selection is complete.
    private boolean fileSelectionComplete = false;

    //The currently selected file.
    private File selectedFile = null;

    /**
     * Runs a new thread that creates a select file dialog.
     */
    public FileChooser() {
        if (openFileChooser != null) {
            //Do nothing.
        } else {
            fileSelectionComplete = false;
            openFileChooser = this;
            Thread fileChoosingThread = new Thread(this);
            fileChoosingThread.start();
        }
    }

    /**
     * Opens the file chooser, which is a blocking command,
     * Once completed, it will be ready to be handled back
     * on the correct thread.
     */
    @Override
    public void run() {
        selectedFile = openFileChooser();
        fileSelectionComplete = true;
    }

    /**
     * If a file chooser currently exists, check if it has
     * completed the file selection process.
     */
    public static void handleFileChooserIfApplicable() {
        if (openFileChooser != null) {
            openFileChooser.handleSelectedFileIfApplicable();
        }
    }

    /**
     * When the file selection is complete, then
     * handle the selected file.
     */
    private void handleSelectedFileIfApplicable() {
        if (fileSelectionComplete) {
            openFileChooser = null;
            if (selectedFile == null) {
                onNoFileSelected();
            } else {
                onFileChosen(selectedFile);
            }
        }
    }

    /**
     * Creates a file selection window and asks the user to choose.
     *
     * @return null if none was chosen, otherwise the file chosen
     */
    private static File openFileChooser() {
        File file = null;
        // Open the file choosed window.
        JFileChooser chooser = new JFileChooser();
        int selectionType = chooser.showOpenDialog(null);
        if (selectionType == JFileChooser.APPROVE_OPTION) {
            // A file was selected
            file = chooser.getSelectedFile();
        } else if (selectionType == JFileChooser.CANCEL_OPTION) {
            // Cancel or X was pressed.
            return null;
        }
        return file;
    }

    /**
     * Called one a file is chosen
     * file will be null if no file was selected.
     *
     * @param file - The file chosen.
     */
    public abstract void onFileChosen(File file);

    /**
     * Called if NO file was selected.
     */
    public abstract void onNoFileSelected();
}
