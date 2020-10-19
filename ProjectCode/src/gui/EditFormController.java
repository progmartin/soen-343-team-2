package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.stage.WindowEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.Event;
import javafx.collections.FXCollections;

import HouseObjects.*;

/**
 * FXML Controller class
 *
 * @author d_ruiz-cigana
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
    CheckBox isAdmin;
    @FXML
    ComboBox accessibility;
    @FXML
    AnchorPane locationPane;
    @FXML
    ComboBox location;
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
        Person person = Driver.simulationController.editedUser;

        if (person == null) {
            title.setText("Create New User");
            newUser = true;
            location.setItems(FXCollections.observableArrayList(Driver.simulation.getRoomNames()));
        } else {
            usernameInput.setText(person.getName());
            usernameInput.setDisable(true);
            passwordInput.setText((String) Driver.simulationController.accounts.get(person.getName())[1]);
            for (int i = 0; i < accessibility.getItems().size(); i++) {
                String item = (String) accessibility.getItems().get(i);
                if (person.getUserType().equals(item)) {
                    accessibility.getSelectionModel().select(i);
                }
            }
            isAdmin.setSelected(person.getIsAdmin());
            location.getItems().add(Driver.simulation.getUserLocation(person.getName()));
            location.getSelectionModel().select(0);
            location.setDisable(true);
            locationPane.setVisible(false);
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
        if (accessibility.getSelectionModel().isEmpty()) {
            output.setText("Must select an accessibility");
            e.consume();
            return;
        }
        if (newUser && Driver.simulationController.accounts.containsKey(usernameInput.getText().trim())) {
            output.setText("Username is already taken");
            e.consume();
            return;
        }
        if (newUser && location.getSelectionModel().isEmpty()) {
            output.setText("Must set location for new user");
            e.consume();
            return;
        }

        if (newUser && Driver.simulation.getRoom((String) location.getSelectionModel().getSelectedItem()) == null) {
            output.setText("ERROR: This location does not exists");
            e.consume();
            return;
        }

        if (newUser) {
            Driver.simulationController.usersList.getItems().add(Driver.simulationController.usersList.getItems().size() - 1, usernameInput.getText().trim());
            Driver.simulationController.usersList.getSelectionModel().selectLast();
            Driver.simulationController.usersList.getSelectionModel().selectPrevious();
        }
        Driver.simulation.updateUser(usernameInput.getText().trim(), isAdmin.isSelected(), Person.getUserType((String) accessibility.getSelectionModel().getSelectedItem()), (String) location.getSelectionModel().getSelectedItem());
        Driver.simulationController.accounts.put(usernameInput.getText().trim(), new Object[]{Driver.simulation.getUser(usernameInput.getText().trim()), passwordInput.getText()});
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
        Driver.simulation.removeUser(usernameInput.getText().trim());
        Driver.simulationController.accounts.remove(usernameInput.getText().trim());
        Driver.simulationController.usersList.getItems().remove(usernameInput.getText().trim());
        SimulationWindowController.editStage.fireEvent(new WindowEvent(SimulationWindowController.editStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
