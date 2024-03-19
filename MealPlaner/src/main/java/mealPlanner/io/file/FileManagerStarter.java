package mealPlanner.io.file;

import mealPlanner.exception.NoSuchFileTypeException;
import mealPlanner.io.ConsolePrinter;
import mealPlanner.io.DataReader;

public class FileManagerStarter {
    private ConsolePrinter printer;
    private DataReader reader;

    public FileManagerStarter(ConsolePrinter printer, DataReader reader) {
        this.printer = printer;
        this.reader = reader;
    }

    public FileManager start() {
        printer.printLine("Choose data type:");
        FileType fileType = getFileType();
        switch (fileType) {
            case CSV -> {
                return new CsvFileManager();
            }
            case SERIAL -> {
                return new SerializableFileManager();
            }
            default -> throw new NoSuchFileTypeException("Unhandled file type");
        }
    }

    private FileType getFileType() {
        boolean typeOk = false;
        FileType result = null;
        do {
            printTypes();
            String type = reader.getString().toUpperCase();
            try {
                result = FileType.valueOf(type);
                typeOk = true;
            } catch (IllegalArgumentException e) {
                printer.printLine("Unhandled data type, choose again.");
            }
        } while (!typeOk);
        return result;
    }

    private void printTypes() {
        for (FileType value : FileType.values()) {
            printer.printLine(value.name());
        }

    }
}
