/**
 * This DataFormatException class handles format of the content incorrect error.
 */
public class DataFormatException extends Exception {
    /**
     * This Constructor provides default error message.
     */
    public DataFormatException() {
        super("The format of the content is incorrect. Please check!");
    }

    /**
     * Overload Constructor provides self-defined error message.
     *
     * @param aMessage self-defined error message
     */
    public DataFormatException(String aMessage) {
        super(aMessage);
    }
}
