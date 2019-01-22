package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class CruxOutputParameters {

    private Map<Integer,String> cruxFileIndexMap;
    private Collection<File> pepXMLFiles;
    private File percolatorOutputXMLFile;
    private String cruxVersion;

    public String getCruxVersion() {
        return cruxVersion;
    }

    public void setCruxVersion(String cruxVersion) {
        this.cruxVersion = cruxVersion;
    }

    public Map<Integer, String> getCruxFileIndexMap() {
        return cruxFileIndexMap;
    }

    public void setCruxFileIndexMap(Map<Integer, String> cruxFileIndexMap) {
        this.cruxFileIndexMap = cruxFileIndexMap;
    }

    public Collection<File> getPepXMLFiles() {
        return pepXMLFiles;
    }

    public void setPepXMLFiles(Collection<File> pepXMLFiles) {
        this.pepXMLFiles = pepXMLFiles;
    }

    public File getPercolatorOutputXMLFile() {
        return percolatorOutputXMLFile;
    }

    public void setPercolatorOutputXMLFile(File percolatorOutputXMLFile) {
        this.percolatorOutputXMLFile = percolatorOutputXMLFile;
    }
}
