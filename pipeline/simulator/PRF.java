package cao.pipeline.simulator;
public class PRF {
	int pr_address;
	int pr_value;
	boolean pr_busy;
	boolean pr_allocated;
	boolean pr_renamed;
	
	PRF(){
		pr_busy = false;
		pr_allocated = false;
		pr_renamed = false;
	}
}
