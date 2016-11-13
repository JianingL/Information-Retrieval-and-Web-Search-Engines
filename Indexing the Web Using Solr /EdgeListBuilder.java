import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.*;

public class EdgeListBuilder {
	public static void main(String[] args) throws Exception{

		String dirPath = "/Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/html";
		String mapPath = "/Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/map.csv";
		String outPath = "/Users/JianingLiu/Desktop/572/homework/hw3/result/edgelist";
		PrintWriter writer = new PrintWriter(outPath);
		Map<String, String> fileUrlMap = new HashMap<String, String>();
		Map<String, String> urlFileMap = new HashMap<String, String>();
		BufferedReader in = new BufferedReader(new FileReader(mapPath));
		String line = null;
		while((line = in.readLine()) != null){
			String[] arr = line.split(",");
			assert(arr.length == 2);
			fileUrlMap.put(arr[0], arr[1]);
			urlFileMap.put(arr[1], arr[0]);
		}
		System.out.println("FU" + fileUrlMap.size());
		System.out.println("UF" + urlFileMap.size());
		in.close();
		
		File dir = new File(dirPath);
		Set<String> edges = new HashSet<String>();
		
		for(File file: dir.listFiles()){
			if(file.getName().equals(".DS_Store")) continue;
			Document doc = Jsoup.parse(file, "UTF-8", fileUrlMap.get(file.getName()));
			Elements links = doc.select("a[href]");
			
			Elements media = doc.select("[src]");
			Elements imports = doc.select("link[href]");
			
			for(Element link: links){
				String url = link.attr("href").trim();
				if(urlFileMap.containsKey(url)){
					edges.add(file.getName() + " " + urlFileMap.get(url));
				}
			}
		}
		
		for(String s: edges){
			writer.println(s);
		}
		writer.flush();
		writer.close();
		System.out.println("finish");
	}
}
