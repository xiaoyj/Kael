import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;


/**
 * DNSWatcher主函数
 */
public class Watcher {

    public Map<String, String> excelRead(String excelRouting){
        File file = new File(excelRouting);
        Map<String,String> webMap = new HashMap<String,String>();
        ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(file);
        for(int i = 0 ;i < result.size() ;i++){
//            System.out.print(result.get(i).get(0));
            webMap.put(result.get(i).get(0).toString(),result.get(i).get(1).toString());
        }
        return webMap;
    }

    public Map<String, String> dnsLookup(String excelRouting){
        Map<String,String> webMap=excelRead(excelRouting);
        Map<String,String> ipIsTrue= new HashMap<String,String>();
        String ipLookup="";
        for (String web:webMap.keySet()){
            try {
                ipLookup = Inet4Address.getByName(web).getHostAddress().toString();
            } catch (UnknownHostException e) {
                ipLookup ="error";
            }
            if (webMap.get(web).toString().contains(ipLookup)){
                ipIsTrue.put(web,ipLookup+" success");
            }else {
                ipIsTrue.put(web,ipLookup+" false");
            }
        }
        return ipIsTrue;
    }
}
