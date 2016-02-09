package ecoRe.recyclingSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RMOSStateSerializer {
	static final File STATE_SAVED_FILE = new File("RMOSState.txt");

	public static List<RecyclingMachine> readStateFromFile() {
		List<RecyclingMachine> rcms = new ArrayList<>();
		FileReader fileReader = null;
		boolean errors = false;
		try {
			fileReader = new FileReader(STATE_SAVED_FILE);
			StringBuilder stringBuilder = new StringBuilder();
			char[] buffer = new char[4096];
			while (true) {
				int readCount = fileReader.read(buffer);
				if (readCount == -1)
					break;
				stringBuilder.append(buffer, 0, readCount);
			}
			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			JSONObject contents = jsonObject.getJSONObject("RMOS");
			JSONArray rcmArray = contents.getJSONArray("allRcms");
			for (int i = 0; i < rcmArray.length(); i++) {
				rcms.add(RecyclingMachine.createFromJSONObject(rcmArray
						.getJSONObject(i)));
			}

		} catch (Exception e) {
			errors = true;
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (errors) {
			RecyclingMachine rcm1 = new RecyclingMachine(101,
					"SCU, Santa Clara", 500, 100);
			RecyclingMachine rcm2 = new RecyclingMachine(102, "SJSU, San Jose",
					500, 100);
			RecyclingMachine rcm3 = new RecyclingMachine(103,
					"SFSU, San Francisco", 500, 100);
			rcms.add(rcm1);
			rcms.add(rcm2);
			rcms.add(rcm3);
		}
		return rcms;
	}

	public static void saveState(NewEcoRe rmos) throws IOException {
		FileWriter fileWriter = new FileWriter(STATE_SAVED_FILE);
		fileWriter.write(rmos.asJSONObject().toString(2));
		fileWriter.close();
	}
}
