package fuentis;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fuentis.etl.adapter.Adaptable;
import com.fuentis.etl.dto.AdapterAttributeDto;
import com.fuentis.etl.dto.AdapterObjectDto;
import org.w3c.dom.Document;

public class Parser implements Adaptable{
	Map<String, Object> parsedFileMap=new LinkedHashMap<String, Object>();
	public String filename;
	public Map<String, Object> fileopen() {
		try
		{
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Node rootNode=doc.getFirstChild();
			while(!(rootNode.getNodeType()==(Node.ELEMENT_NODE)))
			{
				rootNode=rootNode.getNextSibling();
			}
			AdapterObjectDto mainAdapterObject=new AdapterObjectDto();
			mainAdapterObject.setId(doc.getDocumentElement().getNodeName());
			mainAdapterObject.setType("Root Element");
			parsedFileMap= parseFile(rootNode,mainAdapterObject);	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return parsedFileMap;
	}
	public Map<String,Object> parseFile(Node parent,AdapterObjectDto mainAdapter) {
		Map<String, Object> fileMap=new LinkedHashMap<String, Object>();
		fileMap.put("Element Node", mainAdapter);
		try
		{
			NodeList nList=parent.getChildNodes();
			int nodeNumber=0;

			for (int count = 0; count < nList.getLength(); count++) {
				AdapterObjectDto adapterObject=new AdapterObjectDto();
				Node tempNode = nList.item(count);
				if(checkChildNodeOnly(tempNode)) {
					adapterObject.setId(tempNode.getNodeName()+(nodeNumber+1));
					adapterObject.setType("Child Node"+(nodeNumber+1));
					
					NodeList childNodeList=tempNode.getChildNodes();
					for (int x = 0; x < childNodeList.getLength(); x++) {
						Node childAtr= childNodeList.item(x);
						if(checkChildNodeOnly(childAtr)) {
							AdapterAttributeDto adapterAttributes= new AdapterAttributeDto();
							adapterAttributes.setKey(childAtr.getNodeName());
							adapterAttributes.setValue(childAtr.getTextContent());	
							adapterObject.addAttribute(adapterAttributes);
						}
						else {
							continue;
						}
					}
					mainAdapter.addChild(adapterObject);//main is the final adapter object
					fileMap.put("Child Node"+(nodeNumber+1),adapterObject);	
					nodeNumber++;
				}
				else
				{
					continue;
				}
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fileMap;

}
	public Boolean checkChildNodeOnly(Node ns) {
		if(!(ns.getNodeType()==(Node.ELEMENT_NODE))) {
			return false;
		}
		else
		{
			return true;
		}
	}
	@Override
	public Collection<AdapterObjectDto> findAll(Map<String, Object> map) {
		Collection<AdapterObjectDto> finalSet = new LinkedHashSet<>();	
		if(map.get("Element Node")!=null) {
			finalSet.add((AdapterObjectDto) map.get("Element Node"));
		}	
		for(Map.Entry<String, Object> entry : map.entrySet())
		{
			if(entry.getKey().contains("Child Node")) {
				finalSet.add((AdapterObjectDto) entry.getValue());				
			}
		}
	
		return finalSet;
	}

}
