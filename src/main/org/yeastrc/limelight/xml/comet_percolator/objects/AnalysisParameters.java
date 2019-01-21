package org.yeastrc.limelight.xml.comet_percolator.objects;

import java.io.File;

public class AnalysisParameters {
	
	public File getFastaFile() {
		return fastaFile;
	}
	public void setFastaFile(File fastaFile) {
		this.fastaFile = fastaFile;
	}
	public File getCometParametersFile() {
		return cometParametersFile;
	}
	public void setCometParametersFile(File cometParametersFile) {
		this.cometParametersFile = cometParametersFile;
	}
	public File getPepXMLFile() {
		return pepXMLFile;
	}
	public void setPepXMLFile(File pepXMLFile) {
		this.pepXMLFile = pepXMLFile;
	}
	
	private File fastaFile;
	private File cometParametersFile;
	private File pepXMLFile;
	
}
