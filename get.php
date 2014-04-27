
<html>
<head>
<?php
    
    $GLOBALS['THRIFT_ROOT'] = 'thrift';
    
    require_once('thrift/Thrift.php');
    
    require_once( $GLOBALS['THRIFT_ROOT'].'/type/TMessageType.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/type/TType.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/exception/TException.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/factory/TStringFuncFactory.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/stringfunc/TStringFunc.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/stringfunc/Core.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/transport/TSocket.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/transport/TBufferedTransport.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/protocol/TBinaryProtocol.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/lib/Hbase.php' );
    require_once( $GLOBALS['THRIFT_ROOT'].'/lib/Types.php' );
    use Thrift\Transport\TSocket;
    use Thrift\Transport\TBufferedTransport;
    use Thrift\Protocol\TBinaryProtocol;
    use Hbase\HbaseClient;
    use Hbase\Mutation;
    use Hbase\TScan;
    
    
    function searchDup(){
        
        $socket = new TSocket('localhost', 9090);
        $socket->setSendTimeout(10000); // Ten seconds (too long for production, but this is just a demo ;)
        $socket->setRecvTimeout(20000); // Twenty seconds
        $transport = new TBufferedTransport( $socket );
        $protocol = new TBinaryProtocol( $transport );
        $client = new HbaseClient( $protocol );
        
        
	    try {
            $transport->open();
            
            $table = 'dup';
            $rowkey = $_POST['md5'];
            $column = 'cf:label';
            $data = array();
            //	print_r( $client->get($table, $rowkey, $column, $data )[0]);
            $value = $client->get($table,$rowkey,$column,$data);
            $transport->close();
            
            foreach ($value as $k => $v) {
			    // $k = TCell
                $output =  (string)$v->value;
//                echo gettype($output),"\n";  ---> string
                $parts = explode("#", $output);
//                echo gettype($parts),"\n";   ---> array
                
                foreach ($parts as $k => $v){
                    if (strlen($v) == 0)
                        echo "------Done------";
                    else
                        echo " People labels this as --->   {$v}, <br>";
                }
                
            }
            
        } catch (TException $e) {
            print 'TException: '.$e->__toString().' Error: '.$e->getMessage()."\n";
        }
    }
    
    if(isset($_POST['search']))
    {
        searchDup();
    }
    ?>
</body>
</html>
