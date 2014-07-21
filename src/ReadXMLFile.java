import javax.xml.bind.ParseConversionEvent;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadXMLFile {

	public static void getData(String FileName,
			List<Launcher> missileLaunchers,
			List<Destructor> missileDestructor,
			List<Destructor> missileLauncherDestructor) {

		try {

			
			Document doc = loadFile(FileName);
			System.out.println("Load element :"+ doc.getDocumentElement().getNodeName());
			System.out.println("----------------------------");
			loadMissileLauncher(doc, missileLaunchers);
			loadMissileDestructor(doc, missileDestructor,missileLaunchers);
			loadMissileLauncherDestructor(doc, missileLauncherDestructor);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
/**
 * Load XML file by file name and returns XML document
 * @param fileName
 * @return XML document
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws IOException
 */
	private static Document loadFile(String fileName) throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static void loadMissileLauncherDestructor(Document doc,List<Destructor> missileLauncherDestructor) {
		
		Node nMissileLauncherDestructors = doc.getElementsByTagName("missileLauncherDestructors").item(0);
		NodeList nDestructors = ((Element)nMissileLauncherDestructors).getElementsByTagName("destructor");
		for (int i = 0; i < nDestructors.getLength(); i++) {
			Destructor.Type dType;
			String dId;
			int dDestructTime;
			
			Element eDestructor = (Element) nDestructors.item(i);
			System.out.println("\nCurrent Element :" + eDestructor.getNodeName());
			
//			dType = Destructors.Type.valueOf(eDestructor.getAttribute("type"));
//				Launcher launcher = new Launcher(eElement.getAttribute("id"),
//						Boolean.parseBoolean(eElement.getAttribute("isHidden")));
//				NodeList nMissiles = eElement.getElementsByTagName("missile");
//				for (int j = 0; j < nMissiles.getLength(); j++) {
//					mId = ((Element) (nMissiles.item(j))).getAttribute("id");
//					mDestination = ((Element) (nMissiles.item(j)))
//							.getAttribute("destination");
//					mLaunchTime = Integer.parseInt(((Element) (nMissiles
//							.item(j))).getAttribute("launchTime"));
//					mFlyTime = Integer.parseInt(((Element) (nMissiles.item(j)))
//							.getAttribute("flyTime"));
//					mDamage = Integer.parseInt(((Element) (nMissiles.item(j)))
//							.getAttribute("damage"));
//					launcher.addMissile(new Missile(mId, mDestination,
//							mLaunchTime, mFlyTime, mDamage));
//
//					System.out.println("missile : " + mId);
//					System.out.println("	destination	:	 " + mDestination);
//					System.out.println("	launchTime 	:	 " + mLaunchTime);
//					System.out.println("	flyTime 	:	 " + mFlyTime);
//					System.out.println("	damage 		:	 " + mDamage);
//				
//				missileLaunchers.add(launcher);
			} // end of missileLaunchers
		}
	//}

	//}

	private static void loadMissileDestructor(Document doc,List<Destructor> missileDestructors, List<Launcher> missileLaunchers) throws Exception {
		System.out.println("Loading missile destructors");
		Node nMissileDestructor = doc.getElementsByTagName("missileDestructors").item(0);
		NodeList nDestructors = ((Element)nMissileDestructor).getElementsByTagName("destructor");
		for (int i = 0; i < nDestructors.getLength(); i++) {
			
			String dId;
			
			
			Element eDestructor = (Element) nDestructors.item(i);
			dId = eDestructor.getAttribute("id");
			Destructor mDestructor = new Destructor(dId);
			System.out.println("\nCurrent Element :" + eDestructor.getNodeName() +" id = "+dId);
			NodeList nDestructdMissile = eDestructor.getElementsByTagName("destructdMissile");
			for (int j=0;j < nDestructdMissile.getLength();j++){
				String mId;
				int mDestructAfterLaunch;
				Element eDestructedMissile = (Element) nDestructdMissile.item(j);
				mId= eDestructedMissile.getAttribute("id");
				mDestructAfterLaunch = Integer.parseInt(eDestructedMissile.getAttribute("destructAfterLaunch"));
				System.out.println("	Destructd missile	: " + mId);
				System.out.println("	Destructd time		: "+mDestructAfterLaunch);
				Missile mDestructedMissile = Missile.getMissile(missileLaunchers, mId);
				if (mDestructedMissile != null){
					mDestructedMissile.setDestructAfterLaunch(mDestructAfterLaunch);
					mDestructor.addDestructMissile(mDestructedMissile);
				}else{
					throw new Exception("Missile not found");
				}
			}
			missileDestructors.add(mDestructor);
		}

	}

	private static void loadMissileLauncher(Document doc,
			List<Launcher> missileLaunchers) {
		System.out.println("missileLaunchers:");
		NodeList nLaunchers = doc.getElementsByTagName("launcher");
		for (int i = 0; i < nLaunchers.getLength(); i++) {
			String mId;
			String mDestination;
			int mLaunchTime;
			int mFlyTime;
			int mDamage;
			Node nLauncher = nLaunchers.item(i);
			System.out.println("\nCurrent Element :" + nLauncher.getNodeName());
			if (nLauncher.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nLauncher;
				System.out.println("Launcher id : "
						+ eElement.getAttribute("id"));
				System.out.println("Launcher isHidden : "
						+ eElement.getAttribute("isHidden"));
				Launcher launcher = new Launcher(eElement.getAttribute("id"),
						Boolean.parseBoolean(eElement.getAttribute("isHidden")));
				NodeList nMissiles = eElement.getElementsByTagName("missile");
				for (int j = 0; j < nMissiles.getLength(); j++) {
					mId = ((Element) (nMissiles.item(j))).getAttribute("id");
					mDestination = ((Element) (nMissiles.item(j)))
							.getAttribute("destination");
					mLaunchTime = Integer.parseInt(((Element) (nMissiles
							.item(j))).getAttribute("launchTime"));
					mFlyTime = Integer.parseInt(((Element) (nMissiles.item(j)))
							.getAttribute("flyTime"));
					mDamage = Integer.parseInt(((Element) (nMissiles.item(j)))
							.getAttribute("damage"));
					launcher.addMissile(new Missile(mId, mDestination,
							mLaunchTime, mFlyTime, mDamage));

					System.out.println("missile : " + mId);
					System.out.println("	destination	:	 " + mDestination);
					System.out.println("	launchTime 	:	 " + mLaunchTime);
					System.out.println("	flyTime 	:	 " + mFlyTime);
					System.out.println("	damage 		:	 " + mDamage);
				}
				missileLaunchers.add(launcher);
			} // end of missileLaunchers
		}
	}
}