package org.yeastrc.limelight.xml.crux_comet_percolator.reader;

import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometReportedPeptide;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometResults;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.IndexedPercolatorPeptideData;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.IndexedPercolatorResults;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.CometParsingUtils;

import java.util.HashMap;
import java.util.Map;

public class CometPercolatorValidator {

	/**
	 * Ensure all percolator results have a result in the comet data
	 *
	 * @param cometResults
	 * @param percolatorResults
	 * @throws Exception if the data could not be validated
	 */
	public static void validateData( CometResults cometResults, IndexedPercolatorResults percolatorResults, String pepXMLFileRoot ) throws Exception {

		Map<String, CometReportedPeptide> lookupMap = new HashMap<>();
		for(CometReportedPeptide cometReportedPeptide : cometResults.getPeptidePSMMap().keySet()) {
			lookupMap.put(cometReportedPeptide.getReportedPeptideString(), cometReportedPeptide);
		}

		for( String percolatorReportedPeptide : percolatorResults.getIndexedReportedPeptideResults().keySet() ) {

			IndexedPercolatorPeptideData indexedPercolatorPeptideData = percolatorResults.getIndexedReportedPeptideResults().get( percolatorReportedPeptide );

			// There are no percolator data for this peptide in this file index
			if( !indexedPercolatorPeptideData.getPercolatorPSMs().containsKey( pepXMLFileRoot ) ) {
				continue;
			}

			CometReportedPeptide cometReportedPeptide = lookupMap.get(percolatorReportedPeptide);

			if( cometReportedPeptide == null ) {
				throw new Exception( "Error: Comet results not found for peptide: " + percolatorReportedPeptide );
			}

			for( int scanNumber : indexedPercolatorPeptideData.getPercolatorPSMs().get( pepXMLFileRoot ).keySet() ) {

				if( !cometResults.getPeptidePSMMap().get( cometReportedPeptide ).containsKey( scanNumber ) ) {
					throw new Exception( "Error: Could not find PSM data for scan number " + scanNumber + " in comet results for peptide: " + percolatorReportedPeptide );
				}
			}

		}

	}
	
}
