import Dependencies.Unpacker;

public class SeerUnpack implements Unpacker{

	private SeerObservation obs;
	public SeerUnpack(SeerObservation obs){
		this.obs = obs;
	}
	
	public SeerIncident unpack(){
		return new SeerIncident();
	}
}
