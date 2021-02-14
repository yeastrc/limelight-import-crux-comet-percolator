package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import org.yeastrc.limelight.xml.crux_comet_percolator.constants.CometConstants;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometPSM;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometParameters;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometReportedPeptide;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometResults;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class CometParsingUtils {
	
	public static CometReportedPeptide getCometReportedPeptideForString( String reportedPeptide, Set<CometReportedPeptide> allCometReportedPeptides ) {
		
		for( CometReportedPeptide cometPeptide : allCometReportedPeptides ) {
			if( cometPeptide.getReportedPeptideString().equals( reportedPeptide ) ) {
				return cometPeptide;
			}
		}
		
		return null;
	}

	public static CometReportedPeptide getCometReportedPeptideForString( String reportedPeptide, CometResults cometResults ) {

		for( CometReportedPeptide cometPeptide : cometResults.getPeptidePSMMap().keySet() ) {
			if( cometPeptide.getReportedPeptideString().equals( reportedPeptide ) ) {
				return cometPeptide;
			}
		}

		return null;
	}

	/**
	 * Comet reports n-terminal mod mass as the mod mass plus Hydrogen (don't ask). So subtract hydrogen
	 * from the reported mod mass to get real mod mass.
	 *
	 * @param reportedModMass
	 * @return
	 */
	public static BigDecimal getNTerminalModMass( BigDecimal reportedModMass ) {
		return reportedModMass.subtract(CometConstants.COMET_MASS_HYDROGEN_MONO.setScale(4, RoundingMode.HALF_UP) );
	}

	/**
	 * Comet reports c-terminal mod mass as the mod mass plus Hydrogen plus Oxygen (don't ask). So subtract hydrogen
	 * and oxygen from the reported mod mass to get real mod mass.
	 *
	 * @param reportedModMass
	 * @return
	 */
	public static BigDecimal getCTerminalModMass( BigDecimal reportedModMass ) {
		return reportedModMass.subtract( CometConstants.COMET_MASS_HYDROGEN_MONO.setScale(4, RoundingMode.HALF_UP) ).subtract( CometConstants.COMET_MASS_OXYGEN_MONO );
	}

	/**
	 * Return true if the mod at the reported position is an N-terminal mod in that peptide
	 *
	 * @param peptide
	 * @param position
	 * @return
	 */
	public static boolean isNTerminalMod( String peptide, int position ) {
		if( position == 0 ) { return true; }
		return false;
	}

	/**
	 * Return true if the mod at the reported position is a C-terminal mod in that peptide
	 *
	 * @param peptide
	 * @param position
	 * @return
	 */
	public static boolean isCTerminalMod( String peptide, int position ) {
		if( position == peptide.length() + 1 ) { return true; }
		return false;
	}

	/**
	 * Return true if the given protein name is a decoy protein hit, given the comet parameters
	 * used in the search.
	 *
	 * @param proteinName
	 * @param cometParams
	 * @return
	 */
	public static boolean isDecoyProtein(String proteinName, CometParameters cometParams ) {
		if( cometParams.getDecoyPrefix() != null ) {
			if( proteinName.startsWith( cometParams.getDecoyPrefix() ) ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the calculated m/z for a precursor for a psm that is (neutral mass + charge * hydrogen mass) / charge
	 *
	 * @param psm
	 * @return
	 */
	public static BigDecimal getObservedMoverZForPsm(CometPSM psm) {

		final double charge = psm.getCharge();
		final double neutralMass = psm.getPrecursorNeutralMass().doubleValue();
		final double observedMoverZ = (CometConstants.COMET_MASS_HYDROGEN_MONO.doubleValue() * charge + neutralMass) / charge;
		
		return BigDecimal.valueOf(observedMoverZ);
	}

}
