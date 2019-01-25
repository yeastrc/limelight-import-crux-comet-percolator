package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometPSM;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometReportedPeptide;

public class ReportedPeptideUtils {

	public static CometReportedPeptide getTPPReportedPeptideForTPPPSM( CometPSM psm ) throws Exception {
		
		CometReportedPeptide rp = new CometReportedPeptide();
		
		rp.setNakedPeptide( psm.getPeptideSequence() );
		rp.setMods( psm.getModifications() );
		rp.setReportedPeptideString( getReportedPeptideStringForSequenceAndMods( psm.getPeptideSequence(), psm.getModifications() ));
		rp.setProteinMatches( psm.getProteinNames() );

		return rp;
	}
	
	public static String getReportedPeptideStringForSequenceAndMods( String sequence, Map<Integer, BigDecimal> mods ) throws Exception {
		
		for( int position : mods.keySet() ) {
			if( position < 0 || position > sequence.length() )
				throw new Exception( "Position " + position + " is not in the peptide sequence." );
		}
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < sequence.length(); i++){
			int position = i + 1;
		    char c = sequence.charAt(i);        

		    sb.append( c );
		    
		    if( mods.containsKey( position ) ) {
		    	
		    	BigDecimal v = mods.get( position ).setScale( 2, RoundingMode.HALF_UP );
		    	sb.append( "[" );
		    	sb.append( v.toString() );
		    	sb.append( "]" );
		    	
		    }
		}

		// add in n-term mod
		if( mods.containsKey( 0 ) ) {
			BigDecimal v = mods.get( 0 ).setScale( 2, RoundingMode.HALF_UP );

			sb.insert( 0, "n[" + v.toString() + "]" );
		}


		// add in c-term mod
		if( mods.containsKey( sequence.length() + 1 ) ) {
			BigDecimal v = mods.get( sequence.length() + 1 ).setScale( 2, RoundingMode.HALF_UP );

			sb.append( "c[" + v.toString() + "]" );
		}

		
		return sb.toString();	
	}	
}
