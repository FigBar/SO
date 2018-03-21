package Exceptions;

/**
 * is thrown when simulation cannot be carried out due to
 * providing incorrect data
 */
public class ImpossibleToSimulateException extends RuntimeException {

    public ImpossibleToSimulateException() {
        super("The simulation can't be carried out due to providing incorrect data");
    }
}
