package org.yeastrc.limelight.xml.crux_comet_percolator.reader;

import org.yeastrc.limelight.xml.crux_comet_percolator.constants.CruxConstants;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CruxOutputParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * Given a directory, ascertain the structure of the Crux run:
 *  1. Find all pepxml files
 *  2. find index number for each pepxml file in crux log (if necessary)
 *  3. find percolator output xml file
 *
 */
public class CruxDirectoryReader {

    public static CruxOutputParameters processCruxDirectory( File workingDirectory ) throws Exception {


        File cruxOutputDirectory = getCruxOutputDirectory( workingDirectory );

        File cruxOutputLogFile = getCruxOutputLogFile( cruxOutputDirectory );
        File cruxOutputPercolatorXMLFile = getCruxOutputPercolatorXMLFile( cruxOutputDirectory );

        CruxOutputParameters cop = new CruxOutputParameters();

        cop.setPepXMLFiles( getPepXMLFiles( workingDirectory ) );
        cop.setPercolatorOutputXMLFile( cruxOutputPercolatorXMLFile );
        cop.setCruxOutputDirectory( cruxOutputDirectory );

        try ( InputStream is = new FileInputStream( cruxOutputLogFile ) ) {
            cop.setCruxVersion( CruxLogFileParser.getCruxVersionFromLogFile( is ) );
        }

        try ( InputStream is = new FileInputStream( cruxOutputLogFile ) ) {
            cop.setCruxFileIndexMap( CruxLogFileParser.getPepXMLFileIndexFromLogFile( is ) );
        }

        validatePepXMLFileIndex( cop );

        return cop;
    }

    private static void validatePepXMLFileIndex( CruxOutputParameters cop ) throws Exception {

        for( String filename : cop.getCruxFileIndexMap().values() ) {
            if( !fileCollectionContainsFile( cop.getPepXMLFiles(), filename ) ) {
                throw new Exception( "Error: " + filename + " + was assigned an index in Crux log file, but no pepXML was found." );
            }
        }

        for( File pepXMLFile : cop.getPepXMLFiles() ) {
            if( !cop.getCruxFileIndexMap().containsValue( pepXMLFile.getName() ) ) {
                throw new Exception( "Error: pepXML file (" + pepXMLFile.getName() + ") was found, but was not assigned an index by Crux." );
            }
        }

    }

    private static boolean fileCollectionContainsFile( Collection<File> files, String filename ) {

        for( File file : files ) {
            if( file.getName().equals( filename ) ) {
                return true;
            }
        }

        return false;

    }

    private static File getCruxOutputPercolatorXMLFile( File cruxOutputDirectory ) throws Exception {

        File cruxOutputPercolatorXMLFile = new File( cruxOutputDirectory, CruxConstants.cruxOutputPercolatorXMLFileName );

        if( !cruxOutputPercolatorXMLFile.exists() ) {
            throw new Exception( "could not find Percolator output XML file" + cruxOutputPercolatorXMLFile.getAbsolutePath() );
        }

        return cruxOutputPercolatorXMLFile;

    }

    private static File getCruxOutputLogFile( File cruxOutputDirectory ) throws Exception {

        File cruxOutputLogFile = new File( cruxOutputDirectory, CruxConstants.cruxOutputLogFileName );

        if( !cruxOutputLogFile.exists() ) {
            throw new Exception( "Could not find Crux output log file" + cruxOutputLogFile.getAbsolutePath() );
        }

        return cruxOutputLogFile;

    }

    private static File getCruxOutputDirectory( File workingDirectory ) throws Exception {

        File directory = new File( workingDirectory, CruxConstants.cruxOutputDirectoryName );

        if( !directory.exists() ) {
            throw new Exception( "Could not find directory: " + directory.getAbsolutePath() );
        }

        if( !directory.isDirectory() ) {
            throw new Exception( directory.getAbsolutePath() + " is not a directory." );
        }

        return directory;

    }

    private static File getWorkingDirectory( String directoryName ) throws Exception {

        File directory = new File( directoryName );

        if( !directory.exists() ) {
            throw new Exception( "Could not find directory: " + directoryName );
        }

        if( !directory.isDirectory() ) {
            throw new Exception( directoryName + " is not a directory." );
        }

        return directory;
    }

    /**
     *
     * @param directory
     * @return
     * @throws Exception
     */
    private static Collection<File> getPepXMLFiles( File directory ) throws Exception {

        Collection<File> pepXMLFiles = new HashSet<>();


        for (final File fileEntry : directory.listFiles()) {
            if( fileEntry.isDirectory() ) {
                continue;
            }

            if( fileEntry.getName().endsWith( CruxConstants.pepXMLSuffix ) ) {
                pepXMLFiles.add( fileEntry );
            }
        }


        return pepXMLFiles;
    }

}
