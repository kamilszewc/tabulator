package com.computinglaboratory.tabulator.exceptions;

/**
 * Exception that to be risen in the case of some errors in Tables or Cards generation
 */
public class TabulatorException extends Exception {

    /**
     * TabulatorException constructor
     * @param message message to be included in exception
     */
    public TabulatorException(String message) {
        super(message);
    }
}
