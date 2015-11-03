package ch.unibe.ese.Tutorfinder.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * Converts strings formatted as yyyy-MM-dd (which happens to be given by a input with type date) to java.util.date objects and vice versa.
 * @author Nicola
 *
 */
public class DateFormatter implements Formatter<Date> {
	
	public DateFormatter() {
		super();
	}

	@Override
	public String print(Date object, Locale locale) {
		final SimpleDateFormat dateFormat = createDateFormat(locale);
		return dateFormat.format(object);
	}

	private SimpleDateFormat createDateFormat(Locale locale) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		return dateFormat;
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		final SimpleDateFormat dateFormat = createDateFormat(locale);
		return dateFormat.parse(text);
	}

}
