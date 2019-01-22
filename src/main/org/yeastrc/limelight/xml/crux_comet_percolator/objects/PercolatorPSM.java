/*
 * Original author: Michael Riffle <mriffle .at. uw.edu>
 *                  
 * Copyright 2018 University of Washington - Seattle, WA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

public class PercolatorPSM {
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PercolatorPSM [svmScore=" + svmScore + ", qValue=" + qValue + ", pValue=" + pValue + ", pep=" + pep
				+ ", reportedPeptide=" + reportedPeptide + ", psmId=" + psmId + ", scanNumber=" + scanNumber + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((psmId == null) ? 0 : psmId.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		PercolatorPSM other = (PercolatorPSM) obj;
		if (psmId == null) {
			if (other.psmId != null)
				return false;
		} else if (!psmId.equals(other.psmId))
			return false;
		return true;
	}
	
	/**
	 * @return the svmScore
	 */
	public double getSvmScore() {
		return svmScore;
	}
	/**
	 * @param svmScore the svmScore to set
	 */
	public void setSvmScore(double svmScore) {
		this.svmScore = svmScore;
	}
	/**
	 * @return the qValue
	 */
	public double getqValue() {
		return qValue;
	}
	/**
	 * @param qValue the qValue to set
	 */
	public void setqValue(double qValue) {
		this.qValue = qValue;
	}
	/**
	 * @return the pValue
	 */
	public double getpValue() {
		return pValue;
	}
	/**
	 * @param pValue the pValue to set
	 */
	public void setpValue(double pValue) {
		this.pValue = pValue;
	}
	/**
	 * @return the pep
	 */
	public double getPep() {
		return pep;
	}
	/**
	 * @param pep the pep to set
	 */
	public void setPep(double pep) {
		this.pep = pep;
	}
	/**
	 * @return the reportedPeptide
	 */
	public String getReportedPeptide() {
		return reportedPeptide;
	}
	/**
	 * @param reportedPeptide the reportedPeptide to set
	 */
	public void setReportedPeptide(String reportedPeptide) {
		this.reportedPeptide = reportedPeptide;
	}
	/**
	 * @return the psmId
	 */
	public String getPsmId() {
		return psmId;
	}
	/**
	 * @param psmId the psmId to set
	 */
	public void setPsmId(String psmId) {
		this.psmId = psmId;
	}
	/**
	 * @return the scanNumber
	 */
	public int getScanNumber() {
		return scanNumber;
	}
	/**
	 * @param scanNumber the scanNumber to set
	 */
	public void setScanNumber(int scanNumber) {
		this.scanNumber = scanNumber;
	}
	private double svmScore;
	private double qValue;
	private double pValue;
	private double pep;
	private String reportedPeptide;
	private String psmId;
	private int scanNumber;
}
