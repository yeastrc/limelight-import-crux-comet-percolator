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

import org.yeastrc.limelight.xml.crux_comet_percolator.builder.XMLBuilder;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.*;
import org.yeastrc.limelight.xml.crux_comet_percolator.reader.*;

public class ConverterRunner {

	// conveniently get a new instance of this class
	public static ConverterRunner createInstance() { return new ConverterRunner(); }
	
	
	public void convertCometPercolatorToLimelightXML(ConversionParameters conversionParameters ) throws Throwable {

		System.err.print( "Determining location of Crux output files..." );
		CruxOutputParameters cruxOutputParams = CruxDirectoryReader.processCruxDirectory( conversionParameters.getCruxOutputDirectory() );
		System.err.println( " Done." );

		System.err.print( "Reading comet params into memory..." );
		CometParameters cometParams = CometParamsReader.getCometParameters( conversionParameters.getCometParametersFile() );
		System.err.println( " Done." );
		
		System.err.print( "Reading Percolator XML data into memory..." );
		IndexedPercolatorResults percResults = PercolatorResultsReader.getPercolatorResults( conversionParameters.getPercolatorXMLFile() );
		System.err.println( " Done." );

		/*
		System.err.print( "Verifying all percolator results have comet results..." );
		CometPercolatorValidator.validateData( cometResults, percResults );
		System.err.println( " Done." );

		System.err.print( "Writing out XML..." );
		(new XMLBuilder()).buildAndSaveXML( conversionParameters, cometResults, percResults, cometParams );
		System.err.println( " Done." );
		*/
	}
}
