package telran.time.applications;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class PrintCalendar {
	private static final int TITLE_OFFSET = 10;
	private static final int WEEK_DAYS_OFFSET = 2;
	private static final int COLUMN_WIDTH = 4;
	private static final int weekLength = DayOfWeek.values().length;
	private static Locale LOCALE = Locale.ENGLISH;

	public static void main(String[] args) {
		try {
			RecordArguments recordArguments = getRecordArguments(args);
			printCalendar(recordArguments);

		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void printCalendar(RecordArguments recordArguments) {
		printTitle(recordArguments.month(), recordArguments.year());
		printWeekDays(recordArguments.firstDay());
		printDays(recordArguments);
	}

	private static void printDays(RecordArguments recordArguments) {
		int nDays = getMonthDays(recordArguments.month(), recordArguments.year());
		int currentWeekDay = getCurrentMonthWeekDay(recordArguments.month(), recordArguments.year(),
				recordArguments.firstDay());
		System.out.printf("%s", " ".repeat(getFirstColumnOffset(currentWeekDay)));

		for (int day = 1; day <= nDays; day++) {
			System.out.printf("%4d", day);

			if (currentWeekDay == weekLength) {
				currentWeekDay = 0;
				System.out.println();
			}
			currentWeekDay++;
		}
	}

	private static int getFirstColumnOffset(int currentWeekDay) {
		return COLUMN_WIDTH * (currentWeekDay - 1);
	}

	private static int getCurrentMonthWeekDay(int month, int year, DayOfWeek firstDay) {
		LocalDate ld = LocalDate.of(year, month, 1);
		return (ld.get(ChronoField.DAY_OF_WEEK) + weekLength - firstDay.getValue()) % weekLength + 1;
	}

	private static int getMonthDays(int month, int year) {
		YearMonth ym = YearMonth.of(year, month);
		return ym.lengthOfMonth();
	}

	private static void printWeekDays(DayOfWeek firstDay) {
		System.out.printf("%s", " ".repeat(WEEK_DAYS_OFFSET));

		for (int i = 0; i < weekLength; i++) {
			System.out.printf("%3s ", firstDay.getDisplayName(TextStyle.SHORT, LOCALE));
			firstDay = firstDay.plus(1);
		}

		System.out.println();
	}

	private static void printTitle(int month, int year) {
		Month monthEn = Month.of(month);
		System.out.printf("%s%s %d\n", " ".repeat(TITLE_OFFSET), monthEn.getDisplayName(TextStyle.FULL, LOCALE), year);
	}

	private static RecordArguments getRecordArguments(String[] args) throws Exception {
		int month = getMonthArg(args);
		int year = getYearArg(args);
		DayOfWeek dayOfWeek = getFirstDayOfWeek(args);
		return new RecordArguments(month, year, dayOfWeek);
	}

	private static DayOfWeek getFirstDayOfWeek(String[] args) throws Exception {
		DayOfWeek day = DayOfWeek.of(1);

		if (args.length > 2) {
			try {
				day = DayOfWeek.valueOf(args[2].toUpperCase());
			} catch (Exception e) {
				throw new Exception("day must be a string of full name day of week");
			}
		}
		return day;
	}

	private static int getYearArg(String[] args) throws Exception {
		int yearRes = LocalDate.now().getYear();

		if (args.length > 1) {
			try {
				yearRes = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				throw new Exception("year must be a number");
			}
		}
		return yearRes;
	}

	private static int getMonthArg(String[] args) throws Exception {
		int monthRes = LocalDate.now().getMonthValue();

		if (args.length > 0) {
			try {
				monthRes = Integer.parseInt(args[0]);

				if (monthRes < 1) {
					throw new Exception("Month value must not be less than 1");
				}

				if (monthRes > 12) {
					throw new Exception("Month value must not be greater than 12");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Month value must be a number");
			}
		}
		return monthRes;
	}
}
