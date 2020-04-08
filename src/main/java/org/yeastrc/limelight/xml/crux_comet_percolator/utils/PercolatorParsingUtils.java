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
	 * @return
	 */
	public static String getPepXMLFileName( String scanId ) {

		Matcher m = filenamePattern.matcher( scanId );

		if( m.matches() ) {
			return "comet." + m.group( 1 );
		}

		throw new IllegalArgumentException( "Scan id is not of the expected syntax. Got: " + scanId + ", expected something like: comet.2020_0212_Loomis_10_DDA_newLC_58146_3_1" );
	}

	private static final Pattern filenamePattern = Pattern.compile( "^.*comet\\.(.+)_\\d+_\\d+_\\d+$" );

	private static final Pattern scanNumberPattern = Pattern.compile( "^.*comet\\..+_(\\d)+_\\d+_\\d+$" );

}
