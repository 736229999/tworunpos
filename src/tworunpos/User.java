package tworunpos;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

import GuiElements.TrDialogYesNo;
import GuiElements.TrUserLogin;
import GuiElements.TrSounds;

public class User  {
	
	private String name;
	private String userId;
	private String password;
	private boolean loggedInStatus = false;
	
	//default user to login every time
	private String defaultUserName = "Default User";
	private String defaultUserId = "123";
	private String defaultUserPassword = "123";






	// Verhindere die Erzeugung des Objektes �ber andere Methoden
	
	public User(){


	    //todo move defaultuserthing to UserList
		//check if there is the default user in the database. if not create a default user
		UserList userList = new UserList();		
		try {
			User defaultUser = userList.lookupUserById(defaultUserId);
			DebugScreen.getInstance().print("Default User found.");
		} catch (Exception e) {

			userList.addUser(new User(defaultUserName, defaultUserId, defaultUserPassword));
			DebugScreen.getInstance().print("Default User created!");
		}
	}


	public User(Integer userKey){
		//check if there is the default user in the database. if not create a default user
		UserList userList = new UserList();
		try {
			User tmpUser = userList.lookupUserById(userKey.toString());
			//map onject to this
			this.name = tmpUser.name;
			this.userId = tmpUser.userId;
			this.password = tmpUser.password;
			DebugScreen.getInstance().print("User login Successful by ID: "+userKey);

		} catch (Exception e) {

			DebugScreen.getInstance().print("Login failed.");
		}




	}
	
	
	public User(String setName, String setUserId, String setPassword){
		name = setName;
		userId = setUserId;
		password = setPassword;
	}
	
	public User(DBObject userDbObject){		
		userId = (userDbObject.get("userId") != null ? userDbObject.get("userId").toString():null);
		name = (userDbObject.get("name") != null ? userDbObject.get("name").toString():null);
	}
	
	public  void logout() throws Exception{

	    if(loggedIn()){
            //check if logout is allowed
            DebugScreen.getInstance().print("State before logout: "+PosState.getInstance().getState());
            if( PosState.getInstance().logoutLocked()){
                throw new Exception("Logout nicht möglich");
            }

            //check if the user wants really to logout?
            TrDialogYesNo yesNo = new TrDialogYesNo("Wirklich ausloggen?", "Sicherheitsabfrage");
            if(yesNo.isYes()){
                setLoggedInStatus(false);


            }
        }

	}
	
	public  void login(Integer entry) {
		//todo real check for login
		UserList userList = new UserList();
		try {
			userList.lookupUserById(entry.toString());
			setLoggedInStatus(true);

			//notifyObservers( new Object[]{"login", article} );


			DebugScreen.getInstance().print("Login für "+entry.toString()+" erfolgreich!");




		} catch (Exception e) {
			// TODO Auto-generated catch block
			setLoggedInStatus(false);
			PosState.getInstance().changeStateToLogin(false);

			DebugScreen.getInstance().print("Login für "+entry.toString()+" nicht erfolgreich!");
		}
		
		
	}
	
	
	//tells if a user is logged in or not
	public boolean loggedIn(){
		return loggedInStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLoggedInStatus() {
		return loggedInStatus;
	}

	public void setLoggedInStatus(boolean b) {
		this.loggedInStatus = b;
	}
	
	
	/*
	 * This method will return the document object for a mongodb entry
	 */
	public BasicDBObject getMyDocument(){
		
		BasicDBObject document = new BasicDBObject();		
		document.put("userId", this.getUserId());
		document.put("name", this.getName());
		
		return document;
	}





	

}
