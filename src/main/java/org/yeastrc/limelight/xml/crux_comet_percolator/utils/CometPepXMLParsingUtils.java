package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import static java.lang.Math.toIntExact;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometPSM;

import net.systemsbiology.regis_web.pepxml.AltProteinDataType;
import net.systemsbiology.regis_web.pepxml.ModInfoDataType;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis;
import net.systemsbiology.regis_web.pepxml.NameValueType;
import net.systemsbiology.regis_web.pepxml.ModInfoDataType.ModAminoacidMass;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SearchSummary;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.CometParameters;

public class CometPepXMLParsingUtils {

	/**
	 * Attempt to get the comet version from the pepXML file. Returns "Unknown" if not found.
	 * 
	 * @param msAnalysis
	 * @return
	 */
	public static String getCometVersionFromXML( MsmsPipelineAnalysis msAnalysis ) {
		
		for( MsmsRunSummary runSummary : msAnalysis.getMsmsRunSummary() ) {
			for( SearchSummary searchSummary : runSummary.getSearchSummary() ) {

				if( searchSummary.getSearchEngine().value().equals( "Comet" ) ) {
					return searchSummary.getSearchEngineVersion();
				}
			
			}
		}
		
		return "Unknown";
	}

	/**
	 * Return true if this searchHit is a decoy. This means that it only matches
	 * decoy proteins.
	 * 
	 * @param searchHit
	 * @return
	 */
	public static boolean searchHitIsDecoy( SearchHit searchHit, CometParameters cometParams ) {
		
		String protein = searchHit.getProtein();

		if( CometParsingUtils.isDecoyProtein( protein, cometParams ) ) {

			if( searchHit.getAlternativeProtein() != null ) {
				for( AltProteinDataType ap : searchHit.getAlternativeProtein() ) {

					if( !CometParsingUtils.isDecoyProtein( ap.getProtein(), cometParams ) ) {
						return false;
					}
				}
			}
			
			return true;			
		}
		
		return false;
	}
	
	/**
	 * Return the top-most parent element of the pepXML file as a JAXB object.
	 * 
	 * @param file
	 * @return
	 * @throws Throwable
	 */
	public static MsmsPipelineAnalysis getMSmsPipelineAnalysis( File file ) throws Throwable {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(MsmsPipelineAnalysis.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		MsmsPipelineAnalysis msAnalysis = (MsmsPipelineAnalysis)jaxbUnmarshaller.unmarshal( file );
		
		return msAnalysis;
	}
	
	/**
	 * Get the retention time from the spectrumQuery JAXB object
	 * 
	 * @param spectrumQuery
	 * @return
	 */
	public static BigDecimal getRetentionTimeFromSpectrumQuery( SpectrumQuery spectrumQuery ) {
		return spectrumQuery.getRetentionTimeSec();
	}
	
	/**
	 * Get the neutral mass from the spectrumQuery JAXB object
	 * 
	 * @param spectrumQuery
	 * @return
	 */
	public static BigDecimal getNeutralMassFromSpectrumQuery( SpectrumQuery spectrumQuery ) {
		return spectrumQuery.getPrecursorNeutralMass();
	}
	
	/**
	 * Get the scan number from the spectrumQuery JAXB object
	 * 
	 * @param spectrumQuery
	 * @return
	 */
	public static int getScanNumberFromSpectrumQuery( SpectrumQuery spectrumQuery ) {
		return toIntExact( spectrumQuery.getStartScan() );
	}
	
	/**
	 * Get the charge from the spectrumQuery JAXB object
	 * 
	 * @param spectrumQuery
	 * @return
	 */
	public static int getChargeFromSpectrumQuery( SpectrumQuery spectrumQuery ) {
		return spectrumQuery.getAssumedCharge().intValue();
	}

    /**
     * Get a TPPPSM (psm object) from the supplied searchHit JAXB object.
     *
     * If the searchHit has no peptideprophet score, null is returned.
     *
     * @param searchHit
     * @param charge
     * @param scanNumber
     * @param obsMass
     * @param retentionTime
     * @return
     * @throws Throwable
     */
	public static CometPSM getPsmFromSearchHit(
			SearchHit searchHit,
			int charge,
			int scanNumber,
			BigDecimal obsMass,
			BigDecimal retentionTime,
			CometParameters cometParams ) throws Throwable {
				
		CometPSM psm = new CometPSM();
		
		psm.setCharge( charge );
		psm.setScanNumber( scanNumber );
		psm.setPrecursorNeutralMass( obsMass );
		psm.setRetentionTime( retentionTime );
		psm.setHitRank( getHitRankForSearchHit( searchHit ) );
		
		psm.setPeptideSequence( searchHit.getPeptide() );
		
		psm.setxCorr( getScoreForType( searchHit, "xcorr" ) );
		psm.setDeltaCn( getScoreForType( searchHit, "deltacn" ) );
		psm.setSpScore( getScoreForType( searchHit, "spscore" ) );
		psm.setSpRank( getScoreForType( searchHit, "sprank" ) );
		psm.seteValue( getScoreForType( searchHit, "expect" ) );

		// detalcnstar may not be present
		try {
			psm.setDeltaCnStar(getScoreForType(searchHit, "deltacnstar"));
		} catch(Exception e) {
			;
		}

		try {
			psm.setProteinNames( getProteinNamesForSearchHit( searchHit, cometParams ) );
		} catch( Throwable t ) {

			String error = "Error getting protein names for PSM.\n";
			error += "Psm: " + psm + "\n";
			error += "Error: " + t.getMessage();

			System.err.println( error );
			throw t;
		}

		try {
			psm.setModifications( getModificationsForSearchHit( searchHit ) );
		} catch( Throwable t ) {

			String error = "Error getting mods for PSM.\n";
			error += "Psm: " + psm + "\n";
			error += "Error: " + t.getMessage();

			System.err.println( error );
			throw t;
		}

		return psm;
	}
	
	public static int getHitRankForSearchHit( SearchHit searchHit ) throws Exception {
		
		return toIntExact( searchHit.getHitRank() );
		
	}

	/**
	 * Get the requested score from the searchHit JAXB object
	 *
	 * @param searchHit
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public static BigDecimal getScoreForType( SearchHit searchHit, String type ) throws Throwable {
		
		for( NameValueType searchScore : searchHit.getSearchScore() ) {
			if( searchScore.getName().equals( type ) ) {
				
				return new BigDecimal( searchScore.getValueAttribute() );
			}
		}
		
		throw new Exception( "Could not find a score of name: " + type + " for PSM..." );		
	}

	/**
	 * Get the variable modifications from the supplied searchHit JAXB object
	 *
	 * @param searchHit
	 * @return
	 * @throws Throwable
	 */
	public static Map<Integer, BigDecimal> getModificationsForSearchHit( SearchHit searchHit ) throws Throwable {
		
		Map<Integer, BigDecimal> modMap = new HashMap<>();
		
		ModInfoDataType mofo = searchHit.getModificationInfo();
		if( mofo != null ) {
			for( ModAminoacidMass mod : mofo.getModAminoacidMass() ) {
				
				if( mod.getVariable() != null ) {
					modMap.put( mod.getPosition().intValueExact(), BigDecimal.valueOf( mod.getVariable() ) );
				}
			}

			// set n-term mod at position 0
			if( mofo.getModNtermMass() != null ) {
				modMap.put( 0, CometParsingUtils.getNTerminalModMass( BigDecimal.valueOf( mofo.getModNtermMass() ) ) );
			}

			// set c-term mod at peptide_length + 1
			if( mofo.getModCtermMass() != null ) {
				modMap.put( searchHit.getPeptide().length() + 1, CometParsingUtils.getCTerminalModMass( BigDecimal.valueOf( mofo.getModNtermMass() ) ) );
			}
		}

		
		return modMap;
	}

	public static Collection<String> getProteinNamesForSearchHit(SearchHit searchHit, CometParameters cometParams ) throws Throwable {

		Collection<String> proteins = new HashSet<>();

		if( searchHit.getProtein() != null && !CometParsingUtils.isDecoyProtein( searchHit.getProtein(), cometParams ) ) {
			proteins.add( searchHit.getProtein());
		}

		if( searchHit.getAlternativeProtein() != null && searchHit.getAlternativeProtein().size() > 0 ) {

			for( AltProteinDataType apdt : searchHit.getAlternativeProtein() ) {
				if( !CometParsingUtils.isDecoyProtein( apdt.getProtein(), cometParams ) ) {
					proteins.add( apdt.getProtein() );
				}
			}

		}

		if( proteins.size() < 1 ) {
			throw new Exception( "Found zero target proteins for searchHit." );
		}

		return proteins;
	}


	
}
