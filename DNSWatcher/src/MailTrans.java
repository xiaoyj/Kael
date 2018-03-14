import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTrans {

    public static void SendEmail(final String transEmail, final String transPasswd, ArrayList<String> receiveEmail,String text){
        // 收件人电子邮箱
        ArrayList<String> toTrans = receiveEmail;
//        String to = receiveEmail;

        // 发件人电子邮箱
        String from = transEmail;

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.126.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host",host);

        properties.put("mail.smtp.auth","true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(transEmail, transPasswd); //发件人邮件用户名、密码
            }
        });
      try
        {
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            for (int i =0;i<toTrans.size();i++)
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(toTrans.get(i)));

            // Set Subject: 头部头字段
            message.setSubject("DNS LookUp Error");

            // 设置消息体
            message.setText(text);

            // 发送消息
            Transport.send(message);
//            System.out.println("Sent message successfully....from runoob.com");
        }catch(MessagingException mex)
        {
            mex.printStackTrace();
        }
    }
}
