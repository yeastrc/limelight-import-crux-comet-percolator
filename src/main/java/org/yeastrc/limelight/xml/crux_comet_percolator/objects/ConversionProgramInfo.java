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

package org.yeastrc.limelight.xml.crux_comet_percolator.objects;

import java.time.LocalDateTime;

import org.yeastrc.limelight.xml.crux_comet_percolator.constants.Constants;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.Limelight_GetVersion_FromFile_SetInBuildFromEnvironmentVariable;

public class ConversionProgramInfo {
	
	public static ConversionProgramInfo createInstance( String arguments ) throws Exception {
		
		ConversionProgramInfo cpi = new ConversionProgramInfo();
		
		cpi.setName( Constants.CONVERSION_PROGRAM_NAME );
		cpi.setURI( Constants.CONVERSION_PROGRAM_URI );
		cpi.setVersion( Limelight_GetVersion_FromFile_SetInBuildFromEnvironmentVariable.getVersion_FromFile_SetInBuildFromEnvironmentVariable() );
		
		cpi.setArguments( arguments );
		cpi.setConversionDate( LocalDateTime.now() );
		
		return cpi;
		
	}
	
	private ConversionProgramInfo() { }
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the conversionDate
	 */
	public LocalDateTime getConversionDate() {
		return conversionDate;
	}
	/**
	 * @param conversionDate the conversionDate to set
	 */
	public void setConversionDate(LocalDateTime conversionDate) {
		this.conversionDate = conversionDate;
	}
	/**
	 * @return the arguments
	 */
	public String getArguments() {
		return arguments;
	}
	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	/**
	 * @return the uRI
	 */
	public String getURI() {
		return URI;
	}
	/**
	 * @param uRI the uRI to set
	 */
	public void setURI(String uRI) {
		URI = uRI;
	}
	
	private String name;
	private String version;
	private LocalDateTime conversionDate;
	private String arguments;
	private String URI;
	
}
