package io.github.kamilszewc.tabulator.exceptions;

/**
 * Exception to be risen when then card is empty
 */
public class EmptyCardException extends TabulatorException {

    /**
     * NotImplementedException constructor
     * @param message message to be included
     */
    public EmptyCardException(String message) {
        super(message);
    }
}
