package team3_Sub_3.featurealgo;



/*Class used to create the final CSV file which has features-
Gender and Popularity */

public class EntityCharacteristics {

	private String gender;
	private String popularity;
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
//	}
//	public EntityCharacteristics(String gender, String popularity) {
//		super();
//		this.gender = gender;
//		this.popularity = popularity;
//	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the popularity
	 */
	public String getPopularity() {
		return popularity;
	}
	/**
	 * @param popularity the popularity to set
	 */
	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}
	
}
