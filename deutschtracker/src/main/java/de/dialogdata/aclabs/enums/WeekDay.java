package de.dialogdata.aclabs.enums;

public enum WeekDay {
	MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY, SUNDAY;

	public int getDayOfWeek() {
	
		switch (this) {
		case SUNDAY:
			return 1;
		default:
			return this.ordinal() + 2;
		}
	}
}
