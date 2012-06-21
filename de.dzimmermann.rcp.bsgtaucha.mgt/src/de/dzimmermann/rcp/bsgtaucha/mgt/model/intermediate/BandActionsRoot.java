package de.dzimmermann.rcp.bsgtaucha.mgt.model.intermediate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandActionType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandTypes;

@XmlRootElement
@XmlType(name = "band-action-list", propOrder = { "bandActions" })
@XmlAccessorType(value = XmlAccessType.FIELD)
public class BandActionsRoot {

	@XmlElement(name = "band-action")
	private List<BandAction> bandActions;

	public List<BandAction> getBandActions() {
		if (bandActions == null) {
			bandActions = new ArrayList<BandActionsRoot.BandAction>();
		}
		return bandActions;
	}

	@XmlType(name = "bandAction", propOrder = { "personString", "cause",
			"comment", "trackString", "track" })
	@XmlAccessorType(value = XmlAccessType.FIELD)
	public static class BandAction {

		@XmlAttribute(required = true)
		@XmlJavaTypeAdapter(DateAdapter.class)
		private Date date;
		@XmlAttribute(name = "date-string", required = true)
		private String dateString;
		@XmlAttribute(required = true)
		private float time;
		@XmlElement(name = "person-string", required = true)
		private String personString;
		@XmlAttribute(name = "band-added", required = true)
		private boolean bandAdded;
		@XmlAttribute(required = true)
		private Integer amount;
		@XmlElement(required = true)
		private String cause;
		@XmlElement
		private String comment;
		@XmlAttribute(name = "band-type", required = true)
		private BandTypes bandTypes;
		@XmlAttribute(required = true)
		private String type;
		@XmlAttribute(name = "full-amount-c", required = true)
		private Integer fullAmountC;
		@XmlAttribute(name = "full-amount-l", required = true)
		private Integer fullAmountL;
		@XmlElement
		private Set<Integer> track;
		@XmlElement(name = "track-string")
		private String trackString;

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getDateString() {
			return dateString;
		}

		public void setDateString(String dateString) {
			this.dateString = dateString;
		}

		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public String getPersonString() {
			return personString;
		}

		public void setPersonString(String personString) {
			this.personString = personString;
		}

		public boolean isBandAdded() {
			return bandAdded;
		}

		public void setBandAdded(boolean bandAdded) {
			this.bandAdded = bandAdded;
		}

		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer amount) {
			this.amount = amount;
		}

		public String getCause() {
			return cause;
		}

		public void setCause(String cause) {
			this.cause = cause;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public BandTypes getBandTypes() {
			return bandTypes;
		}

		public void setBandTypes(BandTypes bandTypes) {
			this.bandTypes = bandTypes;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getFullAmountC() {
			return fullAmountC;
		}

		public void setFullAmountC(Integer fullAmountC) {
			this.fullAmountC = fullAmountC;
		}

		public Integer getFullAmountL() {
			return fullAmountL;
		}

		public void setFullAmountL(Integer fullAmountL) {
			this.fullAmountL = fullAmountL;
		}

		public Set<Integer> getTrack() {
			if (track == null)
				track = new TreeSet<Integer>();
			return track;
		}

		public String getTrackString() {
			return trackString;
		}

		public void setTrackString(String trackString) {
			this.trackString = trackString;
		}

		public static String getTrackString(BandActionType ba) {
			StringBuilder sb = new StringBuilder();
			if (ba != null && !ba.getTrack().isEmpty()) {
				int i = 0;
				for (Integer value : ba.getTrack()) {
					sb.append(value);
					if ((i + 1) < ba.getTrack().size())
						sb.append(", ");
					i++;
				}
			}
			return sb.toString();
		}
	}
}
