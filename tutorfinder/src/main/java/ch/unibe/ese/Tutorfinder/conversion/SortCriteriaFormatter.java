package ch.unibe.ese.Tutorfinder.conversion;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import ch.unibe.ese.Tutorfinder.util.SortCriteria;

public class SortCriteriaFormatter implements Formatter<SortCriteria> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String print(final SortCriteria object, Locale arg1) {
		return (object != null ? object.toString():"");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortCriteria parse(String arg0, Locale arg1) throws ParseException {
		return SortCriteria.forName(arg0);
	}

}
