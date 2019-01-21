package org.yeastrc.limelight.xml.comet_percolator.builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.yeastrc.limelight.limelight_import.api.xml_dto.*;
import org.yeastrc.limelight.limelight_import.api.xml_dto.ReportedPeptide.ReportedPeptideAnnotations;
import org.yeastrc.limelight.limelight_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.limelight.limelight_import.api.xml_dto.SearchProgram.ReportedPeptideAnnotationTypes;
import org.yeastrc.limelight.limelight_import.create_import_file_from_java_objects.main.CreateImportFileFromJavaObjectsMain;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PSMAnnotationTypeSortOrder;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PSMAnnotationTypes;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PSMDefaultVisibleAnnotationTypes;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PeptideAnnotationTypeSortOrder;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PeptideAnnotationTypes;
import org.yeastrc.limelight.xml.comet_percolator.annotation.PeptideDefaultVisibleAnnotationTypes;
import org.yeastrc.limelight.xml.comet_percolator.constants.Constants;
import org.yeastrc.limelight.xml.comet_percolator.objects.CometPSM;
import org.yeastrc.limelight.xml.comet_percolator.objects.CometParameters;
import org.yeastrc.limelight.xml.comet_percolator.objects.CometReportedPeptide;
import org.yeastrc.limelight.xml.comet_percolator.objects.CometResults;
import org.yeastrc.limelight.xml.comet_percolator.objects.ConversionParameters;
import org.yeastrc.limelight.xml.comet_percolator.objects.PercolatorPSM;
import org.yeastrc.limelight.xml.comet_percolator.objects.PercolatorPeptideData;
import org.yeastrc.limelight.xml.comet_percolator.objects.PercolatorResults;
import org.yeastrc.limelight.xml.comet_percolator.utils.CometParsingUtils;

public class XMLBuilder {

	public void buildAndSaveXML( ConversionParameters conversionParameters,
			                     CometResults cometResults,
			                     PercolatorResults percolatorResults,
			                     CometParameters cometParameters )
    throws Exception {

		LimelightInput limelightInputRoot = new LimelightInput();

		limelightInputRoot.setFastaFilename( conversionParameters.getFastaFile().getName() );
		
		// add in the conversion program (this program) information
		ConversionProgramBuilder.createInstance().buildConversionProgramSection( limelightInputRoot, conversionParameters);
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		limelightInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		{
			SearchProgram searchProgram = new SearchProgram();
			searchPrograms.getSearchProgram().add( searchProgram );
				
			searchProgram.setName( Constants.PROGRAM_NAME_COMET );
			searchProgram.setDisplayName( Constants.PROGRAM_NAME_COMET );
			searchProgram.setVersion( cometResults.getCometVersion() );
			
			
			//
			// Define the annotation types present in percolator data
			//
			PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
			searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
			
			FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
			psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
			
			for( FilterablePsmAnnotationType annoType : PSMAnnotationTypes.getFilterablePsmAnnotationTypes( Constants.PROGRAM_NAME_COMET ) ) {
				filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().add( annoType );
			}
			
		}

		{
			SearchProgram searchProgram = new SearchProgram();
			searchPrograms.getSearchProgram().add( searchProgram );
				
			searchProgram.setName( Constants.PROGRAM_NAME_PERCOLATOR );
			searchProgram.setDisplayName( Constants.PROGRAM_NAME_PERCOLATOR );
			searchProgram.setVersion( percolatorResults.getPercolatorVersion() );
			
			
			//
			// Define the annotation types present in percolator data
			//
			PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
			searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
			
			FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
			psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
			
			for( FilterablePsmAnnotationType annoType : PSMAnnotationTypes.getFilterablePsmAnnotationTypes( Constants.PROGRAM_NAME_PERCOLATOR ) ) {
				filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().add( annoType );
			}
			
			
			ReportedPeptideAnnotationTypes reportedPeptideAnnotationTypes = new ReportedPeptideAnnotationTypes();
			searchProgram.setReportedPeptideAnnotationTypes( reportedPeptideAnnotationTypes );

			FilterableReportedPeptideAnnotationTypes filterableReportedPeptideAnnotationTypes = new FilterableReportedPeptideAnnotationTypes();
			reportedPeptideAnnotationTypes.setFilterableReportedPeptideAnnotationTypes( filterableReportedPeptideAnnotationTypes );
			
			for( FilterableReportedPeptideAnnotationType annoType : PeptideAnnotationTypes.getFilterablePeptideAnnotationTypes( Constants.PROGRAM_NAME_PERCOLATOR ) ) {
				filterableReportedPeptideAnnotationTypes.getFilterableReportedPeptideAnnotationType().add( annoType );
			}
		}
		
		
		
		//
		// Define which annotation types are visible by default
		//
		DefaultVisibleAnnotations xmlDefaultVisibleAnnotations = new DefaultVisibleAnnotations();
		searchProgramInfo.setDefaultVisibleAnnotations( xmlDefaultVisibleAnnotations );
		
		VisiblePsmAnnotations xmlVisiblePsmAnnotations = new VisiblePsmAnnotations();
		xmlDefaultVisibleAnnotations.setVisiblePsmAnnotations( xmlVisiblePsmAnnotations );

		for( SearchAnnotation sa : PSMDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() ) {
			xmlVisiblePsmAnnotations.getSearchAnnotation().add( sa );
		}
		
		VisibleReportedPeptideAnnotations xmlVisibleReportedPeptideAnnotations = new VisibleReportedPeptideAnnotations();
		xmlDefaultVisibleAnnotations.setVisibleReportedPeptideAnnotations( xmlVisibleReportedPeptideAnnotations );

		for( SearchAnnotation sa : PeptideDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() ) {
			xmlVisibleReportedPeptideAnnotations.getSearchAnnotation().add( sa );
		}
		
		//
		// Define the default display order in proxl
		//
		AnnotationSortOrder xmlAnnotationSortOrder = new AnnotationSortOrder();
		searchProgramInfo.setAnnotationSortOrder( xmlAnnotationSortOrder );
		
		PsmAnnotationSortOrder xmlPsmAnnotationSortOrder = new PsmAnnotationSortOrder();
		xmlAnnotationSortOrder.setPsmAnnotationSortOrder( xmlPsmAnnotationSortOrder );
		
		for( SearchAnnotation xmlSearchAnnotation : PSMAnnotationTypeSortOrder.getPSMAnnotationTypeSortOrder() ) {
			xmlPsmAnnotationSortOrder.getSearchAnnotation().add( xmlSearchAnnotation );
		}
		
		ReportedPeptideAnnotationSortOrder xmlReportedPeptideAnnotationSortOrder = new ReportedPeptideAnnotationSortOrder();
		xmlAnnotationSortOrder.setReportedPeptideAnnotationSortOrder( xmlReportedPeptideAnnotationSortOrder );
		
		for( SearchAnnotation xmlSearchAnnotation : PeptideAnnotationTypeSortOrder.getPeptideAnnotationTypeSortOrder() ) {
			xmlReportedPeptideAnnotationSortOrder.getSearchAnnotation().add( xmlSearchAnnotation );
		}
		
		//
		// Define the static mods
		//
		if( cometParameters.getStaticMods() != null && cometParameters.getStaticMods().keySet().size() > 0 ) {
			StaticModifications smods = new StaticModifications();
			limelightInputRoot.setStaticModifications( smods );
			
			
			for( char residue : cometParameters.getStaticMods().keySet() ) {
				
				StaticModification xmlSmod = new StaticModification();
				xmlSmod.setAminoAcid( String.valueOf( residue ) );
				xmlSmod.setMassChange( BigDecimal.valueOf( cometParameters.getStaticMods().get( residue ) ) );
				
				smods.getStaticModification().add( xmlSmod );
			}
		}



		//
		// Build MatchedProteins section and get map of protein names to MatchedProtein ids
		//
		Map<String, Integer> proteinNameIds = MatchedProteinsBuilder.getInstance().buildMatchedProteins(
				limelightInputRoot,
				conversionParameters.getFastaFile(),
				cometResults.getPeptidePSMMap().keySet()
		);


		//
		// Define the peptide and PSM data
		//
		ReportedPeptides reportedPeptides = new ReportedPeptides();
		limelightInputRoot.setReportedPeptides( reportedPeptides );
		
		// iterate over each distinct reported peptide
		for( String percolatorReportedPeptide : percolatorResults.getReportedPeptideResults().keySet() ) {
			
			CometReportedPeptide cometReportedPeptide = CometParsingUtils.getCometReportedPeptideForString( percolatorReportedPeptide, cometResults );
			
			ReportedPeptide xmlReportedPeptide = new ReportedPeptide();
			reportedPeptides.getReportedPeptide().add( xmlReportedPeptide );
			
			xmlReportedPeptide.setReportedPeptideString( cometReportedPeptide.getReportedPeptideString() );
			xmlReportedPeptide.setSequence( cometReportedPeptide.getNakedPeptide() );

			MatchedProteinsForPeptide xProteinsForPeptide = new MatchedProteinsForPeptide();
			xmlReportedPeptide.setMatchedProteinsForPeptide( xProteinsForPeptide );

			// add in protein inference info
			for( String proteinName : cometReportedPeptide.getProteinMatches() ) {

				int matchedProteinId = proteinNameIds.get( proteinName );

				MatchedProteinForPeptide xProteinForPeptide = new MatchedProteinForPeptide();
				xProteinsForPeptide.getMatchedProteinForPeptide().add( xProteinForPeptide );

				xProteinForPeptide.setId( BigInteger.valueOf( matchedProteinId ) );
			}

			PercolatorPeptideData percolatorPeptideData = percolatorResults.getReportedPeptideResults().get( percolatorReportedPeptide );
			
			// add in the filterable peptide annotations (e.g., q-value)
			ReportedPeptideAnnotations xmlReportedPeptideAnnotations = new ReportedPeptideAnnotations();
			xmlReportedPeptide.setReportedPeptideAnnotations( xmlReportedPeptideAnnotations );
			
			FilterableReportedPeptideAnnotations xmlFilterableReportedPeptideAnnotations = new FilterableReportedPeptideAnnotations();
			xmlReportedPeptideAnnotations.setFilterableReportedPeptideAnnotations( xmlFilterableReportedPeptideAnnotations );
			
			// handle q-value
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_QVALUE );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( percolatorPeptideData.getPercolatorPeptideScores().getqValue()) );
			}
			// handle p-value
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PVALUE );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( percolatorPeptideData.getPercolatorPeptideScores().getpValue()) );
			}
			// handle pep
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PEP );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( percolatorPeptideData.getPercolatorPeptideScores().getPep()) );
			}
			// handle svm score
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_SVMSCORE );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( percolatorPeptideData.getPercolatorPeptideScores().getSvmScore()) );
			}
			
			
			// add in the mods for this peptide
			if( cometReportedPeptide.getMods() != null && cometReportedPeptide.getMods().keySet().size() > 0 ) {
					
				PeptideModifications xmlModifications = new PeptideModifications();
				xmlReportedPeptide.setPeptideModifications( xmlModifications );
					
				for( int position : cometReportedPeptide.getMods().keySet() ) {

					PeptideModification xmlModification = new PeptideModification();
					xmlModifications.getPeptideModification().add( xmlModification );

					xmlModification.setMass( cometReportedPeptide.getMods().get( position ) );

					if( CometParsingUtils.isNTerminalMod( cometReportedPeptide.getNakedPeptide(), position ) ) {

						xmlModification.setIsNTerminal( true );

					} else if( CometParsingUtils.isCTerminalMod( cometReportedPeptide.getNakedPeptide(), position ) ) {

						xmlModification.setIsCTerminal( true );

					} else {
						xmlModification.setPosition( BigInteger.valueOf( position ) );
					}
				}
			}

			
			// add in the PSMs and annotations
			Psms xmlPsms = new Psms();
			xmlReportedPeptide.setPsms( xmlPsms );

			// iterate over all PSMs for this reported peptide

			for( int scanNumber : percolatorResults.getReportedPeptideResults().get( percolatorReportedPeptide ).getPercolatorPSMs().keySet() ) {

				CometPSM psm = cometResults.getPeptidePSMMap().get( cometReportedPeptide ).get( scanNumber );

				Psm xmlPsm = new Psm();
				xmlPsms.getPsm().add( xmlPsm );

				xmlPsm.setScanNumber( new BigInteger( String.valueOf( scanNumber ) ) );
				xmlPsm.setPrecursorCharge( new BigInteger( String.valueOf( psm.getCharge() ) ) );

				// add in the filterable PSM annotations (e.g., score)
				FilterablePsmAnnotations xmlFilterablePsmAnnotations = new FilterablePsmAnnotations();
				xmlPsm.setFilterablePsmAnnotations( xmlFilterablePsmAnnotations );

				// handle comet scores
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_DELTACN );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.getDeltaCn() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_DELTACNSTAR );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.getDeltaCnStar() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_EXPECT );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.geteValue() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_SPRANK );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.getSpRank() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_SPSCORE );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.getSpScore() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_XCORR );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( psm.getxCorr() );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );

					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.COMET_ANNOTATION_TYPE_HIT_RANK );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_COMET );
					xmlFilterablePsmAnnotation.setValue( BigDecimal.valueOf( psm.getHitRank() ).setScale( 0, RoundingMode.HALF_UP ) );
				}

				// handle percolator scores
				PercolatorPSM percolatorPSM = percolatorResults.getReportedPeptideResults().get( percolatorReportedPeptide ).getPercolatorPSMs().get( scanNumber );
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PEP );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
					xmlFilterablePsmAnnotation.setValue( BigDecimal.valueOf( percolatorPSM.getPep() ) );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PVALUE );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
					xmlFilterablePsmAnnotation.setValue( BigDecimal.valueOf( percolatorPSM.getpValue() ) );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_QVALUE );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
					xmlFilterablePsmAnnotation.setValue( BigDecimal.valueOf( percolatorPSM.getqValue() ) );
				}
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_SVMSCORE );
					xmlFilterablePsmAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
					xmlFilterablePsmAnnotation.setValue( BigDecimal.valueOf( percolatorPSM.getSvmScore() ) );
				}
				
			}// end iterating over psms for a reported peptide
		
		}//end iterating over reported peptides

		
		
		// add in the config file(s)
		ConfigurationFiles xmlConfigurationFiles = new ConfigurationFiles();
		limelightInputRoot.setConfigurationFiles( xmlConfigurationFiles );
		
		ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
		xmlConfigurationFiles.getConfigurationFile().add( xmlConfigurationFile );
		
		xmlConfigurationFile.setSearchProgram( Constants.PROGRAM_NAME_COMET );
		xmlConfigurationFile.setFileName( conversionParameters.getCometParametersFile().getName() );
		xmlConfigurationFile.setFileContent( Files.readAllBytes( FileSystems.getDefault().getPath( conversionParameters.getCometParametersFile().getAbsolutePath() ) ) );
		
		
		//make the xml file
		CreateImportFileFromJavaObjectsMain.getInstance().createImportFileFromJavaObjectsMain( conversionParameters.getLimelightXMLOutputFile(), limelightInputRoot);
		
	}
	
	
}
