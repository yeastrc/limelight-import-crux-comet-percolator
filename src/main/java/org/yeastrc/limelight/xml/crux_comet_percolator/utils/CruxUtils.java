package org.yeastrc.limelight.xml.crux_comet_percolator.utils;

import java.io.File;
import java.io.FileNotFoundException;

public class CruxUtils {

    public static File getCometParams(File cruxDataDirectory) throws FileNotFoundException {

        File cometParamsFile = new File(cruxDataDirectory, "comet.params.txt");
        if(!cometParamsFile.exists()) {
            throw new FileNotFoundException("Could not find file " + cometParamsFile.getAbsolutePath() );
        }

        return cometParamsFile;
    }

}
