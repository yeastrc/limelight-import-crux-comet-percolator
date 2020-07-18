package org.yeastrc.limelight.xml.crux_comet_percolator.main;

import org.yeastrc.limelight.limelight_import.api.xml_dto.LimelightInput;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class LimelightXMLValidator {

    public static void validateLimelightXML(File limelightXMLFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(LimelightInput.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        LimelightInput limelightInput = (LimelightInput)jaxbUnmarshaller.unmarshal( limelightXMLFile );

    }

}
