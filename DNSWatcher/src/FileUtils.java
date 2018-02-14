import java.io.File;

/**
 * 对文件操作的工具类
 * @author admin
 *
 */
public class FileUtils {

    /**
     * 删除指定文件夹下的所有文件，此文件夹内只有文件，没有任何文件夹
     */
    public static Boolean deleteAllFiles(String filePath){
        Boolean result = false;
        File file = new File(filePath);
        File temp = null;
        if(file.exists()){
            String [] tempList = file.list();
            for (String string : tempList) {
                temp = new File(filePath+"/"+string);
                if(temp.isFile()){
                    temp.delete();
                }
            }
            tempList = file.list();
            if(tempList.length==0){
                result = true;
            }
        }
        return result;
    }
}