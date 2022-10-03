package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.profile.Profile;

/**
 * An UI component that displays information of a {@code Profile}.
 */
public class ProfileCard extends UiPart<Region> {

    private static final String FXML = "ProfileListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Profile profile;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code ProfileCode} with the given {@code Profile} and index to display.
     */
    public ProfileCard(Profile profile, int displayedIndex) {
        super(FXML);
        this.profile = profile;
        id.setText(displayedIndex + ". ");
        name.setText(profile.getName().fullName);
        phone.setText(profile.getPhone().value);
        address.setText(profile.getAddress().value);
        email.setText(profile.getEmail().value);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ProfileCard)) {
            return false;
        }

        // state check
        ProfileCard card = (ProfileCard) other;
        return id.getText().equals(card.id.getText())
                && profile.equals(card.profile);
    }
}
