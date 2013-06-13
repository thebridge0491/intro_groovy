# Groovymixed-archetype
<!-- .md to .html: markdown foo.md > foo.html
                   pandoc -s -f markdown_strict -t html5 -o foo.html foo.md -->

A Maven archetype template for mixed Groovy project.

## Installation
source code tarball download:
        # [aria2c --check-certificate=false | wget --no-check-certificate | curl -kOL]
        FETCHCMD='aria2c --check-certificate=false'
        $FETCHCMD https://bitbucket.org/thebridge0491/groovymixed/[get  | archive]/master.zip

version control repository clone:
        git clone https://bitbucket.org/thebridge0491/groovymixed.git

cp pom.xml src/main/resources/ ; mvn install

## Usage
		// example
		cd <path> ; mvn archetype:generate [-DinteractiveMode=false] -DarchetypeCatalog=local -DarchetypeGroupId=org.sandbox -DarchetypeArtifactId=groovymixed-archetype -Ddate=2013-06-13 -DgroupId=org.sandbox -Dparent=intro_groovy -Dname=util -Dversion=0.1.0 [-DtestFrwk=spock -DffiLib=none -Dexecutable=no]

## Author/Copyright
Copyright (c) 2013 by thebridge0491 <thebridge0491-codelab@yahoo.com>


## License
Licensed under the Apache-2.0 License. See LICENSE for details.

