#!/usr/bin/env python3

'''
author: caela northey
date: July 2019

Unilex is an accent-independent lexicon of English. Entries look like:

cats::NNS/NNP: { k * a t }> s > :{cat}>s>:5787

where each field is separated by a semi-colon. Fields include a word's
orthography, part-of-speech, pronunciation, and enriched orthography. For
more information on Unilex see http://www.cstr.ed.ac.uk/projects/unisyn/.

This script splits a Unilex data file into training, development, and test
datasets (80%, 10%, 10%) where each dataset contains no overlapping word
roots. That is, all words with the root "cat" (cats, catnip, catnap, etc.) are
only present in the training set. This purpose of this division is to simulate
how well a NN trained on one set of roots could generalize to words with
previously unseen roots.

'''

import random

class Entry:

    def __init__(self, ortho, id, pos, pron, enriched_ortho, freq):
        self.ortho = ortho
        self.id = id
        self.pos = pos
        self.pron = pron
        self.enriched_ortho = enriched_ortho
        self.freq = freq

#path is like ""../data/train"
def print_entry_files(entries, path):

    #write source (i.e., grapheme) and target (i.e., enriched orthography)
    with open(path+"_src.txt", "w+") as out_src, open(path+"_tgt.txt", "w+") as out_tgt:

        #" ".join(string) adds space between each char
        for ent in entries:

            out_src.write(" ".join(ent.ortho)+"\n")
            out_tgt.write(" ".join(ent.enriched_ortho)+"\n")

    out_src.close()
    out_tgt.close()

# read in unilex file, extract grapheme and morpho-segmented grapheme
# print train (80%), dev(10%), test(10%) sets to ../data folder
def main():

    #store all Entry objs with same root in a list,
    #lists are stored in dict keyed my root
    entries = {}
    num_lines = 0

    #get all entries
    with open("../data/unilex_gam", "r") as infile:

        for l in infile:

            fields = l.split(":")
            ortho = fields[0]
            id = fields[1]
            pos = fields[2]
            pron = fields[3]
            enriched_ortho = fields[4]
            freq = fields[5]
            ent = Entry(ortho, id, pos, pron, enriched_ortho, freq)

            i = enriched_ortho.index("{")
            j = enriched_ortho.index("}")
            root = enriched_ortho[i+1:j]

            if root not in entries:
                entries[root] = [ent]
            else:
                entries[root].extend([ent])

            num_lines += 1

    infile.close()

    #split into train/dev/test
    ten_percent = int(num_lines/10)

    #make test set of roots not in traning
    test_count = 0
    test_set = []

    while test_count < ten_percent:

        root, ents = random.choice(list(entries.items()))
        test_set.extend(ents)
        test_count += len(ents)
        del entries[root]

    #make dataset with unseen dev set
    dev_count = 0
    dev_set = []

    while dev_count < ten_percent:

        root, ents = random.choice(list(entries.items()))
        dev_set.extend(ents)
        dev_count += len(ents)
        del entries[root]

    #add rest to training set
    train_set = []

    for ents in entries.values():
        train_set.extend(ents)

    random.shuffle(train_set)
    random.shuffle(dev_set)
    random.shuffle(test_set)

    print_entry_files(train_set, "../data/train")
    print_entry_files(dev_set, "../data/dev")
    print_entry_files(test_set, "../data/test")

if __name__ == "__main__":
    main()
