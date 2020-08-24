/**
 * 
 */
package team3_Sub_3.featurealgo;
import java.util.List;

/**
 * @author Gayathri
 *
 */
 
 /* Entity generation with attributes such as
 skeleton of the entity, page id , page name , inlink ids */
 
public class Entity {
	
	private String pageName;
	private String pageID;
	private List<String> redirectNames;
	private List<String> inlinkIds;
	private List<String> anchorTexts;
	private String skeleton;
	
	/**
	 * @return the skeleton
	 */
	public String getSkeleton() {
		return skeleton;
	}

	/**
	 * @return the pageID
	 */
	public String getPageID() {
		return pageID;
	}

	/**
	 * @param pageID the pageID to set
	 */
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	/**
	 * @param skeleton the skeleton to set
	 */
	public void setSkeleton(String skeleton) {
		this.skeleton = skeleton;
	}

	public Entity(String pageName, List<String> redirectNames, List<String> inlinkIds, List<String> anchorTexts,String skeleton,String pageId) {
		this.pageName = pageName;
		this.redirectNames = redirectNames;
		this.inlinkIds = inlinkIds;
		this.anchorTexts = anchorTexts;
		this.skeleton = skeleton;
		this.pageID = pageId;
	}
	
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the redirectNames
	 */
	public List<String> getRedirectNames() {
		return redirectNames;
	}
	/**
	 * @param redirectNames the redirectNames to set
	 */
	public void setRedirectNames(List<String> redirectNames) {
		this.redirectNames = redirectNames;
	}

	/**
	 * @return the inlinkIds
	 */
	public List<String> getInlinkIds() {
		return inlinkIds;
	}

	/**
	 * @param inlinkIds the inlinkIds to set
	 */
	public void setInlinkIds(List<String> inlinkIds) {
		this.inlinkIds = inlinkIds;
	}

	/**
	 * @return the anchorTexts
	 */
	public List<String> getAnchorTexts() {
		return anchorTexts;
	}

	/**
	 * @param anchorTexts the anchorTexts to set
	 */
	public void setAnchorTexts(List<String> anchorTexts) {
		this.anchorTexts = anchorTexts;
	}
}
