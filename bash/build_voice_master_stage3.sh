#!/bin/bash

#builds a voice from a cleaned recordings folder
if [ "$#" -lt 1 ] ; then
  echo "Usage: $0 path/to/partial/voice/dir"
  echo " - run from ss root"
  echo " - path/to/partial/voice is dir created by build_voice_master_stage1.sh"
  echo " - does from building voice > pitch tracking to end where can then"
  echo "   run the voice in Festival."
  exit 1
fi

#==============================================================================
echo "LOG: building the voice > pitch tracking (approx 10 min)"

cd $1
mkdir f0
scripts/make_f0_cjn -f wav/*.wav

if [ "$?" -ne "0" ]; then
  echo "ERROR doing pitch tracking"
  exit 1
fi

#==============================================================================
echo "LOG: building the voice > join cost coefficients"

mkdir coef coef2
make_norm_join_cost_coefs coef f0 mfcc '.*.mfcc'

strip_join_cost_coefs coef coef2 utt/*.utt

if [ "$?" -ne "0" ]; then
  echo "ERROR doing join cost coefficients"
  exit 1
fi

#==============================================================================
echo "LOG: building the voice > waveform representation (5 min)"

mkdir lpc
make_lpc wav/*.wav

if [ "$?" -ne "0" ]; then
  echo "ERROR doing waveform representation"
  exit 1
fi

#==============================================================================
echo "LOG: running the voice!"

echo "Do these things in Festival:"
echo " - bash>festival"
echo " - festival>(voice_localdir_multisyn-gam)"
echo " - festival>(SayText \"Hello world.\")"
echo "DONE!"
