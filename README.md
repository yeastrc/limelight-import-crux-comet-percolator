Comet/Percolator to limelight XML Converter
=======================================

Use this program to convert the results of a Comet + Percolator analysis to
limelight XML suitable for import into the limelight web application. Requires
that the Percolator output be represented as XML (see -X option in Percolator).

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/limelight-import-comet-percolator/releases).
2. Run the program ``java -jar cometPercolator2LimelightXML.jar`` with no arguments to see the possible parameters. Requires Java 8 or higher.

Command line documentation
---------------------------

Usage: java -jar cometPercolator2LimelightXML.jar -c path -p path -r path -f path -o path

Example: java -jar cometPercolator2LimelightXML.jar -c /path/to/comet.params
                                       -o /path/to/output.limelight.xml
                                       -p /path/to/pepXML.xml
                                       -r /path/to/percout.xml
                                       -f /path/to/fasta.fa

Options:
```

        -c      [Required] Path to comet .params file
        -o      [Required] Path to use for the limelight XML output file
        -f      [Required] Path to FASTA file used in the experiment.
        -p      [Required] Path to pepXML file
        -r      [Required] Path to percolator XML file
```
