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

import org.apache.commons.io.FilenameUtils;
import org.yeastrc.limelight.xml.crux_comet_percolator.builder.XMLBuilder;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.*;
import org.yeastrc.limelight.xml.crux_comet_percolator.reader.*;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.CruxUtils;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.PercolatorParsingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConverterRunner {

	// conveniently get a new instance of this class
	public static ConverterRunner createInstance() { return new ConverterRunner(); }


	public void convertCruxCometPercolatorToLimelightXML(ConversionParameters conversionParameters ) throws Throwable {

		System.err.print( "Finding pepXML files..." );
		Collection<File> pepXMLFiles = CruxUtils.getPepXMLFiles(conversionParameters.getCruxOutputDirectory());
		System.err.println( " Found " + pepXMLFiles.size() + " file(s)." );

		System.err.print( "Finding percolator output file..." );
		File percoutFile = CruxUtils.getPercolatorOutputFile(conversionParameters.getCruxOutputDirectory());
		System.err.println( " Done" );

		System.err.print( "Parsing percolator log file..." );
		Map<Integer, String> pepxmlTargetIndexMap = CruxUtils.parsePercolatorLogFile(conversionParameters.getCruxOutputDirectory());
		System.err.println( " Done" );

		System.err.println( "Determining versions for pipeline..." );
		String cruxVersion = CruxUtils.getCruxVersion(conversionParameters.getCruxOutputDirectory());
		String cometVersion = CruxUtils.getCometVersion(conversionParameters.getCruxOutputDirectory());
		String percVersion = CruxUtils.getPercolatorVersion(conversionParameters.getCruxOutputDirectory());
		System.err.println( "\tCrux version: " + cruxVersion );
		System.err.println( "\tComet version: " + cometVersion );
		System.err.println( "\tPercolator version: " + percVersion );

		System.err.print( "Reading comet params..." );
		CometParameters cometParams = CometParamsReader.getCometParameters(CruxUtils.getCometParams(conversionParameters.getCruxOutputDirectory()));
		System.err.println( " Done." );
		
		System.err.print( "Reading Percolator XML data into memory..." );
		IndexedPercolatorResults percResults = PercolatorResultsReader.getPercolatorResults( percoutFile, pepxmlTargetIndexMap );
		System.err.print( " Got " + percResults.getIndexedReportedPeptideResults().size() + " peptides. " );
		System.err.println( " Done." );

		System.err.print( "Determining # of decimal places in mods in percolator peptide strings..." );
		int numberDecimalPlacesInPercolatorMod = PercolatorParsingUtils.getNumberOfDecimalPlacesInPercolatorMod(percResults);
		System.err.println( "Got: " + numberDecimalPlacesInPercolatorMod );

		Map<String, CometResults> indexedCometResults = new HashMap<>();

		// process each pepXML file separately
		for( File pepXMLFile : pepXMLFiles ) {

			String pepXMLFileName = pepXMLFile.getName();
			String pepXMLFileNameRoot = FilenameUtils.removeExtension(pepXMLFileName);	// remove the .xml
			pepXMLFileNameRoot = FilenameUtils.removeExtension(pepXMLFileNameRoot);		// remove the .pep

			if(pepXMLFileNameRoot.endsWith(".target"))
				pepXMLFileNameRoot = FilenameUtils.removeExtension(pepXMLFileNameRoot);		// remove the .target

			System.err.println( "\nProcess pepXML file: " + pepXMLFileName );

			if( !pepXMLFile.exists() ) {
				throw new FileNotFoundException( "Could not find pepXML file: " + pepXMLFile.getAbsolutePath() );
			}

			System.err.print( "\tReading Comet pepXML data into memory..." );
			CometResults cometResults = CometPepXMLResultsParser.getCometResults( pepXMLFile, cometParams, numberDecimalPlacesInPercolatorMod );
			System.err.println( " Done." );

			System.err.print( "\tVerifying all percolator results have comet results..." );
			CometPercolatorValidator.validateData( cometResults, percResults, pepXMLFileNameRoot );
			System.err.println( " Done." );

			indexedCometResults.put(pepXMLFileNameRoot, cometResults);
		}

		System.err.print( "\nWriting out XML..." );
		(new XMLBuilder()).buildAndSaveXML(
				conversionParameters,
				cometParams,
				indexedCometResults,
				percResults,
				cruxVersion,
				cometVersion,
				percVersion,
				pepXMLFiles.size() > 1
		);
		System.err.println(" Done.");

		// validate the limelight xml
		System.err.print( "Validating Limelight XML..." );
		LimelightXMLValidator.validateLimelightXML(conversionParameters.getOutputFile());
		System.err.println( " Done." );

	}
}
