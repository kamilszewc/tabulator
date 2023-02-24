package com.computinglaboratory.tabulator.exceptions;

/**
 * Exception to be risen when the word is longer than allowed by Card or Table
 */
public class TooLongWordException extends TabulatorException {

    /**
     * TooLongWordExpression constructor
     * @param message message to be included in the exception
     */
    public TooLongWordException(String message) {
        super(message);
    }
}
