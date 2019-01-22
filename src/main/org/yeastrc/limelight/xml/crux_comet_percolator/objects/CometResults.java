package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.util.Map;

public class CometResults {

	private String cometVersion;
	private Map<CometReportedPeptide, Map<Integer, CometPSM>> peptidePSMMap;
	
	/**
	 * @return the cometVersion
	 */
	public String getCometVersion() {
		return cometVersion;
	}
	/**
	 * @param cometVersion the cometVersion to set
	 */
	public void setCometVersion(String cometVersion) {
		this.cometVersion = cometVersion;
	}
	/**
	 * @return the peptidePSMMap
	 */
	public Map<CometReportedPeptide, Map<Integer, CometPSM>> getPeptidePSMMap() {
		return peptidePSMMap;
	}
	/**
	 * @param peptidePSMMap the peptidePSMMap to set
	 */
	public void setPeptidePSMMap(Map<CometReportedPeptide, Map<Integer, CometPSM>> peptidePSMMap) {
		this.peptidePSMMap = peptidePSMMap;
	}
	
}
