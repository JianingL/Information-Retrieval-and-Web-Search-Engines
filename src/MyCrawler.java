import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
  private final static Pattern EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png|jpeg|tif|pdf|doc|html)$");
  
  FileWriter fwFetch;
  FileWriter fwVisit;
  FileWriter fwUrls;
  @Override
  /**
   * This function is called just before starting the crawl by this crawler
   * instance. It can be used for setting up the data structures or
   * initializations needed by this crawler instance.
   */
  public void onStart() {
    // Do nothing by default
    // Sub-classed can override this to add their custom functionality
	 try {
		//fwFetch = new FileWriter("111fetch_Huffington.csv", true);
		fwVisit = new FileWriter("????visit_Huffington.csv", true);
		//fwUrls = new FileWriter("111urls_Huffington.csv", true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  @Override
  /**
   * This function is called just before the termination of the current
   * crawler instance. It can be used for persisting in-memory data or other
   * finalization tasks.
   */
  public void onBeforeExit() {
	  try {
		//fwFetch.close();
		fwVisit.close();
		//fwUrls.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    // Do nothing by default
    // Sub-classed can override this to add their custom functionality
  }

  
  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    
    //System.out.println("shoudvisit: " + url);
// URLs:
    
    //System.out.println("All urls: " + url);
    href.replaceAll(",", "-");
//    if (!href.startsWith("http://www.huffingtonpost.com/")) {
//    	try {
//			fwUrls.write(href);
//			fwUrls.write(",");
//			fwUrls.write("N_OK");
//	    	fwUrls.write("\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	//System.out.println("Indicator:" + "N_OK");
//    } else {
//    	try {
//			fwUrls.write(href);
//			fwUrls.write(",");
//			fwUrls.write("OK");
//	    	fwUrls.write("\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	//System.out.println("Indicator:" + "OK");
//    }
//    
//    
    if (!href.startsWith("http://www.latimes.com/")) {
      return false;
    } else if (EXTENSIONS.matcher(href).matches()) {
    	return true;
    } else {
    	return true;
    }
  }
//  @Override
//  protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
//	  String href = webUrl.getURL().toLowerCase();
//	  href.replaceAll(",", "-");
//	    // Do nothing by default
//	    // Sub-classed can override this to add their custom functionality
//	  try {
//		fwFetch.write(href + ",");
//		fwFetch.write(statusCode + "");
//		fwFetch.write("\n");
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}		
//  }
  
  
  /**
  * This function is called when a page is fetched and ready * to be processed by your program.
  */
  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL(); 
    url.replaceAll(",", "-");
    String contentType = page.getContentType();
    System.out.println(contentType);
    if (!contentType.startsWith("text/html") && !contentType.startsWith("image/") && !contentType.startsWith("application/pdf") && !EXTENSIONS.matcher(url).matches()) {
    	System.out.println("not visit type: " + contentType);
    	return;
    }
    int size = page.getContentData().length;
   
    if (page.getParseData() instanceof HtmlParseData || page.getParseData() instanceof BinaryParseData ) {
        Set<WebURL> links = page.getParseData().getOutgoingUrls();
        try {
        	fwVisit.write(url + ",");
            fwVisit.write(size + ",");
			fwVisit.write(links.size() + ",");
			fwVisit.write(contentType + "");
			fwVisit.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
         
    }
  }
}

