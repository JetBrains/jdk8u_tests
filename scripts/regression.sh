#!/bin/bash -x

usage ()
{
echo "Usage: regression.sh [options] <test_results_cur> <test_results_ref> <results>"
echo "Options:"
echo -e " -h, --help\tdisplay this help"
echo -e " -tc\tprint teacmity statistic"
echo -e ""
echo -e "test_results_* files content should be in csv format with header and tab separator:"
echo -e "The 1-st column is the test name"
echo -e "The 2-st column is the test value"
echo -e ""
echo -e "Example:"
echo -e "Test           Value"
echo -e "Testname       51.54"
}

while [ -n "$1" ]
do
case "$1" in
-h | --help) usage
exit 1 ;;
-tc) tc=1
  shift
  break  ;;
*) break;;
esac
done

if [[ "$#" < "3" ]]; then
    echo "Error: Invalid arguments"
    usage
    exit 1
fi

curFile=$1
refFile=$2
resFile=$3
testNamePrefix=$4
echo $curFile
echo $refFile
echo $resFile

curValues=`cat "$curFile" | cut -f 2 | tr -d '\t'`
#curValuesHeader=`echo "$curValues" | head -n +1`_cur
#header=`cat "$refFile" | head -n +1 | awk -F'\t' -v x=$curValuesHeader '{print "  "$1"\t"$2"_ref\t"x"\tratio"}'`

testContent=`paste -d '\t' $refFile <(echo "$curValues") | tail -n +2`
testContent=`echo "$testContent" | awk -F'\t' '{ if ($3>$2+$2*0.1) {print "* "$1"\t"$2"\t"$3"\t"$3/$2} else {print "  "$1"\t"$2"\t"$3"\t"$3/$2} }'`
#echo "$header" > $resFile
echo "$testContent" >> $resFile
cat "$resFile" | tr '\t' ';' | column -t -s ';' | tee $resFile

if [ -z $tc ]; then
exit 0
fi

echo "$testContent" 2>&1 | (
    while read -r s; do
        testname=`echo "$s" | cut -f 1 | tr -d "[:space:]" |  tr -d "*"`
        duration=`echo "$s" | cut -f 3`
        failed=`echo "$s" | cut -c1 | grep -c "*"`
        passed=`echo "$s" | cut -c1 | grep -c " "`
        state=0
        [ $passed -eq 1 ] && state=1
        [ $failed -eq 1 ] && state=2
        echo \#\#teamcity[testStarted name=\'$testNamePrefix$testname\']
        echo "$s"
        [ $state -eq 2 ] && echo \#\#teamcity[testFailed name=\'$testNamePrefix$testname\' message=\'$s\']
        echo \#\#teamcity[buildStatisticValue key=\'$testNamePrefix$testname\' value=\'$duration\']
        echo \#\#teamcity[testFinished name=\'$testNamePrefix$testname\' duration=\'$duration\']
        failed=0
        passed=0
    done
)