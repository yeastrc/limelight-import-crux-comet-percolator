package org.yeastrc.limelight.xml.comet_percolator.objects;

import java.util.Map;

public class PercolatorPeptideData {

	private PercolatorPeptideScores percolatorPeptideScores;
	private Map<Integer, PercolatorPSM> percolatorPSMs;
	
	/**
	 * @return the percolatorPeptideScores
	 */
	public PercolatorPeptideScores getPercolatorPeptideScores() {
		return percolatorPeptideScores;
	}
	/**
	 * @param percolatorPeptideScores the percolatorPeptideScores to set
	 */
	public void setPercolatorPeptideScores(PercolatorPeptideScores percolatorPeptideScores) {
		this.percolatorPeptideScores = percolatorPeptideScores;
	}
	/**
	 * @return the percolatorPSMs
	 */
	public Map<Integer, PercolatorPSM> getPercolatorPSMs() {
		return percolatorPSMs;
	}
	/**
	 * @param percolatorPSMs the percolatorPSMs to set
	 */
	public void setPercolatorPSMs(Map<Integer, PercolatorPSM> percolatorPSMs) {
		this.percolatorPSMs = percolatorPSMs;
	}
	
	
}
