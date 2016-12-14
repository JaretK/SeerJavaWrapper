import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import Dependencies.ImportVariable;

/**
 * One observation within SEER
 * 
 * @author KARNUTJ
 *
 */
public class SeerObservation implements Serializable {
	private String originDataset;
	private String originCancer;
	private transient final Set<ImportVariable> importedVariables;
	private Map<String, String> observations;
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 861977998909353464L;

	public SeerObservation(String originDataset, String originCancer, TreeSet<ImportVariable> vars) {
		this.originDataset = originDataset;
		this.originCancer = originCancer;
		this.importedVariables = (TreeSet<ImportVariable>) vars;
		this.observations = new HashMap<>();
	}

	public void parse(String line) {
		Queue<ImportVariable> parser = new LinkedList<>(importedVariables);
		while (!parser.isEmpty()) {
			ImportVariable v = parser.poll();
			int location = v.getLocation();
			String name = v.getName();
			int numChar = v.getNumChar();
			String toParse = line.substring(location - 1, location + numChar - 1);
			//convert toParse to Byte Array
			Byte[] charObjectArray = 
				    toParse.chars().mapToObj(c -> (byte)c).toArray(Byte[]::new); 
			observations.put(name, toParse);
		}
	}
	
	public Map<String, String> getObservations(){
		return observations;
	}
}