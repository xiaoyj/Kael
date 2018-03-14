import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
    public ArrayList<Map<String, String>> dnsLookup(String excelRouting){
        ArrayList<Map<String, String>> showList = new ArrayList<>();
        Map<String,String> webMap=excelRead(excelRouting);
        Map<String,String> ipIsTrue= new HashMap<String,String>();
        Map<String,String> ipIsFalse= new HashMap<String,String>();
        String provider=Designer.DNSIP;
//        System.out.println(provider);
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        // 指定DNS
        env.put(Context.PROVIDER_URL, "dns://" + provider);
        env.put("com.sun.jndi.dns.timeout.initial", "1000");// 连接时间
        env.put("com.sun.jndi.dns.timeout.retries", "3");// 连接次数
        DirContext ictx = null;
        Attributes attrs =null;
        for (String web:webMap.keySet()){
            String ipLookup="";
            try {
                try {
                    Runtime.getRuntime().exec("ipconfig /flushdns");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ictx = new InitialDirContext(env);
                attrs= ictx.getAttributes(web, new String[]{"A"});
//                System.out.println(attrs);
                for (Enumeration e = attrs.getAll(); e.hasMoreElements();) {
                    Attribute a = (Attribute) e.nextElement();
                    int size = a.size();
                    for (int i = 0; i < size; i++) {
                        // MX string has priority (lower better) followed by associated
                        // mailserver
                        // A string is just IP
                        ipLookup=ipLookup+((String) a.get(i))+";";
                    }// end inner for
                }// end outer for
            } catch (NamingException e) {
                e.printStackTrace();
                ipLookup = "error";
            }
            String[] iplookup_param=ipLookup.split(";");
            for(int i =0; i<iplookup_param.length;i++){
                if (webMap.get(web).toString().contains(iplookup_param[i])){
                    ipIsTrue.put(web,ipLookup+" success");
                }else {
                    ipIsFalse.put(web,ipLookup+" false");
                }
            }
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        showList.add(ipIsTrue);
        showList.add(ipIsFalse);
//        System.out.println(showList);
        return  showList;
    }

    class MapKeyComparator implements Comparator<String>{
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
