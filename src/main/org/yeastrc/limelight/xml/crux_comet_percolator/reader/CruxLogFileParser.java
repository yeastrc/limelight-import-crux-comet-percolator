package org.yeastrc.limelight.xml.crux_comet_percolator.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CruxLogFileParser {

    public static String getCruxVersionFromLogFile( InputStream logFileInputStream ) throws Exception {

        try (BufferedReader br = new BufferedReader( new InputStreamReader( logFileInputStream ) ) ) {

            for ( String line = br.readLine(); line != null; line = br.readLine() ) {

                // skip immediately if it's not a line we want
                if( !line.startsWith( "INFO: Crux version: " ) )
                    continue;

                String[] fields = line.split( "\\s" );
                return fields[ fields.length - 1 ];
            }
        }

        return "unknown";
    }

    public static Map<Integer, String> getPepXMLFileIndexFromLogFile(InputStream logFileInputStream ) throws Exception {

        Map<Integer, String> pepXMLFileMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader( new InputStreamReader( logFileInputStream ) ) ) {

            for ( String line = br.readLine(); line != null; line = br.readLine() ) {

                // skip immediately if it's not a line we want
                if( !line.startsWith( "INFO: Assigning index " ) )
                    continue;

                // INFO: Assigning index 0 to 2018_March_5_UWPRQE_HatchMicro_01.pep.xml.
                Pattern p = Pattern.compile( "^INFO: Assigning index (\\d+) to (\\S+)\\.$" );

                Matcher m = p.matcher( line );

                if( m.matches() ) {
                    pepXMLFileMap.put( Integer.parseInt( m.group( 1 ) ), m.group( 2 ) );
                }

            }
        }

        return pepXMLFileMap;
    }

}
