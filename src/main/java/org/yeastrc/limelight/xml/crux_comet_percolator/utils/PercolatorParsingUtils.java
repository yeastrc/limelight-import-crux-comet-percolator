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

package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import org.apache.commons.io.FilenameUtils;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.IndexedPercolatorResults;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercolatorParsingUtils {

	/**
	 * Examples:
	 * target_17_15544_2_1
	 * @param scanId
	 * @return
	 */
	public static int getScanNumberFromScanId( String scanId ) {

		Matcher m = scanNumberPattern.matcher( scanId );

		if( m.matches() ) {
			return Integer.parseInt( m.group( 1 ) );
		}

		throw new IllegalArgumentException( "Scan id is not of the expected syntax. Got: " + scanId + ", expected something like: comet.2020_0212_Loomis_10_DDA_newLC_58146_3_1" );
	}

	/**
	 * Get the file index from the scan id
	 * @param scanId
	 * @param pepxmlTargetIndexMap
	 * @return
	 */
	public static String getPepXMLFileNameRoot(String scanId, Map<Integer, String> pepxmlTargetIndexMap) throws Exception {

		Matcher m = targetScanIdPattern.matcher(scanId);
		if(m.matches()) {
			return getPepXMLFileRootNameUsingTargetSyntax(Integer.parseInt(m.group(1)), pepxmlTargetIndexMap);
		}

		m = filenamePattern_MultiSearch.matcher( scanId );

		if( m.matches() ) {
			return "comet." + m.group( 1 );
		}

		m = filenamePattern_SingleSearch.matcher( scanId );
		if( m.matches() ) {
			return "comet";
		}

		throw new IllegalArgumentException( "Scan id is not of the expected syntax. Got: " + scanId + ", expected something like: comet.2020_0212_Loomis_10_DDA_newLC_58146_3_1" );
	}

	/**
	 * Get the file index from the scan id
	 * @param pepXMLIndex
	 * @param pepxmlTargetIndexMap
	 * @return
	 */
	public static String getPepXMLFileRootNameUsingTargetSyntax(int pepXMLIndex, Map<Integer, String> pepxmlTargetIndexMap) throws Exception {
		if(!pepxmlTargetIndexMap.containsKey(pepXMLIndex)) {
			throw new Exception("Got a pepXML index of " + pepXMLIndex + ", but did not find this index in the percolator log file.");
		}

		String pepXMLFileName = pepxmlTargetIndexMap.get(pepXMLIndex);

		if(pepXMLFileName.endsWith(".pep.xml")) {
			String pepXMLFileNameRoot = FilenameUtils.removeExtension(pepXMLFileName);    // remove the .xml
			pepXMLFileNameRoot = FilenameUtils.removeExtension(pepXMLFileNameRoot);        // remove the .pep

			return pepXMLFileNameRoot;

		} else {
			throw new Exception("pepXML files must end with \".pep.xml\". Got: " + pepXMLFileName);
		}
	}

	public static int getNumberOfDecimalPlacesInPercolatorMod(IndexedPercolatorResults indexedPercolatorResults) {
		for(String reportedPeptideString : indexedPercolatorResults.getIndexedReportedPeptideResults().keySet()) {
			Matcher m = percolatorPeptideStringModPattern.matcher(reportedPeptideString);
			if(m.matches())
				return m.group(1).length();
		}

		return 0;
	}

	private static final Pattern filenamePattern_MultiSearch = Pattern.compile( "^.*comet\\.(.+)_\\d+_\\d+_\\d+$" );
	private static final Pattern filenamePattern_SingleSearch = Pattern.compile( "^.*comet_\\d+_\\d+_\\d+$" );

	private static final Pattern scanNumberPattern = Pattern.compile( "^.+_(\\d+)_\\d+_\\d+$" );

	private static final Pattern targetScanIdPattern = Pattern.compile("^target_(\\d+)_\\d+_\\d+_\\d+$");

	private static final Pattern percolatorPeptideStringModPattern = Pattern.compile("^[A-Z]+\\[\\d+\\.(\\d+)]");
}
