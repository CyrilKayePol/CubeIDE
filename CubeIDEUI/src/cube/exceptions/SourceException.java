
package cube.exceptions;

import cube.gui.Cube;

public class SourceException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SourceException() {}

    public SourceException(String message, int row, int column)
    {
        super(String.format("Syntax Error (Line: %d, Column: %d): %s", row, column, message));
        Cube.consolePane.setText(Cube.consolePane.getText()+(String.format("Syntax Error (Line: %d, Column: %d): %s", row, column, message)));
    }
}