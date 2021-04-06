package LogFile;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter{
	@Override
	public String format(LogRecord record) {
		return String.format("<%s> <%s> <%s> <%s>: <%s> \n", new Date(record.getMillis()),record.getSourceClassName(),record.getSourceMethodName(),record.getLevel(),record.getMessage());
	}
}
