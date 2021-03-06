package gui.internalization;

import java.util.ListResourceBundle;

public class Translation_sq extends ListResourceBundle {
    private Object[][] content = {
            {"addButton", "Shtoj"},
            {"removeButton", "Fshij"},
            {"editButton", "Ndryshoj"},
            {"fileMenu", "Skedar"},
            {"openMenuItem", "Hapur"},
            {"saveMenuItem", "Mbaj"},
            {"saveAsMenuItem", "Ruaj Si"},
            {"closeMenuItem", "AfëR"},
            {"viewMenu", "PikëPamje"},
            {"showTreeMenuItem", "Show Pemë"},
            {"remoteMenu", "Server Të LargëT"},
            {"connectMenuItem", "Bashkohem"},
            {"reloadMenuItem", "Ringarkoj"},
            {"disconnectItem", "Shqit"},
            {"helpMenu", "Ndihmë"},
            {"releaseNotesMenuItem", "Ndryshimet"},
            {"aboutMenuItem", "Për"},

            {"nameAttribute", "Emër"},
            {"genderAttribute", "Paul"},
            {"genderMale", "burri"},
            {"genderFemale", "gratë"},
            {"bodyColorAttribute", "Ngjyra E Trupit"},
            {"chooseColorButton", "Zgjedh"},
            {"kindnessAttribute", "Karakter"},
            {"positionAttribute", "Pozitë"},
            {"creationDateAttribute", "Krijuar"},

            {"teamSupportButton", "Hapur MbëShtetje Teknike Faqe"},
            {"newCollectionName", "Koleksion Të Ri"},
            {"changedCollectionName", "Mbledhja PashpëTuar"},
            {"rootNodeName", "Mbledhja E Moomin"},
            {"from", "me"},
            {"to", "në"},
            {"OptionPane.okButtonText", "Në Rregull"},
            {"OptionPane.noButtonText", "Jo"},
            {"OptionPane.yesButtonText", "Po"},
            {"OptionPane.cancelButtonText", "Anulim"},
            {"defaultNewMoomintrollName", "I Panjohur"},
            {"adding", "Shtim"},
            {"editing", "Redaktimi"},

            {"kindness:devil:m", "djall"},
            {"kindness:devil:f", "djall"},
            {"kindness:very bad:m", "shumë e keqe"},
            {"kindness:very bad:f", "shumë e keqe"},
            {"kindness:bully:m", "detyroj"},
            {"kindness:bully:f", "detyroj"},
            {"kindness:bad:m", "keq"},
            {"kindness:bad:f", "keq"},
            {"kindness:normal:m", "normal"},
            {"kindness:normal:f", "normal"},
            {"kindness:good:m", "mirë"},
            {"kindness:good:f", "mirë"},
            {"kindness:great:m", "i madh"},
            {"kindness:great:f", "i madh"},
            {"kindness:beautiful:m", "i bukur"},
            {"kindness:beautiful:f", "i bukur"},
            {"kindness:brilliant:m", "i shkëlqyer"},
            {"kindness:brilliant:f", "i shkëlqyer"},
            {"kindness:angel:m", "engjëll"},
            {"kindness:angel:f", "engjëll"},

            {"removeDialogTitle", "A Heqjen Konfirmim"},
            {"removeDialogMessage", "Jeni te sigurte qe doni te hiqni {0} nga mbledhja?"},
            {"removeManyDialogMessage", "Jeni te sigurte qe doni te fshini moomins [{0}] nga mbledhja?"},
            {"chooseColor", "Zgjidhni Një ngjyrë"},
            {"emptyStringErrorMessage", "Gabim: emri i këndoj moomin nuk mund të jetë bosh!"},
            {"emptyStringErrorTitle", "Gabim: emri bosh"},
            {"collectionCloseDialogTitle", "Paralajmërim: koleksion pashpëtuar"},
            {"collectionCloseDialogMessage", "Mbledhja e tanishme nuk është ruajtur.\nMbaj atë para mbylljes?"},

            {"FileChooser.acceptAllFileFilterText", "Të gjitha dosjet"},
            {"FileChooser.lookInLabelText", "Shikojnë në:"},
            {"FileChooser.saveInLabelText", "Ruaj pëR:"},
            {"FileChooser.cancelButtonText", "Anulim"},
            {"FileChooser.openButtonText", "Hapur"},
            {"FileChooser.saveButtonText", "Mbaj"},
            {"FileChooser.directoryOpenButtonText", "Hapur"},
            {"FileChooser.directoryOpenButtonToolTipText", "Lista E Hapur"},
            {"FileChooser.filesOfTypeLabelText", "Lloji I Skedarit:"},
            {"FileChooser.fileNameLabelText", "Emër Skedar:"},
            {"FileChooser.openButtonToolTipText", "Dosja e hapur"},
            {"FileChooser.saveButtonToolTipText", "Ruajtur Skedar"},
            {"FileChooser.cancelButtonToolTipText", "Anulim"},
            {"FileChooser.openDialogTitleText", "Hapur"},
            {"FileChooser.saveDialogTitleText", "Mbaj"},
            {"FileChooser.upFolderToolTipText", "Lart Një Drejtori"},
            {"FileChooser.homeFolderToolTipText", "Shtëpi Drejtori"},
            {"FileChooser.listViewButtonToolTipText", "Listë"},
            {"FileChooser.detailsViewButtonToolTipText", "Pamja E Detajuar"},
            {"FileChooser.fileNameHeaderText", "EmëR"},
            {"FileChooser.fileSizeHeaderText", "MadhëSi"},
            {"FileChooser.fileTypeHeaderText", "Lloj"},
            {"FileChooser.fileDateHeaderText", "Data"},
            {"FileChooser.newFolderToolTipText", "Krijo Një Listë Të Re"},

            {"fileExistsWarningTitle", "Paralajmërim: fotografi prishësh"},
            {"fileExistsWarningMessage", "Të paraqesë me të njëjtin emër ekziston.\nMbishkruaj atë?"},
            {"failedToSaveErrorTitle", "Gabim: në pamundësi për të kursyer"},
            {"failedToSaveErrorMessage", "Në pamundëSi për të shpëtuar {0}\nTë lutem zgjidhni një tjetër fotografi."},
            {"failedToOpenErrorTitle", "Gabim: nuk mund të hapet"},
            {"failedToOpenErrorMessage", "Në pamundëSi për të hapur {0}\nTë lutem zgjidhni një tjetër fotografi."},
            {"successfullySavedDialogTitle", "Ruajtur me sukses"},
            {"successfullySavedDialogMessage", "Ruajtur me sukses {0}"},
            {"wrongFileFormatErrorTitle", "Gabim: format i pavlefshëm skedari"},
            {"wrongFileFormatErrorMessage", "Nuk mundën të lexojnë {0} fotografi\nNë format të gabuar.\nJu lutem zgjidhni një tjetër fotografi."},
            {"releaseNotesContent", "Historia:\n" +
                    "v1.3:\n" +
                    "- lokalizimi për Rusisht, Sllovenisht, në gjuhën shqipe\n" +
                    "- Fixed a bug me n pema\n\n" +
                    "v1.2:\n" +
                    "- lidhje të largët\n" +
                    "- multi-user qasje\n" +
                    "- pema është fshehur nga parazgjedhur\n\n" +
                    "v1.1:\n" +
                    "- \" zgjuar \"shpëtuar files\n" +
                    "- fixes bug\n" +
                    "- përmirësimin e performancës\n\n" +
                    "v1.0.1:\n" +
                    "- shtoi gabim\n" +
                    "- përkeqësimin e performancës\n\n" +
                    "v1.0\n" +
                    "- versioni i parë i\n"}
    };

    @Override
    public Object[][] getContents() {
        return content;
    }
}