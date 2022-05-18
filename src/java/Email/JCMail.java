package Email;

import java.io.File;
import java.util.Date;
import javax.mail.Message;
import javax.mail.Session;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

public class JCMail {

    private String from = "";//tu_correo@gmail.com
    private String password = "";//tu password: 123456 :)
    private String host = "";
    private String port = "";
    // destinatario1@hotmail.com,destinatario2@hotmail.com, destinatario_n@hotmail.com
    private InternetAddress[] addressTo;
    private String Subject = "";//titulo del mensaje
    private String MessageMail = "";//contenido del mensaje

    public JCMail() {
    }

    public void SEND(String nombreFactura, String dirFacturaAutorizada, boolean sslHabilitado) throws Exception {
           Properties props = new Properties();
            props.put("mail.smtp.host", getHost());
            props.put("mail.smtp.port", getPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            if (sslHabilitado) {
                props.put("mail.smtp.ssl.enable", "true");
            }
            props.put("mail.smtp.ssl.trust", getHost());
            //

            //
            SMTPAuthenticator auth = new SMTPAuthenticator(getFrom(), getPassword());
            //si no se le pone getInstance el sistema envia los correos por el primer servidor de correo
            //que utilizó
            Session session = Session.getInstance(props, auth);
            session.setDebug(false);
            //Se crea destino y origen del mensaje
            MimeMessage mimemessage = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(getFrom());
            mimemessage.setFrom(addressFrom);
            mimemessage.setRecipients(Message.RecipientType.TO, addressTo);
            mimemessage.setSubject(getSubject());
            // Se crea el contenido del mensaje
            MimeBodyPart mimebodypart = new MimeBodyPart();
            mimebodypart.setText(getMessage());
            mimebodypart.setContent(getMessage(), "text/html");

            File documento = new File(dirFacturaAutorizada);
            boolean existeDoc = false;
            BodyPart adjunto = new MimeBodyPart();
            if (documento.exists()) {
                existeDoc = true;

                adjunto.setDataHandler(
                        new DataHandler(new FileDataSource(dirFacturaAutorizada)));
                adjunto.setFileName(nombreFactura);
            }
            Multipart multipart = new MimeMultipart();

            dirFacturaAutorizada = dirFacturaAutorizada.replace(".xml", ".pdf");
            nombreFactura = nombreFactura.replace(".xml", ".pdf");
            BodyPart adjunto2 = new MimeBodyPart();
            adjunto2.setDataHandler(
                    new DataHandler(new FileDataSource(dirFacturaAutorizada)));
            adjunto2.setFileName(nombreFactura);
            multipart.addBodyPart(adjunto2);

            multipart.addBodyPart(mimebodypart);
            if (existeDoc) {
                multipart.addBodyPart(adjunto);
            }

            mimemessage.setContent(multipart);
            mimemessage.setSentDate(new Date());
            Transport.send(mimemessage);
           
    }

    //remitente
    public void setFrom(String mail) {
        this.from = mail;
    }

    public String getFrom() {
        return this.from;
    }

    //Contraseña
    public void setPassword(char[] value) {
        this.password = new String(value);
    }

    public String getPassword() {
        return this.password;
    }

    //destinatarios
    public void setTo(String mails) {
        String[] tmp = mails.split(",");
        addressTo = new InternetAddress[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            try {
                addressTo[i] = new InternetAddress(tmp[i]);
            } catch (AddressException ex) {
                System.out.println(ex);
            }
        }
    }

    public InternetAddress[] getTo() {
        return this.addressTo;
    }

    //titulo correo
    public void setSubject(String value) {
        this.Subject = value;
    }

    public String getSubject() {
        return this.Subject;
    }

    //contenido del mensaje
    public void setMessage(String value) {
        this.MessageMail = value;
    }

    public String getMessage() {
        return this.MessageMail;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

}
