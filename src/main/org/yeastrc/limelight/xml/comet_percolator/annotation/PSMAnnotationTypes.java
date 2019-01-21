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
import org.yeastrc.limelight.limelight_import.api.xml_dto.FilterablePsmAnnotationType;
import org.yeastrc.limelight.xml.comet_percolator.constants.Constants;



public class PSMAnnotationTypes {

	// comet scores
	public static final String COMET_ANNOTATION_TYPE_XCORR = "XCorr";
	public static final String COMET_ANNOTATION_TYPE_DELTACN = "DeltaCN";
	public static final String COMET_ANNOTATION_TYPE_DELTACNSTAR = "DeltaCN*";
	public static final String COMET_ANNOTATION_TYPE_SPSCORE = "Sp Score";
	public static final String COMET_ANNOTATION_TYPE_SPRANK = "Sp Rank";
	public static final String COMET_ANNOTATION_TYPE_EXPECT = "E-Value";
	public static final String COMET_ANNOTATION_TYPE_HIT_RANK = "Hit Rank";
	
	// percolator scores
	public static final String PERCOLATOR_ANNOTATION_TYPE_QVALUE = "q-value";
	public static final String PERCOLATOR_ANNOTATION_TYPE_PVALUE = "p-value";
	public static final String PERCOLATOR_ANNOTATION_TYPE_PEP = "PEP";
	public static final String PERCOLATOR_ANNOTATION_TYPE_SVMSCORE = "SVM Score";

	
	
	public static List<FilterablePsmAnnotationType> getFilterablePsmAnnotationTypes( String programName ) {
		List<FilterablePsmAnnotationType> types = new ArrayList<FilterablePsmAnnotationType>();

		if( programName.equals( Constants.PROGRAM_NAME_COMET ) ) {
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_XCORR );
				type.setDescription( "Comet cross-correlation coefficient" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
	
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_DELTACN );
				type.setDescription( "Difference between the XCorr of this PSM and the next best PSM (with a dissimilar peptide)" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
				
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_DELTACNSTAR );
				type.setDescription( "Difference between the XCorr of this PSM and the next best PSM" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
				
				types.add( type );
			}

			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_SPSCORE );
				type.setDescription( "Score indicating how well theoretical and actual peaks matched." );
				type.setFilterDirection( FilterDirectionType.ABOVE );
				
				types.add( type );
			}

			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_SPRANK );
				type.setDescription( "The rank of this peptide match for this spectrum basedo n Sp Score" );
				type.setFilterDirection( FilterDirectionType.BELOW );
				
				types.add( type );
			}

			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_EXPECT );
				type.setDescription( "The e-value, or the estimation of the chance of observing a hit of this quality by chance." );
				type.setFilterDirection( FilterDirectionType.BELOW );
				
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( COMET_ANNOTATION_TYPE_HIT_RANK );
				type.setDescription( "The rank of this PSM for this scan. Rank 1 means highest scoring hit." );
				type.setFilterDirection( FilterDirectionType.BELOW );
				type.setDefaultFilterValue( BigDecimal.valueOf( 1 ) );
				
				types.add( type );
			}
			
		}

		else if( programName.equals( Constants.PROGRAM_NAME_PERCOLATOR ) ) {
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_QVALUE );
				type.setDescription( "Q-value" );
				type.setFilterDirection( FilterDirectionType.BELOW );
				type.setDefaultFilterValue( BigDecimal.valueOf( 0.05 ) );
	
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_PVALUE );
				type.setDescription( "P-value" );
				type.setFilterDirection( FilterDirectionType.BELOW );
	
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_PEP );
				type.setDescription( "Posterior error probability" );
				type.setFilterDirection( FilterDirectionType.BELOW );
	
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( PERCOLATOR_ANNOTATION_TYPE_SVMSCORE );
				type.setDescription( "SVN Score from kernel function" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
	
				types.add( type );
			}
		}

		
		return types;
	}
	
	
}
