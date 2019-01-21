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

package org.yeastrc.limelight.xml.comet_percolator.annotation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.yeastrc.limelight.limelight_import.api.xml_dto.FilterDirectionType;
import org.yeastrc.limelight.limelight_import.api.xml_dto.FilterableReportedPeptideAnnotationType;
import org.yeastrc.limelight.xml.comet_percolator.constants.Constants;

public class PeptideAnnotationTypes {
	
	// percolator scores
	public static final String PERCOLATOR_ANNOTATION_TYPE_QVALUE = "q-value";
	public static final String PERCOLATOR_ANNOTATION_TYPE_PVALUE = "p-value";
	public static final String PERCOLATOR_ANNOTATION_TYPE_PEP = "PEP";
	public static final String PERCOLATOR_ANNOTATION_TYPE_SVMSCORE = "SVM Score";

	
	
	public static List<FilterableReportedPeptideAnnotationType> getFilterablePeptideAnnotationTypes( String programName ) {
		List<FilterableReportedPeptideAnnotationType> types = new ArrayList<FilterableReportedPeptideAnnotationType>();

		if( programName.equals( Constants.PROGRAM_NAME_PERCOLATOR ) ) {
			{
				FilterableReportedPeptideAnnotationType type = new FilterableReportedPeptideAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_QVALUE );
				type.setDescription( "Q-value" );
				type.setFilterDirection( FilterDirectionType.BELOW );
				type.setDefaultFilterValue( BigDecimal.valueOf( 0.05 ) );
	
				types.add( type );
			}
			
			{
				FilterableReportedPeptideAnnotationType type = new FilterableReportedPeptideAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_PVALUE );
				type.setDescription( "P-value" );
				type.setFilterDirection( FilterDirectionType.BELOW );
	
				types.add( type );
			}
			
			{
				FilterableReportedPeptideAnnotationType type = new FilterableReportedPeptideAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_PEP );
				type.setDescription( "Posterior error probability" );
				type.setFilterDirection( FilterDirectionType.BELOW );
	
				types.add( type );
			}
			
			{
				FilterableReportedPeptideAnnotationType type = new FilterableReportedPeptideAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_SVMSCORE );
				type.setDescription( "SVN Score from kernel function" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
	
				types.add( type );
			}
		} else {
			
			throw new IllegalArgumentException( "Unknown program name: " + programName );
			
		}

		
		return types;
	}
	
	
}
