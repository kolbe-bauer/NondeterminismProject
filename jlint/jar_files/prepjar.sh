#! /usr/bin/bash

TEST_PATH="./class_files"
TEST_NUM=1
TEST_JAR_MAP="test_jar_map.txt"

rm -f ${TEST_JAR_MAP}
rm -rf ${TEST_PATH}
mkdir ${TEST_PATH}
DIR=$(find . -type d -name jar)

for f in ${DIR}
do
	echo "${TEST_NUM}: ${f}" >> ${TEST_JAR_MAP}
	JAR_FILE=$(find ${f} -type f -name *.jar)
	if [ -n ${JAR_FILE} ]; then
		echo ${JAR_FILE}
		dir_name="${TEST_PATH}/test${TEST_NUM}"
		mkdir ${dir_name}
		cp ${JAR_FILE} ${dir_name}
		JAR_FILE=$(cut -d / -f 4 <<< ${JAR_FILE})
		cd ${dir_name}
		jar -xf ${JAR_FILE}
		rm ${JAR_FILE}
		classes=$(find . -type f -name *.class)
		for g in ${classes}
		do
			mv ${g} .
		done
		rm -Rf -- */
		cd ../..
		((TEST_NUM++))
	fi
done
