package assignment;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

/**
 * Main class for starting the server
 */
public class TomcatServer
{
    
    private static final int TOMCAT_PORT = 8080;
    
    /**
     * Configure the server
     */
    public void start() throws ServletException, LifecycleException
    {
        // Tomcat configuration
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(TOMCAT_PORT);
        tomcat.addWebapp("", new File("./src/main/webapp").getAbsolutePath());
        
        tomcat.start();
        tomcat.getServer().await();
    }
    
    public static void main(String[] args) throws ServletException, LifecycleException
    {
        new TomcatServer().start();
    }
}
