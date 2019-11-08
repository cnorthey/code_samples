#!/usr/bin/env python3

"""
author: caela northey
date: June 2019

This script evaluates the word accuracy of a predicted dataset given a gold
standard. The predicted and gold datasets are compared line by line and the
word accuracy is the number of correct lines out of the total number of lines.
Optionally, errors from the prediction file can be written to an output file.

"""

import argparse
import re

parser = argparse.ArgumentParser(description = 'Prints word accuracy of predictions given gold standard.')
parser.add_argument("predictions", type = argparse.FileType('r'), help = "predictions filepath")
parser.add_argument("gold", type = argparse.FileType('r'), help = "gold standard filepath")
parser.add_argument('-out', type = argparse.FileType('w'), help = "file to write errors")
args = parser.parse_args()

with args.predictions as p:
    predictions = p.readlines()

with args.gold as g:
    gold = g.readlines()

if len(gold) != len(predictions):
    print("Error: Dataset size mis-match; prediction and gold datasets should have number of entries. Exiting.")
    exit()

total_correct = 0
total_errors = 0
error_lines = [] #list of tuples
index = 0
for pred in predictions:
    if pred == gold[index]:
        total_correct += 1
    else:
        total_errors +=1
        error_lines.append((predictions[index].strip(), gold[index]))
    index +=1

if args.out:
    with args.out as outfile:
        outfile.write("WORD,PREDICTION,GOLD\n" )

        for error in error_lines:
            word = re.sub(r'\W+', '', error[1])
            outfile.write(word+","+error[0]+","+error[1])

print("Total entries: ", len(predictions), "\tTotal correct: ", total_correct, "\tTotal errors: ", total_errors)
print("Word accuracy: ", (total_correct/index)*100, "%")
