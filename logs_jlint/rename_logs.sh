#! /usr/bin/bash
TEST_JAR_MAP="test_jar_map.txt"
TEST_NUM=1

while read -r line
do
	TEST_CLASS=$(cut -d / -f 2 <<< ${line})
	if [[ -f "${TEST_NUM}.log_filtered" ]] 
	then
		cp "${TEST_NUM}.log_filtered" "${TEST_CLASS}.log_filtered"
	fi
	if [[ -f "${TEST_NUM}.err" ]] 
	then
		cp "${TEST_NUM}.err" "${TEST_CLASS}.err"
	fi

	rm -f ${TEST_NUM}.*
	((TEST_NUM++))
done < ${TEST_JAR_MAP}
