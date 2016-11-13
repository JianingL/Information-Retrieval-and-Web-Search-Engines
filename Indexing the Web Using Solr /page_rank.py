import sys
import networkx as nx


inputd = '/Users/JianingLiu/Desktop/572/homework/hw3/result/edgelist'
output = 'external_pageRankFile'
prefix = '/Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/html/'

target = open(output, 'w')

G = nx.read_edgelist(inputd)

pr = nx.pagerank(G, alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight',dangling=None)


for key, value in pr.iteritems():
	target.write(prefix + str(key) + '=' + str(value) + '\n')

target.close();