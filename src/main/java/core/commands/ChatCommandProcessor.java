package core.commands;

import javassist.NotFoundException;

public interface ChatCommandProcessor {

    void execute() throws NotFoundException;
}