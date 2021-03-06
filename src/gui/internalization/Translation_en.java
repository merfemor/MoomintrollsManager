package gui.internalization;

import java.util.ListResourceBundle;

public class Translation_en extends ListResourceBundle {
    private Object[][] content = {
            {"addButton", "Add"},
            {"removeButton", "Remove"},
            {"editButton", "Edit"},
            {"fileMenu", "File"},
            {"openMenuItem", "Open"},
            {"saveMenuItem", "Save"},
            {"saveAsMenuItem", "Save As"},
            {"closeMenuItem", "Close"},
            {"viewMenu", "View"},
            {"showTreeMenuItem", "Show Tree"},
            {"remoteMenu", "Remote"},
            {"connectMenuItem", "Connect"},
            {"reloadMenuItem", "Reload"},
            {"disconnectItem", "Disconnect"},
            {"helpMenu", "Help"},
            {"releaseNotesMenuItem", "Release Notes"},
            {"aboutMenuItem", "About"},

            {"nameAttribute", "Name"},
            {"genderAttribute", "Gender"},
            {"genderMale", "male"},
            {"genderFemale", "female"},
            {"bodyColorAttribute", "Body Color"},
            {"chooseColorButton", "Choose"},
            {"kindnessAttribute", "Kindness"},
            {"positionAttribute", "Position"},
            {"creationDateAttribute", "Created"},

            {"teamSupportButton", "Open Team Support Page"},
            {"newCollectionName", "New Collection"},
            {"changedCollectionName", "Unsaved Collection"},
            {"rootNodeName", "Moomintrolls Collection"},
            {"from", "from"},
            {"to", "to"},
            {"OptionPane.okButtonText", "OK"},
            {"OptionPane.noButtonText", "No"},
            {"OptionPane.yesButtonText", "Yes"},
            {"OptionPane.cancelButtonText", "Cancel"},
            {"defaultNewMoomintrollName", "Unknown"},
            {"adding", "Add"},
            {"editing", "Edit"},

            {"kindness:devil:m", "devil"},
            {"kindness:devil:f", "devil"},
            {"kindness:very bad:m", "very bad"},
            {"kindness:very bad:f", "very bad"},
            {"kindness:bully:m", "bully"},
            {"kindness:bully:f", "bully"},
            {"kindness:bad:m", "bad"},
            {"kindness:bad:f", "bad"},
            {"kindness:normal:m", "normal"},
            {"kindness:normal:f", "normal"},
            {"kindness:good:m", "good"},
            {"kindness:good:f", "good"},
            {"kindness:great:m", "great"},
            {"kindness:great:f", "great"},
            {"kindness:beautiful:m", "beautiful"},
            {"kindness:beautiful:f", "beautiful"},
            {"kindness:brilliant:m", "brilliant"},
            {"kindness:brilliant:f", "brilliant"},
            {"kindness:angel:m", "angel"},
            {"kindness:angel:f", "angel"},

            {"removeDialogTitle", "Confirm removing"},
            {"removeDialogMessage", "Are you sure want to remove {0} from the collection?"},
            {"removeManyDialogMessage", "Are you sure want to remove moomintrolls[{0}] from the collection?"},
            {"chooseColor", "Choose color"},
            {"emptyStringErrorMessage", "Error: the name of the moomintroll can't be empty!"},
            {"emptyStringErrorTitle", "Error: empty name"},
            {"collectionCloseDialogTitle", "Warning: unsaved collection"},
            {"collectionCloseDialogMessage", "Current collection is not saved.\nDo you want to save it before closing?"},

            {"FileChooser.acceptAllFileFilterText", "All Files"},
            {"FileChooser.lookInLabelText", "Look in:"},
            {"FileChooser.saveInLabelText", "Save in:"},
            {"FileChooser.cancelButtonText", "Cancel"},
            {"FileChooser.openButtonText", "Open"},
            {"FileChooser.saveButtonText", "Save"},
            {"FileChooser.directoryOpenButtonText", "Open"},
            {"FileChooser.directoryOpenButtonToolTipText", "Open directory"},
            {"FileChooser.filesOfTypeLabelText", "File Type:"},
            {"FileChooser.fileNameLabelText", "File Name:"},
            {"FileChooser.openButtonToolTipText", "Open file"},
            {"FileChooser.saveButtonToolTipText", "Save file"},
            {"FileChooser.cancelButtonToolTipText", "Cancel"},
            {"FileChooser.openDialogTitleText", "Open"},
            {"FileChooser.saveDialogTitleText", "Save"},
            {"FileChooser.upFolderToolTipText", "Up One Level"},
            {"FileChooser.homeFolderToolTipText", "Home Folder"},
            {"FileChooser.listViewButtonToolTipText", "List View"},
            {"FileChooser.detailsViewButtonToolTipText", "Detailed View"},
            {"FileChooser.fileNameHeaderText", "Name"},
            {"FileChooser.fileSizeHeaderText", "Size"},
            {"FileChooser.fileTypeHeaderText", "Type"},
            {"FileChooser.fileDateHeaderText", "Date"},
            {"FileChooser.newFolderToolTipText", "Create New Folder"},

            {"fileExistsWarningTitle", "Warning: overwriting file"},
            {"fileExistsWarningMessage", "File is already exists.\nOverwrite it?"},
            {"failedToSaveErrorTitle", "Error: failed to save"},
            {"failedToSaveErrorMessage", "Failed to save into {0}\nSelect file again."},
            {"failedToOpenErrorTitle", "Error: failed to open"},
            {"failedToOpenErrorMessage", "Failed to open {0}\nSelect file again."},
            {"successfullySavedDialogTitle", "Successfully saved"},
            {"successfullySavedDialogMessage", "Successfully saved into {0}"},
            {"wrongFileFormatErrorTitle", "Error: failed to read"},
            {"wrongFileFormatErrorMessage", "Failed to read {0}\nFile is in the wrong format.\nSelect file again."},
            {"releaseNotesContent", "Release notes:\n" +
                    "v1.3:\n" +
                    "- localization for Russian, Slovenian, Albanian languages\n" +
                    "- fixed bug with tree\n\n" +
                    "v1.2:\n" +
                    "- remote connection\n" +
                    "- multi-user access\n" +
                    "- tree now hidden by default\n\n" +
                    "v1.1:\n" +
                    "- \"clever\" files saving\n" +
                    "- bugs fixed\n" +
                    "- performance improvements\n\n" +
                    "v1.0.1:\n" +
                    "- bugs added\n" +
                    "- performance impaired\n\n" +
                    "v1.0\n" +
                    "- first working version\n"}
    };

    @Override
    public Object[][] getContents() {
        return content;
    }
}