package Dependencies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Writes serialized objects to file in a buffered fashion. Default buffer size
 * is 10,000 objects
 * 
 * @author KARNUTJ
 *
 * @param <T>
 *            implementer of the Serializable interface
 */
public class BufferedArrayList<T extends Serializable> {

	private final transient ArrayList<T> arr;
	private final int size;
	private ObjectOutputStream oos;
	private int numSaves = 0;
	private final File saveFile;
	private int pos = 0;

	public BufferedArrayList(int size, File saveFile) {
		this.arr = new ArrayList<T>();
		this.size = size;
		this.saveFile = saveFile;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(saveFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(T t) {
		arr.add(t);
		if (arr.size() >= size) {
			save();
		}
	}

	private void save() {
		System.out.println("Init Save Routine");
		numSaves++;
		for (T each : arr) {
			try {
				oos.writeObject(each);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			oos.flush();
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		arr.clear();
	}

	public T get(int index) {
		return arr.get(index);
	}

	public void close() {
		try {
			if (this.arr.size() > 0) {
				save();
			}
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long size() {
		return arr.size();
	}

	public long getNumSaves() {
		return numSaves;
	}

	public long getFileSize() {
		return saveFile.length();
	}

}
