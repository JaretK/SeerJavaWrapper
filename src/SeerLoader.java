import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import Dependencies.BufferedArrayList;
import Dependencies.ImportVariable;

/**
 * MAIN LOGIC CLASS FOR THE JAVA SEER WRAPPER PROGRAM
 * 
 * @author KARNUTJ
 *
 */
public class SeerLoader {
	private String seerDirectory;
	private Set<ImportVariable> variables;

	private final BufferedArrayList<SeerObservation> arr;
	private long numberObservations = 0;

	/*
	 * accepts a sas file used to structure imports
	 * 
	 */
	public SeerLoader(String seerDirectory, File saveFile, int buffer) {
		this.seerDirectory = seerDirectory;
		arr = new BufferedArrayList<SeerObservation>(buffer, saveFile);
	}

	/*
	 * Buffer = 10,000 by default
	 */
	public SeerLoader(String seerDirectory, File saveFile) {
		this.seerDirectory = seerDirectory;
		arr = new BufferedArrayList<SeerObservation>((int) 1e4, saveFile);
	}

	/*
	 * Loads the sas import file and parses to set
	 */
	public void loadImportFile(File sasFile) {
		Set<ImportVariable> variables = new TreeSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader(sasFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("@")) {
					variables.add(new ImportVariable(line));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.variables = variables;
	}

	/*
	 * Reads the Seer data file given according to the import file loaded.
	 */
	public Map<String, HashMap<String, Long>> parseSeerFile(String directory, String datafile) {
		if (variables.size() == 0) {
			throw new IllegalStateException("Must load a SAS import file first");
		}
		File file = new File(seerDirectory + directory + datafile);
		SeerObservationFactory.setFactory(directory, datafile, variables);
		return parseFile(file);
	}

	/**
	 * Parses the SEER file and saves to the saveFile
	 * 
	 * @param readFile
	 */
	private Map<String, HashMap<String, Long>> parseFile(File readFile) {
		Map<String, HashMap<String, Long>> resultMap = new HashMap<>();
		long numLines = 0;
		try {
			numLines = numLines(readFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		long delta = numLines / 10;
		long tracker = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(readFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				numberObservations++; // records number of observations
				// prints progress to console
				tracker++;
				if (tracker > delta) {
					System.out.print("-");
					tracker = 0;
				}
				SeerObservation so = SeerObservationFactory.create();
				so.parse(line);

				//Return map with counts of all different response values
				for (String key : so.getObservations().keySet()) {
					String val = so.getObservations().get(key);
					if (!resultMap.containsKey(key)) {
						resultMap.put(key, new HashMap<String, Long>());
					}
					HashMap<String, Long> temp = resultMap.get(key);
					if (!temp.containsKey(val)){
						temp.put(val, 0L);
					}
					long tempVal = temp.get(val);
					temp.put(val, tempVal + 1);
				}
			}
			System.out.println(" (10/10)"); // reset console to new line

		} catch (IOException e) {
			e.printStackTrace();
		}
		arr.close();
		return resultMap;
	}

	private long numLines(File file) throws IOException {
		LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		lnr.skip(Long.MAX_VALUE);
		long num = lnr.getLineNumber() + 1; // Add 1 because line index starts
											// at 0
		// Finally, the LineNumberReader object should be closed to prevent
		// resource leak
		lnr.close();
		return num;
	}

	public Set<ImportVariable> getImportVariables() {
		return this.variables;
	}

	public long getNumberObservations() {
		return numberObservations;
	}

	public String getNumberObservationsFormatted() {
		return NumberFormat.getNumberInstance(Locale.US).format(numberObservations);
	}
}
