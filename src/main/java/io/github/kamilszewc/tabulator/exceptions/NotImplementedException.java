package io.github.kamilszewc.tabulator.exceptions;

/**
 * Exception to be risen when given method is not implemented
 */
public class NotImplementedException extends TabulatorException {

    /**
     * NotImplementedException constructor
     * @param message message to be included
     */
    public NotImplementedException(String message) {
        super(message);
    }
}
