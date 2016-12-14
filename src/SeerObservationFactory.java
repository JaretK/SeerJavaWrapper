import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import Dependencies.ImportVariable;

public class SeerObservationFactory{

	private static String originDataset;
	private static String originCancer;
	private static TreeSet<ImportVariable> importedVariables;
	private static Map<String, Character[]> observations;

	//Set of ImportVariable objects contains fields for importing
	public static void setFactory(String originDatasetz, String originCancerz, Set<ImportVariable> vars){
		originDataset = originDatasetz;
		originCancer = originCancerz;
		importedVariables = (TreeSet<ImportVariable>) vars;
	}

	public static SeerObservation create(){
		return new SeerObservation(originDataset, originCancer, importedVariables);
	}

}
