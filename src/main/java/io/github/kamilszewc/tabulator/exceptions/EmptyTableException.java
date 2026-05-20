package io.github.kamilszewc.tabulator.exceptions;

/**
 * Exception to be risen when then table is empty
 */
public class EmptyTableException extends TabulatorException {

    /**
     * NotImplementedException constructor
     * @param message message to be included
     */
    public EmptyTableException(String message) {
        super(message);
    }
}
