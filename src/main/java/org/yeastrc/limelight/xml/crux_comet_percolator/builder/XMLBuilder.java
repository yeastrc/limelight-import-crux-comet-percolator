package org.yeastrc.limelight.xml.crux_comet_percolator.builder;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yeastrc.limelight.limelight_import.api.xml_dto.*;
import org.yeastrc.limelight.limelight_import.api.xml_dto.ReportedPeptide.ReportedPeptideAnnotations;
import org.yeastrc.limelight.limelight_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.limelight.limelight_import.api.xml_dto.SearchProgram.ReportedPeptideAnnotationTypes;
import org.yeastrc.limelight.limelight_import.create_import_file_from_java_objects.main.CreateImportFileFromJavaObjectsMain;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PSMAnnotationTypeSortOrder;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PSMAnnotationTypes;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PSMDefaultVisibleAnnotationTypes;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PeptideAnnotationTypeSortOrder;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PeptideAnnotationTypes;
import org.yeastrc.limelight.xml.crux_comet_percolator.annotation.PeptideDefaultVisibleAnnotationTypes;
import org.yeastrc.limelight.xml.crux_comet_percolator.constants.Constants;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.*;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.CometParsingUtils;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.CruxUtils;

public class XMLBuilder {

	public void buildAndSaveXML( ConversionParameters conversionParameters,
			                     CometParameters cometParameters,
			                     Map<String, CometResults> indexedCometResults,
			                     IndexedPercolatorResults percolatorResults,
			                     String cruxVersion,
								 String cometVersion,
								 String percolatorVersion,
								 boolean multiSearch)
    throws Exception {

		LimelightInput limelightInputRoot = new LimelightInput();

		// determine if deltaCNStar is present for all results
		boolean isDeltaCNStarPresent = true;
		for(CometResults value : indexedCometResults.values()) {
			isDeltaCNStarPresent = isDeltaCNStarPresent && value.isDeltaCNStarPresent();
		}

		limelightInputRoot.setFastaFilename( conversionParameters.getFastaFile().getName() );
		ConversionProgramBuilder.createInstance().buildConversionProgramSection( limelightInputRoot, conversionParameters);
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		limelightInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );

		{
			SearchProgram searchProgram = new SearchProgram();
			searchPrograms.getSearchProgram().add( searchProgram );

			searchProgram.setName( Constants.PROGRAM_NAME_CRUX );
			searchProgram.setDisplayName( Constants.PROGRAM_NAME_CRUX );
			searchProgram.setVersion( cruxVersion );


		}

		{
			SearchProgram searchProgram = new SearchProgram();
			searchPrograms.getSearchProgram().add( searchProgram );
				
			searchProgram.setName( Constants.PROGRAM_NAME_COMET );
			searchProgram.setDisplayName( Constants.PROGRAM_NAME_COMET );
			searchProgram.setVersion( cometVersion);
			
			
			//
			// Define the annotation types present in percolator data
			//
			PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
			searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
			
			FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
			psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
			
			for( FilterablePsmAnnotationType annoType : PSMAnnotationTypes.getFilterablePsmAnnotationTypes( Constants.PROGRAM_NAME_COMET, isDeltaCNStarPresent, conversionParameters ) ) {
				filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().add( annoType );
			}
			
		}

		{
			SearchProgram searchProgram = new SearchProgram();
			searchPrograms.getSearchProgram().add( searchProgram );
				
			searchProgram.setName( Constants.PROGRAM_NAME_PERCOLATOR );
			searchProgram.setDisplayName( Constants.PROGRAM_NAME_PERCOLATOR );
			searchProgram.setVersion( percolatorVersion);
			
			
			//
			// Define the annotation types present in percolator data
			//
			PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
			searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
			
			FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
			psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
			
			for( FilterablePsmAnnotationType annoType : PSMAnnotationTypes.getFilterablePsmAnnotationTypes( Constants.PROGRAM_NAME_PERCOLATOR, isDeltaCNStarPresent, conversionParameters ) ) {
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
		Set<CometReportedPeptide> allCometReportedPeptides = new HashSet<>();
		for(CometResults cr : indexedCometResults.values()) {
			allCometReportedPeptides.addAll(cr.getPeptidePSMMap().keySet());
		}

		Map<String, CometReportedPeptide> cometRPLookupMap = new HashMap<>();
		for(CometReportedPeptide cp : allCometReportedPeptides) {
			cometRPLookupMap.put(cp.getReportedPeptideString(), cp);
		}

		Map<String, Integer> proteinNameIds = MatchedProteinsBuilder.getInstance().buildMatchedProteins(
				limelightInputRoot,
				conversionParameters.getFastaFile(),
				allCometReportedPeptides
		);

		//
		// Define the peptide and PSM data
		//
		ReportedPeptides reportedPeptides = new ReportedPeptides();
		limelightInputRoot.setReportedPeptides( reportedPeptides );
		
		// iterate over each distinct reported peptide
		int totalPeptides = percolatorResults.getIndexedReportedPeptideResults().size();
		int count = 0;
		for( String percolatorReportedPeptide : percolatorResults.getIndexedReportedPeptideResults().keySet() ) {

			count++;
			System.err.print( "Processing peptide " + count + " of " + totalPeptides + "...\r" );

			IndexedPercolatorPeptideData indexedPercolatorPeptideData = percolatorResults.getIndexedReportedPeptideResults().get( percolatorReportedPeptide );

			CometReportedPeptide cometReportedPeptide = cometRPLookupMap.get(percolatorReportedPeptide);
			
			ReportedPeptide xmlReportedPeptide = new ReportedPeptide();
			reportedPeptides.getReportedPeptide().add( xmlReportedPeptide );
			
			xmlReportedPeptide.setReportedPeptideString( cometReportedPeptide.getReportedPeptideString() );
			xmlReportedPeptide.setSequence( cometReportedPeptide.getNakedPeptide() );

			MatchedProteinsForPeptide xProteinsForPeptide = new MatchedProteinsForPeptide();
			xmlReportedPeptide.setMatchedProteinsForPeptide( xProteinsForPeptide );

			// add in protein inference info
			boolean foundValidProtein = false;

			for( String proteinName : cometReportedPeptide.getProteinMatches() ) {

				if(proteinNameIds.containsKey(proteinName)) {
					int matchedProteinId = proteinNameIds.get(proteinName);
					foundValidProtein = true;
					MatchedProteinForPeptide xProteinForPeptide = new MatchedProteinForPeptide();
					xProteinsForPeptide.getMatchedProteinForPeptide().add(xProteinForPeptide);

					xProteinForPeptide.setId(BigInteger.valueOf(matchedProteinId));
				}
			}

			if(!foundValidProtein) {
				throw new Exception("Did not find any non-decoy proteins for peptide: " + percolatorReportedPeptide );
			}

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
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( indexedPercolatorPeptideData.getPercolatorPeptideScores().getqValue()) );
			}
			// handle p-value
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PVALUE );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( indexedPercolatorPeptideData.getPercolatorPeptideScores().getpValue()) );
			}
			// handle pep
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PEP );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( indexedPercolatorPeptideData.getPercolatorPeptideScores().getPep()) );
			}
			// handle svm score
			{
				FilterableReportedPeptideAnnotation xmlFilterableReportedPeptideAnnotation = new FilterableReportedPeptideAnnotation();
				xmlFilterableReportedPeptideAnnotations.getFilterableReportedPeptideAnnotation().add( xmlFilterableReportedPeptideAnnotation );
				
				xmlFilterableReportedPeptideAnnotation.setAnnotationName( PeptideAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_SVMSCORE );
				xmlFilterableReportedPeptideAnnotation.setSearchProgram( Constants.PROGRAM_NAME_PERCOLATOR );
				xmlFilterableReportedPeptideAnnotation.setValue( BigDecimal.valueOf( indexedPercolatorPeptideData.getPercolatorPeptideScores().getSvmScore()) );
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

			for( String pepXMLFileRoot : percolatorResults.getIndexedReportedPeptideResults().get(percolatorReportedPeptide).getPercolatorPSMs().keySet()) {

//				System.out.println("pepXMLFileRoot: " + pepXMLFileRoot);
//				for(String p : indexedCometResults.keySet()) {
//					System.out.println(p);
//				}

				Map<Integer, PercolatorPSM> percolatorPSMData = percolatorResults.getIndexedReportedPeptideResults().get(percolatorReportedPeptide).getPercolatorPSMs().get(pepXMLFileRoot);

				// no psm data for this peptide in this pepxml file, go to next file
				if(percolatorPSMData == null || indexedCometResults.get(pepXMLFileRoot).getPeptidePSMMap() == null) {
					continue;
				}

				for (int scanNumber : percolatorPSMData.keySet()) {

					CometPSM psm = indexedCometResults.get(pepXMLFileRoot).getPeptidePSMMap().get(cometReportedPeptide).get(scanNumber);

					Psm xmlPsm = new Psm();
					xmlPsms.getPsm().add(xmlPsm);

					xmlPsm.setScanNumber(new BigInteger(String.valueOf(scanNumber)));
					xmlPsm.setPrecursorCharge(new BigInteger(String.valueOf(psm.getCharge())));
					xmlPsm.setScanFileName(psm.getSpectralFilename());
					xmlPsm.setPrecursorMZ(CometParsingUtils.getObservedMoverZForPsm(psm));
					xmlPsm.setPrecursorRetentionTime(psm.getRetentionTime());

					if(multiSearch) {
						xmlPsm.setSubgroupName(pepXMLFileRoot);
					}

					// add in the filterable PSM annotations (e.g., score)
					FilterablePsmAnnotations xmlFilterablePsmAnnotations = new FilterablePsmAnnotations();
					xmlPsm.setFilterablePsmAnnotations(xmlFilterablePsmAnnotations);

					// handle comet scores
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_DELTACN);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.getDeltaCn());
					}
					if(isDeltaCNStarPresent) {
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_DELTACNSTAR);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.getDeltaCnStar());
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_EXPECT);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.geteValue());
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_SPRANK);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.getSpRank());
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_SPSCORE);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.getSpScore());
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_XCORR);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(psm.getxCorr());
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.COMET_ANNOTATION_TYPE_HIT_RANK);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_COMET);
						xmlFilterablePsmAnnotation.setValue(BigDecimal.valueOf(psm.getHitRank()).setScale(0, RoundingMode.HALF_UP));
					}

					// handle percolator scores
					PercolatorPSM percolatorPSM = percolatorPSMData.get(scanNumber);
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PEP);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_PERCOLATOR);
						xmlFilterablePsmAnnotation.setValue(BigDecimal.valueOf(percolatorPSM.getPep()));
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_PVALUE);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_PERCOLATOR);
						xmlFilterablePsmAnnotation.setValue(BigDecimal.valueOf(percolatorPSM.getpValue()));
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_QVALUE);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_PERCOLATOR);
						xmlFilterablePsmAnnotation.setValue(BigDecimal.valueOf(percolatorPSM.getqValue()));
					}
					{
						FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
						xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add(xmlFilterablePsmAnnotation);

						xmlFilterablePsmAnnotation.setAnnotationName(PSMAnnotationTypes.PERCOLATOR_ANNOTATION_TYPE_SVMSCORE);
						xmlFilterablePsmAnnotation.setSearchProgram(Constants.PROGRAM_NAME_PERCOLATOR);
						xmlFilterablePsmAnnotation.setValue(BigDecimal.valueOf(percolatorPSM.getSvmScore()));
					}

				}// end iterating over psms for a reported peptide
			}
		
		}//end iterating over reported peptides
		System.err.print("\n");

		
		// add in the config file(s)
		ConfigurationFiles xmlConfigurationFiles = new ConfigurationFiles();
		limelightInputRoot.setConfigurationFiles( xmlConfigurationFiles );

		{
			ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
			xmlConfigurationFiles.getConfigurationFile().add(xmlConfigurationFile);

			xmlConfigurationFile.setSearchProgram(Constants.PROGRAM_NAME_COMET);
			xmlConfigurationFile.setFileName(CruxUtils.getCometParams(conversionParameters.getCruxOutputDirectory()).getName());
			xmlConfigurationFile.setFileContent(Files.readAllBytes(FileSystems.getDefault().getPath(CruxUtils.getCometParams(conversionParameters.getCruxOutputDirectory()).getAbsolutePath())));
		}

		File f = CruxUtils.getCometLogFile(conversionParameters.getCruxOutputDirectory());
		if( f.exists() ) {
			ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
			xmlConfigurationFiles.getConfigurationFile().add(xmlConfigurationFile);

			xmlConfigurationFile.setSearchProgram(Constants.PROGRAM_NAME_COMET);
			xmlConfigurationFile.setFileName(f.getName());
			xmlConfigurationFile.setFileContent(Files.readAllBytes(FileSystems.getDefault().getPath(f.getAbsolutePath())));
		}

		f = CruxUtils.getPercolatorLogFile(conversionParameters.getCruxOutputDirectory());
		if( f.exists() ) {
			ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
			xmlConfigurationFiles.getConfigurationFile().add(xmlConfigurationFile);

			xmlConfigurationFile.setSearchProgram(Constants.PROGRAM_NAME_PERCOLATOR);
			xmlConfigurationFile.setFileName(f.getName());
			xmlConfigurationFile.setFileContent(Files.readAllBytes(FileSystems.getDefault().getPath(f.getAbsolutePath())));
		}
		
		//make the xml file
		System.err.print("Writing XML to disk... ");
		CreateImportFileFromJavaObjectsMain.getInstance().createImportFileFromJavaObjectsMain( conversionParameters.getOutputFile(), limelightInputRoot);
		System.err.print("Done.\n");
	}

	
}
