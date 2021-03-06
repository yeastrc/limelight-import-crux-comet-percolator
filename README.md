[![Build Status](https://travis-ci.com/yeastrc/limelight-import-crux-comet-percolator.svg?branch=master)](https://travis-ci.com/yeastrc/limelight-import-crux-comet-percolator)


Crux Comet/Percolator to limelight XML Converter
===================================================

Use this program to convert the results of a Crux Comet + Percolator (https://github.com/crux-toolkit/crux-toolkit) analysis to
limelight XML suitable for import into the limelight web application. Requires that
comet results be output as PepXML and that percolator output be represented as XML.

For example, running crux comet as (for a single mzML file):
```
crux comet --decoy_search 1 --output_percolatorfile 1 ./myspectra.mzML ./human.fasta
```

Or for multiple mzML files:
```
crux comet --decoy_search 1 --output_percolatorfile 1 ./rep1.mzML ./rep2.mzML ./rep3.mzML ./human.fasta
```

Then running Percolator as:
```
crux percolator --pout-output T crux-output/comet*.pin
```

Note about multiple comet searches in one percolator run
-----------------------------------------------------------
When multiple comet output files are processed at once by Percolator, the results of all of these will be present in a single Limelight XML file. This will appear in limelight as a single upload, but the results from each individual comet search will be present as distinct subsearches and may be viewed separately and compared to one another.

How To Run Converter
----------------------
1. Download the [latest release](https://github.com/yeastrc/limelight-import-crux-comet-percolator/releases).
2. Run the program ``java -jar cruxCometPercolator2LimelightXML.jar`` with no arguments to see the possible parameters. Requires Java 8 or higher.

Command line documentation
---------------------------

```
java -jar cruxCometPercolator2LimelightXML.jar [-hvV] -d=<cruxOutputDirectory>
                                               -f=<fastaFile> -o=<outFile>

Description:

Convert the results of a Crux (Comet + Percolator) analysis to a Limelight XML
file suitable for import into Limelight.

More info at: https://github.com/yeastrc/limelight-import-crux-comet-percolator

Options:
  -d, --directory=<cruxOutputDirectory>
                             Full path to the crux output directory. E.g.,
                               /data/my_analysis/crux-output
  -f, --fasta-file=<fastaFile>
                             Full path to FASTA file used in the experiment. E.g.,
                               /data/yeast.fa
  -o, --out-file=<outFile>   Full path to use for the Limelight XML output file. E.
                               g., /data/my_analysis/crux.limelight.xml
  -v, --verbose              If this parameter is present, error messages will
                               include a full stacktrace. Helpful for debugging.
  -h, --help                 Show this help message and exit.
  -V, --version              Print version information and exit
```
