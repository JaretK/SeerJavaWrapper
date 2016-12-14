import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;


public class MainLoader {
	public static final String desktop = "C:/Users/karnutj/Desktop/";
	public static final String incidence = "C:/Users/karnutj/Desktop/SEER_1973_2013_TEXTDATA.d04122016/SEER_1973_2013_TEXTDATA/incidence/";
	
	/*
	 * Designates the datasets within the SEER incidence folder that the program
	 * should read
	 */
	private static String[] datasets = { "yr1973_2013.seer9/", "yr1992_2013.sj_la_rg_ak/", "yr2000_2013.ca_ky_lo_nj_ga/",
			"yr2005.lo_2nd_half/" };

	/*
	 * Designates the cancers within each dataset that the program should read
	 */
	private static String[] cancers = { "BREAST", "COLRECT", "DIGOTHR", "FEMGEN", "LYMYLEUK", "MALEGEN", "OTHER", "RESPIR",
			"URINARY" };

	
	public static void main(String[] args) {
		long initTime = System.currentTimeMillis();
		long total = 0;
		long totalSize = 0;
		HashMap<String, Long> occs = new HashMap<>();
		for (int i = 0 ; i < datasets.length; i++){
			String ds = datasets[i];
			long datasetNum = 0;
			for (int j = 0; j < cancers.length; j++){
				String c = cancers[j];
				File importFile = new File(incidence+"read.seer.research.nov15.sas");
				File saveFile = new File(ds.substring(0, ds.length()-1)+"_"+c+".ser");
		
				SeerLoader loader = new SeerLoader(incidence, saveFile, 50000);
				loader.loadImportFile(importFile);
				Map<String, HashMap<String, Long>> result = loader.parseSeerFile(ds, c+".txt");
				System.out.println("Finished "+ds+ c + " (Obs: "+loader.getNumberObservationsFormatted()+")");
				
				Gson gson = new Gson();
				String json = gson.toJson(result);
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
					bw.write(json);
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				total+=loader.getNumberObservations();
				datasetNum+=loader.getNumberObservations();				
				if (!occs.containsKey(c)){
					occs.put(c, 0L);
				}
				Long temp = occs.get(c);
				occs.put(c, temp + loader.getNumberObservations());
				
				totalSize+=saveFile.length();
			}
			System.out.println("\tFinished "+ds);
			System.out.println("\tNum: "+NumberFormat.getInstance(Locale.US).format(datasetNum));
		}
		
		long endTime = System.currentTimeMillis();
		long delta = endTime - initTime;
		System.out.println("Total num: "+total);
		System.out.println("Runtime: "+delta/(1000.0)+" s");
		System.out.println("Size: " + NumberFormat.getInstance(Locale.US).format(totalSize)+ " B");
	}
}
