/**
 * This DataAccessException class handles file cannot be opened or read error.
 */
public class DataAccessException extends Exception {
    /**
     * This Constructor provides default error message.
     */
    public DataAccessException() {
        super("File cannot be opened or read. Please check!");
    }

    /**
     * Overload Constructor provides self-defined error message.
     *
     * @param aMessage self-defined error message
     */
    public DataAccessException(String aMessage) {
        super(aMessage);
    }
}
