package fuentis;

import java.util.*;

import com.fuentis.etl.dto.*;

public class ParseMain {

	public static void main(String[] args) {
		Parser parsefile = new Parser();
		parsefile.filename = "Fulldata.xml";
		Map<String, Object> parsedFileMap=parsefile.fileopen();
		LinkedHashSet<AdapterObjectDto> fetchedListOfAdapterObject=(LinkedHashSet<AdapterObjectDto>) parsefile.findAll(parsedFileMap);

		for(AdapterObjectDto itrAdp : fetchedListOfAdapterObject) {
			System.out.println(itrAdp.getType()+":"+itrAdp.getId());
			System.out.println();
			if(itrAdp.getChildren()!=null) {
				for(AdapterAttributeDto itrChldAdp : itrAdp.getAttributes()) {

					System.out.println(itrChldAdp.getKey()+":"+itrChldAdp.getValue());
			}
			
		}
			System.out.println("----------------------------------");
			System.out.println(" ");

	}
		System.out.println("End of File");

}
}
