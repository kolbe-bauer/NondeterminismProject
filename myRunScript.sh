#!/bin/sh

pwd=`pwd`

contege="${pwd}/pre-built/ConTeGe.jar"
contegeLibs="${pwd}/lib/scala-lib-2.11.8.jar:${pwd}/lib/asm-tree-4.0.jar:${pwd}/lib/asm-4.0.jar:${pwd}/lib/tools.jar:${pwd}/lib/testSkeleton.jar:${pwd}/lib/commons-io-2.0.1.jar:${pwd}/lib/jpf.jar:${pwd}/lib/bcel-5.2.jar"
contegeOwnLibs="${pwd}/ownLibs/javaModel.jar:${pwd}/ownLibs/clinitRewriter.jar"

# CUT: XYSeries
bmBase="${pwd}/benchmarks/pldi2012"
benchmark="$1"
jarFile=`ls ${bmBase}/${benchmark}/jar/*.jar`
libFiles=`ls ${bmBase}/${benchmark}/lib/*.jar`
jarLibs=""
for eachfile in $libFiles
do
    jarLibs="${jarLibs}:${eachfile}"
done
envTypes=`ls ${bmBase}/${benchmark}/env_types.txt`
CUT=`head -n 1 "${bmBase}/${benchmark}/cut.txt"`


# ClassTester arguments:
#
# 0: the class under test (CUT)
# 1: file with names of auxiliary types
# 2: random seed
# 3: max. nb of tries to generate suffixes (i.e. how long it should run)
# 4: result file (only written when a thread safety violation is found)
# 5: whether to reset static state before each test (only works when classes have been instrumented with ClinitRewriter)

seed=3
maxSuffixGenTries=100

mycmd="java -cp ${contegeLibs}:${contege}:${contegeOwnLibs}:${jarFile}${jarLibs} contege.ClassTester ${CUT} ${envTypes} ${seed} ${maxSuffixGenTries} result.out false"

echo "${mycmd}\n"
eval "${mycmd} > $1.out"
cat result.out

##Testing code
# yourfilenames=`ls ${bmBase}/${bm}/jar/*.jar`
# for eachfile in $yourfilenames
# do
#    echo "${eachfile}"
# done

# benchmark="$1"
# jarFile=`ls ${bmBase}/${benchmark}/jar/*.jar`
# jarLibs=`ls ${bmBase}/${benchmark}/lib/*.jar`
# envTypes=`ls ${bmBase}/${benchmark}/env_types.txt`
# CUT=`head -n 1 "${bmBase}/${benchmark}/cut.txt"`
# CUT=`read -r CUT < "${bmBase}/${bm}/cut.txt"`
# echo "${jarFile}"
# echo "${libFiles}"
# echo "${jarLibs}"
# echo "${envTypes}"
# echo "${CUT}"
#
# mycmd="java -cp ${contegeLibs}:${contege}:${contegeOwnLibs}:${jarFile}:${jarLibs} contege.ClassTester ${CUT} ${envTypes} ${seed} ${maxSuffixGenTries} result.out false"
# echo "${mycmd}"
