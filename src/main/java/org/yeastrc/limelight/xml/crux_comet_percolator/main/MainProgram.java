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

package org.yeastrc.limelight.xml.crux_comet_percolator.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.yeastrc.limelight.xml.crux_comet_percolator.constants.Constants;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.ConversionParameters;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.ConversionProgramInfo;

import picocli.CommandLine;

@CommandLine.Command(name = "java -jar " + Constants.CONVERSION_PROGRAM_NAME,
		mixinStandardHelpOptions = true,
		version = Constants.CONVERSION_PROGRAM_NAME + " " + Constants.CONVERSION_PROGRAM_VERSION,
		sortOptions = false,
		synopsisHeading = "%n",
		descriptionHeading = "%n@|bold,underline Description:|@%n%n",
		optionListHeading = "%n@|bold,underline Options:|@%n",
		description = "Convert the results of a Crux (Comet + Percolator) analysis to a Limelight XML file suitable for import into Limelight.\n\n" +
				"More info at: " + Constants.CONVERSION_PROGRAM_URI
)

/**
 * @author Michael Riffle
 * @date Feb 21, 2018
 *
 */
public class MainProgram implements Runnable{

	@CommandLine.Option(names = { "-d", "--directory" }, required = true, description = "Full path to the crux output directory. E.g., /data/my_analysis/crux-output")
	private File cruxOutputDirectory;

	@CommandLine.Option(names = { "-f", "--fasta-file" }, required = true, description = "Full path to FASTA file used in the experiment. E.g., /data/yeast.fa")
	private File fastaFile;

	@CommandLine.Option(names = { "-o", "--out-file" }, required = true, description = "Full path to use for the Limelight XML output file. E.g., /data/my_analysis/crux.limelight.xml")
	private File outFile;

	@CommandLine.Option(names = { "-v", "--verbose" }, required = false, description = "If this parameter is present, error messages will include a full stacktrace. Helpful for debugging.")
	private boolean verboseRequested = false;

	@CommandLine.Option(names = { "-q", "--q-value-cutoff" }, required = false, description = "The default q-value cutoff to use for filtering data in limelight. Default is " + Constants.DEFAULT_Q_VALUE_CUTOFF)
	private BigDecimal qValueCutoff = new BigDecimal(Constants.DEFAULT_Q_VALUE_CUTOFF);

	private String[] args;

	public void run() {

		printRuntimeInfo();

		if( !fastaFile.exists() ) {
			System.err.println( "Could not find fasta file: " + fastaFile );
			System.exit( 1 );
		}

		if( !cruxOutputDirectory.exists() ) {
			System.err.println( "Could not find crux data directory: " + cruxOutputDirectory );
			System.exit( 1 );
		}

		ConversionProgramInfo cpi = ConversionProgramInfo.createInstance( String.join( " ",  args ) );        

		ConversionParameters cp = new ConversionParameters();
		cp.setConversionProgramInfo( cpi );
		cp.setFastaFile( fastaFile );
		cp.setCruxOutputDirectory( cruxOutputDirectory );
		cp.setOutputFile(outFile);
		cp.setqValueCutoff(qValueCutoff);

		try {
			ConverterRunner.createInstance().convertCruxCometPercolatorToLimelightXML(cp);
		} catch( Throwable t ) {
			System.err.println( "Encountered error during conversion: " + t.getMessage() );

			if(verboseRequested) {
				t.printStackTrace();
			}

			System.exit( 1 );
		}

		System.exit( 0 );
	}

	public static void main( String[] args ) {

		MainProgram mp = new MainProgram();
		mp.args = args;

		CommandLine.run(mp, args);
	}


	/**
	 * Print runtime info to STD ERR
	 * @throws Exception 
	 */
	public static void printRuntimeInfo() {

		try( BufferedReader br = new BufferedReader( new InputStreamReader( MainProgram.class.getResourceAsStream( "run.txt" ) ) ) ) {

			String line = null;
			while ( ( line = br.readLine() ) != null ) {

				line = line.replace( "{{URL}}", Constants.CONVERSION_PROGRAM_URI );
				line = line.replace( "{{VERSION}}", Constants.CONVERSION_PROGRAM_VERSION );

				System.err.println( line );
				
			}
			
			System.err.println( "" );

		} catch ( Exception e ) {
			System.out.println( "Error printing runtime information." );
		}
	}

}
