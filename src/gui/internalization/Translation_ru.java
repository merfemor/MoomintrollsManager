package gui.internalization;

import java.util.ListResourceBundle;

public class Translation_ru extends ListResourceBundle {
    private Object[][] content = {
            {"addButton", "Добавить"},
            {"removeButton", "Удалить"},
            {"editButton", "Изменить"},
            {"fileMenu", "Файл"},
            {"openMenuItem", "Открыть"},
            {"saveMenuItem", "Сохранить"},
            {"saveAsMenuItem", "Сохранить как"},
            {"closeMenuItem", "Закрыть"},
            {"viewMenu", "Вид"},
            {"showTreeMenuItem", "Показывать дерево"},
            {"remoteMenu", "Удаленный сервер"},
            {"connectMenuItem", "Присоединиться"},
            {"reloadMenuItem", "Перезагрузить"},
            {"disconnectItem", "Отключиться"},
            {"helpMenu", "Помощь"},
            {"releaseNotesMenuItem", "Изменения"},
            {"aboutMenuItem", "О программе"},

            {"nameAttribute", "Имя"},
            {"genderAttribute", "Пол"},
            {"genderMale", "муж"},
            {"genderFemale", "жен"},
            {"bodyColorAttribute", "Цвет тела"},
            {"chooseColorButton", "Выбрать"},
            {"kindnessAttribute", "Характер"},
            {"positionAttribute", "Позиция"},
            {"creationDateAttribute", "Создан"},

            {"teamSupportButton", "Открыть страницу техподдержки"},
            {"newCollectionName", "Новая коллекция"},
            {"changedCollectionName", "Несохраненная коллекция"},
            {"rootNodeName", "Коллекция муми-троллей"},
            {"from", "с"},
            {"to", "по"},
            {"OptionPane.okButtonText", "OK"},
            {"OptionPane.noButtonText", "Нет"},
            {"OptionPane.yesButtonText", "Да"},
            {"OptionPane.cancelButtonText", "Отмена"},
            {"defaultNewMoomintrollName", "Безымянный"},
            {"adding", "Добавление"},
            {"editing", "Редактирование"},

            {"kindness:devil:m", "дьявол"},
            {"kindness:devil:f", "дьявол"},
            {"kindness:very bad:m", "очень плохой"},
            {"kindness:very bad:f", "очень плохая"},
            {"kindness:bully:m", "хулиган"},
            {"kindness:bully:f", "хулиганка"},
            {"kindness:bad:m", "плохой"},
            {"kindness:bad:f", "плохая"},
            {"kindness:normal:m", "нормальный"},
            {"kindness:normal:f", "нормальная"},
            {"kindness:good:m", "хороший"},
            {"kindness:good:f", "хорошая"},
            {"kindness:great:m", "отличный"},
            {"kindness:great:f", "отличная"},
            {"kindness:beautiful:m", "прекрасный"},
            {"kindness:beautiful:f", "прекрасная"},
            {"kindness:brilliant:m", "превосходный"},
            {"kindness:brilliant:f", "превосходная"},
            {"kindness:angel:m", "ангел"},
            {"kindness:angel:f", "ангел"},

            {"removeDialogTitle", "Потверждение удаления"},
            {"removeDialogMessage", "Вы действительно хотите удалить {0} из коллекции?"},
            {"removeManyDialogMessage", "Вы действительно хотите удалить муми-троллей[{0}] из коллекции?"},
            {"chooseColor", "Выберите цвет"},
            {"emptyStringErrorMessage", "Ошибка: имя муми-тролля не может быть пустым!"},
            {"emptyStringErrorTitle", "Ошибка: пустое имя"},
            {"collectionCloseDialogTitle", "Предупреждение: несохраненная коллекция"},
            {"collectionCloseDialogMessage", "Текущая коллекция не сохранена.\nСохранить её перед закрытием?"},

            {"FileChooser.acceptAllFileFilterText", "Все файлы"},
            {"FileChooser.lookInLabelText", "Смотреть в:"},
            {"FileChooser.saveInLabelText", "Сохранить в:"},
            {"FileChooser.cancelButtonText", "Отмена"},
            {"FileChooser.openButtonText", "Открыть"},
            {"FileChooser.saveButtonText", "Сохранить"},
            {"FileChooser.directoryOpenButtonText", "Открыть"},
            {"FileChooser.directoryOpenButtonToolTipText", "Открыть директорию"},
            {"FileChooser.filesOfTypeLabelText", "Тип файла:"},
            {"FileChooser.fileNameLabelText", "Имя файла:"},
            {"FileChooser.openButtonToolTipText", "Открыть файл"},
            {"FileChooser.saveButtonToolTipText", "Сохранить файл"},
            {"FileChooser.cancelButtonToolTipText", "Отмена"},
            {"FileChooser.openDialogTitleText", "Открыть"},
            {"FileChooser.saveDialogTitleText", "Сохранить"},
            {"FileChooser.upFolderToolTipText", "Вверх на один каталог"},
            {"FileChooser.homeFolderToolTipText", "Домашняя директория"},
            {"FileChooser.listViewButtonToolTipText", "Список"},
            {"FileChooser.detailsViewButtonToolTipText", "Детализированный вид"},
            {"FileChooser.fileNameHeaderText", "Название"},
            {"FileChooser.fileSizeHeaderText", "Размер"},
            {"FileChooser.fileTypeHeaderText", "Тип"},
            {"FileChooser.fileDateHeaderText", "Дата"},
            {"FileChooser.newFolderToolTipText", "Создать новую директорию"},

            {"fileExistsWarningTitle", "Предупреждение: перезаписывание файла"},
            {"fileExistsWarningMessage", "Файл с таким именем уже существует.\nПерезаписать его?"},
            {"failedToSaveErrorTitle", "Ошибка: не удалось сохранить"},
            {"failedToSaveErrorMessage", "Не удалось сохранить в {0}\nВыберите другой файл."},
            {"failedToOpenErrorTitle", "Ошибка: не удалось открыть"},
            {"failedToOpenErrorMessage", "Не удалось открыть {0}\nВыберите другой файл."},
            {"successfullySavedDialogTitle", "Успешно сохранено"},
            {"successfullySavedDialogMessage", "Успешно сохранено в {0}"},
            {"wrongFileFormatErrorTitle", "Ошибка: неверный формат файла"},
            {"wrongFileFormatErrorMessage", "Не удалось прочитать {0}\nФайл в неверном формате.\nВыберите другой файл."},
            {"releaseNotesContent", "История изменения:\n" +
                    "v1.3:\n" +
                    "- локализация для русского, словенского, албанского языков\n" +
                    "- исправлена ошибка с деревом\n\n" +
                    "v1.2:\n" +
                    "- удаленное соединение\n" +
                    "- многопользовательский доступ\n" +
                    "- дерево теперь скрыто по умолчанию\n\n" +
                    "v1.1:\n" +
                    "- \"умное\" сохранение файлов\n" +
                    "- исправлены ошибки\n" +
                    "- улучшение производительности\n\n" +
                    "v1.0.1:\n" +
                    "- добавлены ошибки\n" +
                    "- ухудшение производительности\n\n" +
                    "v1.0\n" +
                    "- первая версия\n"}
    };

    @Override
    public Object[][] getContents() {
        return content;
    }
}