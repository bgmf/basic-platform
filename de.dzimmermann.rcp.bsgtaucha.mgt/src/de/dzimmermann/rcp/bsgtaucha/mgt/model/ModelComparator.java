package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import java.util.Comparator;

public class ModelComparator implements Comparator<Object> {

	public enum Type {
		//
		DEFAULT,
		//
		DATE, PERSON, WORK,
		//
		BAND_TYPE,
		//
		PERSON_NAME, PERSON_FIRSTNAME, PERSON_STREET, PERSON_PLZ, PERSON_CITY, PERSON_BIRTHDAY,
		//
		WORK_AFFECT_BAND, WORK_NAME;
	}

	private Type type;
	private RootType root;

	public ModelComparator(RootType root, Type type) {
		this.root = root;
		this.type = type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 instanceof PersonType)
			return compare((PersonType) o1, (PersonType) o2);
		else if (o1 instanceof WorkType)
			return compare((WorkType) o1, (WorkType) o2);
		else if (o1 instanceof EntryType)
			return compare((EntryType) o1, (EntryType) o2);
		else
			return 0;
	}

	private int compare(PersonType pt1, PersonType pt2) {
		int result = 0;

		switch (type) {

		case DEFAULT:
		case PERSON:
		case PERSON_NAME:
			result = pt1.name.toLowerCase().compareTo(pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.firstname.toLowerCase().compareTo(
						pt2.firstname.toLowerCase());
			if (result == 0)
				result = pt1.birthday.compareTo(pt2.birthday);
			break;

		case PERSON_FIRSTNAME:
			result = pt1.firstname.toLowerCase().compareTo(
					pt2.firstname.toLowerCase());
			if (result == 0)
				result = pt1.name.toLowerCase().compareTo(
						pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.birthday.compareTo(pt2.birthday);
			break;

		case PERSON_PLZ:
			result = pt1.postalcode.toLowerCase().compareTo(
					pt2.postalcode.toLowerCase());
			if (result == 0)
				result = pt1.name.toLowerCase().compareTo(
						pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.firstname.toLowerCase().compareTo(
						pt2.firstname.toLowerCase());
			if (result == 0)
				result = pt1.birthday.compareTo(pt2.birthday);
			break;

		case PERSON_STREET:
			result = pt1.street.toLowerCase().compareTo(
					pt2.street.toLowerCase());
			if (result == 0)
				result = pt1.name.toLowerCase().compareTo(
						pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.firstname.toLowerCase().compareTo(
						pt2.firstname.toLowerCase());
			if (result == 0)
				result = pt1.birthday.compareTo(pt2.birthday);
			break;

		case PERSON_CITY:
			result = pt1.city.toLowerCase().compareTo(pt2.city.toLowerCase());
			if (result == 0)
				result = pt1.name.toLowerCase().compareTo(
						pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.firstname.toLowerCase().compareTo(
						pt2.firstname.toLowerCase());
			if (result == 0)
				result = pt1.birthday.compareTo(pt2.birthday);
			break;

		case DATE:
		case PERSON_BIRTHDAY:
			if (pt1.birthday != null && pt2.birthday != null)
				result = pt1.birthday.compareTo(pt2.birthday);
			else if (pt1.birthday == null && pt2.birthday != null)
				result = -1;
			else if (pt1.birthday != null && pt2.birthday == null)
				result = 1;
			else
				result = 0;
			result = pt1.birthday.compareTo(pt2.birthday);
			if (result == 0)
				result = pt1.name.toLowerCase().compareTo(
						pt2.name.toLowerCase());
			if (result == 0)
				result = pt1.firstname.toLowerCase().compareTo(
						pt2.firstname.toLowerCase());
			break;

		default:
			throw new RuntimeException("Invalid comparator type" + type
					+ "for persons!");
		}

		return result;
	}

	private int compare(WorkType wt1, WorkType wt2) {
		int result = 0;

		switch (type) {

		case DEFAULT:
		case WORK:
		case WORK_NAME:
			result = wt1.name.toLowerCase().compareTo(wt2.name.toLowerCase());
			if (result == 0)
				result = wt1.affectBand != null ? wt1.affectBand
						.compareTo(wt2.affectBand)
						: wt2.affectBand != null ? -1 : 0;
			break;

		case WORK_AFFECT_BAND:
			result = wt1.affectBand != null && wt2.affectBand != null ? wt1.affectBand
					.compareTo(wt2.affectBand) : wt1.affectBand == null
					&& wt2.affectBand != null ? -1 : wt1.affectBand != null
					&& wt2.affectBand == null ? 1 : 0;
			if (result == 0)
				result = wt1.name.toLowerCase().compareTo(
						wt2.name.toLowerCase());
			break;

		default:
			throw new RuntimeException("Invalid comparator type" + type
					+ "for work definitions!");
		}

		return result;
	}

	private int compare(EntryType et1, EntryType et2) {
		int result = 0;

		String personString1 = getPersonStringByID(et1.personId);
		String personString2 = getPersonStringByID(et2.personId);

		String workString1 = getWorkStringByID(et1.workId);
		String workString2 = getWorkStringByID(et2.workId);

		switch (type) {

		case DEFAULT:
		case DATE:
			if (et1.date != null && et2.date != null)
				result = et1.date.compareTo(et2.date);
			else if (et1.date == null && et2.date != null)
				result = -1;
			else if (et1.date != null && et2.date == null)
				result = 1;
			else
				result = 0;
			if (result == 0)
				result = personString1.toLowerCase().compareTo(
						personString2.toLowerCase());
			if (result == 0)
				result = workString1.toLowerCase().compareTo(
						workString2.toLowerCase());
			break;

		case PERSON:
		case PERSON_NAME:
		case PERSON_FIRSTNAME:
			result = personString1.toLowerCase().compareTo(
					personString2.toLowerCase());
			if (result == 0)
				result = et1.date.compareTo(et2.date);
			if (result == 0)
				result = workString1.toLowerCase().compareTo(
						workString2.toLowerCase());
			break;

		case WORK:
		case WORK_NAME:
			result = workString1.toLowerCase().compareTo(
					workString2.toLowerCase());
			if (result == 0)
				result = et1.date.compareTo(et2.date);
			if (result == 0)
				result = personString1.toLowerCase().compareTo(
						personString2.toLowerCase());
			break;

		case BAND_TYPE:
			if (et1.bandAction == null && et2.bandAction == null)
				result = 0;
			else if (et1.bandAction == null && et2.bandAction != null)
				result = -1;
			else
				result = et1.bandAction != null && et2.bandAction != null ? et1.bandAction.bandType
						.compareTo(et2.bandAction.bandType)
						: et1.bandAction == null && et2.bandAction != null ? -1
								: et1.bandAction != null
										&& et2.bandAction == null ? 1 : 0;
			if (result == 0)
				result = et1.date.compareTo(et2.date);
			if (result == 0)
				result = personString1.toLowerCase().compareTo(
						personString2.toLowerCase());
			if (result == 0)
				result = workString1.toLowerCase().compareTo(
						workString2.toLowerCase());
			break;

		default:
			throw new RuntimeException("Invalid comparator type" + type
					+ "for done works!");
		}

		return result;
	}

	private String getPersonStringByID(String pid) {
		if (root.getPersons() == null)
			return "";
		for (PersonType pt : root.getPersons().getPerson()) {
			if (pt.getId().equals(pid))
				return (pt.getFirstname() != null ? pt.getFirstname() : "")
						+ " " + (pt.getName() != null ? pt.getName() : "");
		}
		return "";
	}

	private String getWorkStringByID(String wid) {
		if (root.getWorks() == null)
			return "";
		for (WorkType wt : root.getWorks().getWork()) {
			if (wt.getId().equals(wid))
				return wt.getName() != null ? wt.getName() : "";
		}
		return "";
	}
}
