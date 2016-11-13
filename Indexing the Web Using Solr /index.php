<?php
$file = fopen("/Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/map.csv","r");
$map = array();

while ($line = fgetcsv($file)) {
  $map[$line[0]] = $line[1];
}
fclose($file);
// $map['00a1a5f8-2b75-407f-a047-6a4243707966.html'] = "http://www.latimes.com/opinion/topoftheticket/la-na-tt-taibbi-hillary-20160915-snap-story"
?>

<?php

// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$results = false;
$algo = isset($_REQUEST['radio']) ? $_REQUEST['radio'] : defaultRank;

if ($query)
{
  // The Apache Solr Client library should be on the include path
  // which is usually most easily accomplished by placing in the
  // same directory as this script ( . or current directory is a default
  // php include path entry in the php.ini)
  require_once('Apache/Solr/Service.php');

  // create a new solr service instance - host, port, and webapp
  // path (all defaults in this example)
  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/latimes_huffington');

  // if magic quotes is enabled then stripslashes will be needed
  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }
  
  if ($algo == 'pageRank') {
    $sort = 'pageRankFile desc';
  } else {
    $sort = '';
  }
  $additionalParameters = array('sort' => $sort);

  // in production code you'll always want to use a try /catch for any
  // possible exceptions emitted  by searching (i.e. connection
  // problems or a query parsing error)
  try
  {
    $results = $solr->search($query, 0, $limit, $additionalParameters);
  }
  catch (Exception $e)
  {
    // in production you'd probably log or email this error to an admin
    // and then show a special message to the user but for this example
    // we're going to show the full exception
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>PHP Solr Client Example</title>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get">
      <label for="q">Search:</label>
      <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
      <input type="radio" name="radio" id="radio" <?php if (isset($_GET['radio']) && $_GET['radio']=="defaultRank") echo "checked";?> onclick="javascript: submit()" value="defaultRank" />defaultRank
      <input type="radio" name="radio" id="radio" <?php if (isset($_GET['radio']) && $_GET['radio']=="pageRank") echo "checked";?> onclick="javascript: submit()" value="pageRank" />pageRank
      <input type="submit"/>
    </form>
<?php
// display results
if ($results)
{
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <ol>
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {

?>
      <li>
      
<?php
    // iterate document fields / values
    foreach ($doc as $field => $value)
    {
      if ($field == 'id') {
        // $value = /Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/html/bbe42339-e19e-4df3-893e-85914e90c49d.html
        $len = strlen('/Users/JianingLiu/Desktop/572/homework/hw3/solr-6.2.1/crawl_data/html/');
        $url = $map[substr($value, $len)];
?>
        <p>
          url: <a href=<?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?>><?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?></a>
        </p>  
        <p>
          id: <?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?>
        </p>
         
<?php
      }
      if ($field == 'title') {
?>
      <p>
        title: <?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?>
      </p>
<?php
      }
      if ($field == 'description') {
?>
      <p>
        description: <?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?>
      </p>
<?php
      }

?>
        

<?php
    }
?>
      </li>
<?php
  }
?>
    </ol>
<?php
}
?>
  </body>
</html>