package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.util.Map;

public class CometParameters {

	@Override
	public String toString() {
		return "CometParameters{" +
				"staticMods=" + staticMods +
				", decoyPrefix='" + decoyPrefix + '\'' +
				'}';
	}

	/**
	 * @return the staticMods
	 */
	public Map<Character, Double> getStaticMods() {
		return staticMods;
	}
	/**
	 * @param staticMods the staticMods to set
	 */
	public void setStaticMods(Map<Character, Double> staticMods) {
		this.staticMods = staticMods;
	}

	public String getDecoyPrefix() {
		return decoyPrefix;
	}

	public void setDecoyPrefix(String decoyPrefix) {
		this.decoyPrefix = decoyPrefix;
	}

	private Map<Character, Double> staticMods;
	private String decoyPrefix;
	
}
