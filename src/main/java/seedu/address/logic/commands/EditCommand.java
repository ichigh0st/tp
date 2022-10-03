package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.profile.Address;
import seedu.address.model.profile.Email;
import seedu.address.model.profile.Name;
import seedu.address.model.profile.Phone;
import seedu.address.model.profile.Profile;

/**
 * Edits the details of an existing profile in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the profile identified "
            + "by the index number used in the displayed profile list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Profile: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This profile already exists in the address book.";

    private final Index index;
    private final EditProfileDescriptor editProfileDescriptor;

    /**
     * @param index of the profile in the filtered profile list to edit
     * @param editProfileDescriptor details to edit the profile with
     */
    public EditCommand(Index index, EditProfileDescriptor editProfileDescriptor) {
        requireNonNull(index);
        requireNonNull(editProfileDescriptor);

        this.index = index;
        this.editProfileDescriptor = new EditProfileDescriptor(editProfileDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Profile> lastShownList = model.getFilteredProfileList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Profile profileToEdit = lastShownList.get(index.getZeroBased());
        Profile editedProfile = createEditedProfile(profileToEdit, editProfileDescriptor);

        if (!profileToEdit.isSameProfile(editedProfile) && model.hasProfile(editedProfile)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setProfile(profileToEdit, editedProfile);
        model.updateFilteredProfileList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedProfile));
    }

    /**
     * Creates and returns a {@code Profile} with the details of {@code profileToEdit}
     * edited with {@code editProfileDescriptor}.
     */
    private static Profile createEditedProfile(Profile profileToEdit, EditProfileDescriptor editProfileDescriptor) {
        assert profileToEdit != null;

        Name updatedName = editProfileDescriptor.getName().orElse(profileToEdit.getName());
        Phone updatedPhone = editProfileDescriptor.getPhone().orElse(profileToEdit.getPhone());
        Email updatedEmail = editProfileDescriptor.getEmail().orElse(profileToEdit.getEmail());
        Address updatedAddress = editProfileDescriptor.getAddress().orElse(profileToEdit.getAddress());

        return new Profile(updatedName, updatedPhone, updatedEmail, updatedAddress);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editProfileDescriptor.equals(e.editProfileDescriptor);
    }

    /**
     * Stores the details to edit the profile with. Each non-empty field value will replace the
     * corresponding field value of the profile.
     */
    public static class EditProfileDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;

        public EditProfileDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditProfileDescriptor(EditProfileDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditProfileDescriptor)) {
                return false;
            }

            // state check
            EditProfileDescriptor e = (EditProfileDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress());
        }
    }
}
