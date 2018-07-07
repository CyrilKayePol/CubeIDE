package cube.semantics.blocks;

import java.util.HashMap;

import cube.semantics.Type;

public class MethodBlock extends MainBlock{

	public MethodBlock(HashMap<Integer, String> line_hash, int start_line, int end_line) {
		
		super(line_hash, Type.METHOD, end_line, start_line);
		
	}

}
