package org.yeastrc.limelight.xml.crux_comet_percolator.main;


import org.yeastrc.limelight.xml.crux_comet_percolator.constants.Constants;
import org.yeastrc.limelight.xml.crux_comet_percolator.utils.Limelight_GetVersion_FromFile_SetInBuildFromEnvironmentVariable;

import picocli.CommandLine;

/**
 * 
 * Return a version number for the program and the conversion program 
 * 
 * This is called from the command line utility program when prompted for the version
 *
 */
public class LimelightConverterVersionProvider implements CommandLine.IVersionProvider {
	
	  
	@Override
	public String[] getVersion() throws Exception {

		String value_LIMELIGHT_RELEASE_TAG = 
				Limelight_GetVersion_FromFile_SetInBuildFromEnvironmentVariable.getVersion_FromFile_SetInBuildFromEnvironmentVariable();

		return new String[]{ Constants.CONVERSION_PROGRAM_NAME + " " + value_LIMELIGHT_RELEASE_TAG };
	}
}
