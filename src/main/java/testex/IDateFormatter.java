package testex;

import java.util.Date;

public interface IDateFormatter {
    public String getFormattedDate(String timeZone, Date time) throws JokeException;
}
