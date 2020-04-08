package org.yeastrc.limelight.xml.crux_comet_percolator.builder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.yeastrc.limelight.limelight_import.api.xml_dto.ConversionProgram;
import org.yeastrc.limelight.limelight_import.api.xml_dto.LimelightInput;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.ConversionParameters;
import org.yeastrc.limelight.xml.crux_comet_percolator.objects.ConversionProgramInfo;

public class ConversionProgramBuilder {

	public static ConversionProgramBuilder createInstance() { return new ConversionProgramBuilder(); }
	
	public void buildConversionProgramSection( LimelightInput limelightInputRoot, ConversionParameters conversionParameters ) throws DatatypeConfigurationException {
		
		ConversionProgramInfo conversionProgramInfo = conversionParameters.getConversionProgramInfo();
		
		ConversionProgram xConversionProgram = new ConversionProgram();
		limelightInputRoot.setConversionProgram( xConversionProgram );
		
		xConversionProgram.setArguments( conversionProgramInfo.getArguments() );		
		xConversionProgram.setConversionDate( getXMLGregorianCalendar( conversionProgramInfo.getConversionDate() ) );
		xConversionProgram.setName( conversionProgramInfo.getName() );
		xConversionProgram.setURI( conversionProgramInfo.getURI() );
		xConversionProgram.setVersion( conversionProgramInfo.getVersion() );
		
		
	}
	
	private XMLGregorianCalendar getXMLGregorianCalendar( LocalDateTime ldt ) throws DatatypeConfigurationException {
		
		ZonedDateTime zdt = ldt.atZone( ZoneId.systemDefault() );
		GregorianCalendar gc = GregorianCalendar.from( zdt );
		XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		
		return xcal;
	}
	
}
