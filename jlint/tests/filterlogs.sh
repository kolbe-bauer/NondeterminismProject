#! /usr/bin/bash

logs=$(find . -type f -name *.log)

for f in ${logs}
do
	grep -vE 'Verification|overridden|shadows|replaced' ${f} >> "${f}_filtered"
done
