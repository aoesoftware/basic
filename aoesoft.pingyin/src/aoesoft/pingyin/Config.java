package aoesoft.pingyin;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * ≈‰÷√œ‡πÿ.
 * @author tangxiucai
 *
 */
public class Config {
	private static String ID = "aoesoft";
	
	
	public static File getConfigDir() {
		   Location location = Platform.getConfigurationLocation();
		   if (location != null) {
		      URL configURL = location.getURL();
		      if (configURL != null
		            && configURL.getProtocol().startsWith("file")) {
		         return new File(configURL.getFile(), ID);
		      }
		   }
		   
		   // If the configuration directory is read-only,
		   // then return an alternate location
		   // rather than null or throwing an Exception.
		   return Platform.getStateLocation(Activator.getContext().getBundle()).toFile();
		}


}
