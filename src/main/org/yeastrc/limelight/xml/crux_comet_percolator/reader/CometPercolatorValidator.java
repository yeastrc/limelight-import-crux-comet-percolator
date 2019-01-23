package org.yeastrc.limelight.xml.crux_comet_percolator.reader;

import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometReportedPeptide;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometResults;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.IndexedPercolatorResults;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.CometParsingUtils;

public class CometPercolatorValidator {

	/**
	 * Ensure all percolator results have a result in the comet data
	 *
	 * @param cometResults
	 * @param percolatorResults
	 * @throws Exception if the data could not be validated
	 */
	public static void validateData( CometResults cometResults, IndexedPercolatorResults percolatorResults ) throws Exception {
		
		for( String percolatorReportedPeptide : percolatorResults.getReportedPeptideResults().keySet() ) {
			
			CometReportedPeptide cometReportedPeptide = CometParsingUtils.getCometReportedPeptideForString( percolatorReportedPeptide, cometResults );

			if( cometReportedPeptide == null ) {
				throw new Exception( "Error: Comet results not found for peptide: " + percolatorReportedPeptide );
			}
			
			for( int scanNumber : percolatorResults.getReportedPeptideResults().get( percolatorReportedPeptide ).getPercolatorPSMs().keySet() ) {
				
				if( !cometResults.getPeptidePSMMap().get( cometReportedPeptide ).containsKey( scanNumber ) ) {
					throw new Exception( "Error: Could not find PSM data for scan number " + scanNumber + " in percolator results for peptide: " + percolatorReportedPeptide );
				}
			}
			
		}
	}
	
}
