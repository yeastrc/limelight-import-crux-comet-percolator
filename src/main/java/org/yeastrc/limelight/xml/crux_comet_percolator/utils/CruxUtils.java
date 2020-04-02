package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import org.yeastrc.limelight.xml.crux_comet_percolator.constants.CruxConstants;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CruxUtils {

    public static File getCometParams(File cruxDataDirectory) throws FileNotFoundException {

        File cometParamsFile = new File(cruxDataDirectory, "comet.params.txt");
        if(!cometParamsFile.exists()) {
            throw new FileNotFoundException("Could not find file " + cometParamsFile.getAbsolutePath() );
        }

        return cometParamsFile;
    }

    public static Collection<File> getPepXMLFiles(File cruxDataDirectory) throws Exception {
        Collection<File> pepXMLFiles = new HashSet<>();

        for (final File fileEntry : cruxDataDirectory.listFiles()) {
            if( fileEntry.isDirectory() ) {
                continue;
            }

            if( fileEntry.getName().endsWith( CruxConstants.pepXMLSuffix ) ) {
                pepXMLFiles.add( fileEntry );
            }
        }

        if(pepXMLFiles.size() < 1) {
            throw new Exception("Could not find any pepXML files in " + cruxDataDirectory.getAbsolutePath() + "." );
        }

        return pepXMLFiles;
    }

    public static File getPercolatorOutputFile(File cruxDataDirectory) throws Exception {
        File percout = new File( cruxDataDirectory, CruxConstants.cruxOutputPercolatorXMLFileName );
        if(!percout.exists()) {
            throw new Exception("Could not find percolator output file: " + percout.getAbsolutePath() );
        }

        return percout;
    }

    public static File getPercolatorLogFile(File cruxDataDirectory) {
        File file = new File( cruxDataDirectory, CruxConstants.percolatorLogFile );
        return file;
    }

    public static File getCometLogFile(File cruxDataDirectory) {
        File file = new File( cruxDataDirectory, CruxConstants.cometLogFile );
        return file;
    }

    public static String getCruxVersion(File cruxDataDirectory) throws IOException {

        File percLogFile = getPercolatorLogFile(cruxDataDirectory);
        if(!percLogFile.exists()) {
            System.err.println("  Warning: " + percLogFile.getAbsolutePath() + " does not exist. Cannot get crux version." );
            return "Unknown";
        }

        Pattern p = Pattern.compile("^INFO: Crux version: (.+)$");

        try (BufferedReader br = new BufferedReader( new FileReader( percLogFile ) ) ) {
            for ( String line = br.readLine(); line != null; line = br.readLine() ) {
                if(line.startsWith("INFO: Crux version:")) {
                    Matcher m = p.matcher( line );
                    if( m.matches() ) {
                        return m.group(1);
                    }
                }
            }
        }

        return "Unknown";
    }

    public static String getPercolatorVersion(File cruxDataDirectory) throws IOException {

        File percLogFile = getPercolatorLogFile(cruxDataDirectory);
        if(!percLogFile.exists()) {
            System.err.println("  Warning: " + percLogFile.getAbsolutePath() + " does not exist. Cannot get percolator version." );
            return "Unknown";
        }

        Pattern p = Pattern.compile("^INFO: Percolator version (.+)$");

        try (BufferedReader br = new BufferedReader( new FileReader( percLogFile ) ) ) {
            for ( String line = br.readLine(); line != null; line = br.readLine() ) {
                if(line.startsWith("INFO: Percolator version")) {
                    Matcher m = p.matcher( line );
                    if( m.matches() ) {
                        return m.group(1);
                    }
                }
            }
        }

        return "Unknown";
    }

    public static String getCometVersion(File cruxDataDirectory) throws IOException {

        File cometLogFile = getCometLogFile(cruxDataDirectory);
        if(!cometLogFile.exists()) {
            System.err.println("  Warning: " + cometLogFile.getAbsolutePath() + " does not exist. Cannot get comet version." );
            return "Unknown";
        }

        Pattern p = Pattern.compile("^INFO:  Comet version \"(.+)\".*$");

        try (BufferedReader br = new BufferedReader( new FileReader( cometLogFile ) ) ) {
            for ( String line = br.readLine(); line != null; line = br.readLine() ) {
                if(line.startsWith("INFO:  Comet version")) {
                    Matcher m = p.matcher( line );
                    if( m.matches() ) {
                        return m.group(1);
                    }
                }
            }
        }

        return "Unknown";
    }

}
