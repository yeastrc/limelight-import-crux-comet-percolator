package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class CometReportedPeptide {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reportedPeptideString == null) ? 0 : reportedPeptideString.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CometReportedPeptide))
			return false;
		CometReportedPeptide other = (CometReportedPeptide) obj;
		if (reportedPeptideString == null) {
			if (other.reportedPeptideString != null)
				return false;
		} else if (!reportedPeptideString.equals(other.reportedPeptideString))
			return false;
		return true;
	}
	
	private String reportedPeptideString;
	private String nakedPeptide;
	private Map<Integer, BigDecimal> mods;

	public Collection<String> getProteinMatches() {
		return proteinMatches;
	}

	public void setProteinMatches(Collection<String> proteinMatches) {
		this.proteinMatches = proteinMatches;
	}

	/**
	 * @return the reportedPeptideString
	 */
	public String getReportedPeptideString() {
		return reportedPeptideString;
	}
	/**
	 * @param reportedPeptideString the reportedPeptideString to set
	 */
	public void setReportedPeptideString(String reportedPeptideString) {
		this.reportedPeptideString = reportedPeptideString;
	}
	/**
	 * @return the nakedPeptide
	 */
	public String getNakedPeptide() {
		return nakedPeptide;
	}
	/**
	 * @param nakedPeptide the nakedPeptide to set
	 */
	public void setNakedPeptide(String nakedPeptide) {
		this.nakedPeptide = nakedPeptide;
	}
	/**
	 * @return the mods
	 */
	public Map<Integer, BigDecimal> getMods() {
		return mods;
	}
	/**
	 * @param mods the mods to set
	 */
	public void setMods(Map<Integer, BigDecimal> mods) {
		this.mods = mods;
	}

	public Collection<String> proteinMatches;


	@Override
	public String toString() {
		return "CometReportedPeptide{" +
				"reportedPeptideString='" + reportedPeptideString + '\'' +
				", nakedPeptide='" + nakedPeptide + '\'' +
				", mods=" + mods +
				", proteinMatches=" + proteinMatches +
				'}';
	}
}
