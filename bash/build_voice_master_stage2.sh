#!/bin/bash

#builds a voice from a cleaned recordings folder
if [ "$#" -lt 1 ] ; then
  echo "Usage: $0 path/to/partial/voice/dir"
  echo " - run from ss root"
  echo " - path/to/partial/voice is dir created by build_voice_master_stage1.sh"
  echo " - does from to phone set selection (when requires human check) to"
  echo "   building the utterance structure (which also requires human check)"
  exit 1
fi

#==============================================================================
echo "LOG: labeling data > time align - extracting MFCCs"

cd $1
make_mfccs alignment wav/*.wav

if [ "$?" -ne "0" ]; then
  echo "ERROR extracting mfccs"
  exit 1
fi

#==============================================================================
echo "LOG: labeling data > time align - doing the alignment (approx 20 min)"

cd alignment
make_mfcc_list ../mfcc ../utts.data train.scp
do_alignment .


if [ "$?" -ne "0" ]; then
  echo "ERROR doing the alignment"
  exit 1
fi

#==============================================================================
echo "LOG: labeling data > time align - splitting the MLF file"

cd ..
mkdir lab
break_mlf alignment/aligned.3.mlf lab

if [ "$?" -ne "0" ]; then
  echo "ERROR splitting the MLF file"
  exit 1
fi

#==============================================================================
echo "LOG: pitchmarking the speech (as female) (10 min)"

scripts/make_pm_wave_cjn -f pm wav/*.wav
make_pm_fix pm/*.pm

if [ "$?" -ne "0" ]; then
  echo "ERROR pitchmarking the speech "
  exit 1
fi

#==============================================================================
echo "LOG: pitchmarking the speech > view pitchmarks"

mkdir pm_lab
make_pmlab_pm pm/*.pm

if [ "$?" -ne "0" ]; then
  echo "ERROR viewing pitchmarks"
  exit 1
fi

#==============================================================================
echo "LOG: building voice > utterance structure in Festval"

mkdir utt dur

echo "Do these things in Festival:"
echo " - festival \$MBDIR/scm/build_unitsel.scm my_lexicon.scm"
echo " - festival>(build_utts \"utts.data\" 'unilex-gam)"
echo "Then, run:"
echo " - phone_lengths dur lab/*.lab"
echo " - festival \$MBDIR/scm/build_unitsel.scm"
echo " - festival>(add_duration_info_utts \"utts.data\" \"dur/durations\")"
echo "Then, run build_voice_master_stage3.sh"
