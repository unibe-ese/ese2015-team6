package ch.unibe.ese.Tutorfinder.conversion;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

/**
 * Converts formatted strings to java.time.LocalDate objects and vice versa.
 * @author Nicola
 *
 */
@Service
public class DateFormatter implements Formatter<LocalDate> {
	
	public DateFormatter() {
		super();
	}

	@Override
	public String print(LocalDate object, Locale locale) {
		final DateTimeFormatter dateFormat = createDateFormat();
		return dateFormat.format(object);
	}

	private DateTimeFormatter createDateFormat() {
		final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dateFormat;
	}

	@Override
	public LocalDate parse(String text, Locale locale) throws ParseException {
		final DateTimeFormatter dateFormat = createDateFormat();
		return LocalDate.from(dateFormat.parse(text));
	}

}
