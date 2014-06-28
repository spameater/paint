package ClientFtp;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by yuechuan on 24/06/14.
 * contains the methods needed to upload images and stuff
 * TODO complete this
 */
public class ClientFtp {
    String host;
    String pass, uname;
    int port;
    FTPClient ftpClient;

    /**
     * builder class to build the ftp client
     */
    public static class FTPClientBuilder {
        String url = null;
        String uname, pass;
        int port;

        public FTPClientBuilder(String url) {
            this.url = url;
        }

        /**
         * take the builder object and convert it into a client object
         *
         * @return a FTPClient object
         */
        public ClientFtp build() {
            return new ClientFtp(url, uname, pass, port);
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setLogin(String uname) {
            this.uname = uname;
        }

        public void setPassword(String upass) {
            this.pass = upass;
        }
    }
    /*
    ftpClient.connect(InetAddress.getByName(server));
    ftpClient.login(user, password);
    ftpClient.changeWorkingDirectory(serverRoad);
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    BufferedInputStream buffIn=null;
*/

    private ClientFtp(
            String host,
            String uname,
            String pass,
            int port
    ) {
        this.host = host;
        this.uname = uname;
        this.pass = pass;
        this.port = port;
        ftpClient = new FTPClient();
    }

    /**
     * open connection if connection not opened
     * calls this to open the connection before
     * making requests. take note of time outs
     */
    public void openConnection() throws IOException{
        if (!ftpClient.isConnected()){
            ftpClient.connect(InetAddress.getByName(host));
            ftpClient.login(uname, pass);
            Log.i(TAG,"connection opened");
        }
        else Log.i(TAG,"connection already open");
    }

    /**
     * uploads the bitmap to the server
     *
     * @param bm bitmap to be uploaded
     */
    public void uploadBitmap( Bitmap bm , String directory ){

        try {
            openConnection();
            ftpClient.changeWorkingDirectory(directory);//TODO set directory
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            buffIn = new BufferedInputStream(new FileInputStream(file));
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile("test.txt", buffIn);
            buffIn.close();
            closeConnection();
        } catch (Exception e) {
            Log.d(ClientFtp.class.getName(), "error: " + e.getMessage());
        }
    }

    /**
     * close the connection if necessary
     * @throws IOException
     */
    private void closeConnection() throws IOException {
        if (ftpClient.isConnected()){
            ftpClient.logout();
            ftpClient.disconnect();
            Log.i(TAG,"connection closed");
        }
        else Log.i(TAG,"connection already closed");
     }

}
