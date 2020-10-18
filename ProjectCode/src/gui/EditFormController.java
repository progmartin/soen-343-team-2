/**
 * <pre>
 * TODO:
 * </pre>
 */
package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author DRC
 */
public class EditFormController implements Initializable {

    @FXML
    Pane parentPane;

    @FXML
    Label title;
    @FXML
    TextField usernameInput;
    @FXML
    TextField passwordInput;
    @FXML
    ComboBox accessibility;
    @FXML
    Button saveButton;
    @FXML
    Button deleteButton;
    @FXML
    Label output;

    private boolean newUser = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accessibility.getItems().addAll(Arrays.asList(new String[]{"Adult (Family)", "Child (Family)", "Guest", "Stranger"}));
        String[] input = Driver.simulationController.editedUser.split(",");
   
        if (input.length == 1) {
            title.setText("Create New User");
            newUser = true;
        } else {
            usernameInput.setText(input[0]);
            usernameInput.setDisable(true);
            passwordInput.setText(input[1]);
            for (int i = 0; i < accessibility.getItems().size(); i++) {
                String item = (String) accessibility.getItems().get(i);
                if (input[2].equals(item)) {
                    accessibility.getSelectionModel().select(i);
                }
            }
        }
    }

    @FXML
    private void handleSave(Event e) {
        if (usernameInput.getText().trim().equals("")) {
            output.setText("Cannot have an empty username");
            e.consume();
            return;
        }
        if (usernameInput.getText().trim().equals("[New User]")) {
            output.setText("Cannot have username be \"[New User]\" since it is a keyword");
            e.consume();
            return;
        }
        if (accessibility.getSelectionModel().isEmpty()){
            output.setText("Must select an accessibility");
            e.consume();
            return;
        }
        if (newUser && Driver.simulationController.accounts.containsKey(usernameInput.getText().trim())) {
            output.setText("Username is already taken");
            e.consume();
            return;
        }
        Driver.simulationController.accounts.put(usernameInput.getText().trim(), new String[]{passwordInput.getText(), (String) accessibility.getSelectionModel().getSelectedItem()});
        if (newUser) {
            Driver.simulationController.usersList.getItems().add(Driver.simulationController.usersList.getItems().size() - 1, usernameInput.getText().trim());
            Driver.simulationController.usersList.getSelectionModel().selectLast();
            Driver.simulationController.usersList.getSelectionModel().selectPrevious();
        }
        SimulationWindowController.editStage.fireEvent(new WindowEvent(SimulationWindowController.editStage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @FXML
    private void handleDelete(Event e) {
        if (newUser) {
            output.setText("Cannot delete a user not create yet");
            e.consume();
            return;
        }

        Alert continueWindow = new Alert(Alert.AlertType.CONFIRMATION);
        continueWindow.setTitle("Delete User?");
        continueWindow.setHeaderText("Are you sure you wish to delete this user?");
        continueWindow.setContentText("Once you delete this user, they will be completely removed.");

        continueWindow.getButtonTypes().removeAll(continueWindow.getButtonTypes());
        continueWindow.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        continueWindow.showAndWait();
        // If the use selects to not delete;
        if (continueWindow.getResult().equals(ButtonType.NO)) {
            e.consume();
            return;
        }
        
        Driver.simulationController.accounts.remove(usernameInput.getText().trim());
        Driver.simulationController.usersList.getItems().remove(usernameInput.getText().trim());
        SimulationWindowController.editStage.fireEvent(new WindowEvent(SimulationWindowController.editStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
