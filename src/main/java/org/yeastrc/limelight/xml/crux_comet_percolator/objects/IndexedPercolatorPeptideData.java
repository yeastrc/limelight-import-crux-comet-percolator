package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.util.Map;

public class IndexedPercolatorPeptideData {

	private PercolatorPeptideScores percolatorPeptideScores;

	/**
	 * The percolator PSMs. First keyed by file index, then by scan number
	 */
	private Map<Integer, Map<Integer,PercolatorPSM>> percolatorPSMs;
	
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
	 * Get the percolator PSMs. First keyed by file index, then by scan number
	 * @return
	 */
	public Map<Integer, Map<Integer, PercolatorPSM>> getPercolatorPSMs() {
		return percolatorPSMs;
	}

	/**
	 * Set the percolator PSMs. First keyed by file index, then by scan number
	 * @param percolatorPSMs
	 */
	public void setPercolatorPSMs(Map<Integer, Map<Integer, PercolatorPSM>> percolatorPSMs) {
		this.percolatorPSMs = percolatorPSMs;
	}
}
