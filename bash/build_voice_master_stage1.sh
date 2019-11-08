#!/bin/bash

#builds a voice from a cleaned recordings folder
if [ "$#" -lt 2 ] ; then
  echo "Usage: $0 path/to/clean/recs/dir name_of_new_voice"
  echo " - run from ss root"
  echo " - creates new voice folder called name_of_new_voice"
  echo " - assumes recs folder contains .wav and .txt files"
  echo " - .wav's should be cleaned (don't need to clean .txt's)"
  echo " - does up to phone set selection (when requires human check)"
  exit 1
fi

#==============================================================================
echo "LOG: splitting recs dir into wavs and labs"

mkdir $1/wavs $1/labs
mv $1/*.wav $1/wavs
mv $1/*.txt $1/labs

if [ "$?" -ne "0" ]; then
  echo "ERROR during split rec dir"
  exit 1
fi

#==============================================================================
echo "LOG: creating new base dir structure"

SSDIR=ss_$2
cp -r ss_template $SSDIR
source $SSDIR/setup.sh

if [ "$?" -ne "0" ]; then
  echo "ERROR during create new base dir"
  exit 1
fi

#==============================================================================
echo "LOG: creating voice utts.data"

scripts/make_utts.sh $1
mv $1/utts.data $SSDIR

if [ "$?" -ne "0" ]; then
  echo "ERROR during utts.data creation"
  exit 1
fi

#==============================================================================
echo "LOG: move recording files to new data folder"

mv $1/wavs/*.wav $SSDIR/recordings

if [ "$?" -ne "0" ]; then
  echo "ERROR during move recording files"
  exit 1
fi

#==============================================================================
echo "LOG: downsampling recordings to 16KHz"

downsample.sh $1

if [ "$?" -ne "0" ]; then
  echo "ERROR during downsampling"
  exit 1
fi

#==============================================================================
echo "LOG: labeling data part 1 - set dictionary and phone set to GAM"

setup_alignment

#choose dict and phone set
bash$ cp $MBDIR/resources/phone_list.unilex-gam alignment/phone_list
bash$ cp $MBDIR/resources/phone_substitutions.unilex-gam \
   alignment/phone_substitutions

touch $SSDIR/my_lexcion.scm

if [ "$?" -ne "0" ]; then
  echo "ERROR during setting dict and phone set"
  exit 1
fi

echo "LOG: check script against dictionary in Festival:"
echo " - bash> festival \$MBDIR/scm/build_unitsel.scm"
echo " - festival>(check_script \"utts.data\" 'unilex-rpx)"
echo " - festival>(exit)"
echo " - edit my_lexcion.scm file w pronunciations for any missing words"

#start time alignment
echo "LOG: labeling data > time align - create initial labels in Festival:"
echo " - bash$ festival \$MBDIR/scm/build_unitsel.scm ./my_lexicon.scm"
echo " - festival>(make_initial_phone_labs \"utts.data\" \"utts.mlf\" 'unilex-gam)"
echo " - festival>(exit)"
