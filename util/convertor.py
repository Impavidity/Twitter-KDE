import argparse
from collections import defaultdict
import re

idx_collect = defaultdict(list)

def load_id():
    files = ["id123", "id124", "id134", "id234"]
    for file in files:
        fin = open("data/{}".format(file))
        for line in fin.readlines():
            items = line.strip().split()
            qid = items[0]
            docid = items[1]
            time = items[2]
            score = items[3]
            idx_collect[qid].append((docid, time, score))

query_time = defaultdict(int)

# def load_query_time():
#     files = ["topics.microblog2011.txt", "topics.microblog2012.txt", "topics.microblog2013.txt", "topics.microblog2014.txt"]
#     qtime, qnum = [], []
#     for file in files:
#         fin = open("data/{}".format(file))
#         doc = "".join(fin.readlines())
#         pattern = re.compile(r'<num>((.|\s)+?)</num>')
#         m = pattern.findall(doc)
#         for item in m:
#             idx = item[0].replace("Number:", "").strip()
#             qnum.append(int(idx[2:]))
#         pattern = re.compile(r'<querytweettime>((.|\s)+?)</querytweettime>')
#         m = pattern.findall(doc)
#         for item in m:
#             querytime = item[0].strip()
#             qtime.append(int(querytime))
#     for qid, querytime in zip(qnum, qtime):
#         query_time[str(qid)] = querytime



def convert_ql(input, output):
    fin = open(input)
    fout = open(output, "w")
    for line in fin.readlines():
        items = line.strip().split()
        qid = items[0]
        docid = items[1]
        score = items[5]
        fout.write("{} Q0 {} 0 {} Baseline\n".format(qid, docid, score))
    fout.flush()
    fout.close()

def convert(input, output):
    fin = open(input)
    fout = open(output, "w")
    data_collection = defaultdict(list)
    for line in fin.readlines():
        items = line.strip().split('\t')
        qid = items[0]
        docseq = int(items[1])
        info = idx_collect[qid][docseq-1]
        if query_time[qid] == 0:
            query_time[qid] = int(info[1])
        score = float(items[3])
        data_collection[qid].append((qid, info[0], info[1], query_time[qid] - int(info[1]), score))
    for qid in data_collection:
        data_collection[qid] = sorted(data_collection[qid], key=lambda x:x[4], reverse=True)
    for qid in sorted(data_collection.keys()):
        for idx, items in enumerate(data_collection[qid]):
            fout.write('{} {} {} {} {} {}\n'.format(items[0], items[1], idx+1, items[2], items[3], items[4]))
    fout.flush()
    fout.close()





if __name__=="__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=str)
    parser.add_argument("--output", type=str)
    args = parser.parse_args()
    #convert_ql(args.input, args.output)
    load_id()
    # load_query_time()
    convert(args.input, args.output)

