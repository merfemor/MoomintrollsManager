package gui.internalization;

import java.util.ListResourceBundle;

public class Translation_sl extends ListResourceBundle {
    private Object[][] content = {
            {"addButton", "Dodaj"},
            {"removeButton", "Brisanje"},
            {"editButton", "Spremeniti"},
            {"fileMenu", "Datoteka"},
            {"openMenuItem", "Odprta"},
            {"saveMenuItem", "Ohranijo"},
            {"saveAsMenuItem", "Shrani kot"},
            {"closeMenuItem", "Zapri"},
            {"viewMenu", "Pogled"},
            {"showTreeMenuItem", "Predstava drevo"},
            {"remoteMenu", "Oddaljeni strežnik"},
            {"connectMenuItem", "Pridružijo"},
            {"reloadMenuItem", "Osveži"},
            {"disconnectItem", "Odjava"},
            {"helpMenu", "Pomoč"},
            {"releaseNotesMenuItem", "Spremembe"},
            {"aboutMenuItem", "O programu"},

            {"nameAttribute", "Ime"},
            {"genderAttribute", "Spol"},
            {"genderMale", "mož"},
            {"genderFemale", "žene"},
            {"bodyColorAttribute", "Barva"},
            {"chooseColorButton", "Izberite"},
            {"kindnessAttribute", "Značaj"},
            {"positionAttribute", "Položaj"},
            {"creationDateAttribute", "Ustvarjen"},

            {"teamSupportButton", "Odpri Tehnična podpora Stran"},
            {"newCollectionName", "Nova kolekcija"},
            {"changedCollectionName", "Shranjen zbiranje"},
            {"rootNodeName", "Zbiranje Moomin"},
            {"from", "z"},
            {"to", "na"},
            {"OptionPane.okButtonText", "Dobro"},
            {"OptionPane.noButtonText", "Ne"},
            {"OptionPane.yesButtonText", "Da"},
            {"OptionPane.cancelButtonText", "Odpoved"},
            {"defaultNewMoomintrollName", "Anonimen"},
            {"adding", "Poleg tega"},
            {"editing", "Urejanje"},

            {"kindness:devil:m", "hudič"},
            {"kindness:devil:f", "hudič"},
            {"kindness:very bad:m", "zelo slabo"},
            {"kindness:very bad:f", "zelo slabo"},
            {"kindness:bully:m", "svađati"},
            {"kindness:bully:f", "svađati"},
            {"kindness:bad:m", "slabo"},
            {"kindness:bad:f", "slabo"},
            {"kindness:normal:m", "normalno"},
            {"kindness:normal:f", "normalno"},
            {"kindness:good:m", "dobra"},
            {"kindness:good:f", "dobra"},
            {"kindness:great:m", "velika"},
            {"kindness:great:f", "velika"},
            {"kindness:beautiful:m", "lepa"},
            {"kindness:beautiful:f", "lepa"},
            {"kindness:brilliant:m", "odlično"},
            {"kindness:brilliant:f", "odlično"},
            {"kindness:angel:m", "angela"},
            {"kindness:angel:f", "angela"},

            {"removeDialogTitle", "Odstranitev potrditev"},
            {"removeDialogMessage", "Ali ste prepričani, da želite odstraniti {0} iz zbirke?"},
            {"removeManyDialogMessage", "Ali ste prepričani, da želite izbrisati Moomins [{0}] iz zbirke?"},
            {"chooseColor", "Izberite barvo"},
            {"emptyStringErrorMessage", "Napaka: Ime Moomin troll ne sme biti prazno!"},
            {"emptyStringErrorTitle", "Napaka: prazno ime"},
            {"collectionCloseDialogTitle", "Opozorilo: Neshranjena zbiranje"},
            {"collectionCloseDialogMessage", "Sedanja zbirka se ne shrani.\nShrani to pred zaprtjem?"},

            {"FileChooser.acceptAllFileFilterText", "Vse datoteke"},
            {"FileChooser.lookInLabelText", "Oglejte si na:"},
            {"FileChooser.saveInLabelText", "Shrani v:"},
            {"FileChooser.cancelButtonText", "Odpoved"},
            {"FileChooser.openButtonText", "Odprta"},
            {"FileChooser.saveButtonText", "Ohranijo"},
            {"FileChooser.directoryOpenButtonText", "Odprta"},
            {"FileChooser.directoryOpenButtonToolTipText", "Odprt imenik"},
            {"FileChooser.filesOfTypeLabelText", "Vrsta datoteke:"},
            {"FileChooser.fileNameLabelText", "Ime datoteke:"},
            {"FileChooser.openButtonToolTipText", "Odpreti datoteke"},
            {"FileChooser.saveButtonToolTipText", "Shranite datoteko"},
            {"FileChooser.cancelButtonToolTipText", "Odpoved"},
            {"FileChooser.openDialogTitleText", "Odprta"},
            {"FileChooser.saveDialogTitleText", "Ohranijo"},
            {"FileChooser.upFolderToolTipText", "Do enega imenika"},
            {"FileChooser.homeFolderToolTipText", "Domača stran imenika"},
            {"FileChooser.listViewButtonToolTipText", "Seznam"},
            {"FileChooser.detailsViewButtonToolTipText", "Podroben pogled"},
            {"FileChooser.fileNameHeaderText", "Ime"},
            {"FileChooser.fileSizeHeaderText", "Velikost"},
            {"FileChooser.fileTypeHeaderText", "Tip"},
            {"FileChooser.fileDateHeaderText", "Datum"},
            {"FileChooser.newFolderToolTipText", "Ustvari novo mapo"},

            {"fileExistsWarningTitle", "Opozorilo: pisanja preko datotek"},
            {"fileExistsWarningMessage", "Datoteka s tem imenom že obstaja.\nPerezapisat njej?"},
            {"failedToSaveErrorTitle", "Napaka: Ni moč shraniti"},
            {"failedToSaveErrorMessage", "Ni mogoče shraniti {0}\nProsim izberi drugo datoteko."},
            {"failedToOpenErrorTitle", "Napaka: Ni bilo mogoče odpreti"},
            {"failedToOpenErrorMessage", "Ne morem odpreti {0}\nProsim izberi drugo datoteko."},
            {"successfullySavedDialogTitle", "Uspešno shranjen"},
            {"successfullySavedDialogMessage", "Uspešno shranjeni v {0}"},
            {"wrongFileFormatErrorTitle", "Napaka: Neveljavna oblika datoteke"},
            {"wrongFileFormatErrorMessage", "Ni bilo mogoče prebrati {0}\nE datoteko v napačni obliki.\nZmanjšajte drugo datoteko."},
            {"releaseNotesContent", "Zgodovina:\n" +
                    "v1.3:\n" +
                    "- lokalizacija za ruski, slovenski, albanski jezik\n" +
                    "- Fixed bug z drevesa\n\n" +
                    "v1.2\n" +
                    "- oddaljena povezava\n" +
                    "- dostop večuporabniški \n" +
                    "- drevo je zdaj skrita privzeto\n\n" +
                    "v1.1:\n" +
                    "- \" pametne \"shranjevanje datotek\n" +
                    "- popravki napak\n" +
                    "- izboljšanje uspešnosti\n\n" +
                    "v1.0.1:\n" +
                    "- doda napaka\n" +
                    "- poslabšanje uspešnosti\n\n" +
                    "v1.0\n" +
                    "- prva različica\n"}
    };

    @Override
    public Object[][] getContents() {
        return content;
    }
}