package model;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Report extends Thread implements ReportObservable {

	private User reporterUser;
	private Address address;
	private String info;
	private int numOfLikes;
	private List<User> usersLiked;
	private Button likeBtn;
	private EventHandler<ActionEvent> eventLike;

	public Report(String description, User user, Address address) {
		this.reporterUser = user;
		this.address = address;
		this.info = description;
		this.numOfLikes = 0;
		this.usersLiked = new ArrayList<User>();
		likeBtn = new Button("❤");
		this.eventLike = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (likeBtn.getTextFill() == Color.RED) {
					likeBtn.setTextFill(Color.BLACK);
				} else {
					likeBtn.setTextFill(Color.RED);
				}
			}
		};
		likeBtn.setOnAction(eventLike);
		this.start();
	}

	public User getReporterUser() {
		return reporterUser;
	}

	public final void setReporterUser(User reporterUser) {
		this.reporterUser = reporterUser;
	}

	public final String getInfo() {
		return info;
	}

	public final void setInfo(String description) {
		this.info = description;
	}

	public final int getNumOfLikes() {
		return numOfLikes;
	}

	public final void setNumOfLikes(int numOfLikes) {
		this.numOfLikes = numOfLikes;
	}

	public final List<User> getUsersLiked() {
		return usersLiked;
	}

	public final void setUsersLiked(List<User> usersLiked) {
		this.usersLiked = usersLiked;
	}

	public final Address getAddress() {
		return address;
	}

	public final void setAddress(Address address) {
		this.address = address;
	}

	/* Part of the Observer pattern: the report add itself to arrayList of reports
	* that the users observe on*/
	@Override
	public void notifyAllObserversOnReport() {
		this.reporterUser.getAllReports().add(this);
	}

	public final Button getLikeBtn() {
		return likeBtn;
	}

	public final void setLikeBtn(Button likeBtn) {
		this.likeBtn = likeBtn;
	}

	@Override
	public String toString() {
		return info + "\nReported by: " + this.reporterUser.getUserName() + " likes:" + numOfLikes;
	}

	public void addLikedUser(User user) {
		this.usersLiked.add(user);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Report) {
			Report other = (Report) obj;
			if (this.address.equals(other.getAddress())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		try {
			sleep(90000);
			reporterUser.getDatabaseConnector().addReportToDatabase(this);
			reporterUser.getAllReports().remove(this);
		} catch (Exception e) {
			
		}
	}

}
