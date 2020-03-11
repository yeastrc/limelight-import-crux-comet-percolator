Crux Comet/Percolator to limelight XML Converter
=======================================

Use this program to convert the results of a Crux Comet + Percolator analysis to
limelight XML suitable for import into the limelight web application. Requires
that the Percolator output be represented as XML (see -X option in Percolator).

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/limelight-import-crux-comet-percolator/releases).
2. Run the program ``java -jar cruxCometPercolator2LimelightXML.jar`` with no arguments to see the possible parameters. Requires Java 8 or higher.

Command line documentation
---------------------------

Usage: java -jar cruxCometPercolator2LimelightXML.jar -c path -d path -f path

Example: java -jar cruxCometPercolator2LimelightXML.jar
                                       -c /path/to/comet.params
                                       -f /path/to/fasta.fa
                                       -d /path/to/crux-output-directory

Options:
```
  -c	[Required] Path to comet .params file
  -f	[Required] Path to FASTA file used in the experiment.
  -d	[Required] Path to the output directory for Crux (contains pepXML files) and
	    crux-output-xml directory.
```
